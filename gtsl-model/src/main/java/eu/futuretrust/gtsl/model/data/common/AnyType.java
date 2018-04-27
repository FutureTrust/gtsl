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

import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <xsd:complexType name="AnyType" mixed="true">
 *   <xsd:sequence minOccurs="0" maxOccurs="unbounded">
 *     <xsd:any processContents="lax"/>
 *   </xsd:sequence>
 * </xsd:complexType>
 */
public class AnyType {

  @NotNull(message = "{NotNull.anyType.values}", payload = {Severity.Error.class, Impact.Legal.class})
  @Valid
  protected List<Object> values;

  public AnyType() {
    this.values = new ArrayList<>();
  }

  public AnyType(List<Object> values) {
    this.values = values;
  }

  public List<Object> getValues() {
    return values;
  }

  public void setValues(List<Object> values) {
    this.values = values;
  }
}
