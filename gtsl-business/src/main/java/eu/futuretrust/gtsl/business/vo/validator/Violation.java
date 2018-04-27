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

package eu.futuretrust.gtsl.business.vo.validator;

import eu.futuretrust.gtsl.business.validator.ViolationConstant;

public class Violation {

  private ViolationConstant violation;
  private Object currentValue;
  private Object[] args;

  public Violation(ViolationConstant code) {
    this(code, null);
  }

  public Violation(ViolationConstant code, Object currentValue) {
    this.violation = code;
    this.currentValue = currentValue;
    this.args = new Object[]{};
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Violation that = (Violation) o;

    return violation.toString().equals(that.violation.toString());
  }

  @Override
  public int hashCode() {
    return violation.toString() != null ? violation.toString().hashCode() : 0;
  }

  public ViolationConstant getViolation() {
    return violation;
  }

  public void setViolation(ViolationConstant violation) {
    this.violation = violation;
  }

  public Object getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(Object currentValue) {
    this.currentValue = currentValue;
  }

  public Object[] getArgs() {
    return args;
  }

  public void setArgs(Object[] args) {
    this.args = args;
  }

}
