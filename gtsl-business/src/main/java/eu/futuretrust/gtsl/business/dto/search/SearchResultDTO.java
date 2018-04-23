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

import eu.futuretrust.gtsl.model.data.enums.ServiceType;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class SearchResultDTO {

  private String countryCode;
  private String name;
  private String trustmark;
  private Set<ServiceType> qServiceTypes;
  private Set<String> serviceStatus;
  private boolean active;
  private int index;

  public SearchResultDTO(int index, String countryCode, String name, String trustmark) {
    this.index = index;
    this.countryCode = countryCode;
    this.name = name;
    this.trustmark = trustmark;
    this.qServiceTypes = new HashSet<>();
    this.serviceStatus = new HashSet<>();
    this.active = false;
  }

  public boolean validate() {
    return StringUtils.isNotEmpty(countryCode)
        && StringUtils.isNotEmpty(name)
        && StringUtils.isNotEmpty(trustmark)
        && CollectionUtils.isNotEmpty(qServiceTypes)
        && CollectionUtils.isNotEmpty(serviceStatus);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SearchResultDTO that = (SearchResultDTO) o;

    if (active != that.active) {
      return false;
    }
    if (index != that.index) {
      return false;
    }
    if (countryCode != null ? !countryCode.equals(that.countryCode) : that.countryCode != null) {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }
    if (trustmark != null ? !trustmark.equals(that.trustmark) : that.trustmark != null) {
      return false;
    }
    if (qServiceTypes != null ? !qServiceTypes.equals(that.qServiceTypes)
        : that.qServiceTypes != null) {
      return false;
    }
    return serviceStatus != null ? serviceStatus.equals(that.serviceStatus)
        : that.serviceStatus == null;
  }

  @Override
  public int hashCode() {
    int result = countryCode != null ? countryCode.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (trustmark != null ? trustmark.hashCode() : 0);
    result = 31 * result + (qServiceTypes != null ? qServiceTypes.hashCode() : 0);
    result = 31 * result + (serviceStatus != null ? serviceStatus.hashCode() : 0);
    result = 31 * result + (active ? 1 : 0);
    result = 31 * result + index;
    return result;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<ServiceType> getqServiceTypes() {
    return qServiceTypes;
  }

  public void setqServiceTypes(Set<ServiceType> qServiceTypes) {
    this.qServiceTypes = qServiceTypes;
  }

  public Set<String> getServiceStatus() {
    return serviceStatus;
  }

  public void setServiceStatus(Set<String> serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public String getTrustmark() {
    return trustmark;
  }

  public void setTrustmark(String trustmark) {
    this.trustmark = trustmark;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
