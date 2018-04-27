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

import eu.futuretrust.gtsl.ethereum.contracts.RulesPropertiesContract;
import eu.futuretrust.gtsl.ethereum.utils.StringUtils;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;

public class RulesPropertiesWrapper extends AbstractContract {

  private static final Logger LOGGER = LoggerFactory.getLogger(RulesPropertiesWrapper.class);

  private RulesPropertiesContract contract;

  public RulesPropertiesWrapper(String endpoint, InputStream keystore, String password)
      throws IOException, CipherException {
    super(endpoint, keystore, password);
  }

  public RulesPropertiesWrapper(String endpoint, byte[] keystore, String password)
      throws IOException, CipherException {
    super(endpoint, keystore, password);
  }

  public RulesPropertiesWrapper(String endpoint, WalletFile wallet, String password)
      throws CipherException {
    super(endpoint, wallet, password);
  }

  public void load(String address) {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Loading RulesProperties contract (address={})", address);
    }
    contract = RulesPropertiesContract
        .load(address, web3j, credentials, Contract.GAS_PRICE, AbstractContract.GAS_LIMIT);
  }

  /**
   * Update the properties
   *
   * @param hash is the address to which the properties are stored
   * @throws InvalidParameterException whenever the hash is null or empty
   * @throws UnauthorizedException whenever the user is not authorized to update the properties
   * address
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public void update(String hash) throws Exception {
    if (hash == null) {
      throw new InvalidParameterException("Hash is null");
    }
    String trimmedHash = hash.trim();
    if (trimmedHash.isEmpty()) {
      throw new InvalidParameterException("Hash is empty");
    }

    // create a transaction to update the hash value
    TransactionReceipt transactionReceipt = contract.update(trimmedHash.getBytes()).send();
    List<RulesPropertiesContract.UpdatedEventResponse> events = contract
        .getUpdatedEvents(transactionReceipt);
    if (events.isEmpty()) {
      throw new UnauthorizedException(
          "Not allowed to proceed this action (you are not allowed to execute this function)");
    }
    RulesPropertiesContract.UpdatedEventResponse event = events.get(0);
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("RulesProperties updated (hash={}), by user \"{}\"",
          StringUtils.safeString(event.hash), event.user);
    }
  }

  /**
   * Get the address to which the properties are stored
   *
   * @return address retrieved from the smart-contract
   * @throws Exception all exceptions including exceptions thrown due to an invalid operation on the
   * blockchain
   */
  public Optional<String> get() throws Exception {
    byte[] hash = contract.get().send();
    String hashStr = StringUtils.safeString(hash);
    if (StringUtils.isEmpty(hashStr)) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Unable to retrieve rules properties");
      }
      return Optional.empty();
    }
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Rules properties has been retrieved from Ethereum at address {}", hashStr);
    }
    return Optional.of(hashStr);
  }

}
