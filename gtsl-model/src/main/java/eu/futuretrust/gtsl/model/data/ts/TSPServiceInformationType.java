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

package eu.futuretrust.gtsl.model.data.ts;

import eu.futuretrust.gtsl.jaxb.tsl.DigitalIdentityListTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.TSPServiceInformationTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIListType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIOptionalListType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import eu.futuretrust.gtsl.model.data.digitalidentity.ServiceDigitalIdentityType;
import eu.futuretrust.gtsl.model.data.extension.ServiceInformationExtensionListType;
import eu.futuretrust.gtsl.model.utils.DateUtils;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import eu.futuretrust.gtsl.model.utils.XmlGregorianCalendarUtils;

import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.5 Service information
 *
 * <xsd:complexType name="TSPServiceInformationType"> <xsd:sequence> <!-- Specified in TS 119 612
 * v2.1.1 clause 5.5.1 Service type identifier --> <xsd:element ref="tsl:ServiceTypeIdentifier"/>
 * <!-- Specified in TS 119 612 v2.1.1 clause 5.5.2 Service name --> <xsd:element name="ServiceName"
 * type="tsl:InternationalNamesType"/> <!-- Specified in TS 119 612 v2.1.1 clause 5.5.3 Service
 * digital identity --> <xsd:element ref="tsl:ServiceDigitalIdentity"/> <!-- Specified in TS 119 612
 * v2.1.1 clause 5.5.4 Service current status --> <xsd:element ref="tsl:ServiceStatus"/> <!--
 * Specified in TS 119 612 v2.1.1 clause 5.5.5 Service status starting date and time -->
 * <xsd:element name="StatusStartingTime" type="xsd:dateTime"/> <!-- Specified in TS 119 612 v2.1.1
 * clause 5.5.6 Scheme service definition URI --> <xsd:element name="SchemeServiceDefinitionURI"
 * type="tsl:NonEmptyMultiLangURIListType" minOccurs="0"/> <!-- Specified in TS 119 612 v2.1.1
 * clause 5.5.7 Service supply points --> <xsd:element ref="tsl:ServiceSupplyPoints" minOccurs="0"/>
 * <!-- Specified in TS 119 612 v2.1.1 clause 5.5.8 TSP service definition URI --> <xsd:element
 * name="TSPServiceDefinitionURI" type="tsl:NonEmptyMultiLangURIListType" minOccurs="0"/> <!--
 * Specified in TS 119 612 v2.1.1 clause 5.5.9 Service information extensions --> <xsd:element
 * name="ServiceInformationExtensions" type="tsl:ExtensionsListType" minOccurs="0"/> </xsd:sequence>
 * </xsd:complexType>
 *
 * <xsd:element name="ServiceTypeIdentifier" type="tsl:NonEmptyURIType"/> <xsd:element
 * name="ServiceDigitalIdentity" type="tsl:ServiceDigitalIdentityType"/> <xsd:element
 * name="ServiceStatus" type="tsl:NonEmptyURIType"/> <xsd:element name="ServiceSupplyPoints"
 * type="tsl:ServiceSupplyPointsType"/>
 */
public class TSPServiceInformationType {

