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

package eu.futuretrust.gtsl.properties.rules;

import javax.validation.constraints.NotEmpty;

public class AdditionalQualifierProperties {

  @NotEmpty
  private String foreSignatures;
  @NotEmpty
  private String foreSeals;
  @NotEmpty
  private String forWebSiteAuthentication;
  @NotEmpty
  private String rootCAQC;

  @Override
  public String toString() {
    return "AdditionalQualifierProperties{" +
        "foreSignatures='" + foreSignatures + '\'' +
        ", foreSeals='" + foreSeals + '\'' +
        ", forWebSiteAuthentication='" + forWebSiteAuthentication + '\'' +
        ", rootCAQC='" + rootCAQC + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AdditionalQualifierProperties that = (AdditionalQualifierProperties) o;

    if (foreSignatures != null ? !foreSignatures.equals(that.foreSignatures)
        : that.foreSignatures != null) {
      return false;
    }
    if (foreSeals != null ? !foreSeals.equals(that.foreSeals) : that.foreSeals != null) {
      return false;
    }
    if (forWebSiteAuthentication != null ? !forWebSiteAuthentication
        .equals(that.forWebSiteAuthentication) : that.forWebSiteAuthentication != null) {
      return false;
    }
    return rootCAQC != null ? rootCAQC.equals(that.rootCAQC) : that.rootCAQC == null;
  }

  @Override
  public int hashCode() {
    int result = foreSignatures != null ? foreSignatures.hashCode() : 0;
    result = 31 * result + (foreSeals != null ? foreSeals.hashCode() : 0);
    result =
        31 * result + (forWebSiteAuthentication != null ? forWebSiteAuthentication.hashCode() : 0);
    result = 31 * result + (rootCAQC != null ? rootCAQC.hashCode() : 0);
    return result;
  }

  public String getForeSignatures() {
    return foreSignatures;
  }

  public void setForeSignatures(String foreSignatures) {
    this.foreSignatures = foreSignatures;
  }

  public String getForeSeals() {
    return foreSeals;
  }

  public void setForeSeals(String foreSeals) {
    this.foreSeals = foreSeals;
  }

  public String getForWebSiteAuthentication() {
    return forWebSiteAuthentication;
  }

  public void setForWebSiteAuthentication(String forWebSiteAuthentication) {
    this.forWebSiteAuthentication = forWebSiteAuthentication;
  }

  public String getRootCAQC() {
    return rootCAQC;
  }

  public void setRootCAQC(String rootCAQC) {
    this.rootCAQC = rootCAQC;
  }
}
