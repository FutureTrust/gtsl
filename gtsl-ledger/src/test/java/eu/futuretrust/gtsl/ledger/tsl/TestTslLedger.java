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

package eu.futuretrust.gtsl.ledger.tsl;

import eu.futuretrust.gtsl.ledger.AbstractTestContract;
import eu.futuretrust.gtsl.ledger.TslLedger;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import eu.futuretrust.gtsl.ledger.helper.EthereumConstants;
import eu.futuretrust.gtsl.ledger.helper.ResourcesUtils;
import eu.futuretrust.gtsl.ledger.utils.WalletFileUtils;
import eu.futuretrust.gtsl.ledger.vo.Tsl;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class TestTslLedger extends AbstractTestContract {

  private static TslLedger authorizedEu;
  private static TslLedger unauthorized;

  @BeforeClass
  public static void init() throws Exception {
    byte[] authorizedKeystore = ResourcesUtils
        .loadBytes(EthereumConstants.AUTHORIZED_KEYSTORE_PATH);
    byte[] unauthorizedKeystore = ResourcesUtils
        .loadBytes(EthereumConstants.UNAUTHORIZED_KEYSTORE_PATH);

    Web3j web3j = Web3j.build(new HttpService(EthereumConstants.ENDPOINT));
    WalletFile wallet = WalletFileUtils.retrieveWallet(authorizedKeystore);
    Credentials credentials = Credentials
        .create(Wallet.decrypt(EthereumConstants.AUTHORIZED_KEYSTORE_PASSWORD, wallet));

    String consortiumAddress = deployConsortium(web3j, credentials);
    String ledgerAddress = deployTslStore(web3j, credentials, consortiumAddress);

    authorizedEu = new TslLedger(EthereumConstants.ENDPOINT, authorizedKeystore,
        EthereumConstants.AUTHORIZED_KEYSTORE_PASSWORD);
    unauthorized = new TslLedger(EthereumConstants.ENDPOINT, unauthorizedKeystore,
        EthereumConstants.UNAUTHORIZED_KEYSTORE_PASSWORD);

    authorizedEu.load(ledgerAddress);
    unauthorized.load(ledgerAddress);
  }

  @Test
  public void fullProcess() throws Exception {
    Optional<Tsl> notExist = authorizedEu.findByCountryCode(VALID_CODE);
    Assert.assertFalse(notExist.isPresent());

    authorizedEu.add(VALID_CODE, VALID_ADDRESS);

    Optional<Tsl> tsl = authorizedEu.findByCountryCode(VALID_CODE);
    Assert.assertTrue(tsl.isPresent());
    Assert.assertEquals(BigInteger.ONE, tsl.get().getIndex());
    Assert.assertEquals(VALID_CODE, tsl.get().getCountryCode());
    Assert.assertEquals(VALID_ADDRESS, tsl.get().getAddress());

    Optional<Tsl> tslById = authorizedEu.findById(BigInteger.ONE);
    Assert.assertTrue(tslById.isPresent());
    Assert.assertEquals(BigInteger.ONE, tslById.get().getIndex());
    Assert.assertEquals(VALID_CODE, tslById.get().getCountryCode());
    Assert.assertEquals(VALID_ADDRESS, tslById.get().getAddress());

    authorizedEu.update(VALID_CODE, VALID_ADDRESS2);

    Optional<Tsl> tsl2 = authorizedEu.findByCountryCode(VALID_CODE);
    Assert.assertTrue(tsl2.isPresent());
    Assert.assertEquals(BigInteger.ONE, tsl2.get().getIndex());
    Assert.assertEquals(VALID_CODE, tsl2.get().getCountryCode());
    Assert.assertEquals(VALID_ADDRESS2, tsl2.get().getAddress());

    Optional<Tsl> tslById2 = authorizedEu.findById(BigInteger.ONE);
    Assert.assertTrue(tslById2.isPresent());
    Assert.assertEquals(BigInteger.ONE, tslById2.get().getIndex());
    Assert.assertEquals(VALID_CODE, tslById2.get().getCountryCode());
    Assert.assertEquals(VALID_ADDRESS2, tslById2.get().getAddress());

    List<Tsl> tslList = authorizedEu.findAll();
    Assert.assertEquals(1, tslList.size());
    Assert.assertEquals(BigInteger.ONE, tslList.get(0).getIndex());
    Assert.assertEquals(VALID_CODE, tslList.get(0).getCountryCode());
    Assert.assertEquals(VALID_ADDRESS2, tslList.get(0).getAddress());

    authorizedEu.remove(VALID_CODE);
    Optional<Tsl> tsl3 = authorizedEu.findByCountryCode(VALID_CODE);
    Assert.assertFalse(tsl3.isPresent());
  }

  @Test
  public void addUnauthorized() throws Exception {
    thrown.expect(UnauthorizedException.class);
    thrown.expectMessage(
        "Not allowed to proceed this action (you are not allowed to execute this function or TSL "
            + VALID_CODE + " already exists)");
    unauthorized.add(VALID_CODE, VALID_ADDRESS);
  }


  @Test
  public void addAddressNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("Hash is invalid: null or empty");
    authorizedEu.add(VALID_CODE, null);
  }

  @Test
  public void addAddressEmpty() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("Hash is invalid: null or empty");
    authorizedEu.add(VALID_CODE, EMPTY_STRING);
  }

  @Test
  public void addCodeNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: null or empty");
    authorizedEu.add(null, VALID_ADDRESS);
  }

  @Test
  public void addCodeEmpty() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: null or empty");
    authorizedEu.add(EMPTY_STRING, VALID_ADDRESS);
  }

  @Test
  public void addCodeLengthSup32() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: more than 32 bytes");
    authorizedEu.add(INVALID_CODE, VALID_ADDRESS);
  }

  @Test
  public void addCodeAlreadyExists() throws Exception {
    authorizedEu.add(VALID_CODE, VALID_ADDRESS);
    Optional<Tsl> tsl = authorizedEu.findByCountryCode(VALID_CODE);
    Assert.assertTrue(tsl.isPresent());
    Assert.assertEquals(VALID_ADDRESS, tsl.get().getAddress());

    boolean isThrown = false;
    try {
      authorizedEu.add(VALID_CODE, VALID_ADDRESS);
    } catch (UnauthorizedException e) {
      isThrown = true;
      Assert.assertEquals(
          "Not allowed to proceed this action (you are not allowed to execute this function or TSL "
              + VALID_CODE + " already exists)", e.getMessage());
    } finally {
      safeRemove(VALID_CODE);
      Assert.assertTrue(isThrown);
    }
  }

  @Test
  public void updateUnauthorized() throws Exception {
    authorizedEu.add(VALID_CODE, VALID_ADDRESS);

    boolean isThrown = false;
    try {
      unauthorized.update(VALID_CODE, VALID_ADDRESS2);
    } catch (UnauthorizedException e) {
      isThrown = true;
      Assert.assertEquals(
          "Not allowed to proceed this action (you are not allowed to execute this function or TSL "
              + VALID_CODE + " does not exist)", e.getMessage());
    } finally {
      safeRemove(VALID_CODE);
      Assert.assertTrue(isThrown);
    }
  }

  @Test
  public void updateCodeNotFound() throws Exception {
    thrown.expect(UnauthorizedException.class);
    thrown.expectMessage(
        "Not allowed to proceed this action (you are not allowed to execute this function or TSL "
            + VALID_CODE + " does not exist)");

    authorizedEu.update(VALID_CODE, VALID_ADDRESS);
  }

  @Test
  public void updateCodeNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: null or empty");
    authorizedEu.update(null, VALID_ADDRESS);
  }

  @Test
  public void updateCodeEmpty() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: null or empty");
    authorizedEu.update(EMPTY_STRING, VALID_ADDRESS);
  }

  @Test
  public void updateCodeLengthSup32() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: more than 32 bytes");
    authorizedEu.update(INVALID_CODE, VALID_ADDRESS);
  }

  @Test
  public void updateAddressNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("Hash is invalid: null or empty");
    authorizedEu.update(VALID_CODE, null);
  }

  @Test
  public void updateAddressEmpty() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("Hash is invalid: null or empty");
    authorizedEu.update(VALID_CODE, EMPTY_STRING);
  }

  @Test
  public void removeUnauthorized() throws Exception {
    authorizedEu.add(VALID_CODE, VALID_ADDRESS);

    boolean isThrown = false;
    try {
      unauthorized.remove(VALID_CODE);
    } catch (UnauthorizedException e) {
      isThrown = true;
      Assert.assertEquals(
          "Not allowed to proceed this action (you are not allowed to execute this function or TSL "
              + VALID_CODE + " does not exist)", e.getMessage());
    } finally {
      safeRemove(VALID_CODE);
      Assert.assertTrue(isThrown);
    }
  }

  @Test
  public void removeCodeNotFound() throws Exception {
    thrown.expect(UnauthorizedException.class);
    thrown.expectMessage(
        "Not allowed to proceed this action (you are not allowed to execute this function or TSL "
            + VALID_CODE + " does not exist)");

    authorizedEu.remove(VALID_CODE);
  }

  @Test
  public void removeCodeNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: null or empty");
    authorizedEu.remove(null);
  }

  @Test
  public void removeCodeEmpty() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: null or empty");
    authorizedEu.remove(EMPTY_STRING);
  }

  @Test
  public void removeCodeLengthSup32() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: more than 32 bytes");
    authorizedEu.remove(INVALID_CODE);
  }

  @Test
  public void findByCountryCode() throws Exception {
    authorizedEu.add(VALID_CODE, VALID_ADDRESS);

    Optional<Tsl> tsl = authorizedEu.findByCountryCode(VALID_CODE);
    Assert.assertTrue(tsl.isPresent());
    Assert.assertEquals(BigInteger.ONE, tsl.get().getIndex());
    Assert.assertEquals(VALID_CODE, tsl.get().getCountryCode());
    Assert.assertEquals(VALID_ADDRESS, tsl.get().getAddress());

    safeRemove(VALID_CODE);
  }

  @Test
  public void findByCountryCodeNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: null or empty");
    authorizedEu.findByCountryCode(null);
  }

  @Test
  public void findByCountryCodeEmpty() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: null or empty");
    authorizedEu.findByCountryCode(EMPTY_STRING);
  }

  @Test
  public void findByCountryCodeLengthSup32() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL identifier is invalid: more than 32 bytes");
    authorizedEu.findByCountryCode(INVALID_CODE);
  }

  @Test
  public void findByCountryCodeNotFound() throws Exception {
    Optional<Tsl> tsl = authorizedEu.findByCountryCode(VALID_CODE);
    Assert.assertFalse(tsl.isPresent());
  }

  @Test
  public void findById() throws Exception {
    authorizedEu.add(VALID_CODE, VALID_ADDRESS);

    Optional<Tsl> tsl = authorizedEu.findById(BigInteger.ONE);
    Assert.assertTrue(tsl.isPresent());
    Assert.assertEquals(BigInteger.ONE, tsl.get().getIndex());
    Assert.assertEquals(VALID_CODE, tsl.get().getCountryCode());
    Assert.assertEquals(VALID_ADDRESS, tsl.get().getAddress());

    safeRemove(VALID_CODE);
  }

  @Test
  public void findByIdNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL index is invalid: null");
    authorizedEu.findById(null);
  }

  @Test
  public void findByIdLowerOutOfBounds() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL index is invalid: must be greater than 0");
    authorizedEu.findById(BigInteger.ZERO);
  }

  @Test
  public void findByIdUpperOutOfBounds() throws Exception {
    thrown.expect(InvalidParameterException.class);
    thrown.expectMessage("TSL index is invalid: must be lower than members length (current length is " + authorizedEu.length() + ")");
    authorizedEu.findById(BigInteger.valueOf(100));
  }

  @Test
  public void existsTrue() throws Exception {
    authorizedEu.add(VALID_CODE, VALID_ADDRESS);
    Assert.assertTrue(authorizedEu.exists(VALID_CODE));
    safeRemove(VALID_CODE);
  }

  @Test
  public void existsFalse() throws Exception {
    Assert.assertFalse(authorizedEu.exists(VALID_CODE));
  }

  /**
   * safe remove for other tests
   * @param code country code of the TSL to be removed
   * @throws Exception whenever the TSL cannot be removed or an error occurred with the contract
   */
  private void safeRemove(final String code) throws Exception {
    authorizedEu.remove(code);
    Optional<Tsl> tsl = authorizedEu.findByCountryCode(code);
    Assert.assertFalse(tsl.isPresent());
  }

}
