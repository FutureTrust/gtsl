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

package eu.futuretrust.gtsl.ledger;

import eu.futuretrust.gtsl.ledger.utils.WalletFileUtils;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public abstract class AbstractContract {

  public static final BigInteger GAS_LIMIT = BigInteger.valueOf(3141592L);
  public static final int TSL_CODE_LENGTH = 32;

  protected final Web3j web3j;
  protected final Credentials credentials;

  public AbstractContract(String endpoint, WalletFile wallet, String password)
      throws CipherException {
    this.web3j = Web3j.build(new HttpService(endpoint));
    this.credentials = Credentials.create(Wallet.decrypt(password, wallet));
  }

  public AbstractContract(String endpoint, InputStream keystore, String password)
      throws IOException, CipherException {
    this(endpoint, WalletFileUtils.retrieveWallet(keystore), password);
  }

  public AbstractContract(String endpoint, byte[] keystore, String password)
      throws IOException, CipherException {
    this(endpoint, WalletFileUtils.retrieveWallet(keystore), password);
  }

  /**
   * Load the smart-contract from the blockchain using its address
   *
   * @param address is the address of the smart-contract
   */
  public abstract void load(String address);

  public Web3j getWeb3j() {
    return web3j;
  }

  public Credentials getCredentials() {
    return credentials;
  }

  protected boolean futureIsValid(Future future) throws ExecutionException, InterruptedException {
    return future != null && future.get() != null;
  }
}
