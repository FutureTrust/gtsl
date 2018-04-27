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

package eu.futuretrust.gtsl.ledger.vo;

import eu.futuretrust.gtsl.ethereum.utils.StringUtils;
import java.math.BigInteger;

public class Member {

  private String address;
  private BigInteger index;
  private BigInteger since;
  private boolean authorized;
  private String tslIdentifier;

  public Member(String address, BigInteger index, BigInteger since, boolean authorized,
      String tslIdentifier) {
    this.address = address.trim();
    this.index = index;
    this.since = since;
    this.authorized = authorized;
    this.tslIdentifier = tslIdentifier.trim();
  }

  public boolean isValid() {
    return index != null
        && index.signum() >= 0
        && StringUtils.isNotEmpty(address)
        && StringUtils.isNotEmpty(tslIdentifier)
        && authorized;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public BigInteger getIndex() {
    return index;
  }

  public void setIndex(BigInteger index) {
    this.index = index;
  }

  public BigInteger getSince() {
    return since;
  }

  public void setSince(BigInteger since) {
    this.since = since;
  }

  public boolean isAuthorized() {
    return authorized;
  }

  public void setAuthorized(boolean authorized) {
    this.authorized = authorized;
  }

  public String getTslIdentifier() {
    return tslIdentifier;
  }

  public void setTslIdentifier(String tslIdentifier) {
    this.tslIdentifier = tslIdentifier;
  }
}
