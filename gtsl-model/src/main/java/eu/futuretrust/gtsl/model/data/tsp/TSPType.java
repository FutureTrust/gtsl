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

package eu.futuretrust.gtsl.model.data.tsp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import eu.futuretrust.gtsl.jaxb.tsl.TSPTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.enums.ServiceType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.18 Trust Service Provider List
 *
 * <xsd:complexType name="TSPType"> <xsd:sequence> <!-- Specified in TS 119 612 v2.1.1 clause 5.4
 * TSP information --> <xsd:element ref="tsl:TSPInformation"/> <!-- Specified in TS 119 612 v2.1.1
 * clause 5.4.6 TSP Services (list of services) --> <xsd:element ref="tsl:TSPServices"/>
 * </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="TSPInformation" type="tsl:TSPInformationType"/> <xsd:element
 * name="TSPServices" type="tsl:TSPServicesListType"/>
 */
public class TSPType {

  @NotNull(message = "{NotNull.tsp.tspInformation}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected TSPInformationType tspInformation;

  @NotNull(message = "{NotNull.tsp.tspServices}", payload = {Severity.Error.class,
      Impact.Legal.class})
  @Valid
  protected TSPServicesListType tspServices;

  // use to know if the TSP have at least one active service
  @JsonInclude(Include.NON_NULL)
  protected boolean active;

  @JsonInclude(Include.NON_NULL)
  protected Set<ServiceType> serviceTypes;

  public TSPType() {
  }

  public TSPType(TSPTypeJAXB tsp) {
    if (tsp != null) {
      if (tsp.getTSPInformation() != null) {
        tspInformation = new TSPInformationType(tsp.getTSPInformation());
      } else {
        tspInformation = new TSPInformationType();
      }
      if (tsp.getTSPServices() != null) {
        tspServices = new TSPServicesListType(tsp.getTSPServices());
      } else {
        tspServices = new TSPServicesListType();
      }
    }
  }

  public TSPTypeJAXB asJAXB() {
    TSPTypeJAXB tsp = new TSPTypeJAXB();
    if (tspInformation != null) {
      tsp.setTSPInformation(tspInformation.asJAXB());
    }
    if (tspServices != null) {
      tsp.setTSPServices(tspServices.asJAXB());
    }
    return tsp;
  }

  public TSPInformationType getTspInformation() {
    return tspInformation;
  }

  public void setTspInformation(TSPInformationType tspInformation) {
    this.tspInformation = tspInformation;
  }

  public TSPServicesListType getTspServices() {
    return tspServices;
  }

  public void setTspServices(TSPServicesListType tspServices) {
    this.tspServices = tspServices;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Set<ServiceType> getServiceTypes() {
    return serviceTypes;
  }

  public void setServiceTypes(Set<ServiceType> serviceTypes) {
    this.serviceTypes = serviceTypes;
  }
}
