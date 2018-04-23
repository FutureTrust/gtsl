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

import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.model.data.common.AnyURI;
import eu.futuretrust.gtsl.model.data.common.StringType;
import eu.futuretrust.gtsl.model.data.signature.SignatureType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections.CollectionUtils;

/**
 * <!--  ROOT Element  --> <xsd:element name="TrustServiceStatusList"
 * type="tsl:TrustStatusListType"/> <xsd:complexType name="TrustStatusListType"> <xsd:sequence> <!--
 * Specified in TS 119 612 v2.1.1 clause 5.3 Scheme information --> <xsd:element
 * ref="tsl:SchemeInformation"/> <!-- Specified in TS 119 612 v2.1.1 clause 5.3.18 Trust Service
 * Provider List --> <xsd:element ref="tsl:TrustServiceProviderList" minOccurs="0"/> <!-- Specified
 * in TS 119 612 v2.1.1 clause 5.7 Digital signature --> <xsd:element ref="ds:Signature"
 * minOccurs="0"/> </xsd:sequence> <!-- Specified in TS 119 612 v2.1.1 clause 5.2 Trusted List Tag
 * --> <xsd:attribute name="TSLTag" type="xsd:anyURI" use="required"/> <xsd:attribute name="Id"
 * type="xsd:ID" use="optional"/> </xsd:complexType>
 *
 * <xsd:element name="SchemeInformation" type="tsl:TSLSchemeInformationType"/> <xsd:element
 * name="TrustServiceProviderList" type="tsl:TrustServiceProviderListType"/>
 */
public class TrustStatusListType {

  @Valid
  private StringType id; // optional

  @Valid
  @NotNull(message = "{NotNull.tsl.tslTag}", payload = {Severity.Error.class, Impact.Legal.class})
  private AnyURI tslTag;

  @Valid
  @NotNull(message = "{NotNull.tsl.schemeInformation}", payload = {Severity.Error.class,
      Impact.Legal.class})
  private TSLSchemeInformationType schemeInformation;

  @Valid
  private TrustServiceProviderListType trustServiceProviderList; // optional

  @Valid
  protected SignatureType signature; // optional

  public TrustStatusListType() {
    this.id = new StringType();
    this.tslTag = new AnyURI();
    this.schemeInformation = new TSLSchemeInformationType();
    this.trustServiceProviderList = new TrustServiceProviderListType();
    this.signature = new SignatureType();
  }

  public TrustStatusListType(TrustStatusListTypeJAXB tsl) {
    if (tsl != null) {
      if (tsl.getId() != null) {
        this.id = new StringType(tsl.getId());
      } else {
        this.id = new StringType();
      }
      if (tsl.getTSLTag() != null) {
        this.tslTag = new AnyURI(tsl.getTSLTag());
      } else {
        this.tslTag = new AnyURI();
      }
      if (tsl.getSchemeInformation() != null) {
        this.schemeInformation = new TSLSchemeInformationType(tsl.getSchemeInformation());
      } else {
        this.schemeInformation = new TSLSchemeInformationType();
      }
      if (tsl.getTrustServiceProviderList() != null) {
        this.trustServiceProviderList = new TrustServiceProviderListType(
            tsl.getTrustServiceProviderList());
      } else {
        this.trustServiceProviderList = new TrustServiceProviderListType();
      }
      if (tsl.getSignature() != null) {
        this.signature = new SignatureType(tsl.getSignature());
      } else {
        this.signature = new SignatureType();
      }
    }
  }

  public TrustStatusListTypeJAXB asJAXB() {
    TrustStatusListTypeJAXB tslJAXB = new TrustStatusListTypeJAXB();
    if (id != null) {
      tslJAXB.setId(id.getValue());
    }
    if (tslTag != null) {
      tslJAXB.setTSLTag(tslTag.getValue());
    }
    if (schemeInformation != null) {
      tslJAXB.setSchemeInformation(schemeInformation.asJAXB());
    }
    if (trustServiceProviderList != null) {
      tslJAXB.setTrustServiceProviderList(trustServiceProviderList.asJAXB());
    }
    if (signature != null && (signature.getId() != null || signature.getKeyInfo() != null
        || signature.getSignatureValue() != null
        || signature.getSignedInfo() != null || (signature.getObject() != null && CollectionUtils
        .isNotEmpty(signature.getObject())))) {
      tslJAXB.setSignature(signature.asJAXB());
    }

    return tslJAXB;
  }

  public TSLSchemeInformationType getSchemeInformation() {
    return schemeInformation;
  }

  public void setSchemeInformation(TSLSchemeInformationType schemeInformation) {
    this.schemeInformation = schemeInformation;
  }

  public TrustServiceProviderListType getTrustServiceProviderList() {
    return trustServiceProviderList;
  }

  public void setTrustServiceProviderList(
      TrustServiceProviderListType trustServiceProviderList) {
    this.trustServiceProviderList = trustServiceProviderList;
  }

  public AnyURI getTslTag() {
    return tslTag;
  }

  public void setTslTag(AnyURI tslTag) {
    this.tslTag = tslTag;
  }

  public StringType getId() {
    return id;
  }

  public void setId(StringType id) {
    this.id = id;
  }

  public SignatureType getSignature() {
    return signature;
  }

  public void setSignature(SignatureType signature) {
    this.signature = signature;
  }
}
