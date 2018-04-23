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

import eu.futuretrust.gtsl.jaxb.tsl.NextUpdateTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.TSLSchemeInformationTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.common.*;
import eu.futuretrust.gtsl.model.utils.DateUtils;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import eu.futuretrust.gtsl.model.utils.XmlGregorianCalendarUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3 Scheme information
 *
 * <xsd:complexType name="TSLSchemeInformationType"> <xsd:sequence> <!-- Specified in TS 119 612
 * v2.1.1 clause 5.3.1 TSL version identifier --> <xsd:element name="TSLVersionIdentifier"
 * type="xsd:integer"/> <!-- Specified in TS 119 612 v2.1.1 clause 5.3.2 TSL sequence number -->
 * <xsd:element name="TSLSequenceNumber" type="xsd:positiveInteger"/> <!-- Specified in TS 119 612
 * v2.1.1 clause 5.3.3 TSL type --> <xsd:element ref="tsl:TSLType"/> <!-- Specified in TS 119 612
 * v2.1.1 clause 5.3.4 Scheme operator name --> <xsd:element ref="tsl:SchemeOperatorName"/> <!--
 * Specified in TS 119 612 v2.1.1 clause 5.3.5 Scheme operator address --> <xsd:element
 * name="SchemeOperatorAddress" type="tsl:AddressType"/> <!-- Specified in TS 119 612 v2.1.1 clause
 * 5.3.6 Scheme name --> <xsd:element ref="tsl:SchemeName"/> <!-- Specified in TS 119 612 v2.1.1
 * clause 5.3.7 Scheme information URI --> <xsd:element ref="tsl:SchemeInformationURI"/> <!--
 * Specified in TS 119 612 v2.1.1 clause 5.3.8 Status determination approach --> <xsd:element
 * name="StatusDeterminationApproach" type="tsl:NonEmptyURIType"/> <!-- Specified in TS 119 612
 * v2.1.1 clause 5.3.9 Scheme type/community/rules --> <xsd:element ref="tsl:SchemeTypeCommunityRules"
 * minOccurs="0"/> <!-- Specified in TS 119 612 v2.1.1 clause 5.3.10 Scheme territory -->
 * <xsd:element ref="tsl:SchemeTerritory" minOccurs="0"/> <!-- Specified in TS 119 612 v2.1.1 clause
 * 5.3.11 TSL policy/legal notice --> <xsd:element ref="tsl:PolicyOrLegalNotice" minOccurs="0"/>
 * <!-- Specified in TS 119 612 v2.1.1 clause 5.3.12 Historical information period --> <xsd:element
 * name="HistoricalInformationPeriod" type="xsd:nonNegativeInteger"/> <!-- Specified in TS 119 612
 * v2.1.1 clause 5.3.13 Pointers to other TSLs --> <xsd:element ref="tsl:PointersToOtherTSL"
 * minOccurs="0"/> <!-- Specified in TS 119 612 v2.1.1 clause 5.3.14 List issue date and time -->
 * <xsd:element name="ListIssueDateTime" type="xsd:dateTime"/> <!-- Specified in TS 119 612 v2.1.1
 * clause 5.3.15 Next update --> <xsd:element ref="tsl:NextUpdate"/> <!-- Specified in TS 119 612
 * v2.1.1 clause 5.3.16 Distribution points --> <xsd:element ref="tsl:DistributionPoints"
 * minOccurs="0"/> <!-- Specified in TS 119 612 v2.1.1 clause 5.3.17 Scheme extensions -->
 * <xsd:element name="SchemeExtensions" type="tsl:ExtensionsListType" minOccurs="0"/>
 * </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="TSLType" type="tsl:NonEmptyURIType"/> <xsd:element name="SchemeOperatorName"
 * type="tsl:InternationalNamesType"/> <xsd:element name="SchemeName"
 * type="tsl:InternationalNamesType"/> <xsd:element name="SchemeInformationURI"
 * type="tsl:NonEmptyMultiLangURIListType"/> <xsd:element name="SchemeTypeCommunityRules"
 * type="tsl:NonEmptyMultiLangURIListType"/> <xsd:element name="SchemeTerritory" type="xsd:string"/>
 * <xsd:element name="PolicyOrLegalNotice" type="tsl:PolicyOrLegalnoticeType"/> <xsd:element
 * name="PointersToOtherTSL" type="tsl:OtherTSLPointersType"/> <xsd:element name="NextUpdate"
 * type="tsl:NextUpdateType"/> <xsd:element name="DistributionPoints"
 * type="tsl:NonEmptyURIListType"/>
 */
