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

package eu.futuretrust.gtsl.model.data.extension;

import eu.futuretrust.gtsl.jaxb.tsl.AdditionalServiceInformationTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.AnyTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.common.AnyType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.5.9.1 additionalServiceInformation Extension
 *
 * <xsd:complexType name="AdditionalServiceInformationType"> <xsd:sequence> <xsd:element name="URI"
 * type="tsl:NonEmptyMultiLangURIType"/> <xsd:element name="InformationValue" type="xsd:string"
 * minOccurs="0"/> <xsd:element name="OtherInformation" type="tsl:AnyType" minOccurs="0"/>
 * </xsd:sequence> </xsd:complexType>
 */
public class AdditionalServiceInformationType {

  @Valid
  @NotNull(message = "{NotNull.asiUri.lang}", payload = {Severity.Error.class, Impact.Legal.class})
  private NonEmptyMultiLangURIType uri;
  private String informationValue; // optional
  private AnyType otherInformation; // optional

  public AdditionalServiceInformationType() {
    this.uri = new NonEmptyMultiLangURIType();
    this.informationValue = "";
    this.otherInformation = new AnyType();
  }

  public AdditionalServiceInformationType(AdditionalServiceInformationTypeJAXB asiJAXB) {
    if (asiJAXB != null) {
      if (asiJAXB.getURI() != null) {
        this.uri = new NonEmptyMultiLangURIType(asiJAXB.getURI());
      }
      this.informationValue = asiJAXB.getInformationValue();
      if (asiJAXB.getOtherInformation() != null && CollectionUtils
          .isNotEmpty(asiJAXB.getOtherInformation().getContent())) {
        this.otherInformation = new AnyType(asiJAXB.getOtherInformation().getContent());
      }
    }
  }

  public AdditionalServiceInformationTypeJAXB asJAXB() {
    AdditionalServiceInformationTypeJAXB asiJAXB = new AdditionalServiceInformationTypeJAXB();
    if (uri != null) {
      asiJAXB.setURI(uri.asJAXB());
    }
    if (informationValue != null) {
      asiJAXB.setInformationValue(informationValue);
    }
    if (otherInformation != null) {
      AnyTypeJAXB otherInformationJAXB = new AnyTypeJAXB();
      otherInformationJAXB.getContent().addAll(otherInformation.getValues());
      asiJAXB.setOtherInformation(otherInformationJAXB);
    }
    return asiJAXB;
  }

  public NonEmptyMultiLangURIType getUri() {
    return uri;
  }

  public void setUri(NonEmptyMultiLangURIType uri) {
    this.uri = uri;
  }

  public String getInformationValue() {
    return informationValue;
  }

  public void setInformationValue(String informationValue) {
    this.informationValue = informationValue;
  }

  public AnyType getOtherInformation() {
    return otherInformation;
  }

  public void setOtherInformation(AnyType otherInformation) {
    this.otherInformation = otherInformation;
  }

}
