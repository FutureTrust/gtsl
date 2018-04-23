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

package eu.futuretrust.gtsl.business.properties.helper;

public class Cron {

  private String value;
  private boolean enabled;

  public Cron() {
  }

  public Cron(String value) {
    this.value = value;
    this.enabled = true;
  }

  public Cron(String value, boolean enabled) {
    this.value = value;
    this.enabled = enabled;
  }

  public void updateProperly(Cron cron) {
    if (cron.isEnabled()) {
      this.setEnabled(true);
      this.setValue(cron.getValue());
    } else {
      this.setEnabled(false);
    }
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

}
