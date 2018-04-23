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

package eu.futuretrust.gtsl.business.services.ledger;

import eu.futuretrust.gtsl.ledger.vo.Member;
import eu.futuretrust.gtsl.ledger.vo.Proposal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface ConsortiumService {

  boolean isAuthorized() throws Exception;

  boolean isAuthorized(String address) throws Exception;

  boolean isSignerTsl(String code) throws Exception;

  boolean isSignerTsl(String address, String code) throws Exception;

  String authorizedTsl() throws Exception;

  BigInteger requestAddMember(String memberAddress, String tslIdentifier, BigInteger durationInDays)
      throws Exception;

  BigInteger requestRemoveMember(String memberAddress, BigInteger duration) throws Exception;

  void executeProposal(BigInteger proposalID) throws Exception;

  void vote(BigInteger proposalID, boolean inSupport) throws Exception;

  List<Member> findAllMembers() throws Exception;

  Optional<Member> findMemberByAddress(String address) throws Exception;

  Optional<Member> findMemberById(BigInteger memberId) throws Exception;

  List<Proposal> findAllProposals() throws Exception;

  Optional<Proposal> findProposalById(BigInteger proposalId) throws Exception;

  BigInteger countMembers() throws Exception;

  BigInteger countProposals() throws Exception;
}
