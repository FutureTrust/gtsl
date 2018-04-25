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

import eu.futuretrust.gtsl.storage.exceptions.StorageException;
import eu.futuretrust.gtsl.storage.helper.IpfsConstants;
import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.Set;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestStorage {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static final String EMPTY_STRING = "";

  private static final String VALID_DATA = "my text";
  private static final String VALID_DATA2 = "new text";
  private static final String VALID_DATA3 = "another text";

  private static final String VALID_NOT_USED_ADDRESS = "QmYmCM2ttrNscwfqSroXP5yVb5LT58zRhvEvMp6WqBHBBB";

  private Storage storage;

  public TestStorage() {
    storage = new Storage(IpfsConstants.ENDPOINT);
  }

  @Test
  public void createTest() throws Exception {
    byte[] sToBytes = VALID_DATA.getBytes();
    String commitAddress = storage.create(sToBytes);

    Optional<String> dataAddress = storage.resolve(commitAddress);
    Assert.assertTrue(dataAddress.isPresent());

    Optional<byte[]> data = storage.getData(dataAddress.get());
    Assert.assertTrue(data.isPresent());

    Optional<Commit> commit = storage.getCommit(commitAddress);
    Assert.assertTrue(commit.isPresent());

    Assert.assertNull(commit.get().getParent());
    Assert.assertEquals(dataAddress.get(), commit.get().getDataAddress());
    Assert.assertEquals(VALID_DATA, new String(data.get()));
  }

  @Test
  public void updateTest() throws Exception {
    // data 1
    byte[] str1ToBytes = VALID_DATA.getBytes();
    String commitAddress1 = storage.create(str1ToBytes);

    Optional<Commit> commit1 = storage.getCommit(commitAddress1);
    Assert.assertTrue(commit1.isPresent());

    Optional<String> dataAddress1 = storage.resolve(commitAddress1);
    Assert.assertTrue(dataAddress1.isPresent());

    Optional<byte[]> data1 = storage.getData(dataAddress1.get());
    Assert.assertTrue(data1.isPresent());

    // data 2
    byte[] str2ToBytes = VALID_DATA2.getBytes();
    String commitAddress2 = storage.update(commitAddress1, str2ToBytes);

    Optional<Commit> commit2 = storage.getCommit(commitAddress2);
    Assert.assertTrue(commit2.isPresent());

    Optional<String> dataAddress2 = storage.resolve(commitAddress2);
    Assert.assertTrue(dataAddress2.isPresent());

    Optional<byte[]> data2 = storage.getData(dataAddress2.get());
    Assert.assertTrue(data2.isPresent());

    // data 3
    byte[] str3ToBytes = VALID_DATA3.getBytes();
    String commitAddress3 = storage.update(commitAddress2, str3ToBytes);

    Optional<Commit> commit3 = storage.getCommit(commitAddress3);
    Assert.assertTrue(commit3.isPresent());

    Optional<String> dataAddress3 = storage.resolve(commitAddress3);
    Assert.assertTrue(dataAddress3.isPresent());

    Optional<byte[]> data3 = storage.getData(dataAddress3.get());
    Assert.assertTrue(data3.isPresent());

    Assert.assertArrayEquals(VALID_DATA.getBytes(), data1.get());
    Assert.assertArrayEquals(VALID_DATA2.getBytes(), data2.get());
    Assert.assertArrayEquals(VALID_DATA3.getBytes(), data3.get());

    Assert.assertNull(commit1.get().getParent());
    Assert.assertNotNull(commit2.get().getParent());
    Assert.assertNotNull(commit3.get().getParent());

    Assert.assertNull(commit1.get().getParent());
    Assert.assertEquals(commitAddress1, commit2.get().getParent());
    Assert.assertEquals(commitAddress2, commit3.get().getParent());

    Optional<String> parentCommit2 = storage.parent(commitAddress2);
    Assert.assertTrue(parentCommit2.isPresent());
    Assert.assertEquals(commitAddress1, parentCommit2.get());

    Optional<String> parentCommit3 = storage.parent(commitAddress3);
    Assert.assertTrue(parentCommit3.isPresent());
    Assert.assertEquals(commitAddress2, parentCommit3.get());

    Assert.assertNotEquals(commit1.get().getDataAddress(), commit2.get().getDataAddress());
    Assert.assertNotEquals(commit2.get().getDataAddress(), commit3.get().getDataAddress());

    Set<String> ancestors = storage.ancestors(commitAddress3);
    Assert.assertFalse(ancestors.isEmpty());
    Assert.assertEquals(2, ancestors.size());
    Assert.assertArrayEquals(
        new String[]{
            commitAddress2,
            commitAddress1
        },
        ancestors.toArray());
  }

  @Test
  public void updateSameValueTest() throws Exception {
    String commitAddress = storage.create(VALID_DATA.getBytes());

    Optional<Commit> commit1 = storage.getCommit(commitAddress);
    Assert.assertTrue(commit1.isPresent());

    String commitAddress2 = storage.update(commitAddress, VALID_DATA.getBytes());

    Optional<Commit> commit2 = storage.getCommit(commitAddress2);
    Assert.assertTrue(commit2.isPresent());

    Assert.assertEquals(commit1.get(), commit2.get());
  }

  @Test
  public void createDataNullTest() throws Exception {
    thrown.expect(InvalidParameterException.class);
    storage.create(null);
  }

  @Test
  public void createDataEmptyTest() throws Exception {
    thrown.expect(InvalidParameterException.class);
    storage.create(EMPTY_STRING.getBytes());
  }

  @Test
  public void updateAddressNotExist() throws Exception {
    thrown.expect(StorageException.class);
    storage.update(VALID_NOT_USED_ADDRESS, VALID_DATA.getBytes());
  }

  @Test
  public void updateAddressNullTest() throws Exception {
    thrown.expect(InvalidParameterException.class);
    storage.update(null, VALID_DATA.getBytes());
  }

  @Test
  public void updateAddressEmptyTest() throws Exception {
    thrown.expect(InvalidParameterException.class);
    storage.update(EMPTY_STRING, VALID_DATA.getBytes());
  }

  @Test
  public void updateDataNullTest() throws Exception {
    thrown.expect(InvalidParameterException.class);
    String commitAddress = storage.create(VALID_DATA.getBytes());
    storage.update(commitAddress, null);
  }

  @Test
  public void updateDataEmptyTest() throws Exception {
    thrown.expect(InvalidParameterException.class);
    String commitAddress = storage.create(VALID_DATA.getBytes());
    storage.update(commitAddress, EMPTY_STRING.getBytes());
  }

  @Test
  public void getAddressNullTest() throws Exception {
    thrown.expect(InvalidParameterException.class);
    storage.getData(null);
  }

  @Test
  public void getAddressEmptyTest() throws Exception {
    thrown.expect(InvalidParameterException.class);
    storage.getData(EMPTY_STRING);
  }

  @Test
  public void getAddressNotExistsTest() throws Exception {
    Optional<byte[]> data = storage.getData(VALID_NOT_USED_ADDRESS);
    Assert.assertFalse(data.isPresent());
  }

  @Test
  public void getCommitAddressNullTest() throws Exception {
    thrown.expect(InvalidParameterException.class);
    storage.getCommit(null);
  }

  @Test
  public void getCommitAddressEmptyTest() throws Exception {
    thrown.expect(InvalidParameterException.class);
    storage.getCommit(EMPTY_STRING);
  }

  @Test
  public void getCommitAddressNotExistsTest() throws Exception {
    Optional<Commit> commit = storage.getCommit(VALID_NOT_USED_ADDRESS);
    Assert.assertFalse(commit.isPresent());
  }

}
