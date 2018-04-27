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

import eu.futuretrust.gtsl.ledger.vo.Member;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.web3j.abi.datatypes.Address;

public class TestConsortiumFindMemberByAddress extends AbstractTestConsortium {

  @Test
  public void findMemberByAddress() throws Exception {
    Optional<Member> member = authorized.findMemberByAddress(authorizedWallet.getAddress());
    Assert.assertTrue(member.isPresent());
    Assert.assertEquals(new Address(authorizedWallet.getAddress()),
        new Address(member.get().getAddress()));
    Assert.assertEquals(VALID_CODE, member.get().getTslIdentifier());
    Assert.assertEquals(BigInteger.ZERO, member.get().getIndex());
  }

  @Test
  public void findMemberByAddressNotFound() throws Exception {
    Optional<Member> member = authorized.findMemberByAddress(unauthorizedWallet.getAddress());
    Assert.assertFalse(member.isPresent());
  }

  @Test
  public void findMemberByAddressNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.findMemberByAddress(null);
  }

  @Test
  public void findMemberByAddressEmpty() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.findMemberByAddress(EMPTY_STRING);
  }

  @Test
  public void findMemberByAddressInvalid() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.findMemberByAddress(INVALID_ADDRESS);
  }

}
