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

import eu.futuretrust.gtsl.jaxb.tsl.AdditionalInformationTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.AnyTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.InternationalNamesTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.NonEmptyMultiLangURIListTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.OtherTSLPointerTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.common.CountryCode;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.common.MultiLangNormStringType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIListType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import eu.futuretrust.gtsl.model.data.digitalidentity.ServiceDigitalIdentityListType;
import eu.futuretrust.gtsl.model.data.enums.MimeType;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.13 Pointers to other TSLs
 *
 * <xsd:complexType name="OtherTSLPointerType"> <xsd:sequence> <!-- Specified in TS 119 612 v2.1.1
 * clause 5.3.13 Pointers to other TSLs item b) from Format --> <xsd:element
 * ref="tsl:ServiceDigitalIdentities" minOccurs="0"/> <!-- Specified in TS 119 612 v2.1.1 clause
 * 5.3.13 Pointers to other TSLs item a) from Format --> <xsd:element name="TSLLocation"
 * type="tsl:NonEmptyURIType"/> <!-- Specified in TS 119 612 v2.1.1 clause 5.3.13 Pointers to other
 * TSLs item c) from Format --> <xsd:element ref="tsl:AdditionalInformation" minOccurs="0"/>
 * </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="ServiceDigitalIdentities" type="tsl:ServiceDigitalIdentityListType"/>
 * <xsd:element name="AdditionalInformation" type="tsl:AdditionalInformationType"/>
 */
public class OtherTSLPointerType {

