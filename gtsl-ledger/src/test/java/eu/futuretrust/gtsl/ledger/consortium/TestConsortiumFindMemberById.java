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

public class TestConsortiumFindMemberById extends AbstractTestConsortium {

  @Test
  public void findMemberById() throws Exception {
    Optional<Member> member = authorized.findMemberById(BigInteger.ZERO);
    Assert.assertTrue(member.isPresent());
    Assert.assertEquals(new Address(authorizedWallet.getAddress()),
        new Address(member.get().getAddress()));
    Assert.assertEquals(VALID_CODE, member.get().getTslIdentifier());
    Assert.assertEquals(BigInteger.ZERO, member.get().getIndex());
  }

  @Test
  public void findMemberByIdLowerThan0() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.findMemberById(BigInteger.valueOf(-1));
  }

  @Test
  public void findMemberByIdGreaterThanLength() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.findMemberById(BigInteger.valueOf(100));
  }

  @Test
  public void findMemberByIdNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.findMemberById(null);
  }

}
