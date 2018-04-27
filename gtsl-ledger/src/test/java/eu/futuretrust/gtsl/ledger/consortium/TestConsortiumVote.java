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

import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import eu.futuretrust.gtsl.ledger.vo.Proposal;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;

public class TestConsortiumVote extends AbstractTestConsortium {

  @Test
  public void voteNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    unauthorized.vote(null, true);
  }

  @Test
  public void voteLowerThan0() throws Exception {
    thrown.expect(InvalidParameterException.class);
    unauthorized.vote(BigInteger.valueOf(-1), true);
  }

  @Test
  public void voteGreaterThanLength() throws Exception {
    thrown.expect(InvalidParameterException.class);
    unauthorized.vote(BigInteger.valueOf(100), true);
  }

  @Test
  public void voteUnauthorized() throws Exception {
    thrown.expect(UnauthorizedException.class);

    BigInteger proposalAddId = authorized
        .requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
    Assert.assertNotNull(proposalAddId);

    unauthorized.vote(proposalAddId, true);
  }

  @Test
  public void voteFor() throws Exception {
    BigInteger proposalAddId = authorized
        .requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
    Assert.assertNotNull(proposalAddId);

    authorized.vote(proposalAddId, true);
    Optional<Proposal> proposalAdd = authorized.findProposalById(proposalAddId);
    Assert.assertTrue(proposalAdd.isPresent());
    Assert.assertTrue(proposalAdd.get().isExecuted());
    Assert.assertTrue(proposalAdd.get().isResult());
    Assert.assertTrue(unauthorized.isAuthorized());
    Assert.assertEquals(2, authorized.countMembers().longValue());

    BigInteger proposalRmId = authorized
        .requestRemoveMember(unauthorizedWallet.getAddress(), BigInteger.ONE);
    Assert.assertNotNull(proposalRmId);

    authorized.vote(proposalRmId, true);
    Optional<Proposal> proposalRm1 = authorized.findProposalById(proposalRmId);
    Assert.assertTrue(proposalRm1.isPresent());
    Assert.assertFalse(proposalRm1.get().isExecuted());
    Assert.assertFalse(proposalRm1.get().isResult());
    Assert.assertTrue(unauthorized.isAuthorized());
    Assert.assertEquals(2, authorized.countMembers().longValue());

    unauthorized.vote(proposalRmId, true);
    Optional<Proposal> proposalRm2 = authorized.findProposalById(proposalRmId);
    Assert.assertTrue(proposalRm2.isPresent());
    Assert.assertTrue(proposalRm2.get().isExecuted());
    Assert.assertTrue(proposalRm2.get().isResult());
    Assert.assertFalse(unauthorized.isAuthorized());
    Assert.assertEquals(1, authorized.countMembers().longValue());
  }

  @Test
  public void voteAgainst() throws Exception {
    BigInteger proposalAddId = authorized
        .requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
    Assert.assertNotNull(proposalAddId);

    authorized.vote(proposalAddId, false);
    Optional<Proposal> proposal = authorized.findProposalById(proposalAddId);
    Assert.assertTrue(proposal.isPresent());
    Assert.assertTrue(proposal.get().isExecuted());
    Assert.assertFalse(proposal.get().isResult());
    Assert.assertFalse(unauthorized.isAuthorized());
    Assert.assertEquals(1, authorized.countMembers().longValue());
  }

  @Test
  public void voteTwice() throws Exception {
    BigInteger proposalAddId = authorized
        .requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
    Assert.assertNotNull(proposalAddId);

    authorized.vote(proposalAddId, true);
    Assert.assertTrue(unauthorized.isAuthorized());
    Assert.assertEquals(2, authorized.countMembers().longValue());

    BigInteger proposalRmId = authorized
        .requestRemoveMember(unauthorizedWallet.getAddress(), BigInteger.ONE);
    Assert.assertNotNull(proposalRmId);

    authorized.vote(proposalRmId, true);
    Assert.assertEquals(2, authorized.countMembers().longValue());

    boolean isThrown = false;
    try {
      authorized.vote(proposalRmId, true);
    } catch (UnauthorizedException e) {
      isThrown = true;
      Assert.assertEquals("Your vote has already been submitted or the voting deadline is exceeded",
          e.getMessage());
    } finally {
      unauthorized.vote(proposalRmId, true);
      Assert.assertFalse(unauthorized.isAuthorized());
      Assert.assertEquals(1, authorized.countMembers().longValue());
      Assert.assertTrue(isThrown);
    }
  }

}
