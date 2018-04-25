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

package eu.futuretrust.gtsl.model.data.tsl;

import eu.futuretrust.gtsl.jaxb.tsl.OtherTSLPointersTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.13 Pointers to other TSLs
 *
 * <xsd:complexType name="OtherTSLPointersType"> <xsd:sequence> <xsd:element
 * ref="tsl:OtherTSLPointer" maxOccurs="unbounded"/> </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="OtherTSLPointer" type="tsl:OtherTSLPointerType"/>
 */
public class OtherTSLPointersType extends ListModel<OtherTSLPointerType> {

  public OtherTSLPointersType() {
  }

  public OtherTSLPointersType(List<OtherTSLPointerType> values) {
    super(values);
  }

  public OtherTSLPointersType(OtherTSLPointersTypeJAXB pointersToOtherTSL) {
    super(CollectionUtils.isNotEmpty(pointersToOtherTSL.getOtherTSLPointer()) ?
        pointersToOtherTSL.getOtherTSLPointer()
            .stream()
            .map(OtherTSLPointerType::new)
            .collect(Collectors.toList())
        : Collections.emptyList());
  }

  @Override
  @NotEmpty(message = "{NotEmpty.otherTSLPointers.values}", payload = {Severity.Info.class,
      Impact.Legal.class})
  public List<OtherTSLPointerType> getValues() {
    return super.getValues();
  }

  public OtherTSLPointersTypeJAXB asJAXB() {
    if (CollectionUtils.isNotEmpty(values)) {
    OtherTSLPointersTypeJAXB otherTSLPointersType = new OtherTSLPointersTypeJAXB();
      otherTSLPointersType.getOtherTSLPointer()
          .addAll(
              values.stream()
                  .map(OtherTSLPointerType::asJAXB)
                  .collect(Collectors.toList()));
      return otherTSLPointersType;
    }
    return null;
  }
}
