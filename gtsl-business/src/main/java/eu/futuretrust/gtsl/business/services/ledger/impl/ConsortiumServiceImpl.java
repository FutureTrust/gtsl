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

package eu.futuretrust.gtsl.business.services.ledger.impl;

import eu.futuretrust.gtsl.business.services.ledger.ConsortiumService;
import eu.futuretrust.gtsl.business.utils.DebugUtils;
import eu.futuretrust.gtsl.ledger.Consortium;
import eu.futuretrust.gtsl.ledger.vo.Member;
import eu.futuretrust.gtsl.ledger.vo.Proposal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsortiumServiceImpl implements ConsortiumService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConsortiumServiceImpl.class);

  private final Consortium consortium;

  @Autowired
  public ConsortiumServiceImpl(Consortium consortium) {
    this.consortium = consortium;
  }

  @Override
  public boolean isAuthorized() throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "isAuthorized");

    return consortium.isAuthorized();
  }

  @Override
  public boolean isAuthorized(String address) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "isAuthorized");

    return consortium.isAuthorized(address);
  }

  @Override
  public boolean isSignerTsl(String code) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "isSignerTsl");

    return consortium.isSignerTsl(code);
  }

  @Override
  public boolean isSignerTsl(String address, String code) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "isSignerTsl");

    return consortium.isSignerTsl(address, code);
  }

  @Override
  public String authorizedTsl() throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "authorizedTsl");

    return consortium.authorizedTsl();
  }

  @Override
  public BigInteger requestAddMember(String memberAddress, String tslIdentifier,
      BigInteger duration) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "requestAddMember");

    return consortium.requestAddMember(memberAddress, tslIdentifier, duration);
  }

  @Override
  public BigInteger requestRemoveMember(String memberAddress, BigInteger duration)
      throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "requestRemoveMember");

    return consortium.requestRemoveMember(memberAddress, duration);
  }

  @Override
  public void executeProposal(BigInteger proposalID) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "executeProposal");

    consortium.executeProposal(proposalID);
  }

  @Override
  public void vote(BigInteger proposalID, boolean inSupport) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "vote");

    consortium.vote(proposalID, inSupport);
  }

  @Override
  public List<Member> findAllMembers() throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "findAllMembers");

    return consortium.findAllMembers();
  }

  @Override
  public Optional<Member> findMemberByAddress(String address) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "findMemberByAddress");

    return consortium.findMemberByAddress(address);
  }

  @Override
  public Optional<Member> findMemberById(BigInteger memberId) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "findMemberById");

    return consortium.findMemberById(memberId);
  }

  @Override
  public List<Proposal> findAllProposals() throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "findAllProposals");

    return consortium.findAllProposals();
  }

  @Override
  public Optional<Proposal> findProposalById(BigInteger proposalId) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "findProposalById");

    return consortium.findProposalById(proposalId);
  }

  @Override
  public BigInteger countMembers() throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "countMembers");

    return consortium.countMembers();
  }

  @Override
  public BigInteger countProposals() throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "countProposals");

    return consortium.countProposals();
  }
}