  @NotNull(message = "{NotNull.serviceInformation.serviceTypeIdentifier}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected NonEmptyURIType serviceTypeIdentifier;

  @NotNull(message = "{NotNull.serviceInformation.serviceName}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected InternationalNamesType serviceName;

  @NotNull(message = "{NotNull.serviceInformation.serviceDigitalIdentity}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected ServiceDigitalIdentityType serviceDigitalIdentity;

  @NotNull(message = "{NotNull.serviceInformation.serviceStatus}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected NonEmptyURIType serviceStatus;

  @NotNull(message = "{NotNull.serviceInformation.statusStartingTime}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  @DateTimeFormat(pattern = DateUtils.SPRING_PATTERN)
  protected LocalDateTime statusStartingTime;

  @Valid
  protected NonEmptyMultiLangURIOptionalListType schemeServiceDefinitionURI; // optional

  @Valid
  protected ServiceSupplyPointsType serviceSupplyPoints; //optional

  @Valid
  protected NonEmptyMultiLangURIOptionalListType tspServiceDefinitionURI; // optional

  @Valid
  protected ServiceInformationExtensionListType serviceInformationExtensions; // optional

  public TSPServiceInformationType() {
    this.serviceTypeIdentifier = new NonEmptyURIType();
    this.serviceName = new InternationalNamesType();
    this.serviceDigitalIdentity = new ServiceDigitalIdentityType();
    this.serviceStatus = new NonEmptyURIType();
    this.schemeServiceDefinitionURI = new NonEmptyMultiLangURIOptionalListType();
    this.serviceSupplyPoints = new ServiceSupplyPointsType();
    this.tspServiceDefinitionURI = new NonEmptyMultiLangURIOptionalListType();
    this.serviceInformationExtensions = new ServiceInformationExtensionListType();
  }

  public TSPServiceInformationType(TSPServiceInformationTypeJAXB serviceInformation) {
    if (serviceInformation != null) {
      if (serviceInformation.getServiceTypeIdentifier() != null) {
        this.serviceTypeIdentifier = new NonEmptyURIType(
            serviceInformation.getServiceTypeIdentifier());
      } else {
        this.serviceTypeIdentifier = new NonEmptyURIType();
      }
      if (serviceInformation.getServiceName() != null) {
        this.serviceName = new InternationalNamesType(serviceInformation.getServiceName());
      } else {
        this.serviceName = new InternationalNamesType();
      }
      if (serviceInformation.getServiceDigitalIdentity() != null) {
        this.serviceDigitalIdentity = new ServiceDigitalIdentityType(
            serviceInformation.getServiceDigitalIdentity());
      } else {
        this.serviceDigitalIdentity = new ServiceDigitalIdentityType();
      }
      if (serviceInformation.getServiceStatus() != null) {
        this.serviceStatus = new NonEmptyURIType(serviceInformation.getServiceStatus());
      } else {
        this.serviceStatus = new NonEmptyURIType();
      }
      if (serviceInformation.getStatusStartingTime() != null) {
        this.statusStartingTime = XmlGregorianCalendarUtils
            .toLocalDateTime(serviceInformation.getStatusStartingTime());
      }
      if (serviceInformation.getSchemeServiceDefinitionURI() != null) {
        this.schemeServiceDefinitionURI = new NonEmptyMultiLangURIOptionalListType(
            serviceInformation.getSchemeServiceDefinitionURI());
      } else {
        this.schemeServiceDefinitionURI = new NonEmptyMultiLangURIOptionalListType();
      }
      if (serviceInformation.getServiceSupplyPoints() != null) {
        this.serviceSupplyPoints = new ServiceSupplyPointsType(
            serviceInformation.getServiceSupplyPoints());
      } else {
        this.serviceSupplyPoints = new ServiceSupplyPointsType();
      }
      if (serviceInformation.getTSPServiceDefinitionURI() != null) {
        this.tspServiceDefinitionURI = new NonEmptyMultiLangURIOptionalListType(
            serviceInformation.getTSPServiceDefinitionURI());
      } else {
        this.tspServiceDefinitionURI = new NonEmptyMultiLangURIOptionalListType();
      }
      if (serviceInformation.getServiceInformationExtensions() != null) {
        this.serviceInformationExtensions = new ServiceInformationExtensionListType(
            serviceInformation.getServiceInformationExtensions());
      } else {
        this.serviceInformationExtensions = new ServiceInformationExtensionListType();
      }
    }
  }

  public TSPServiceInformationTypeJAXB asJAXB() {
    TSPServiceInformationTypeJAXB serviceInformation = new TSPServiceInformationTypeJAXB();
    if (serviceTypeIdentifier != null) {
      serviceInformation.setServiceTypeIdentifier(serviceTypeIdentifier.getValue());
    }
    if (serviceName != null) {
      serviceInformation.setServiceName(serviceName.asJAXB());
    }
    if (serviceDigitalIdentity != null) {
      DigitalIdentityListTypeJAXB serviceDigitalIdentityJAXB = serviceDigitalIdentity.asJAXB();
      if (serviceDigitalIdentityJAXB != null) {
        serviceInformation.setServiceDigitalIdentity(serviceDigitalIdentityJAXB);
      }
    }
    if (serviceStatus != null) {
      serviceInformation.setServiceStatus(serviceStatus.getValue());
    }
    if (statusStartingTime != null) {
      serviceInformation.setStatusStartingTime(ModelUtils.toXMLGregorianDate(statusStartingTime));
    }
    if (schemeServiceDefinitionURI != null && CollectionUtils.isNotEmpty(schemeServiceDefinitionURI.getValues())) {
      serviceInformation.setSchemeServiceDefinitionURI(schemeServiceDefinitionURI.asJAXB());
    }
    if (serviceSupplyPoints != null) {
      serviceInformation.setServiceSupplyPoints(serviceSupplyPoints.asJAXB());
    }
    if (tspServiceDefinitionURI != null && CollectionUtils.isNotEmpty(tspServiceDefinitionURI.getValues())) {
      serviceInformation.setTSPServiceDefinitionURI(tspServiceDefinitionURI.asJAXB());
    }
    if (serviceInformationExtensions != null) {
      serviceInformation.setServiceInformationExtensions(serviceInformationExtensions.asJAXB());
    }
    return serviceInformation;
  }

  public NonEmptyURIType getServiceTypeIdentifier() {
    return serviceTypeIdentifier;
  }

  public void setServiceTypeIdentifier(
      NonEmptyURIType serviceTypeIdentifier) {
    this.serviceTypeIdentifier = serviceTypeIdentifier;
  }

  public InternationalNamesType getServiceName() {
    return serviceName;
  }

  public void setServiceName(InternationalNamesType serviceName) {
    this.serviceName = serviceName;
  }

  public ServiceDigitalIdentityType getServiceDigitalIdentity() {
    return serviceDigitalIdentity;
  }

  public void setServiceDigitalIdentity(
      ServiceDigitalIdentityType serviceDigitalIdentity) {
    this.serviceDigitalIdentity = serviceDigitalIdentity;
  }

  public NonEmptyURIType getServiceStatus() {
    return serviceStatus;
  }

  public void setServiceStatus(NonEmptyURIType serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public LocalDateTime getStatusStartingTime() {
    return statusStartingTime;
  }

  public void setStatusStartingTime(LocalDateTime statusStartingTime) {
    this.statusStartingTime = statusStartingTime;
  }

  public NonEmptyMultiLangURIOptionalListType getSchemeServiceDefinitionURI() {
    return schemeServiceDefinitionURI;
  }

  public void setSchemeServiceDefinitionURI(
      NonEmptyMultiLangURIOptionalListType schemeServiceDefinitionURI) {
    this.schemeServiceDefinitionURI = schemeServiceDefinitionURI;
  }

  public ServiceSupplyPointsType getServiceSupplyPoints() {
    return serviceSupplyPoints;
  }

  public void setServiceSupplyPoints(
      ServiceSupplyPointsType serviceSupplyPoints) {
    this.serviceSupplyPoints = serviceSupplyPoints;
  }

  public NonEmptyMultiLangURIOptionalListType getTspServiceDefinitionURI() {
    return tspServiceDefinitionURI;
  }

  public void setTspServiceDefinitionURI(
      NonEmptyMultiLangURIOptionalListType tspServiceDefinitionURI) {
    this.tspServiceDefinitionURI = tspServiceDefinitionURI;
  }

  public ServiceInformationExtensionListType getServiceInformationExtensions() {
    return serviceInformationExtensions;
  }

  public void setServiceInformationExtensions(
      ServiceInformationExtensionListType serviceInformationExtensions) {
    this.serviceInformationExtensions = serviceInformationExtensions;
  }

}
