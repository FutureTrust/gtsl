/*
 * Copyright (c) 2017 European Commission.
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence").
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl5
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence
 * is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * See the Licence for the specific language governing permissions and limitations under the
 * Licence.
 */

package eu.futuretrust.gtsl.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.futuretrust.gtsl.storage.exceptions.StorageException;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Storage {

  private static final Logger LOGGER = LoggerFactory.getLogger(Storage.class);
  private static final long TIMEOUT = 10L;

  /**
   * IPFS endpoint used to manage data
   */
  private final IPFS ipfs;
  private final ObjectMapper mapper;


  public Storage(String endpoint) {
    this.ipfs = new IPFS(endpoint);
    this.mapper = new ObjectMapper();
  }

  /**
   * Store the data in IPFS and return the commit address
   *
   * @param data data to be stored
   * @param version initial version
   * @return commit address where the commit information (including the data address) has been
   * stored
   */
  public String create(byte[] data, BigInteger version) throws IOException, StorageException {
    String dataAddress = this.add(data);
    Commit commit = new Commit(dataAddress, version);
    String commitAddress = this.add(mapper.writeValueAsBytes(commit));

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Data has been added successfully to IPFS");
    }
    return commitAddress;
  }

  /**
   * Store the data in IPFS and return the commit address
   *
   * @param data data to be stored
   * @return commit address where the commit information (including the data address) has been
   * stored
   */
  public String create(byte[] data) throws IOException, StorageException {
    return this.create(data, BigInteger.ONE);
  }

  /**
   * Use the current commit address to retrieve information then update this information with the
   * data to be stored and finally return the new commit address
   *
   * @param commitAddress (hash) commit location on IPFS
   * @param data data to be updated
   * @param version new version of the commit, if null increment the current version
   * @return new commit address where the commit information (including the data address) has been
   * stored
   */
  public String update(final String commitAddress, final byte[] data, final BigInteger version)
      throws ExecutionException, InterruptedException, IOException, StorageException {
    Optional<Commit> commit = this.getCommit(commitAddress);
    if (commit.isPresent()) {
      String newDataAddress = this.add(data);
      if (newDataAddress.equals(commit.get().getDataAddress())) {
        if (LOGGER.isInfoEnabled()) {
          LOGGER.info("No change detected the version has not been updated");
        }
        return commitAddress;
      } else {
        BigInteger newVersion = determineNewVersion(commit.get().getVersionNumber(), version);
        Commit newCommitObject = new Commit(newDataAddress, commitAddress, newVersion);
        String newCommitAddress = this.add(mapper.writeValueAsBytes(newCommitObject));
        if (LOGGER.isInfoEnabled()) {
          LOGGER.info("Data has been updated successfully into IPFS");
        }
        return newCommitAddress;
      }
    } else {
      throw new StorageException("The commit does not exist, the data cannot be updated");
    }
  }

  /**
   * Use the current commit address to retrieve information then update this information with the
   * data to be stored and finally return the new commit address
   *
   * @param commitAddress (hash) commit location on IPFS
   * @param data data to be updated
   * @return new commit address where the commit information (including the data address) has been
   * stored
   */
  public String update(String commitAddress, byte[] data)
      throws ExecutionException, InterruptedException, IOException, StorageException {
    return this.update(commitAddress, data, null);
  }

  private BigInteger determineNewVersion(BigInteger currentVersion, BigInteger newVersion)
      throws StorageException {
    BigInteger version;
    if (newVersion == null) {
      // if no version has been set by user add 1 to the current version
      version = currentVersion.add(BigInteger.ONE);
    } else if (currentVersion.compareTo(newVersion) < 0) {
      throw new StorageException(
          "The new version number cannot be lower than the previous version number");
    } else {
      version = newVersion;
    }
    return version;
  }

  /**
   * Return bytes based on the address of the data
   *
   * @param dataAddress (hash) data location on IPFS
   * @return byte array corresponding to the content retrieve from the address
   */
  public Optional<byte[]> getData(String dataAddress)
      throws ExecutionException, InterruptedException {
    try {
      byte[] data = this.cat(dataAddress).get(TIMEOUT, TimeUnit.SECONDS);
      return Optional.ofNullable(data);
    } catch (TimeoutException e) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("No data found for the address {}", dataAddress);
      }
      return Optional.empty();
    }
  }

  /**
   * Return a Commit object based on the address of the commit
   *
   * @param commitAddress (hash) commit location on IPFS
   * @return Commit object corresponding to the content retrieve from the address
   */
  public Optional<Commit> getCommit(String commitAddress)
      throws ExecutionException, InterruptedException, InvalidParameterException, IOException {
    Optional<byte[]> data = this.getData(commitAddress);
    if (!data.isPresent()) {
      return Optional.empty();
    }
    Commit commit = mapper.readValue(data.get(), Commit.class);
    return Optional.ofNullable(commit);
  }

  /**
   * Resolve the address where the data has been stored
   *
   * @param commitAddress (hash) commit location on IPFS
   * @return the address of the data
   */
  public Optional<String> resolve(String commitAddress)
      throws ExecutionException, InterruptedException, IOException {
    Optional<Commit> commit = this.getCommit(commitAddress);
    if (!commit.isPresent()) {
      return Optional.empty();
    }
    return Optional.ofNullable(commit.get().getDataAddress());
  }

  /**
   * Get the previous version for a certain address
   *
   * @param commitAddress (hash) commit location on IPFS
   * @return the previous version for a certain address
   */
  public Optional<String> parent(String commitAddress)
      throws ExecutionException, InterruptedException, IOException {
    Optional<Commit> commit = this.getCommit(commitAddress);
    if (!commit.isPresent()) {
      return Optional.empty();
    }
    return Optional.ofNullable(commit.get().getParent());
  }

  /**
   * Get all previous versions for a certain address
   *
   * @param commitAddress (hash) commit location on IPFS
   * @return a set of all previous versions for a certain address
   */
  public Set<String> ancestors(String commitAddress)
      throws ExecutionException, InterruptedException, IOException {
    Optional<Commit> commit = this.getCommit(commitAddress);
    if (!commit.isPresent()) {
      return Collections.emptySet();
    }

    Set<String> ancestors = new LinkedHashSet<>();
    while (commit.isPresent() && commit.get().getParent() != null) {
      ancestors.add(commit.get().getParent());
      commit = this.getCommit(commit.get().getParent());
    }
    return ancestors;
  }

  /**
   * Pin the address of the commit and the address of the data in order to be redundant
   *
   * @param commitAddress (hash) commit location on IPFS
   * @return true if pinned
   */
  public boolean pinCurrentVersion(String commitAddress)
      throws ExecutionException, InterruptedException, IOException {
    Optional<Commit> commit = this.getCommit(commitAddress);
    if (commit.isPresent()) {
      this.pin(commitAddress);
      this.pin(commit.get().getDataAddress());
      return true;
    } else {
      return false;
    }
  }

  /**
   * Pin the address of the oommit and the address of the data for all versions in order to be
   * redundant
   *
   * @param commitAddress (hash) commit location on IPFS
   * @return a set of all pins
   */
  public boolean pinAllVersions(String commitAddress)
      throws IOException, ExecutionException, InterruptedException {
    if (this.pinCurrentVersion(commitAddress)) {
      Optional<Commit> commit = this.getCommit(commitAddress);
      if (commit.isPresent()) {
        if (commit.get().getParent() != null) {
          return this.pinAllVersions(commit.get().getParent());
        } else {
          // no parent found, no need to pin
          return true;
        }
      } else {
        // cannot found the current commit
        return false;
      }
    } else {
      // cannot pin the current commit
      return false;
    }
  }

  /**
   * Pin the address in order to be redundant
   *
   * @param address (hash) address of an object on IPFS
   */
  private void pin(String address) throws IOException {
    validateAddress(address);
    ipfs.pin.add(Multihash.fromBase58(address));
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Address \"" + address + "\" has been pinned");
    }
  }

  /**
   * Unpin the address in order to be delete the file from the local node
   *
   * @param address (hash) address of an object on IPFS
   */
  public void unpin(String address) throws IOException {
    validateAddress(address);
    ipfs.pin.rm(Multihash.fromBase58(address));
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Address \"" + address + "\" has been unpinned");
    }
  }

  /**
   * Get the data from an address
   *
   * @param address (hash) data location on IPFS
   * @return data
   */
  private Future<byte[]> cat(String address) {
    validateAddress(address);
    ExecutorService executor = Executors.newSingleThreadExecutor();
    return executor.submit(() -> ipfs.cat(Multihash.fromBase58(address)));
  }

  /**
   * Add data to the IPFS network
   *
   * @param data data to be added to IPFS
   * @return address (hash) which points to the data
   */
  private String add(byte[] data)
      throws IOException, StorageException, InvalidParameterException {
    validateData(data);
    List<MerkleNode> merkleNodes = ipfs.add(new NamedStreamable.ByteArrayWrapper(data));
    if (merkleNodes.isEmpty()) {
      throw new StorageException("Data address cannot be retrieved");
    }
    String hash = merkleNodes.get(0).hash.toBase58();
    if (hash == null || hash.trim().isEmpty()) {
      throw new StorageException("Data address cannot be retrieved");
    }
    return hash;
  }

  private void validateAddress(String address) throws InvalidParameterException {
    if (address == null || address.trim().isEmpty()) {
      throw new InvalidParameterException("Address (hash) is invalid: null or empty");
    }
  }

  private void validateData(byte[] data) throws InvalidParameterException {
    if (data == null || data.length == 0) {
      throw new InvalidParameterException("Data are invalid: null or empty");
    }
  }

}
