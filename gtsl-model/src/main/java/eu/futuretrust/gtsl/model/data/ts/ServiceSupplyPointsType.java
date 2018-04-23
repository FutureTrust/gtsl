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

import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.jaxb.tsl.ServiceSupplyPointsTypeJAXB;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.5.7 Service supply points
 *
 * ServiceSupplyPointsType changed to use AttributedNonEmptyURIType. The optional type attribute of
 * AttributedNonEmptyURIType allows for each URI to specify the service/information to be found
 * under this URI. For example a ServiceSupplyPoints field associated to a service of a type
 * "http://uri.etsi.org/TrstSvc/Svctype/CA/QC" could include: -	a URI pointing towards a descriptive
 * text where users could be given information on (local) registration authorities and procedures to
 * follow for being issued qualified certificates; -	a URI providing a CRL distribution point giving
 * certificate status information for qualified certificates issued by or under the and further
 * specified by the type attibute value "http://uri.etsi.org/TrstSvc/Svctype/Certstatus/CRL/QC".
 * Such URI can for example provide access to a last and final CRL in case of service unexpected
 * termination and/or impossibility to provide such a final CRL at the CRL distribution point
 * available from issued certificate's extensions; and/or -	a URI providing one access location of
 * an OCSP responder authorized to provide certificate status information for qualified certificates
 * issued by or under the service, and further specified by the type attibute value
 * "http://uri.etsi.org/TrstSvc/Svctype/Certstatus/OCSP/QC".
 *
 * <xsd:complexType name="ServiceSupplyPointsType"> <xsd:sequence maxOccurs="unbounded">
 * <xsd:element name="ServiceSupplyPoint" type="tsl:AttributedNonEmptyURIType"/> </xsd:sequence>
 * </xsd:complexType>
 */
public class ServiceSupplyPointsType extends ListModel<NonEmptyURIType> {

  public ServiceSupplyPointsType() {
  }

  public ServiceSupplyPointsType(
      List<NonEmptyURIType> values) {
    super(values);
  }

  public ServiceSupplyPointsType(ServiceSupplyPointsTypeJAXB serviceSupplyPoints) {
    super(CollectionUtils.isNotEmpty(serviceSupplyPoints.getServiceSupplyPoint())
        ? serviceSupplyPoints.getServiceSupplyPoint().stream().map(NonEmptyURIType::new)
        .collect(Collectors.toList()) : Collections.emptyList());
  }

  @Override
  public List<NonEmptyURIType> getValues() {
    return values;
  }

  public ServiceSupplyPointsTypeJAXB asJAXB() {
    if (CollectionUtils.isNotEmpty(values)) {
      ServiceSupplyPointsTypeJAXB supplyPoints = new ServiceSupplyPointsTypeJAXB();
      supplyPoints.getServiceSupplyPoint()
          .addAll(values.stream().map(NonEmptyURIType::getValue).collect(
              Collectors.toList()));
      return supplyPoints;
    }
    return null;
  }
}
