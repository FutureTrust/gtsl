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
import java.security.InvalidParameterException;
import org.junit.Assert;
import org.junit.Test;

public class TestConsortiumAuthorization extends AbstractTestConsortium {

  @Test
  public void authorizedTsl() throws Exception {
    String tsl = authorized.authorizedTsl();
    Assert.assertEquals(VALID_CODE, tsl);
  }

  @Test
  public void unauthorizedTsl() throws Exception {
    thrown.expect(UnauthorizedException.class);
    unauthorized.authorizedTsl();
  }

  @Test
  public void authorized() throws Exception {
    Assert.assertTrue(authorized.isAuthorized());
  }

  @Test
  public void unauthorized() throws Exception {
    Assert.assertFalse(unauthorized.isAuthorized());
  }

  @Test
  public void authorizedAddressNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.isAuthorized(null);
  }

  @Test
  public void authorizedAddressEmpty() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.isAuthorized(EMPTY_STRING);
  }

  @Test
  public void authorizedAddressInvalid() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.isAuthorized(INVALID_ADDRESS);
  }

  @Test
  public void authorizedAddress() throws Exception {
    Assert.assertTrue(authorized.isAuthorized(authorizedWallet.getAddress()));
  }

  @Test
  public void unauthorizedAddress() throws Exception {
    Assert.assertFalse(authorized.isAuthorized(unauthorizedWallet.getAddress()));
  }

  @Test
  public void isSignerTsl() throws Exception {
    boolean signerTsl = authorized.isSignerTsl(VALID_CODE);
    Assert.assertTrue(signerTsl);
  }

  @Test
  public void isNotSignerTsl() throws Exception {
    boolean signerTsl = authorized.isSignerTsl(VALID_CODE_FR);
    Assert.assertFalse(signerTsl);
  }

  @Test
  public void isUnauthorizedSignerTsl() throws Exception {
    boolean signerTsl = unauthorized.isSignerTsl(VALID_CODE);
    Assert.assertFalse(signerTsl);
  }

  @Test
  public void isSignerTslNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.isSignerTsl(null);
  }

  @Test
  public void isSignerTslEmpty() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.isSignerTsl(EMPTY_STRING);
  }

  @Test
  public void isSignerTslAddress() throws Exception {
    boolean signerTsl = authorized.isSignerTsl(authorizedWallet.getAddress(), VALID_CODE);
    Assert.assertTrue(signerTsl);
  }

  @Test
  public void isNotSignerTslAddress() throws Exception {
    boolean signerTsl = authorized.isSignerTsl(unauthorizedWallet.getAddress(), VALID_CODE);
    Assert.assertFalse(signerTsl);
  }

  @Test
  public void isSignerTslAddressNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.isSignerTsl(null, VALID_CODE);
  }

  @Test
  public void isSignerTslAddressEmpty() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.isSignerTsl(EMPTY_STRING, VALID_CODE);
  }

  @Test
  public void isSignerTslAddressInvalid() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.isSignerTsl(INVALID_ADDRESS, VALID_CODE);
  }

  @Test
  public void isSignerTslCodeNull() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.isSignerTsl(authorizedWallet.getAddress(), null);
  }

  @Test
  public void isSignerTslCodeEmpty() throws Exception {
    thrown.expect(InvalidParameterException.class);
    authorized.isSignerTsl(authorizedWallet.getAddress(), EMPTY_STRING);
  }

}
