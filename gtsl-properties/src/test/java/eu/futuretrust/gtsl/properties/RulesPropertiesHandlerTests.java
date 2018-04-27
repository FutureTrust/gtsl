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

package eu.futuretrust.gtsl.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.futuretrust.gtsl.ethereum.contracts.ConsortiumContract;
import eu.futuretrust.gtsl.ethereum.contracts.RulesPropertiesContract;
import eu.futuretrust.gtsl.ethereum.utils.StringUtils;
import eu.futuretrust.gtsl.ledger.RulesPropertiesWrapper;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import eu.futuretrust.gtsl.ledger.utils.WalletFileUtils;
import eu.futuretrust.gtsl.properties.helper.EthereumConstants;
import eu.futuretrust.gtsl.properties.helper.IpfsConstants;
import eu.futuretrust.gtsl.properties.helper.ResourcesUtils;
import eu.futuretrust.gtsl.properties.helper.RulesPropertiesConstants;
import eu.futuretrust.gtsl.properties.rules.Country;
import eu.futuretrust.gtsl.storage.Storage;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

public class RulesPropertiesHandlerTests {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static final BigInteger GAS_LIMIT = BigInteger.valueOf(3141592L);
  private static final String VALID_CODE = "EU";

  private static Storage storage;
  private static RulesPropertiesWrapper authorizedWrapper;
  private static RulesPropertiesWrapper unauthorizedWrapper;
  private static byte[] propertiesFile;

  private static String deployConsortium(Web3j web3j, Credentials credentials, BigInteger quorum,
      byte[] code) throws Exception {
    ConsortiumContract consortiumContract = ConsortiumContract
        .deploy(web3j, credentials, Contract.GAS_PRICE, GAS_LIMIT, quorum, code)
        .send();
    return consortiumContract.getContractAddress();
  }

  private static String deployRulesProperties(Web3j web3j, Credentials credentials,
      String consortiumAddress) throws Exception {
    RulesPropertiesContract rulesPropertiesContract = RulesPropertiesContract
        .deploy(web3j, credentials, Contract.GAS_PRICE, GAS_LIMIT, consortiumAddress).send();
    return rulesPropertiesContract.getContractAddress();
  }


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

    String consortiumAddress = deployConsortium(web3j, credentials, BigInteger.valueOf(70),
        StringUtils.safeBytes(VALID_CODE, 32));
    String propertiesAddress = deployRulesProperties(web3j, credentials, consortiumAddress);

    authorizedWrapper = new RulesPropertiesWrapper(EthereumConstants.ENDPOINT, authorizedKeystore,
        EthereumConstants.AUTHORIZED_KEYSTORE_PASSWORD);
    authorizedWrapper.load(propertiesAddress);

    unauthorizedWrapper = new RulesPropertiesWrapper(EthereumConstants.ENDPOINT, unauthorizedKeystore,
        EthereumConstants.UNAUTHORIZED_KEYSTORE_PASSWORD);
    unauthorizedWrapper.load(propertiesAddress);

    propertiesFile = ResourcesUtils.loadBytes(RulesPropertiesConstants.PROPERTIES_FILE);

    storage = new Storage(IpfsConstants.ENDPOINT);
  }

  @Test
  public void authorized() throws Exception {
    RulesPropertiesHandler handler = new RulesPropertiesHandler(storage, authorizedWrapper);
    handler.update(propertiesFile);

    Optional<RulesProperties> result = handler.retrieve();
    Assert.assertTrue(result.isPresent());

    ObjectMapper mapper = new ObjectMapper();
    RulesProperties rulesProperties = mapper.readValue(propertiesFile, RulesProperties.class);

    Assert.assertEquals(rulesProperties, result.get());
  }

  @Test
  public void unauthorized() throws Exception {
    thrown.expect(UnauthorizedException.class);

    ObjectMapper mapper = new ObjectMapper();
    RulesProperties rulesProperties = mapper.readValue(propertiesFile, RulesProperties.class);

    // change a value to have a difference with the properties used in authorized() test
    Country random = new Country();
    random.setLang(new HashSet<>(Arrays.asList("ra","re")));
    random.setName("random");
    rulesProperties.getCountries().put("RAN", random);

    RulesPropertiesHandler handler = new RulesPropertiesHandler(storage, unauthorizedWrapper);
    handler.update(rulesProperties);
  }

  @Test
  public void updateNull() throws Exception {
    thrown.expect(InvalidParameterException.class);

    RulesPropertiesHandler handler = new RulesPropertiesHandler(storage, authorizedWrapper);
    RulesProperties properties = null;
    handler.update(properties);
  }

  @Test
  public void updateContainingNull() throws Exception {
    thrown.expect(InvalidParameterException.class);

    ObjectMapper mapper = new ObjectMapper();
    RulesProperties rulesProperties = mapper.readValue(propertiesFile, RulesProperties.class);
    rulesProperties.setCountries(null);

    RulesPropertiesHandler handler = new RulesPropertiesHandler(storage, authorizedWrapper);
    handler.update(rulesProperties);
  }

}
