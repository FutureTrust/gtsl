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

package eu.futuretrust.gtsl.business.dto.consortium;

import java.math.BigInteger;

public class ProposalDTO {

  private String memberAddress;
  private String tslIdentifier;
  private BigInteger duration;

  public String getMemberAddress() {
    return memberAddress;
  }

  public void setMemberAddress(String memberAddress) {
    this.memberAddress = memberAddress;
  }

  public String getTslIdentifier() {
    return tslIdentifier;
  }

  public void setTslIdentifier(String tslIdentifier) {
    this.tslIdentifier = tslIdentifier;
  }

  public BigInteger getDuration() {
    return duration;
  }

  public void setDuration(BigInteger duration) {
    this.duration = duration;
  }
}
