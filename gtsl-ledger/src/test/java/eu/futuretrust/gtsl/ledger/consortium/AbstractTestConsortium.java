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

package eu.futuretrust.gtsl.ledger.consortium;

import eu.futuretrust.gtsl.ledger.AbstractTestContract;
import eu.futuretrust.gtsl.ledger.Consortium;
import eu.futuretrust.gtsl.ledger.helper.EthereumConstants;
import eu.futuretrust.gtsl.ledger.helper.ResourcesUtils;
import eu.futuretrust.gtsl.ledger.utils.WalletFileUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public abstract class AbstractTestConsortium extends AbstractTestContract {

  protected static Consortium authorized;
  protected static Consortium unauthorized;

  protected static WalletFile authorizedWallet;
  protected static WalletFile unauthorizedWallet;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @BeforeClass
  public static void init() throws Exception {
    byte[] authorizedKeystore = ResourcesUtils
        .loadBytes(EthereumConstants.AUTHORIZED_KEYSTORE_PATH);
    byte[] unauthorizedKeystore = ResourcesUtils
        .loadBytes(EthereumConstants.UNAUTHORIZED_KEYSTORE_PATH);

    authorizedWallet = WalletFileUtils.retrieveWallet(authorizedKeystore);
    unauthorizedWallet = WalletFileUtils.retrieveWallet(unauthorizedKeystore);

    Web3j web3j = Web3j.build(new HttpService(EthereumConstants.ENDPOINT));
    Credentials credentials = Credentials
        .create(Wallet.decrypt(EthereumConstants.AUTHORIZED_KEYSTORE_PASSWORD, authorizedWallet));

    authorized = new Consortium(EthereumConstants.ENDPOINT, authorizedKeystore,
        EthereumConstants.AUTHORIZED_KEYSTORE_PASSWORD);
    unauthorized = new Consortium(EthereumConstants.ENDPOINT, unauthorizedKeystore,
        EthereumConstants.UNAUTHORIZED_KEYSTORE_PASSWORD);
    String consortiumAddress = deployConsortium(web3j, credentials);
    authorized.load(consortiumAddress);
    unauthorized.load(consortiumAddress);
  }

}
