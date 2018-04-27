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

package eu.futuretrust.gtsl.business.services.storage.impl;

import eu.futuretrust.gtsl.business.services.storage.StorageService;
import eu.futuretrust.gtsl.business.utils.DebugUtils;
import eu.futuretrust.gtsl.storage.Commit;
import eu.futuretrust.gtsl.storage.Storage;
import eu.futuretrust.gtsl.storage.exceptions.StorageException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl implements StorageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

  private Storage storage;

  @Autowired
  public StorageServiceImpl(Storage storage) {
    this.storage = storage;
  }

  @Override
  public String create(byte[] data) throws IOException, StorageException {
    DebugUtils.debug(LOGGER, this.getClass(), "create");

    return storage.create(data);
  }

  @Override
  public String create(byte[] data, BigInteger version) throws IOException, StorageException {
    DebugUtils.debug(LOGGER, this.getClass(), "create");

    return storage.create(data, version);
  }

  @Override
  public String update(String commitAddress, byte[] data)
      throws ExecutionException, InterruptedException, IOException, StorageException {
    DebugUtils.debug(LOGGER, this.getClass(), "update");

    return storage.update(commitAddress, data);
  }

  @Override
  public String update(String commitAddress, byte[] data, BigInteger version)
      throws ExecutionException, InterruptedException, IOException, StorageException {
    DebugUtils.debug(LOGGER, this.getClass(), "update");

    return storage.update(commitAddress, data, version);
  }

  @Override
  public Optional<byte[]> get(String dataAddress)
      throws ExecutionException, InterruptedException {
    DebugUtils.debug(LOGGER, this.getClass(), "get");

    return storage.getData(dataAddress);
  }

  @Override
  public Optional<Commit> getCommit(String commitAddress)
      throws ExecutionException, InterruptedException, IOException {
    DebugUtils.debug(LOGGER, this.getClass(), "getCommit");

    return storage.getCommit(commitAddress);
  }

  @Override
  public Optional<String> resolve(String commitAddress)
      throws ExecutionException, InterruptedException, IOException {
    DebugUtils.debug(LOGGER, this.getClass(), "resolve");

    return storage.resolve(commitAddress);
  }

  @Override
  public Optional<String> parent(String commitAddress)
      throws ExecutionException, InterruptedException, IOException {
    DebugUtils.debug(LOGGER, this.getClass(), "parent");

    return storage.parent(commitAddress);
  }

  @Override
  public Set<String> ancestors(String commitAddress)
      throws ExecutionException, InterruptedException, IOException {
    DebugUtils.debug(LOGGER, this.getClass(), "ancestors");

    return storage.ancestors(commitAddress);
  }

  @Override
  public boolean pinCurrentVersion(String commitAddress)
      throws ExecutionException, InterruptedException, IOException {
    DebugUtils.debug(LOGGER, this.getClass(), "pinCurrentVersion");

    return storage.pinCurrentVersion(commitAddress);
  }

  @Override
  public boolean pinAllVersions(String commitAddress)
      throws ExecutionException, InterruptedException, IOException {
    DebugUtils.debug(LOGGER, this.getClass(), "pinAllVersions");

    return storage.pinAllVersions(commitAddress);
  }

  @Override
  public void unpin(String address) {
    DebugUtils.debug(LOGGER, this.getClass(), "unpin");

    try {
      storage.unpin(address);
    } catch (Exception e) {
      LOGGER.error("Unable to unpin the address \"" + address + "\": " + e.getMessage());
    }
  }
}
