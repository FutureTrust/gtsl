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

package eu.futuretrust.gtsl.ledger.properties;

import eu.futuretrust.gtsl.ledger.AbstractTestContract;
import eu.futuretrust.gtsl.ledger.RulesPropertiesWrapper;
import eu.futuretrust.gtsl.ledger.helper.EthereumConstants;
import eu.futuretrust.gtsl.ledger.helper.ResourcesUtils;
import eu.futuretrust.gtsl.ledger.utils.WalletFileUtils;
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

public class TestAuthorizedRulesPropertiesWrapper extends AbstractTestContract {

  private static RulesPropertiesWrapper authorized;

  @BeforeClass
  public static void init() throws Exception {
    byte[] authorizedKeystore = ResourcesUtils
        .loadBytes(EthereumConstants.AUTHORIZED_KEYSTORE_PATH);

    Web3j web3j = Web3j.build(new HttpService(EthereumConstants.ENDPOINT));
    WalletFile wallet = WalletFileUtils.retrieveWallet(authorizedKeystore);
    Credentials credentials = Credentials
        .create(Wallet.decrypt(EthereumConstants.AUTHORIZED_KEYSTORE_PASSWORD, wallet));

    String consortiumAddress = deployConsortium(web3j, credentials);
    String propertiesAddress = deployRulesProperties(web3j, credentials, consortiumAddress);

    authorized = new RulesPropertiesWrapper(EthereumConstants.ENDPOINT, authorizedKeystore,
        EthereumConstants.AUTHORIZED_KEYSTORE_PASSWORD);

    authorized.load(propertiesAddress);
  }

  @Test
  public void fullProcess() throws Exception {
    Optional<String> hash = authorized.get();
    Assert.assertFalse(hash.isPresent());

    authorized.update(VALID_ADDRESS);

    Optional<String> hash2 = authorized.get();
    Assert.assertTrue(hash2.isPresent());
    Assert.assertEquals(VALID_ADDRESS, hash2.get());

    authorized.update(VALID_ADDRESS2);

    Optional<String> hash3 = authorized.get();
    Assert.assertTrue(hash3.isPresent());
    Assert.assertEquals(VALID_ADDRESS2, hash3.get());
  }

}
