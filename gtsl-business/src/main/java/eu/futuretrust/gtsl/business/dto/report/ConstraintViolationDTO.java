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

package eu.futuretrust.gtsl.business.dto.report;

import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;

public class ConstraintViolationDTO {

  private String impact;
  private String severity;
  private String target;
  private String description;
  private Object currentValue;

  public ConstraintViolationDTO() {
    this.impact = "";
    this.severity = "";
    this.target = "";
    this.description = "";
    this.currentValue = null;
  }

  public ConstraintViolationDTO(String impact, String severity, String target,
      String description, Object currentValue) {
    this.impact = impact;
    this.severity = severity;
    this.target = target;
    this.description = description;
    this.currentValue = currentValue;
  }

  public ConstraintViolationDTO(Class<? extends Impact> impact, Class<? extends Severity> severity, String target,
      String description, Object currentValue) {
    this.impact = impact.getSimpleName();
    this.severity = severity.getSimpleName();
    this.target = target;
    this.description = description;
    this.currentValue = currentValue;
  }

  @Override
  public String toString() {
    return "ConstraintViolationDTO{" +
        "impact='" + impact + '\'' +
        ", severity='" + severity + '\'' +
        ", target='" + target + '\'' +
        ", description='" + description + '\'' +
        ", currentValue=" + currentValue +
        '}';
  }

  public String getImpact() {
    return impact;
  }

  public void setImpact(String impact) {
    this.impact = impact;
  }

  public String getSeverity() {
    return severity;
  }

  public void setSeverity(String severity) {
    this.severity = severity;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Object getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(Object currentValue) {
    this.currentValue = currentValue;
  }
}
