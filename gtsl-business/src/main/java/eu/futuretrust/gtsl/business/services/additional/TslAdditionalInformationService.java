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

package eu.futuretrust.gtsl.business.services.additional;

import eu.futuretrust.gtsl.model.data.enums.ServiceType;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.model.data.tsp.TSPType;
import eu.futuretrust.gtsl.properties.RulesProperties;
import java.util.List;
import java.util.Set;

public interface TslAdditionalInformationService {

  void appendAdditionalInformation(RulesProperties properties, TrustStatusListType tsl);

  void appendAdditionalInformation(RulesProperties properties, TSPType tsp);

  void appendAdditionalInformation(RulesProperties properties, TSPServiceType service);

  Set<ServiceType> getServiceTypes(RulesProperties properties, TSPServiceType service);

  Set<String> getServiceStatus(List<ServiceType> serviceTypes, TSPServiceType service,
      Set<ServiceType> qServiceTypes);

  boolean isTspActive(RulesProperties properties, TSPType tsp);

  Set<ServiceType> sortServiceTypes(Set<ServiceType> set);
}
