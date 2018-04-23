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

public class TestConsortiumRemove extends AbstractTestConsortium {

  @Test
  public void requestRemoveMember() throws Exception {
    BigInteger proposalAddId = authorized
        .requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
    Assert.assertNotNull(proposalAddId);
    Assert.assertEquals(1, authorized.countProposals().longValue());

    authorized.vote(proposalAddId, true);
    Assert.assertEquals(2, authorized.countMembers().longValue());
    Assert.assertTrue(unauthorized.isAuthorized());

    BigInteger proposalRmId = authorized
        .requestRemoveMember(unauthorizedWallet.getAddress(), BigInteger.ONE);
    Assert.assertNotNull(proposalRmId);
    Assert.assertEquals(2, authorized.countProposals().longValue());

    authorized.vote(proposalRmId, true);
    unauthorized.vote(proposalRmId, true);
    Assert.assertEquals(1, authorized.countMembers().longValue());
    Assert.assertFalse(unauthorized.isAuthorized());
  }

  @Test
  public void requestRemoveMemberNotExist() throws Exception {
    thrown.expect(UnauthorizedException.class);
    authorized.requestRemoveMember(unauthorizedWallet.getAddress(), BigInteger.ONE);
  }

  @Test
  public void requestRemoveMemberUnauthorized() throws Exception {
    thrown.expect(UnauthorizedException.class);
    unauthorized.requestRemoveMember(authorizedWallet.getAddress(), BigInteger.ONE);
  }

  @Test
  public void requestRemoveMemberNullAddress() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.requestRemoveMember(null, BigInteger.ONE);
  }

  @Test
  public void requestRemoveMemberInvalidAddress() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.requestRemoveMember(INVALID_ADDRESS, BigInteger.ONE);
  }

  @Test
  public void requestRemoveMemberNullDuration() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.requestRemoveMember(unauthorizedWallet.getAddress(), null);
  }

  @Test
  public void requestRemoveMemberZeroDuration() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.requestRemoveMember(unauthorizedWallet.getAddress(), BigInteger.ZERO);
  }

}
