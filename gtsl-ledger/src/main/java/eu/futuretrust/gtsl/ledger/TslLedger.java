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

import eu.futuretrust.gtsl.ethereum.contracts.TslStoreContract;
import eu.futuretrust.gtsl.ethereum.utils.StringUtils;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import eu.futuretrust.gtsl.ledger.utils.InputValidator;
import eu.futuretrust.gtsl.ledger.vo.Tsl;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;

public class TslLedger extends AbstractContract {

  private static final Logger LOGGER = LoggerFactory.getLogger(TslLedger.class);

  private TslStoreContract contract;

  public TslLedger(final String endpoint, final InputStream keystore, final String password)
      throws IOException, CipherException {
    super(endpoint, keystore, password);
  }

  public TslLedger(final String endpoint, final byte[] keystore, final String password)
      throws IOException, CipherException {
    super(endpoint, keystore, password);
  }

  public TslLedger(final String endpoint, final WalletFile wallet, final String password)
      throws CipherException {
    super(endpoint, wallet, password);
  }

  public void load(final String address) {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Loading TslLedger contract (address={})", address);
    }
    contract = TslStoreContract
        .load(address, web3j, credentials, Contract.GAS_PRICE, AbstractContract.GAS_LIMIT);
  }

  /**
   * Add a new TSL in the smart-contract
   *
   * @param code is an unique code identifying the TSL
   * @param hash is the address to which the information of the TSL are stored
   * @throws InvalidParameterException whenever at least one of the params is invalid
   * @throws UnauthorizedException whenever the TSL already exists or the user is not authorized to add
   * this TSL
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public void add(final String code, final String hash) throws Exception {
    InputValidator.validateTslCode(code);
    InputValidator.validateHash(hash);

    // create a transaction to add a new TSL
    TransactionReceipt transactionReceipt = contract
        .addTsl(StringUtils.safeBytes(code, TSL_CODE_LENGTH), hash.getBytes()).send();
    List<TslStoreContract.TslAddingEventResponse> events = contract
        .getTslAddingEvents(transactionReceipt);
    if (events.isEmpty()) {
      throw new UnauthorizedException(
          "Not allowed to proceed this action (you are not allowed to execute this function or TSL "
              + code + " already exists)");
    }

    TslStoreContract.TslAddingEventResponse event = events.get(0);
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Tsl \"{}\" has been added into Ethereum at the address \"{}\", by user \"{}\"",
          StringUtils.safeString(event.code), StringUtils.safeString(event.hash), event.user);
    }
  }

  /**
   * Update an existing TSL in the smart-contract
   *
   * @param code is an unique code identifying the TSL
   * @param hash is the address to which the information of the TSL are stored
   * @throws InvalidParameterException whenever at least one of the params is invalid
   * @throws UnauthorizedException whenever the TSL does not exist or the user is not authorized to
   * update this TSL
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public void update(final String code, final String hash) throws Exception {
    InputValidator.validateTslCode(code);
    InputValidator.validateHash(hash);

    // create a transaction to update an existing TSL
    TransactionReceipt transactionReceipt = contract
        .updateTsl(StringUtils.safeBytes(code, TSL_CODE_LENGTH), hash.getBytes()).send();
    List<TslStoreContract.TslUpdatingEventResponse> events = contract
        .getTslUpdatingEvents(transactionReceipt);
    if (events.isEmpty()) {
      throw new UnauthorizedException(
          "Not allowed to proceed this action (you are not allowed to execute this function or TSL "
              + code + " does not exist)");
    }

    TslStoreContract.TslUpdatingEventResponse event = events.get(0);
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Tsl \"{}\" has been updated into Ethereum at the address \"{}\", by user \"{}\"",
          StringUtils.safeString(event.code), StringUtils.safeString(event.hash), event.user);
    }
  }

  /**
   * Remove an existing TSL in the smart-contract
   *
   * @param code is an unique code identifying the TSL
   * @throws InvalidParameterException whenever at least one of the params is invalid
   * @throws UnauthorizedException whenever the TSL does not exist or the user is not authorized to
   * update this TSL
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public void remove(final String code) throws Exception {
    InputValidator.validateTslCode(code);

    // create a transaction to remove an existing TSL
    TransactionReceipt transactionReceipt = contract
        .removeTsl(StringUtils.safeBytes(code, TSL_CODE_LENGTH)).send();
    List<TslStoreContract.TslRemovingEventResponse> events = contract
        .getTslRemovingEvents(transactionReceipt);
    if (events.isEmpty()) {
      throw new UnauthorizedException(
          "Not allowed to proceed this action (you are not allowed to execute this function or TSL "
              + code + " does not exist)");
    }
    TslStoreContract.TslRemovingEventResponse event = events.get(0);
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Tsl \"{}\" has been removed from Ethereum, by user \"{}\"",
          StringUtils.safeString(event.code), event.user);
    }
  }

  /**
   * Read the smart-contract and return an existing TSL based on the code provided
   *
   * @param code is an unique code identifying the TSL
   * @return the TSL retrieved from the smart-contract
   * @throws InvalidParameterException whenever at least one of the params is invalid
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public Optional<Tsl> findByCountryCode(final String code) throws Exception {
    InputValidator.validateTslCode(code);

    // read in the smart-contract the current address for a given code
    Tuple2<BigInteger, byte[]> tslTuple = contract
        .getTsl(StringUtils.safeBytes(code, TSL_CODE_LENGTH)).send();
    if (tslTuple == null) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Unable to find TSL with country code {} from Ethereum", code);
      }
      return Optional.empty();
    }

    BigInteger index = tslTuple.getValue1();
    String address = StringUtils.safeString(tslTuple.getValue2());
    Tsl tsl = new Tsl(index, code, address);
    if (!tsl.isValid()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Unable to find TSL with country code {} from Ethereum", code);
      }
      return Optional.empty();
    }

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Tsl \"{}\" has been retrieved from Ethereum (index={}, address={})", code, index,
          address);
    }
    return Optional.of(tsl);
  }

  /**
   * Read the smart-contract and return an existing TSL based on the id provided
   *
   * @param index is the index of the tsl to be retrieved
   * @return the TSL retrieved from the smart-contract
   * @throws InvalidParameterException whenever at least one of the params is invalid
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public Optional<Tsl> findById(final BigInteger index) throws Exception {
    InputValidator.validateTslId(index, length());

    // read in the smart-contract the current address for a given id
    Tuple2<byte[], byte[]> tslTuple = contract.getTsl(index).send();
    if (tslTuple == null) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Unable to find TSL at index {} from Ethereum", index);
      }
      return Optional.empty();
    }

    String code = StringUtils.safeString(tslTuple.getValue1());
    String address = StringUtils.safeString(tslTuple.getValue2());
    Tsl tsl = new Tsl(index, code, address);
    if (!tsl.isValid()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Unable to find TSL at index {} from Ethereum", index);
      }
      return Optional.empty();
    }

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Tsl at index \"{}\" has been retrieved from Ethereum (CC={}, address={})", index,
          code, address);
    }
    return Optional.of(tsl);
  }

  /**
   * Read the smart-contract and return all existing TSLs
   *
   * @return list of the TSLs retrieved from the smart-contract
   * @throws Exception all exceptions including exceptions thrown due to an invalid operation on the
   * blockchain
   */
  public List<Tsl> findAll() throws Exception {
    BigInteger length = length();

    // index starts at 1 because of the (mandatory) empty value at index 0 in the smart contract
    Stream<BigInteger> stream = Stream.iterate(BigInteger.ONE, i -> i.add(BigInteger.ONE))
        .limit(length.longValue());

    List<Tsl> tslList = stream.parallel()
        .map(i -> {
          try {
            return this.findById(i);
          } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
              LOGGER.error("Unable to find TSL at index {} from Ethereum: {}", i,
                  e.getMessage());
            }
            return Optional.<Tsl>empty();
          }
        })
        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
        .collect(Collectors.toList());

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(
          "{} valid TSL(s) has/have been retrieved from Ethereum, {} TSL(s) was/were available",
          tslList.size(), length);
    }
    return tslList;
  }

  public BigInteger length() throws Exception {
    return contract.length().send();
  }

  public boolean exists(final String code) throws Exception {
    InputValidator.validateTslCode(code);
    return contract.exists(StringUtils.safeBytes(code, TSL_CODE_LENGTH)).send();
  }

}
