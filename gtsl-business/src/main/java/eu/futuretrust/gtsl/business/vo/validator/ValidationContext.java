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

import eu.futuretrust.gtsl.properties.RulesProperties;
import java.util.ArrayList;
import java.util.List;

public class ValidationContext {

  private List<Violation> report;
  private RulesProperties properties;
  private TslAttributesForValidation attributes;
  private List<Object> args;

  public ValidationContext(RulesProperties properties, TslAttributesForValidation attributes) {
    this.report = new ArrayList<>();
    this.properties = properties;
    this.attributes = attributes;
    this.args = new ArrayList<>();
  }

  public void addViolation(Violation violation) {
    violation.setArgs(getArgs().toArray());
    this.report.add(violation);
  }

  public boolean isLotl() {
    return attributes.getTslType().getValue().equals(properties.getTslType().getEuListofthelists());
  }

  public boolean isNonEuLotl() {
    return attributes.getTslType().getValue().equals(properties.getTslType().getCcListofthelists());
  }

  public boolean isEuTsl() {
    return attributes.getTslType().getValue().equals(properties.getTslType().getEuGeneric());
  }

  public boolean isNonEuTsl() {
    return attributes.getTslType().getValue().equals(properties.getTslType().getCcList());
  }

  public List<Violation> getReport() {
    return report;
  }

  public void setReport(List<Violation> report) {
    this.report = report;
  }

  public RulesProperties getProperties() {
    return properties;
  }

  public void setProperties(RulesProperties properties) {
    this.properties = properties;
  }

  public TslAttributesForValidation getAttributes() {
    return attributes;
  }

  public void setAttributes(TslAttributesForValidation attributes) {
    this.attributes = attributes;
  }

  public List<Object> getArgs() {
    return args;
  }

  public void setArgs(List<Object> args) {
    this.args = args;
  }
}
