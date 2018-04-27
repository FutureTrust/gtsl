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
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.web3j.abi.datatypes.Address;

public class TestConsortiumFindAllProposals extends AbstractTestConsortium {

  @Test
  public void findAllProposals() throws Exception {
    List<Proposal> proposals;

    proposals = authorized.findAllProposals();
    Assert.assertEquals(0, proposals.size());

    authorized.requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
    proposals = authorized.findAllProposals();
    Assert.assertEquals(1, proposals.size());
    assertProposal0(proposals.get(0));

    authorized.requestRemoveMember(authorizedWallet.getAddress(), BigInteger.ONE);
    proposals = authorized.findAllProposals();
    Assert.assertEquals(2, proposals.size());
    assertProposal0(proposals.get(0));
    assertProposal1(proposals.get(1));
  }

  private void assertProposal0(Proposal proposal) {
    Assert.assertEquals(BigInteger.ZERO, proposal.getIndex());
    Assert.assertEquals(Action.ADD, proposal.getAction());
    Assert.assertEquals(VALID_CODE, proposal.getTslIdentifier());
    Assert.assertEquals(new Address(unauthorizedWallet.getAddress()),
        new Address(proposal.getMember()));
  }

  private void assertProposal1(Proposal proposal) {
    Assert.assertEquals(BigInteger.ONE, proposal.getIndex());
    Assert.assertEquals(Action.REMOVE, proposal.getAction());
    Assert.assertEquals(VALID_CODE, proposal.getTslIdentifier());
    Assert.assertEquals(new Address(authorizedWallet.getAddress()),
        new Address(proposal.getMember()));
  }

}
