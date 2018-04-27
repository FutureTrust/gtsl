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

import eu.futuretrust.gtsl.jaxb.tsl.ServiceHistoryInstanceTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
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
 * Specified in TS 119 612 v2.1.1 clause 5.6 Service history instance
 *
 * <xsd:complexType name="ServiceHistoryInstanceType"> <xsd:sequence> <!-- Specified in TS 119 612
 * v2.1.1 clause 5.6.1 Service type identifier --> <xsd:element ref="tsl:ServiceTypeIdentifier"/>
 * <!-- Specified in TS 119 612 v2.1.1 clause 5.6.2 Service name --> <xsd:element name="ServiceName"
 * type="tsl:InternationalNamesType"/> <!-- Specified in TS 119 612 v2.1.1 clause 5.6.3 Service
 * digital identity --> <xsd:element ref="tsl:ServiceDigitalIdentity"/> <!-- Specified in TS 119 612
 * v2.1.1 clause 5.6.4 Service previous status --> <xsd:element ref="tsl:ServiceStatus"/> <!--
 * Specified in TS 119 612 v2.1.1 clause 5.6.5 Previous status starting date and time -->
 * <xsd:element name="StatusStartingTime" type="xsd:dateTime"/> <!-- Specified in TS 119 612 v2.1.1
 * clause 5.6.6 Service information extensions --> <xsd:element name="ServiceInformationExtensions"
 * type="tsl:ExtensionsListType" minOccurs="0"/> </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="ServiceTypeIdentifier" type="tsl:NonEmptyURIType"/> <xsd:element
 * name="ServiceDigitalIdentity" type="tsl:ServiceDigitalIdentityType"/> <xsd:element
 * name="ServiceStatus" type="tsl:NonEmptyURIType"/>
 */
public class ServiceHistoryInstanceType {

  @NotNull(message = "{NotNull.serviceHistoryInstance.serviceTypeIdentifier}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected NonEmptyURIType serviceTypeIdentifier;

  @NotNull(message = "{NotNull.serviceHistoryInstance.serviceName}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected InternationalNamesType serviceName;

  @NotNull(message = "{NotNull.serviceHistoryInstance.serviceDigitalIdentity}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected ServiceDigitalIdentityType serviceDigitalIdentity;

  @NotNull(message = "{NotNull.serviceHistoryInstance.serviceStatus}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  protected NonEmptyURIType serviceStatus;

  @NotNull(message = "{NotNull.serviceHistoryInstance.statusStartingTime}", payload = {
      Severity.Error.class, Impact.Legal.class})
  @Valid
  @DateTimeFormat(pattern = DateUtils.SPRING_PATTERN)
  protected LocalDateTime statusStartingTime;

  @Valid
  protected ServiceInformationExtensionListType serviceInformationExtensions; // optional

  public ServiceHistoryInstanceType() {
  }

  public ServiceHistoryInstanceType(ServiceHistoryInstanceTypeJAXB serviceHistoryInstanceJAXB) {
    if (serviceHistoryInstanceJAXB != null) {
      if (serviceHistoryInstanceJAXB.getServiceTypeIdentifier() != null) {
        this.serviceTypeIdentifier = new NonEmptyURIType(
            serviceHistoryInstanceJAXB.getServiceTypeIdentifier());
      }
      if (serviceHistoryInstanceJAXB.getServiceName() != null) {
        this.serviceName = new InternationalNamesType(serviceHistoryInstanceJAXB.getServiceName());
      }
      if (serviceHistoryInstanceJAXB.getServiceDigitalIdentity() != null) {
        this.serviceDigitalIdentity = new ServiceDigitalIdentityType(
            serviceHistoryInstanceJAXB.getServiceDigitalIdentity());
      }
      if (serviceHistoryInstanceJAXB.getServiceStatus() != null) {
        this.serviceStatus = new NonEmptyURIType(serviceHistoryInstanceJAXB.getServiceStatus());
      }
      if (serviceHistoryInstanceJAXB.getStatusStartingTime() != null) {
        this.statusStartingTime = XmlGregorianCalendarUtils
            .toLocalDateTime(serviceHistoryInstanceJAXB.getStatusStartingTime());
      }
      if (serviceHistoryInstanceJAXB.getServiceInformationExtensions() != null) {
        this.serviceInformationExtensions = new ServiceInformationExtensionListType(
            serviceHistoryInstanceJAXB.getServiceInformationExtensions());
      } else {
        this.serviceInformationExtensions = new ServiceInformationExtensionListType();
      }
    }
  }

  public ServiceHistoryInstanceTypeJAXB asJAXB(CertificateType serviceCert) {
    ServiceHistoryInstanceTypeJAXB serviceHistoryInstanceJAXB = new ServiceHistoryInstanceTypeJAXB();
    if (serviceTypeIdentifier != null) {
      serviceHistoryInstanceJAXB.setServiceTypeIdentifier(serviceTypeIdentifier.getValue());
    }
    if (serviceName != null) {
      serviceHistoryInstanceJAXB.setServiceName(serviceName.asJAXB());
    }
    if (serviceStatus != null) {
      serviceHistoryInstanceJAXB.setServiceStatus(serviceStatus.getValue());
    }
    if (statusStartingTime != null) {
      serviceHistoryInstanceJAXB
          .setStatusStartingTime(ModelUtils.toXMLGregorianDate(statusStartingTime));
    }
    if (serviceDigitalIdentity != null && CollectionUtils
        .isNotEmpty(serviceDigitalIdentity.getValues())) {
      serviceHistoryInstanceJAXB.setServiceDigitalIdentity(
          serviceDigitalIdentity.getValues().get(0).asHistoricTSLTypeV5(serviceCert));
    }
    if (serviceInformationExtensions != null) {
      serviceHistoryInstanceJAXB
          .setServiceInformationExtensions(serviceInformationExtensions.asJAXB());
    }
    return serviceHistoryInstanceJAXB;
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

  public ServiceInformationExtensionListType getServiceInformationExtensions() {
    return serviceInformationExtensions;
  }

  public void setServiceInformationExtensions(
      ServiceInformationExtensionListType serviceInformationExtensions) {
    this.serviceInformationExtensions = serviceInformationExtensions;
  }

}
