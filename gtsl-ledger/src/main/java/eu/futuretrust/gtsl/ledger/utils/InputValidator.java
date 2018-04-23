package eu.futuretrust.gtsl.ledger.utils;

import eu.futuretrust.gtsl.ethereum.utils.StringUtils;
import eu.futuretrust.gtsl.ledger.AbstractContract;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import org.web3j.abi.datatypes.Address;

public final class InputValidator {

  private InputValidator() {
  }

  /**
   * Throws InvalidParameterException if the provided address is invalid
   *
   * @param address address to be validated
   * @throws InvalidParameterException whenever the provided parameter is invalid
   */
  public static void validateAddress(String address) {
    if (StringUtils.isEmpty(address)) {
      throw new InvalidParameterException("Address is invalid: null or empty");
    }
    try {
      new Address(address);
    } catch (NumberFormatException e) {
      throw new InvalidParameterException("Address is invalid: invalid format");
    }
  }

  /**
   * Throws InvalidParameterException if the provided hash is invalid
   *
   * @param hash hash to be validated
   * @throws InvalidParameterException whenever the provided parameter is invalid
   */
  public static void validateHash(String hash) {
    if (StringUtils.isEmpty(hash)) {
      throw new InvalidParameterException("Hash is invalid: null or empty");
    }
  }

  /**
   * Throws InvalidParameterException if the provided country code is invalid
   *
   * @param countryCode country code to be validated
   * @throws InvalidParameterException whenever the provided parameter is invalid
   */
  public static void validateTslCode(String countryCode) {
    if (StringUtils.isEmpty(countryCode)) {
      throw new InvalidParameterException("TSL identifier is invalid: null or empty");
    } else if (countryCode.trim().getBytes().length > AbstractContract.TSL_CODE_LENGTH) {
      throw new InvalidParameterException("TSL identifier is invalid: more than 32 bytes");
    }
  }

  /**
   * Throws InvalidParameterException if the provided tsl index is invalid
   *
   * @param tslId index to be validated
   * @param length maximum value of the index (length of the list of tsls)
   * @throws InvalidParameterException whenever the provided parameter is invalid
   */
  public static void validateTslId(BigInteger tslId, BigInteger length) {
    if (tslId == null) {
      throw new InvalidParameterException("TSL index is invalid: null");
    } else if (tslId.signum() <= 0) {
      throw new InvalidParameterException("TSL index is invalid: must be greater than 0");
    } else {
      if (tslId.compareTo(length) > 0) {
        throw new InvalidParameterException(
            "TSL index is invalid: must be lower than members length (current length is " + length
                + ")");
      }
    }
  }

  /**
   * Throws InvalidParameterException if the provided proposal index is invalid
   *
   * @param proposalId index to be validated
   * @param length maximum value of the index (length of the list of proposals)
   * @throws InvalidParameterException whenever the provided parameter is invalid
   */
  public static void validateProposalId(BigInteger proposalId, BigInteger length) {
    if (proposalId == null) {
      throw new InvalidParameterException("ProposalId is invalid: null");
    } else if (proposalId.signum() < 0) {
      throw new InvalidParameterException(
          "ProposalId is invalid: must be greater than or equals to 0");
    } else {
      if (proposalId.compareTo(length) >= 0) {
        throw new InvalidParameterException(
            "ProposalId is invalid: must be lower than proposals length (current length is "
                + length + ")");
      }
    }
  }

  /**
   * Throws InvalidParameterException if the provided member index is invalid
   *
   * @param memberId index to be validated
   * @param length maximum value of the index (length of the list of members)
   * @throws InvalidParameterException whenever the provided parameter is invalid
   */
  public static void validateMemberId(BigInteger memberId, BigInteger length) {
    if (memberId == null) {
      throw new InvalidParameterException("MemberId is invalid: null");
    } else if (memberId.signum() < 0) {
      throw new InvalidParameterException(
          "MemberId is invalid: must be greater than or equals to 0");
    } else {
      if (memberId.compareTo(length) >= 0) {
        throw new InvalidParameterException(
            "MemberId is invalid: must be lower than members length (current length is " + length
                + ")");
      }
    }
  }

  /**
   * Throws InvalidParameterException if the provided member index is null or lower than 1
   *
   * @param duration duration to be validated
   * @throws InvalidParameterException whenever the provided parameter is invalid
   */
  public static void validateDuration(BigInteger duration) {
    if (duration == null) {
      throw new InvalidParameterException("Duration is invalid: null");
    } else if (duration.signum() < 1) {
      throw new InvalidParameterException("Duration is invalid: must be greater than 0");
    }
  }
}
