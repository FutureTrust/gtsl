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

package eu.futuretrust.gtsl.properties;

import eu.futuretrust.gtsl.properties.rules.AdditionalQualifierProperties;
import eu.futuretrust.gtsl.properties.rules.ConstantProperties;
import eu.futuretrust.gtsl.properties.rules.Country;
import eu.futuretrust.gtsl.properties.rules.LoTLProperties;
import eu.futuretrust.gtsl.properties.rules.NationalServiceTypeProperties;
import eu.futuretrust.gtsl.properties.rules.NonQualifiedServiceTypeProperties;
import eu.futuretrust.gtsl.properties.rules.QualifiedServiceTypeProperties;
import eu.futuretrust.gtsl.properties.rules.QualifierProperties;
import eu.futuretrust.gtsl.properties.rules.SchemeRulesProperties;
import eu.futuretrust.gtsl.properties.rules.SearchType;
import eu.futuretrust.gtsl.properties.rules.ServicePreviousStatusProperties;
import eu.futuretrust.gtsl.properties.rules.ServiceStatusProperties;
import eu.futuretrust.gtsl.properties.rules.StatusDetnProperties;
import eu.futuretrust.gtsl.properties.rules.TslProperties;
import eu.futuretrust.gtsl.properties.rules.TslTypeProperties;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;

public class RulesProperties {

  @NotNull
  private Map<String, String> mimeType;
  @NotNull
  private Map<String, Country> countries;
  @NotNull
  private ConstantProperties constant;
  @NotNull
  private LoTLProperties lotl;
  @NotNull
  private SchemeRulesProperties schemeRules;
  @NotNull
  private ServicePreviousStatusProperties servicePreviousStatus;
  @NotNull
  private ServiceStatusProperties serviceStatus;
  @NotNull
  private QualifiedServiceTypeProperties qualifiedServiceType;
  @NotNull
  private NonQualifiedServiceTypeProperties nonQualifiedServiceType;
  @NotNull
  private NationalServiceTypeProperties nationalServiceType;
  @NotNull
  private StatusDetnProperties statusDetn;
  @NotNull
  private QualifierProperties qualifier;
  @NotNull
  private AdditionalQualifierProperties additionalQualifier;
  @NotNull
  private TslProperties tsl;
  @NotNull
  private TslTypeProperties tslType;
  @NotNull
  private List<SearchType> searchTypes;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RulesProperties that = (RulesProperties) o;

