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
import eu.futuretrust.gtsl.ledger.RulesPropertiesWrapper;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import eu.futuretrust.gtsl.storage.Commit;
import eu.futuretrust.gtsl.storage.Storage;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.Optional;
import javax.validation.Validation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesPropertiesHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(RulesPropertiesHandler.class);

  /**
   * storage object allowing to store the data into IPFS
   */
  private final Storage storage;

  /**
   * ethereum contract wrapper allowing to store the IPFS hash into the blockchain
   */
  private final RulesPropertiesWrapper contract;

  /**
   * JSON mapper allowing the data to be converted into JSON format
   */
  private final ObjectMapper mapper;

  /**
   * validator to validate the data content before storing them
   */
  private final Validator validator;

  /**
   * Constructor
   *
   * @param storage storage object allowing to store the data into IPFS
   * @param contract ethereum contract wrapper allowing to store the IPFS hash into the blockchain
   */
  public RulesPropertiesHandler(Storage storage, RulesPropertiesWrapper contract) {
    this.storage = storage;
    this.contract = contract;
    this.mapper = new ObjectMapper();
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  /**
   * Update the rules properties
   *
   * @param rulesProperties rules properties to be added
   * @throws InvalidParameterException whenever the parameters are invalid
   * @throws UnauthorizedException – whenever the user is not authorized to update the properties
   * @throws Exception all others exceptions
   */
  public void update(RulesProperties rulesProperties) throws Exception {
    if (rulesProperties == null || !validator.validate(rulesProperties).isEmpty()) {
      throw new InvalidParameterException("Rules properties are invalid");
    }

    // object to Json
    byte[] data = mapper.writeValueAsBytes(rulesProperties);

    // get the current hash in the contract
    Optional<String> currentHash = contract.get();

    String newHash;
    if (!currentHash.isPresent()) {
      // create new commit of the data into IPFS
      newHash = storage.create(data);
    } else {
      // update existing commit of the data into IPFS
      newHash = storage.update(currentHash.get(), data);
      if (currentHash.get().equals(newHash)) {
        // No changes detected
        if (LOGGER.isInfoEnabled()) {
          LOGGER.info("No changes detected when updating the rules properties");
        }
        return;
      }
    }

    // store IPFS hash into Ethereum smart contract
    contract.update(newHash);
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Rules properties have been updated");
    }
  }

  /**
   * Update the rules properties
   *
   * @param is rules properties to be added
   * @throws InvalidParameterException whenever the parameters are invalid
   * @throws UnauthorizedException – whenever the user is not authorized to update the properties
   * @throws Exception all others exceptions
   */
  public void update(InputStream is) throws Exception {
    RulesProperties rulesProperties = mapper.readValue(is, RulesProperties.class);
    update(rulesProperties);
  }

  /**
   * Update the rules properties
   *
   * @param file rules properties to be added
   * @throws InvalidParameterException whenever the parameters are invalid
   * @throws UnauthorizedException – whenever the user is not authorized to update the properties
   * @throws Exception all others exceptions
   */
  public void update(byte[] file) throws Exception {
    RulesProperties rulesProperties = mapper.readValue(file, RulesProperties.class);
    update(rulesProperties);
  }

  /**
   * Retrieve the rules properties
   *
   * @return rules properties retrieved from Ethereum & IPFS
   * @throws Exception when something went wrong when retrieving the rules properties
   */
  public Optional<RulesProperties> retrieve() throws Exception {
    // retrieve the commit address from Ethereum
    Optional<String> commitAddress = contract.get();
    if (!commitAddress.isPresent()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("The commit address cannot be retrieved from Ethereum");
      }
      return Optional.empty();
    }

    // retrieve the data address by using the commit address from IPFS
    Optional<String> dataAddress = storage.resolve(commitAddress.get());
    if (!dataAddress.isPresent()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("The data address cannot be retrieved from IPFS, (commitAddress={})",
            commitAddress.get());
      }
      return Optional.empty();
    }

    // retrieve the data by using the data address from IPFS
    Optional<byte[]> data = storage.getData(dataAddress.get());
    if (!data.isPresent()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("The data cannot be loaded from IPFS, (dataAddress={})", dataAddress.get());
      }
      return Optional.empty();
    }

    return Optional.ofNullable(mapper.readValue(data.get(), RulesProperties.class));
  }

  /**
   * Return the current version number
   *
   * @return current version number
   * @throws Exception when something went wrong when retrieving the version
   */
  public Optional<BigInteger> currentVersion() throws Exception {
    // retrieve the commit address from Ethereum
    Optional<String> commitAddress = contract.get();
    if (!commitAddress.isPresent()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("The commit address cannot be retrieved from Ethereum");
      }
      return Optional.empty();
    }

    // retrieve the commit by using the commit address from IPFS
    Optional<Commit> commit = storage.getCommit(commitAddress.get());
    if (!commit.isPresent()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("The commit cannot be retrieved from IPFS, (commitAddress={})",
            commitAddress.get());
      }
      return Optional.empty();
    }

    return Optional.of(commit.get().getVersionNumber());
  }

}