public class TSLSchemeInformationType {

  @NotNull(message = "{NotNull.schemeInformation.tslVersionIdentifier}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Min(value = 5, message = "{Min.schemeInformation.tslVersionIdentifier}", payload = {
      Severity.Error.class, Impact.TrustBackbone.class})
  protected BigInteger tslVersionIdentifier; // -INF..INF

  @NotNull(message = "{NotNull.schemeInformation.tslSequenceNumber}", payload = {
      Severity.Error.class, Impact.TrustBackbone.class})
  @Min(value = 1, message = "{Min.schemeInformation.tslSequenceNumber}", payload = {
      Severity.Error.class, Impact.TrustBackbone.class})
  protected BigInteger tslSequenceNumber; // 1..INF

  @NotNull(message = "{NotNull.schemeInformation.tslType}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected NonEmptyURIType tslType;

  @NotNull(message = "{NotNull.schemeInformation.schemeOperatorName}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected InternationalNamesType schemeOperatorName;

  @NotNull(message = "{NotNull.schemeInformation.schemeOperatorAddress}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected AddressType schemeOperatorAddress;

  @NotNull(message = "{NotNull.schemeInformation.schemeName}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected InternationalNamesType schemeName;

  @NotNull(message = "{NotNull.schemeInformation.schemeInformationURI}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected NonEmptyMultiLangURIListType schemeInformationURI;

  @NotNull(message = "{NotNull.schemeInformation.statusDeterminationApproach}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected NonEmptyURIType statusDeterminationApproach;

  @NotNull(message = "{NotNull.schemeInformation.schemeTypeCommunityRules}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected NonEmptyMultiLangURIListType schemeTypeCommunityRules;

  @NotNull(message = "{NotNull.schemeInformation.schemeTerritory}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected CountryCode schemeTerritory;

  @NotNull(message = "{NotNull.schemeInformation.policyOrLegalNotice}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected PolicyOrLegalnoticeType policyOrLegalNotice;

  @NotNull(message = "{NotNull.schemeInformation.historicalInformationPeriod}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Min(value = 0, message = "{Min.schemeInformation.historicalInformationPeriod}", payload = {
      Severity.Error.class, Impact.TrustBackbone.class})
  protected BigInteger historicalInformationPeriod; // 0..INF

  @Valid
  protected OtherTSLPointersType pointersToOtherTSL; // optional

  @NotNull(message = "{NotNull.schemeInformation.listIssueDateTime}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  @DateTimeFormat(pattern = DateUtils.SPRING_PATTERN)
  protected LocalDateTime listIssueDateTime;

  @NotNull(message = "{NotNull.schemeInformation.nextUpdate}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  @DateTimeFormat(pattern = DateUtils.SPRING_PATTERN)
  protected LocalDateTime nextUpdate;

  @Valid
  protected URIListType distributionPoints; // optional

  @Valid
  protected ExtensionsListType schemeExtensions; // optional

  public TSLSchemeInformationType() {
    this.tslType = new NonEmptyURIType();
    this.schemeOperatorName = new InternationalNamesType();
    this.schemeOperatorAddress = new AddressType();
    this.schemeName = new InternationalNamesType();
    this.schemeInformationURI = new NonEmptyMultiLangURIListType();
    this.statusDeterminationApproach = new NonEmptyURIType();
    this.schemeTypeCommunityRules = new NonEmptyMultiLangURIListType();
    this.schemeTerritory = new CountryCode();
    this.policyOrLegalNotice =  new PolicyOrLegalnoticeType();
    this.pointersToOtherTSL = new OtherTSLPointersType();
    this.distributionPoints = new URIListType();
    this.schemeExtensions = new ExtensionsListType();
  }

  public TSLSchemeInformationType(TSLSchemeInformationTypeJAXB schemeInformation) {
    if (schemeInformation != null) {
      if (schemeInformation.getTSLVersionIdentifier() != null) {
        this.tslVersionIdentifier = schemeInformation.getTSLVersionIdentifier();
      }
      if (schemeInformation.getTSLSequenceNumber() != null) {
        this.tslSequenceNumber = schemeInformation.getTSLSequenceNumber();
      }
      if (schemeInformation.getTSLType() != null) {
        this.tslType = new NonEmptyURIType(schemeInformation.getTSLType());
      } else {
        this.tslType = new NonEmptyURIType();
      }
      if (schemeInformation.getSchemeOperatorName() != null) {
        this.schemeOperatorName = new InternationalNamesType(
            schemeInformation.getSchemeOperatorName());
      } else {
        this.schemeOperatorName = new InternationalNamesType();
      }
      if (schemeInformation.getSchemeOperatorAddress() != null) {
        this.schemeOperatorAddress = new AddressType(schemeInformation.getSchemeOperatorAddress());
      } else {
        this.schemeOperatorAddress = new AddressType();
      }
      if (schemeInformation.getSchemeName() != null) {
        this.schemeName = new InternationalNamesType(schemeInformation.getSchemeName());
      } else {
        this.schemeName = new InternationalNamesType();
      }
      if (schemeInformation.getSchemeInformationURI() != null) {
        this.schemeInformationURI = new NonEmptyMultiLangURIListType(
            schemeInformation.getSchemeInformationURI());
      } else {
        this.schemeInformationURI = new NonEmptyMultiLangURIListType();
      }
      if (schemeInformation.getStatusDeterminationApproach() != null) {
        this.statusDeterminationApproach = new NonEmptyURIType(
            schemeInformation.getStatusDeterminationApproach());
      } else {
        this.statusDeterminationApproach = new NonEmptyURIType();
      }
      if (schemeInformation.getSchemeTypeCommunityRules() != null) {
        this.schemeTypeCommunityRules = new NonEmptyMultiLangURIListType(
            schemeInformation.getSchemeTypeCommunityRules());
      } else {
        this.schemeTypeCommunityRules = new NonEmptyMultiLangURIListType();
      }
      if (schemeInformation.getSchemeTerritory() != null) {
        this.schemeTerritory = new CountryCode(schemeInformation.getSchemeTerritory());
      } else {
        this.schemeTerritory = new CountryCode();
      }
      if (schemeInformation.getPolicyOrLegalNotice() != null) {
        this.policyOrLegalNotice = new PolicyOrLegalnoticeType(
            schemeInformation.getPolicyOrLegalNotice());
      } else {
        this.policyOrLegalNotice = new PolicyOrLegalnoticeType();
      }
      if (schemeInformation.getHistoricalInformationPeriod() != null) {
        this.historicalInformationPeriod = schemeInformation.getHistoricalInformationPeriod();
      }
      if (schemeInformation.getPointersToOtherTSL() != null) {
        this.pointersToOtherTSL = new OtherTSLPointersType(
            schemeInformation.getPointersToOtherTSL());
      } else {
        this.pointersToOtherTSL = new OtherTSLPointersType();
      }
      if (schemeInformation.getListIssueDateTime() != null) {
        this.listIssueDateTime = XmlGregorianCalendarUtils
            .toLocalDateTime(schemeInformation.getListIssueDateTime());
      }
      if (schemeInformation.getNextUpdate() != null
          && schemeInformation.getNextUpdate().getDateTime() != null) {
        this.nextUpdate = XmlGregorianCalendarUtils
            .toLocalDateTime(schemeInformation.getNextUpdate().getDateTime());
      }
      if (schemeInformation.getDistributionPoints() != null) {
        this.distributionPoints = new URIListType(
            schemeInformation.getDistributionPoints());
      } else {
        this.distributionPoints = new URIListType();
      }
      if (schemeInformation.getSchemeExtensions() != null) {
        this.schemeExtensions = new ExtensionsListType(schemeInformation.getSchemeExtensions());
      } else {
        this.schemeExtensions = new ExtensionsListType();
      }
    }
  }

  public TSLSchemeInformationTypeJAXB asJAXB() {
    TSLSchemeInformationTypeJAXB schemeInformation = new TSLSchemeInformationTypeJAXB();
    if (tslVersionIdentifier != null) {
      schemeInformation.setTSLVersionIdentifier(tslVersionIdentifier);
    }
    if (tslSequenceNumber != null) {
      schemeInformation.setTSLSequenceNumber(tslSequenceNumber);
    }
    if (tslType != null) {
      schemeInformation.setTSLType(tslType.getValue());
    }
    if (schemeOperatorName != null) {
      schemeInformation.setSchemeOperatorName(schemeOperatorName.asJAXB());
    }
    if (schemeOperatorAddress != null) {
      schemeInformation.setSchemeOperatorAddress(schemeOperatorAddress.asJAXB());
    }
    if (schemeName != null) {
      schemeInformation.setSchemeName(schemeName.asJAXB());
    }
    if (schemeInformationURI != null) {
      schemeInformation.setSchemeInformationURI(schemeInformationURI.asJAXB());
    }
    if (statusDeterminationApproach != null) {
      schemeInformation.setStatusDeterminationApproach(statusDeterminationApproach.getValue());
    }
    if (schemeTypeCommunityRules != null) {
      schemeInformation.setSchemeTypeCommunityRules(schemeTypeCommunityRules.asJAXB());
    }
    if (schemeTerritory != null) {
      schemeInformation.setSchemeTerritory(schemeTerritory.getValue());
    }
    if (policyOrLegalNotice != null) {
      schemeInformation.setPolicyOrLegalNotice(policyOrLegalNotice.asJAXB());
    }
    if (historicalInformationPeriod != null) {
      schemeInformation.setHistoricalInformationPeriod(historicalInformationPeriod);
    }
    if (pointersToOtherTSL != null) {
      schemeInformation.setPointersToOtherTSL(pointersToOtherTSL.asJAXB());
    }
    if (listIssueDateTime != null) {
      schemeInformation.setListIssueDateTime(ModelUtils.toXMLGregorianDate(listIssueDateTime));
    }
    if (nextUpdate != null) {
      NextUpdateTypeJAXB nextUpdateJAXB = new NextUpdateTypeJAXB();
      nextUpdateJAXB.setDateTime(ModelUtils.toXMLGregorianDate(nextUpdate));
      schemeInformation.setNextUpdate(nextUpdateJAXB);
    }
    if (distributionPoints != null && CollectionUtils.isNotEmpty(distributionPoints.getValues())) {
      schemeInformation.setDistributionPoints(distributionPoints.asJAXB());
    }
    if (schemeExtensions != null && CollectionUtils.isNotEmpty(schemeExtensions.getValues())) {
      schemeInformation.setSchemeExtensions(schemeExtensions.asJAXB());
    }
    return schemeInformation;
  }

  public BigInteger getTslVersionIdentifier() {
    return tslVersionIdentifier;
  }

  public void setTslVersionIdentifier(BigInteger tslVersionIdentifier) {
    this.tslVersionIdentifier = tslVersionIdentifier;
  }

  public BigInteger getTslSequenceNumber() {
    return tslSequenceNumber;
  }

  public void setTslSequenceNumber(BigInteger tslSequenceNumber) {
    this.tslSequenceNumber = tslSequenceNumber;
  }

  public NonEmptyURIType getTslType() {
    return tslType;
  }

  public void setTslType(NonEmptyURIType tslType) {
    this.tslType = tslType;
  }

  public InternationalNamesType getSchemeOperatorName() {
    return schemeOperatorName;
  }

  public void setSchemeOperatorName(
      InternationalNamesType schemeOperatorName) {
    this.schemeOperatorName = schemeOperatorName;
  }

  public AddressType getSchemeOperatorAddress() {
    return schemeOperatorAddress;
  }

  public void setSchemeOperatorAddress(AddressType schemeOperatorAddress) {
    this.schemeOperatorAddress = schemeOperatorAddress;
  }

  public InternationalNamesType getSchemeName() {
    return schemeName;
  }

  public void setSchemeName(InternationalNamesType schemeName) {
    this.schemeName = schemeName;
  }

  public NonEmptyMultiLangURIListType getSchemeInformationURI() {
    return schemeInformationURI;
  }

  public void setSchemeInformationURI(
      NonEmptyMultiLangURIListType schemeInformationURI) {
    this.schemeInformationURI = schemeInformationURI;
  }

  public NonEmptyURIType getStatusDeterminationApproach() {
    return statusDeterminationApproach;
  }

  public void setStatusDeterminationApproach(
      NonEmptyURIType statusDeterminationApproach) {
    this.statusDeterminationApproach = statusDeterminationApproach;
  }

  public NonEmptyMultiLangURIListType getSchemeTypeCommunityRules() {
    return schemeTypeCommunityRules;
  }

  public void setSchemeTypeCommunityRules(
      NonEmptyMultiLangURIListType schemeTypeCommunityRules) {
    this.schemeTypeCommunityRules = schemeTypeCommunityRules;
  }

  public CountryCode getSchemeTerritory() {
    return schemeTerritory;
  }

  public void setSchemeTerritory(CountryCode schemeTerritory) {
    this.schemeTerritory = schemeTerritory;
  }

  public PolicyOrLegalnoticeType getPolicyOrLegalNotice() {
    return policyOrLegalNotice;
  }

  public void setPolicyOrLegalNotice(
      PolicyOrLegalnoticeType policyOrLegalNotice) {
    this.policyOrLegalNotice = policyOrLegalNotice;
  }

  public BigInteger getHistoricalInformationPeriod() {
    return historicalInformationPeriod;
  }

  public void setHistoricalInformationPeriod(BigInteger historicalInformationPeriod) {
    this.historicalInformationPeriod = historicalInformationPeriod;
  }

  public OtherTSLPointersType getPointersToOtherTSL() {
    return pointersToOtherTSL;
  }

  public void setPointersToOtherTSL(OtherTSLPointersType pointersToOtherTSL) {
    this.pointersToOtherTSL = pointersToOtherTSL;
  }

  public LocalDateTime getListIssueDateTime() {
    return listIssueDateTime;
  }

  public void setListIssueDateTime(LocalDateTime listIssueDateTime) {
    this.listIssueDateTime = listIssueDateTime;
  }

  public LocalDateTime getNextUpdate() {
    return nextUpdate;
  }

  public void setNextUpdate(LocalDateTime nextUpdate) {
    this.nextUpdate = nextUpdate;
  }

  public URIListType getDistributionPoints() {
    return distributionPoints;
  }

  public void setDistributionPoints(URIListType distributionPoints) {
    this.distributionPoints = distributionPoints;
  }

  public ExtensionsListType getSchemeExtensions() {
    return schemeExtensions;
  }

  public void setSchemeExtensions(ExtensionsListType schemeExtensions) {
    this.schemeExtensions = schemeExtensions;
  }

}