    if (mimeType != null ? !mimeType.equals(that.mimeType) : that.mimeType != null) {
      return false;
    }
    if (countries != null ? !countries.equals(that.countries) : that.countries != null) {
      return false;
    }
    if (constant != null ? !constant.equals(that.constant) : that.constant != null) {
      return false;
    }
    if (lotl != null ? !lotl.equals(that.lotl) : that.lotl != null) {
      return false;
    }
    if (schemeRules != null ? !schemeRules.equals(that.schemeRules) : that.schemeRules != null) {
      return false;
    }
    if (servicePreviousStatus != null ? !servicePreviousStatus.equals(that.servicePreviousStatus)
        : that.servicePreviousStatus != null) {
      return false;
    }
    if (serviceStatus != null ? !serviceStatus.equals(that.serviceStatus)
        : that.serviceStatus != null) {
      return false;
    }
    if (qualifiedServiceType != null ? !qualifiedServiceType.equals(that.qualifiedServiceType)
        : that.qualifiedServiceType != null) {
      return false;
    }
    if (nonQualifiedServiceType != null ? !nonQualifiedServiceType
        .equals(that.nonQualifiedServiceType) : that.nonQualifiedServiceType != null) {
      return false;
    }
    if (nationalServiceType != null ? !nationalServiceType.equals(that.nationalServiceType)
        : that.nationalServiceType != null) {
      return false;
    }
    if (statusDetn != null ? !statusDetn.equals(that.statusDetn) : that.statusDetn != null) {
      return false;
    }
    if (qualifier != null ? !qualifier.equals(that.qualifier) : that.qualifier != null) {
      return false;
    }
    if (additionalQualifier != null ? !additionalQualifier.equals(that.additionalQualifier)
        : that.additionalQualifier != null) {
      return false;
    }
    if (tsl != null ? !tsl.equals(that.tsl) : that.tsl != null) {
      return false;
    }
    if (tslType != null ? !tslType.equals(that.tslType) : that.tslType != null) {
      return false;
    }
    return searchTypes != null ? searchTypes.equals(that.searchTypes) : that.searchTypes == null;
  }

  @Override
  public int hashCode() {
    int result = mimeType != null ? mimeType.hashCode() : 0;
    result = 31 * result + (countries != null ? countries.hashCode() : 0);
    result = 31 * result + (constant != null ? constant.hashCode() : 0);
    result = 31 * result + (lotl != null ? lotl.hashCode() : 0);
    result = 31 * result + (schemeRules != null ? schemeRules.hashCode() : 0);
    result = 31 * result + (servicePreviousStatus != null ? servicePreviousStatus.hashCode() : 0);
    result = 31 * result + (serviceStatus != null ? serviceStatus.hashCode() : 0);
    result = 31 * result + (qualifiedServiceType != null ? qualifiedServiceType.hashCode() : 0);
    result =
        31 * result + (nonQualifiedServiceType != null ? nonQualifiedServiceType.hashCode() : 0);
    result = 31 * result + (nationalServiceType != null ? nationalServiceType.hashCode() : 0);
    result = 31 * result + (statusDetn != null ? statusDetn.hashCode() : 0);
    result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
    result = 31 * result + (additionalQualifier != null ? additionalQualifier.hashCode() : 0);
    result = 31 * result + (tsl != null ? tsl.hashCode() : 0);
    result = 31 * result + (tslType != null ? tslType.hashCode() : 0);
    result = 31 * result + (searchTypes != null ? searchTypes.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "RulesProperties{" +
        "mimeType=" + mimeType +
        ", countries=" + countries +
        ", constant=" + constant +
        ", lotl=" + lotl +
        ", schemeRules=" + schemeRules +
        ", servicePreviousStatus=" + servicePreviousStatus +
        ", serviceStatus=" + serviceStatus +
        ", qualifiedServiceType=" + qualifiedServiceType +
        ", nonQualifiedServiceType=" + nonQualifiedServiceType +
        ", nationalServiceType=" + nationalServiceType +
        ", statusDetn=" + statusDetn +
        ", qualifier=" + qualifier +
        ", additionalQualifier=" + additionalQualifier +
        ", tsl=" + tsl +
        ", tslType=" + tslType +
        '}';
  }

  public Map<String, String> getMimeType() {
    return mimeType;
  }

  public void setMimeType(Map<String, String> mimeType) {
    this.mimeType = mimeType;
  }

  public Map<String, Country> getCountries() {
    return countries;
  }

  public void setCountries(Map<String, Country> countries) {
    this.countries = countries;
  }

  public ConstantProperties getConstant() {
    return constant;
  }

  public void setConstant(ConstantProperties constant) {
    this.constant = constant;
  }

  public LoTLProperties getLotl() {
    return lotl;
  }

  public void setLotl(LoTLProperties lotl) {
    this.lotl = lotl;
  }

  public SchemeRulesProperties getSchemeRules() {
    return schemeRules;
  }

  public void setSchemeRules(SchemeRulesProperties schemeRules) {
    this.schemeRules = schemeRules;
  }

  public ServicePreviousStatusProperties getServicePreviousStatus() {
    return servicePreviousStatus;
  }

  public void setServicePreviousStatus(ServicePreviousStatusProperties servicePreviousStatus) {
    this.servicePreviousStatus = servicePreviousStatus;
  }

  public ServiceStatusProperties getServiceStatus() {
    return serviceStatus;
  }

  public void setServiceStatus(ServiceStatusProperties serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public QualifiedServiceTypeProperties getQualifiedServiceType() {
    return qualifiedServiceType;
  }

  public void setQualifiedServiceType(QualifiedServiceTypeProperties qualifiedServiceType) {
    this.qualifiedServiceType = qualifiedServiceType;
  }

  public NonQualifiedServiceTypeProperties getNonQualifiedServiceType() {
    return nonQualifiedServiceType;
  }

  public void setNonQualifiedServiceType(
      NonQualifiedServiceTypeProperties nonQualifiedServiceType) {
    this.nonQualifiedServiceType = nonQualifiedServiceType;
  }

  public NationalServiceTypeProperties getNationalServiceType() {
    return nationalServiceType;
  }

  public void setNationalServiceType(NationalServiceTypeProperties nationalServiceType) {
    this.nationalServiceType = nationalServiceType;
  }

  public StatusDetnProperties getStatusDetn() {
    return statusDetn;
  }

  public void setStatusDetn(StatusDetnProperties statusDetn) {
    this.statusDetn = statusDetn;
  }

  public QualifierProperties getQualifier() {
    return qualifier;
  }

  public void setQualifier(QualifierProperties qualifier) {
    this.qualifier = qualifier;
  }

  public AdditionalQualifierProperties getAdditionalQualifier() {
    return additionalQualifier;
  }

  public void setAdditionalQualifier(AdditionalQualifierProperties additionalQualifier) {
    this.additionalQualifier = additionalQualifier;
  }

  public TslProperties getTsl() {
    return tsl;
  }

  public void setTsl(TslProperties tsl) {
    this.tsl = tsl;
  }

  public TslTypeProperties getTslType() {
    return tslType;
  }

  public void setTslType(TslTypeProperties tslType) {
    this.tslType = tslType;
  }

  public List<SearchType> getSearchTypes() {
    return searchTypes;
  }

  public void setSearchTypes(List<SearchType> searchTypes) {
    this.searchTypes = searchTypes;
  }
}
