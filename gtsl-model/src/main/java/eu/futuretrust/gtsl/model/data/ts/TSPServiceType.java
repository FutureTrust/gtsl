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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import eu.futuretrust.gtsl.jaxb.tsl.TSPServiceTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.enums.ServiceType;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.4.6 TSP Services (list of services)
 *
 * <xsd:complexType name="TSPServiceType"> <xsd:sequence> <!-- Specified in TS 119 612 v2.1.1 clause
 * 5.5 Service information --> <xsd:element ref="tsl:ServiceInformation"/> <!-- Specified in TS 119
 * 612 v2.1.1 clause 5.5.10 Service history --> <xsd:element ref="tsl:ServiceHistory"
 * minOccurs="0"/> </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="ServiceInformation" type="tsl:TSPServiceInformationType"/> <xsd:element
 * name="ServiceHistory" type="tsl:ServiceHistoryType"/>
 */
public class TSPServiceType {

  @NotNull(message = "{NotNull.service.serviceInformation}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected TSPServiceInformationType serviceInformation;

  @Valid
  protected ServiceHistoryType serviceHistory; // optional

  @JsonInclude(Include.NON_NULL)
  protected Set<ServiceType> serviceTypes;

  public TSPServiceType() {
    this.serviceInformation = new TSPServiceInformationType();
    this.serviceHistory = new ServiceHistoryType();
  }

  public TSPServiceType(TSPServiceTypeJAXB service) {
    if (service != null) {
      if (service.getServiceInformation() != null) {
        this.serviceInformation = new TSPServiceInformationType(service.getServiceInformation());
      } else {
        this.serviceInformation = new TSPServiceInformationType();
      }
      if (service.getServiceHistory() != null) {
        this.serviceHistory = new ServiceHistoryType(service.getServiceHistory());
      } else {
        this.serviceHistory = new ServiceHistoryType();
      }
    }
  }

  public TSPServiceTypeJAXB asJAXB() {
    TSPServiceTypeJAXB service = new TSPServiceTypeJAXB();
    if (serviceInformation != null) {
      service.setServiceInformation(serviceInformation.asJAXB());
      if (serviceHistory != null && serviceInformation.getServiceDigitalIdentity() != null) {
        service.setServiceHistory(
            serviceHistory.asJAXB(serviceInformation.getServiceDigitalIdentity().getValues()));
      }
    }
    return service;
  }

  public TSPServiceInformationType getServiceInformation() {
    return serviceInformation;
  }

  public void setServiceInformation(
      TSPServiceInformationType serviceInformation) {
    this.serviceInformation = serviceInformation;
  }

  public ServiceHistoryType getServiceHistory() {
    return serviceHistory;
  }

  public void setServiceHistory(ServiceHistoryType serviceHistory) {
    this.serviceHistory = serviceHistory;
  }

  public Set<ServiceType> getServiceTypes() {
    return serviceTypes;
  }

  public void setServiceTypes(Set<ServiceType> serviceTypes) {
    this.serviceTypes = serviceTypes;
  }
}
