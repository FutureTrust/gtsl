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

package eu.futuretrust.gtsl.model.data.extension.qualification;

import eu.futuretrust.gtsl.jaxb.sie.KeyUsageBitTypeJAXB;

public class KeyUsageBitType {

  private String name;
  private Boolean value;

  public KeyUsageBitType() {
  }

  public KeyUsageBitType(KeyUsageBitTypeJAXB keyUsageBitJAXB) {
    if (keyUsageBitJAXB != null) {
      this.name = keyUsageBitJAXB.getName();
      this.value = keyUsageBitJAXB.isValue();
    }
  }

  public KeyUsageBitTypeJAXB asJAXB() {
    KeyUsageBitTypeJAXB keyUsageBitJAXB = new KeyUsageBitTypeJAXB();
    if (value != null) {
      keyUsageBitJAXB.setName(this.name);
      keyUsageBitJAXB.setValue(this.value);
    }
    return keyUsageBitJAXB;
  }

  public Boolean getValue() {
    return value;
  }

  public void setValue(Boolean value) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
