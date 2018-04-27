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
import eu.futuretrust.gtsl.ledger.enums.Action;
import java.math.BigInteger;

public class Proposal {

  private BigInteger index;
  private boolean result;
  private boolean executed;
  private String member;
  private String tslIdentifier;
  private Action action;

  public Proposal(BigInteger index, boolean result, boolean executed, String member,
      String tslIdentifier, Action action) {
    this.index = index;
    this.result = result;
    this.executed = executed;
    this.member = member.trim();
    this.tslIdentifier = tslIdentifier.trim();
    this.action = action;
  }

  public boolean isValid() {
    return index != null
        && index.signum() >= 0
        && StringUtils.isNotEmpty(member)
        && StringUtils.isNotEmpty(tslIdentifier);
  }

  public BigInteger getIndex() {
    return index;
  }

  public void setIndex(BigInteger index) {
    this.index = index;
  }

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public boolean isExecuted() {
    return executed;
  }

  public void setExecuted(boolean executed) {
    this.executed = executed;
  }

  public String getMember() {
    return member;
  }

  public void setMember(String member) {
    this.member = member;
  }

  public String getTslIdentifier() {
    return tslIdentifier;
  }

  public void setTslIdentifier(String tslIdentifier) {
    this.tslIdentifier = tslIdentifier;
  }

  public Action getAction() {
    return action;
  }

  public void setAction(Action action) {
    this.action = action;
  }

}
