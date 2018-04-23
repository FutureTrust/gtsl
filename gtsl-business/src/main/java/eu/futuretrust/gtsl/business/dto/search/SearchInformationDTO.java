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

package eu.futuretrust.gtsl.business.dto.search;


import eu.futuretrust.gtsl.business.dto.tsl.CountryDTO;
import eu.futuretrust.gtsl.model.data.enums.ServiceType;
import java.util.List;

public class SearchInformationDTO {

  private List<CountryDTO> countries;
  private List<ServiceType> serviceTypes;

  public SearchInformationDTO() {
  }

  public SearchInformationDTO(List<CountryDTO> countries, List<ServiceType> serviceTypes) {
    this.countries = countries;
    this.serviceTypes = serviceTypes;
  }

  public List<CountryDTO> getCountries() {
    return countries;
  }

  public void setCountries(List<CountryDTO> countries) {
    this.countries = countries;
  }

  public List<ServiceType> getServiceTypes() {
    return serviceTypes;
  }

  public void setServiceTypes(List<ServiceType> serviceTypes) {
    this.serviceTypes = serviceTypes;
  }
}
