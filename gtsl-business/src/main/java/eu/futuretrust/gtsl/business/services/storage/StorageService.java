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

package eu.futuretrust.gtsl.business.services.storage;

import eu.futuretrust.gtsl.storage.Commit;
import eu.futuretrust.gtsl.storage.exceptions.StorageException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface StorageService {

  String create(byte[] data) throws IOException, StorageException;

  String create(byte[] data, BigInteger versionNumber) throws IOException, StorageException;

  String update(String commitAddress, byte[] data)
      throws ExecutionException, InterruptedException, IOException, StorageException;

  String update(String commitAddress, byte[] data, BigInteger version)
      throws ExecutionException, InterruptedException, IOException, StorageException;

  Optional<byte[]> get(String dataAddress)
      throws ExecutionException, InterruptedException;

  Optional<Commit> getCommit(String commitAddress)
      throws ExecutionException, InterruptedException, IOException, ClassNotFoundException;

  Optional<String> resolve(String commitAddress)
      throws ExecutionException, InterruptedException, IOException, ClassNotFoundException;

  Optional<String> parent(String commitAddress)
      throws ExecutionException, InterruptedException, IOException, ClassNotFoundException;

  Set<String> ancestors(String commitAddress)
      throws ExecutionException, InterruptedException, IOException, ClassNotFoundException;

  boolean pinCurrentVersion(String commitAddress)
      throws ExecutionException, InterruptedException, IOException, ClassNotFoundException;

  boolean pinAllVersions(String commitAddress)
      throws ExecutionException, InterruptedException, IOException, ClassNotFoundException;

  void unpin(String address);
}
