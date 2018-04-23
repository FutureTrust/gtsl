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

import eu.futuretrust.gtsl.ethereum.contracts.ConsortiumContract;
import eu.futuretrust.gtsl.ethereum.utils.StringUtils;
import eu.futuretrust.gtsl.ledger.enums.Action;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import eu.futuretrust.gtsl.ledger.utils.InputValidator;
import eu.futuretrust.gtsl.ledger.vo.Member;
import eu.futuretrust.gtsl.ledger.vo.Proposal;
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
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;

public class Consortium extends AbstractContract {

  private static final Logger LOGGER = LoggerFactory.getLogger(Consortium.class);

  private ConsortiumContract contract;

  public Consortium(String endpoint, InputStream keystore, String password)
      throws IOException, CipherException {
    super(endpoint, keystore, password);
  }

  public Consortium(String endpoint, byte[] keystore, String password)
      throws IOException, CipherException {
    super(endpoint, keystore, password);
  }

  public Consortium(String endpoint, WalletFile wallet, String password)
      throws CipherException {
    super(endpoint, wallet, password);
  }

  public void load(String address) {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Loading ConsortiumContract contract (address={})", address);
    }
    contract = ConsortiumContract
        .load(address, web3j, credentials, Contract.GAS_PRICE, AbstractContract.GAS_LIMIT);
  }

  /**
   * Check if this node is authorized to manage a TSL
   *
   * @return true if the user is part of the consortium
   * @throws Exception all exceptions including exceptions thrown due to an invalid operation on the
   * blockchain
   */
  public boolean isAuthorized() throws Exception {
    return contract.isAuthorized().send();
  }

  /**
   * Check if the address in params is authorized to manage a TSL
   *
   * @param address address of the user
   * @return true if the user is part of the consortium
   * @throws InvalidParameterException whenever the address of the user is empty or null
   * @throws NumberFormatException whenever the address of the user is invalid
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public boolean isAuthorized(String address) throws Exception {
    InputValidator.validateAddress(address);
    return contract.isAuthorized(address).send();
  }

  /**
   * Check if this node is authorized to manage the TSL in params
   *
   * @return true if the user is allowed to manage the TSL @code
   * @throws InvalidParameterException whenever at least one of the params is invalid
   * @throws Exception all exceptions including exceptions thrown due to an invalid operation on the
   * blockchain
   */
  public boolean isSignerTsl(String code) throws Exception {
    InputValidator.validateTslCode(code);
    return contract.isSignerTsl(StringUtils.safeBytes(code, TSL_CODE_LENGTH)).send();
  }

  /**
   * Check if the address in params is authorized to manage the TSL in params
   *
   * @param address address of the user
   * @return true if the user is part of the consortium
   * @throws InvalidParameterException whenever at least one of the params is invalid
   * @throws NumberFormatException whenever the address of the user is invalid
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public boolean isSignerTsl(String address, String code) throws Exception {
    InputValidator.validateAddress(address);
    InputValidator.validateTslCode(code);
    return contract.isSignerTsl(address, StringUtils.safeBytes(code, TSL_CODE_LENGTH)).send();
  }

  /**
   * get the country code for which the node is authorized
   *
   * @return country code of the TSL for which the node is authorized
   * @throws UnauthorizedException whenever the user is not part of the consortium
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public String authorizedTsl() throws Exception {
    if (!isAuthorized()) {
      throw new UnauthorizedException("You are not allowed to execute this function");
    }

    byte[] id = contract.authorizedTsl().send();
    String tslId = StringUtils.safeString(id);

    if (StringUtils.isEmpty(tslId)) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(
            "The member is authorized but the country code of the trusted list for which he is authorized cannot be found in the blockchain, critical error should not happened");
      }
      throw new Exception(
          "Unable to retrieve the country code of the trusted list for which you are authorized");
    }

    return tslId;
  }

  /**
   * Request to add a new member in the consortium
   *
   * @param memberAddress address of the member to be added
   * @param tslId unique code identifying the TSL
   * @param duration duration of the vote in hour
   * @return identifier of the proposal
   * @throws InvalidParameterException whenever at least one of the params is invalid
   * @throws NumberFormatException whenever the address of the user is invalid
   * @throws UnauthorizedException whenever you are not allowed to execute this function or the user
   * is already part of the consortium
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public BigInteger requestAddMember(String memberAddress, String tslId, BigInteger duration)
      throws Exception {
    InputValidator.validateAddress(memberAddress);
    InputValidator.validateTslCode(tslId);
    InputValidator.validateDuration(duration);

    if (!isAuthorized()) {
      throw new UnauthorizedException("You are not allowed to execute this function");
    }

    TransactionReceipt transactionReceipt = contract
        .requestAddMember(memberAddress, StringUtils.safeBytes(tslId, TSL_CODE_LENGTH), duration)
        .send();

    List<ConsortiumContract.ProposalAddedEventResponse> events = contract
        .getProposalAddedEvents(transactionReceipt);
    if (events.isEmpty()) {
      throw new UnauthorizedException(
          "The user to be added is already part of the consortium");
    }

    ConsortiumContract.ProposalAddedEventResponse event = events.get(0);
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Proposal \"{}\""
              + " added by \"{}\" into Ethereum for {} hours; Reason : Add member \"{}\"",
          event.proposalID, event.creator, event.duration, event.member);
    }
    return event.proposalID;
  }

  /**
   * Request to remove a member from the consortium
   *
   * @param memberAddress address of the member to be removed
   * @param duration duration of the vote in hour
   * @return identifier of the proposal
   * @throws InvalidParameterException whenever at least one of the params is invalid
   * @throws NumberFormatException whenever the address of the user is invalid
   * @throws UnauthorizedException whenever you are not allowed to execute this function or the user
   * is not part of the consortium
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public BigInteger requestRemoveMember(String memberAddress, BigInteger duration)
      throws Exception {
    InputValidator.validateAddress(memberAddress);
    InputValidator.validateDuration(duration);

    if (!isAuthorized()) {
      throw new UnauthorizedException("You are not allowed to execute this function");
    }

    TransactionReceipt transactionReceipt = contract
        .requestRemoveMember(memberAddress, duration)
        .send();

    List<ConsortiumContract.ProposalAddedEventResponse> events = contract
        .getProposalAddedEvents(transactionReceipt);
    if (events.isEmpty()) {
      throw new UnauthorizedException(
          "The member to be removed is not part of the consortium");
    }

    ConsortiumContract.ProposalAddedEventResponse event = events.get(0);
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Proposal \"{}\""
              + " added by \"{}\" into Ethereum for {} hours; Reason : Remove member \"{}\"",
          event.proposalID, event.creator, event.duration, event.member);
    }
    return event.proposalID;
  }

  /**
   * Execute an existing proposal if deadline is exceeded
   *
   * @param proposalId is an unique code identifying the proposal
   * @throws InvalidParameterException whenever the proposalId is null or lower than 0
   * @throws UnauthorizedException whenever you are not allowed to execute this function or the
   * voting deadline is not exceeded yet
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public void executeProposal(BigInteger proposalId) throws Exception {
    InputValidator.validateProposalId(proposalId, countProposals());

    if (!isAuthorized()) {
      throw new UnauthorizedException("You are not allowed to execute this function");
    }

    TransactionReceipt transactionReceipt = contract.executeProposal(proposalId).send();

    List<ConsortiumContract.ProposalTalliedEventResponse> events = contract
        .getProposalTalliedEvents(transactionReceipt);
    if (events.isEmpty()) {
      throw new UnauthorizedException(
          "The voting deadline is not exceeded yet");
    }

    ConsortiumContract.ProposalTalliedEventResponse event = events.get(0);
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(
          "Proposal \"{}\" has been executed with the following result [{}; {} voters out of {}; {} votes in support; {} votes against]",
          event.proposalID, (event.result ? "Accepted" : "Rejected"), event.nbOfVoters,
          event.maxVoters, event.votesFor, event.votesAgainst);
    }
  }

  /**
   * Vote in favour or not of the proposal
   *
   * @param proposalId is an unique code identifying the proposal
   * @param inSupport true: vote FOR, false: vote AGAINST
   * @throws InvalidParameterException whenever the proposalId is null or lower than 0
   * @throws UnauthorizedException whenever you are not allowed to execute this function or your
   * vote has already been submitted or the voting deadline is exceeded
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public void vote(BigInteger proposalId, boolean inSupport) throws Exception {
    InputValidator.validateProposalId(proposalId, countProposals());

    if (!isAuthorized()) {
      throw new UnauthorizedException("You are not allowed to execute this function");
    }

    TransactionReceipt transactionReceipt = contract.vote(proposalId, inSupport).send();

    List<ConsortiumContract.VotedEventResponse> events = contract
        .getVotedEvents(transactionReceipt);
    if (events.isEmpty()) {
      throw new UnauthorizedException(
          "Your vote has already been submitted or the voting deadline is exceeded");
    }

    ConsortiumContract.VotedEventResponse event = events.get(0);
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Proposal \"{}\" has received a new vote from \"{}\"", event.proposalID,
          event.voter);
    }
  }

  /**
   * Retrieve a member in the contract using its address
   *
   * @param memberAddress address of the member to be retrieved
   * @return member retrieved from the contract
   * @throws InvalidParameterException whenever the memberAddress is null or empty
   * @throws NumberFormatException whenever the memberAddress is not a valid address
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public Optional<Member> findMemberByAddress(String memberAddress) throws Exception {
    InputValidator.validateAddress(memberAddress);

    if (!isAuthorized(memberAddress)) {
      // not found
      return Optional.empty();
    }

    Tuple4<BigInteger, BigInteger, Boolean, byte[]> result = contract
        .members(memberAddress.trim()).send();
    if (result == null) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Unable to find member at address {} from Ethereum", memberAddress);
      }
      return Optional.empty();
    }

    BigInteger index = result.getValue1();
    BigInteger since = result.getValue2();
    boolean authorized = result.getValue3();
    String tslIdentifier = new String(result.getValue4());
    Member member = new Member(memberAddress, index, since, authorized, tslIdentifier);

    if (!member.isValid()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Unable to find member at address {} from Ethereum", memberAddress);
      }
      return Optional.empty();
    }

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(
          "Member at address \"{}\" has been retrieved from Ethereum (index={}, tslId={}, authorized={})",
          memberAddress, index, tslIdentifier, authorized);
    }
    return Optional.of(member);
  }

  /**
   * Retrieve a member in the contract using its identifier
   *
   * @param memberId index of the member to be retrieved
   * @return member retrieved from the contract
   * @throws InvalidParameterException whenever the memberId is null or out of bounds
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public Optional<Member> findMemberById(BigInteger memberId) throws Exception {
    InputValidator.validateMemberId(memberId, countMembers());

    String address = contract.memberAddresses(memberId).send();
    if (StringUtils.isEmpty(address)) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Unable to find member at index {} from Ethereum", memberId);
      }
      return Optional.empty();
    }
    return findMemberByAddress(address);
  }

  /**
   * Retrieve all members in the contract
   *
   * @return list of members
   * @throws Exception all exceptions including exceptions thrown due to an invalid operation on the
   * blockchain
   */
  public List<Member> findAllMembers() throws Exception {
    BigInteger length = countMembers();

    Stream<BigInteger> stream = Stream.iterate(BigInteger.ZERO, i -> i.add(BigInteger.ONE))
        .limit(length.longValue());

    List<Member> members = stream.parallel()
        .map(i -> {
          try {
            return this.findMemberById(i);
          } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
              LOGGER.error("Unable to find member at index {} from Ethereum: {}", i,
                  e.getMessage());
            }
            return Optional.<Member>empty();
          }
        })
        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
        .collect(Collectors.toList());

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(
          "{} valid member(s) has/have been retrieved from Ethereum, {} member(s) was/were available",
          members.size(), length);
    }
    return members;
  }

  /**
   * Retrieve a proposal in the contract using its identifier
   *
   * @param proposalId index of the proposal to be retrieved
   * @return proposal retrieved from the contract
   * @throws InvalidParameterException whenever the proposalId is null or out of bounds
   * @throws Exception all others exceptions including exceptions thrown due to an invalid operation
   * on the blockchain
   */
  public Optional<Proposal> findProposalById(BigInteger proposalId) throws Exception {
    InputValidator.validateProposalId(proposalId, countProposals());

    Tuple5<Boolean, Boolean, String, byte[], BigInteger> tuple = contract.getProposal(proposalId)
        .send();
    if (tuple == null) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Unable to find proposal at index {} from Ethereum", proposalId);
      }
      return Optional.empty();
    }

    boolean result = tuple.getValue1();
    boolean executed = tuple.getValue2();
    String member = tuple.getValue3();
    String tslIdentifier = new String(tuple.getValue4());
    Action action = Action.valueOf(tuple.getValue5());
    Proposal proposal = new Proposal(proposalId, result, executed, member, tslIdentifier, action);

    if (!proposal.isValid()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Unable to find proposal at index {} from Ethereum", proposalId);
      }
      return Optional.empty();
    }

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(
          "Proposal at index \"{}\" has been retrieved from Ethereum (member={}, tslId={}, executed={}, result={}, action={})",
          proposalId, member, tslIdentifier, executed, result, action);
    }
    return Optional.of(proposal);
  }

  /**
   * Retrieve all proposals in the contract
   *
   * @return list of proposals
   * @throws Exception all exceptions including exceptions thrown due to an invalid operation on the
   * blockchain
   */
  public List<Proposal> findAllProposals() throws Exception {
    BigInteger length = countProposals();

    Stream<BigInteger> stream = Stream.iterate(BigInteger.ZERO, i -> i.add(BigInteger.ONE))
        .limit(length.longValue());

    List<Proposal> proposals = stream.parallel()
        .map(i -> {
          try {
            return this.findProposalById(i);
          } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
              LOGGER.error("Unable to find proposal at index {} from Ethereum: {}", i,
                  e.getMessage());
            }
            return Optional.<Proposal>empty();
          }
        })
        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
        .collect(Collectors.toList());

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(
          "{} valid proposal(s) has/have been retrieved from Ethereum, {} proposal(s) was/were available",
          proposals.size(), length);
    }
    return proposals;
  }

  /**
   * Count the number of proposals
   *
   * @return number of proposals
   * @throws Exception all exceptions including exceptions thrown due to an invalid operation on the
   * blockchain
   */
  public BigInteger countProposals() throws Exception {
    return contract.countProposals().send();
  }

  /**
   * Count the number of members
   *
   * @return number of members
   * @throws Exception all exceptions including exceptions thrown due to an invalid operation on the
   * blockchain
   */
  public BigInteger countMembers() throws Exception {
    return contract.countMembers().send();
  }

}
