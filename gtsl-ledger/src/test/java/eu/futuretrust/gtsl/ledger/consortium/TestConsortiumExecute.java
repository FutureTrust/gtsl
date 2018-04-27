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
import java.math.BigInteger;
import java.security.InvalidParameterException;
import org.junit.Assert;
import org.junit.Test;

public class TestConsortiumExecute extends AbstractTestConsortium {

  @Test
  public void executeProposalDeadlineNotExceeded() throws Exception {
    thrown.expect(UnauthorizedException.class);

    BigInteger proposalAddId = authorized
        .requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
    Assert.assertNotNull(proposalAddId);

    authorized.executeProposal(proposalAddId);
  }

  @Test
  public void executeProposalNotAuthorized() throws Exception {
    thrown.expect(UnauthorizedException.class);

    BigInteger proposalAddId = authorized
        .requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
    Assert.assertNotNull(proposalAddId);

    unauthorized.executeProposal(proposalAddId);
  }

  @Test
  public void executeProposalNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.executeProposal(null);
  }

  @Test
  public void executeProposalLowerThan0() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.executeProposal(BigInteger.valueOf(-1));
  }

  @Test
  public void executeProposalGreaterThanLength() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.executeProposal(BigInteger.valueOf(100));
  }

}
