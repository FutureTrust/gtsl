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

import eu.futuretrust.gtsl.ledger.enums.Action;
import eu.futuretrust.gtsl.ledger.vo.Proposal;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.web3j.abi.datatypes.Address;

public class TestConsortiumFindProposalById extends AbstractTestConsortium {

  @Test
  public void findProposalById() throws Exception {
    BigInteger proposalId0 = authorized
        .requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
    BigInteger proposalId1 = authorized
        .requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
    authorized.vote(proposalId1, true);
    BigInteger proposalId2 = authorized
        .requestRemoveMember(unauthorizedWallet.getAddress(), BigInteger.ONE);

    Optional<Proposal> proposal0 = authorized.findProposalById(BigInteger.ZERO);
    Assert.assertTrue(proposal0.isPresent());
    Assert.assertTrue(proposal0.get().isValid());
    Assert.assertFalse(proposal0.get().isResult());
    Assert.assertFalse(proposal0.get().isExecuted());
    Assert.assertEquals(new Address(unauthorizedWallet.getAddress()),
        new Address(proposal0.get().getMember()));
    Assert.assertEquals(VALID_CODE, proposal0.get().getTslIdentifier());
    Assert.assertEquals(BigInteger.ZERO, proposal0.get().getIndex());
    Assert.assertEquals(proposalId0, proposal0.get().getIndex());
    Assert.assertEquals(Action.ADD, proposal0.get().getAction());

    Optional<Proposal> proposal1 = authorized.findProposalById(proposalId1);
    Assert.assertTrue(proposal1.isPresent());
    Assert.assertTrue(proposal1.get().isValid());
    Assert.assertTrue(proposal1.get().isResult());
    Assert.assertTrue(proposal1.get().isExecuted());
    Assert.assertEquals(new Address(unauthorizedWallet.getAddress()),
        new Address(proposal1.get().getMember()));
    Assert.assertEquals(VALID_CODE, proposal1.get().getTslIdentifier());
    Assert.assertEquals(BigInteger.ONE, proposal1.get().getIndex());
    Assert.assertEquals(proposalId1, proposal1.get().getIndex());
    Assert.assertEquals(Action.ADD, proposal1.get().getAction());

    Optional<Proposal> proposal2 = authorized.findProposalById(proposalId2);
    Assert.assertTrue(proposal2.isPresent());
    Assert.assertTrue(proposal2.get().isValid());
    Assert.assertFalse(proposal2.get().isResult());
    Assert.assertFalse(proposal2.get().isExecuted());
    Assert.assertEquals(new Address(unauthorizedWallet.getAddress()),
        new Address(proposal2.get().getMember()));
    Assert.assertEquals(VALID_CODE, proposal2.get().getTslIdentifier());
    Assert.assertEquals(BigInteger.valueOf(2), proposal2.get().getIndex());
    Assert.assertEquals(proposalId2, proposal2.get().getIndex());
    Assert.assertEquals(Action.REMOVE, proposal2.get().getAction());
  }

  @Test
  public void findProposalByIdLowerThan0() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.findProposalById(BigInteger.valueOf(-1));
  }

  @Test
  public void findProposalByIdGreaterThanLength() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.findProposalById(BigInteger.valueOf(100));
  }

  @Test
  public void findProposalByIdNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.findProposalById(null);
  }

}
