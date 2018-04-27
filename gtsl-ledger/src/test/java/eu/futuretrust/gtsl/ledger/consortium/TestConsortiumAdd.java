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

public class TestConsortiumAdd extends AbstractTestConsortium {

  @Test
  public void requestAddMember() throws Exception {
    BigInteger proposalId = authorized
        .requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
    Assert.assertNotNull(proposalId);
    Assert.assertEquals(1, authorized.countProposals().longValue());

    authorized.vote(proposalId, true);
    Assert.assertEquals(2, authorized.countMembers().longValue());
    Assert.assertTrue(unauthorized.isAuthorized());
  }

  @Test
  public void requestAddMemberAlreadyExist() throws Exception {
    thrown.expect(UnauthorizedException.class);
    authorized.requestAddMember(authorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
  }

  @Test
  public void requestAddMemberUnauthorized() throws Exception {
    thrown.expect(UnauthorizedException.class);
    unauthorized.requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ONE);
  }

  @Test
  public void requestAddMemberNullAddress() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.requestAddMember(null, VALID_CODE, BigInteger.ONE);
  }

  @Test
  public void requestAddMemberInvalidAddress() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.requestAddMember(INVALID_ADDRESS, VALID_CODE, BigInteger.ONE);
  }

  @Test
  public void requestAddMemberNullCode() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.requestAddMember(unauthorizedWallet.getAddress(), null, BigInteger.ONE);
  }

  @Test
  public void requestAddMemberEmptyCode() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.requestAddMember(unauthorizedWallet.getAddress(), EMPTY_STRING, BigInteger.ONE);
  }

  @Test
  public void requestAddMemberNullDuration() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, null);
  }

  @Test
  public void requestAddMemberZeroDuration() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.requestAddMember(unauthorizedWallet.getAddress(), VALID_CODE, BigInteger.ZERO);
  }

}
