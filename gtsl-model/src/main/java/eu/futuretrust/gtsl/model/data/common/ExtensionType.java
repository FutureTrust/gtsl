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

package eu.futuretrust.gtsl.model.data.common;


import eu.futuretrust.gtsl.jaxb.tsl.ExtensionTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotNull;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.17 Scheme extensions
 *
 * <xsd:complexType name="ExtensionType"> <xsd:complexContent> <xsd:extension base="tsl:AnyType">
 * <xsd:attribute name="Critical" type="xsd:boolean" use="required"/> </xsd:extension>
 * </xsd:complexContent> </xsd:complexType>
 */
public class ExtensionType extends AnyType {

  @NotNull(message = "{NotNull.extension.critical}", payload = {Severity.Error.class,
      Impact.Legal.class})
  private Boolean critical;

  public ExtensionType() {
  }

  public ExtensionType(ExtensionTypeJAXB extension) {
    super(extension != null ? extension.getContent() : null);
    if (extension != null) {
      this.critical = extension.isCritical();
    }
  }

  public ExtensionTypeJAXB asJAXB() {
    ExtensionTypeJAXB extension = new ExtensionTypeJAXB();
    if (critical != null) {
      extension.setCritical(critical);
    }
    if (CollectionUtils.isNotEmpty(values)) {
      extension.getContent().addAll(values);
    }
    return extension;
  }

  public Boolean getCritical() {
    return critical;
  }

  public void setCritical(Boolean critical) {
    this.critical = critical;
  }

}