  @NotNull(message = "{NotNull.otherTSLPointer.tslLocation}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected NonEmptyURIType tslLocation;

  @NotNull(message = "{NotNull.otherTSLPointer.serviceDigitalIdentities}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected ServiceDigitalIdentityListType serviceDigitalIdentities; // optional

  @NotNull(message = "{NotNull.schemeInformation.tslType}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  private NonEmptyURIType tslType;

  @NotNull(message = "{NotNull.schemeInformation.schemeOperatorAddress}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  private InternationalNamesType schemeOperatorName;

  @NotNull(message = "{NotNull.schemeInformation.schemeTypeCommunityRules}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  private NonEmptyMultiLangURIListType schemeRules;

  @NotNull(message = "{NotNull.schemeInformation.schemeTerritory}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  private CountryCode schemeTerritory;

  @NotNull(message = "{NotNull.otherTSLPointer.mimeType}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  private MimeType mimeType;

  public OtherTSLPointerType() {
    this.tslLocation = new NonEmptyURIType();
    this.serviceDigitalIdentities = new ServiceDigitalIdentityListType();
    this.tslType = new NonEmptyURIType();
    this.schemeOperatorName = new InternationalNamesType();
    this.schemeRules = new NonEmptyMultiLangURIListType();
    this.schemeTerritory = new CountryCode();
  }

  public OtherTSLPointerType(OtherTSLPointerTypeJAXB otherTSLPointer) {
    if (otherTSLPointer != null) {
      if (otherTSLPointer.getTSLLocation() != null) {
        this.tslLocation = new NonEmptyURIType(otherTSLPointer.getTSLLocation());
      }
      if (otherTSLPointer.getServiceDigitalIdentities() != null) {
        this.serviceDigitalIdentities = new ServiceDigitalIdentityListType(
            otherTSLPointer.getServiceDigitalIdentities());
      } else {
        this.serviceDigitalIdentities = new ServiceDigitalIdentityListType();
      }
      if (otherTSLPointer.getAdditionalInformation() != null && CollectionUtils.isNotEmpty(
          otherTSLPointer.getAdditionalInformation().getTextualInformationOrOtherInformation())) {
        Map<String, Object> properties = ModelUtils.extractAsMap(
            otherTSLPointer.getAdditionalInformation().getTextualInformationOrOtherInformation());

        Object tslTypeJAXB = properties.get("{http://uri.etsi.org/02231/v2#}TSLType");
        if (tslTypeJAXB instanceof String) {
          this.tslType = new NonEmptyURIType((String) tslTypeJAXB);
        }

        Object schemeOpeNameJAXB = properties
            .get("{http://uri.etsi.org/02231/v2#}SchemeOperatorName");
        if (schemeOpeNameJAXB instanceof InternationalNamesTypeJAXB) {
          this.schemeOperatorName = new InternationalNamesType(
              (InternationalNamesTypeJAXB) schemeOpeNameJAXB);
        }

        Object schemeRulesJAXB = properties
            .get("{http://uri.etsi.org/02231/v2#}SchemeTypeCommunityRules");
        if (schemeRulesJAXB instanceof NonEmptyMultiLangURIListTypeJAXB) {
          this.schemeRules = new NonEmptyMultiLangURIListType(
              (NonEmptyMultiLangURIListTypeJAXB) schemeRulesJAXB);
        }

        Object schemeTerritoryJAXB = properties
            .get("{http://uri.etsi.org/02231/v2#}SchemeTerritory");
        if (schemeTerritoryJAXB instanceof String) {
          this.schemeTerritory = new CountryCode((String) schemeTerritoryJAXB);
        }

        Object mm = properties.get("{http://uri.etsi.org/02231/v2/additionaltypes#}MimeType");
        if (mm instanceof String) {
          this.mimeType = ModelUtils.convert((String) mm);
        }
      }
    }
  }

  public OtherTSLPointerTypeJAXB asJAXB() {
    OtherTSLPointerTypeJAXB otherTSLPointer = new OtherTSLPointerTypeJAXB();
    if (tslLocation != null) {
      otherTSLPointer.setTSLLocation(tslLocation.getValue());
    }
    if (serviceDigitalIdentities != null && CollectionUtils
        .isNotEmpty(serviceDigitalIdentities.getValues())) {
      otherTSLPointer.setServiceDigitalIdentities(serviceDigitalIdentities.asJAXB());
    }
    otherTSLPointer.setAdditionalInformation(additionalInformationJAXB());
    return otherTSLPointer;
  }

  // from TL-Manager
  private AdditionalInformationTypeJAXB additionalInformationJAXB() {
    // ADDITIONAL INFORMATION
    AdditionalInformationTypeJAXB additionalInformation = new AdditionalInformationTypeJAXB();

    if (tslType != null) {
      JAXBElement<String> el = new JAXBElement<>(
          new QName("http://uri.etsi.org/02231/v2#", "TSLType"), String.class, tslType.getValue());
      AnyTypeJAXB anyTSLType = new AnyTypeJAXB();
      anyTSLType.getContent().add(el);
      additionalInformation.getTextualInformationOrOtherInformation().add(anyTSLType);
    }

    if (schemeTerritory != null) {
      JAXBElement<String> el = new JAXBElement<>(
          new QName("http://uri.etsi.org/02231/v2#", "SchemeTerritory"),
          String.class, schemeTerritory.getValue());
      AnyTypeJAXB anySchemeTerritory = new AnyTypeJAXB();
      anySchemeTerritory.getContent().add(el);
      additionalInformation.getTextualInformationOrOtherInformation().add(anySchemeTerritory);
    }

    if (mimeType != null) {
      JAXBElement<String> el = new JAXBElement<>(
          new QName("http://uri.etsi.org/02231/v2/additionaltypes#", "MimeType"),
          String.class, ModelUtils.convert(this.mimeType));
      AnyTypeJAXB anyMime = new AnyTypeJAXB();
      anyMime.getContent().add(el);
      additionalInformation.getTextualInformationOrOtherInformation().add(anyMime);
    }

    InternationalNamesTypeJAXB intSchemeOperatorName = new InternationalNamesTypeJAXB();
    if (schemeOperatorName != null) {
      for (MultiLangNormStringType tlName : schemeOperatorName.getValues()) {
        intSchemeOperatorName.getName().add(tlName.asJAXB());
      }
    }
    JAXBElement<InternationalNamesTypeJAXB> el2 = new JAXBElement<>(
        new QName("http://uri.etsi.org/02231/v2#", "SchemeOperatorName"),
        InternationalNamesTypeJAXB.class,
        intSchemeOperatorName);
    AnyTypeJAXB anySchemeOpe = new AnyTypeJAXB();
    anySchemeOpe.getContent().add(el2);
    additionalInformation.getTextualInformationOrOtherInformation().add(anySchemeOpe);

    NonEmptyMultiLangURIListTypeJAXB typeCommunity = new NonEmptyMultiLangURIListTypeJAXB();
    if (schemeRules != null) {
      for (NonEmptyMultiLangURIType tlTypeComm : schemeRules.getValues()) {
        typeCommunity.getURI().add(tlTypeComm.asJAXB());
      }
    }
    JAXBElement<NonEmptyMultiLangURIListTypeJAXB> el3 = new JAXBElement<>(
        new QName("http://uri.etsi.org/02231/v2#", "SchemeTypeCommunityRules"),
        NonEmptyMultiLangURIListTypeJAXB.class, typeCommunity);
    AnyTypeJAXB anyComm = new AnyTypeJAXB();
    anyComm.getContent().add(el3);
    additionalInformation.getTextualInformationOrOtherInformation().add(anyComm);

    return additionalInformation;
  }

  public ServiceDigitalIdentityListType getServiceDigitalIdentities() {
    return serviceDigitalIdentities;
  }

  public void setServiceDigitalIdentities(
      ServiceDigitalIdentityListType serviceDigitalIdentities) {
    this.serviceDigitalIdentities = serviceDigitalIdentities;
  }

  public NonEmptyURIType getTslLocation() {
    return tslLocation;
  }

  public void setTslLocation(NonEmptyURIType tslLocation) {
    this.tslLocation = tslLocation;
  }

  public MimeType getMimeType() {
    return mimeType;
  }

  public void setMimeType(MimeType mimeType) {
    this.mimeType = mimeType;
  }

  public NonEmptyURIType getTslType() {
    return tslType;
  }

  public void setTslType(NonEmptyURIType tslType) {
    this.tslType = tslType;
  }

  public CountryCode getSchemeTerritory() {
    return schemeTerritory;
  }

  public void setSchemeTerritory(CountryCode schemeTerritory) {
    this.schemeTerritory = schemeTerritory;
  }

  public InternationalNamesType getSchemeOperatorName() {
    return schemeOperatorName;
  }

  public void setSchemeOperatorName(
      InternationalNamesType schemeOperatorName) {
    this.schemeOperatorName = schemeOperatorName;
  }

  public NonEmptyMultiLangURIListType getSchemeRules() {
    return schemeRules;
  }

  public void setSchemeRules(
      NonEmptyMultiLangURIListType schemeRules) {
    this.schemeRules = schemeRules;
  }
}
