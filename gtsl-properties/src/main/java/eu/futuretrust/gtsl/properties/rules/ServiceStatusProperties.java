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

public class ServiceStatusProperties {

  @NotEmpty
  private String granted;
  @NotEmpty
  private String withdrawn;
  @NotEmpty
  private String setbynationallaw;
  @NotEmpty
  private String deprecatedbynationallaw;
  @NotEmpty
  private String recognisedatnationallevel;
  @NotEmpty
  private String deprecatedatnationallevel;

  @Override
  public String toString() {
    return "ServiceStatusProperties{" +
        "granted='" + granted + '\'' +
        ", withdrawn='" + withdrawn + '\'' +
        ", setbynationallaw='" + setbynationallaw + '\'' +
        ", deprecatedbynationallaw='" + deprecatedbynationallaw + '\'' +
        ", recognisedatnationallevel='" + recognisedatnationallevel + '\'' +
        ", deprecatedatnationallevel='" + deprecatedatnationallevel + '\'' +
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

    ServiceStatusProperties that = (ServiceStatusProperties) o;

    if (granted != null ? !granted.equals(that.granted) : that.granted != null) {
      return false;
    }
    if (withdrawn != null ? !withdrawn.equals(that.withdrawn) : that.withdrawn != null) {
      return false;
    }
    if (setbynationallaw != null ? !setbynationallaw.equals(that.setbynationallaw)
        : that.setbynationallaw != null) {
      return false;
    }
    if (deprecatedbynationallaw != null ? !deprecatedbynationallaw
        .equals(that.deprecatedbynationallaw) : that.deprecatedbynationallaw != null) {
      return false;
    }
    if (recognisedatnationallevel != null ? !recognisedatnationallevel
        .equals(that.recognisedatnationallevel) : that.recognisedatnationallevel != null) {
      return false;
    }
    return deprecatedatnationallevel != null ? deprecatedatnationallevel
        .equals(that.deprecatedatnationallevel) : that.deprecatedatnationallevel == null;
  }

  @Override
  public int hashCode() {
    int result = granted != null ? granted.hashCode() : 0;
    result = 31 * result + (withdrawn != null ? withdrawn.hashCode() : 0);
    result = 31 * result + (setbynationallaw != null ? setbynationallaw.hashCode() : 0);
    result =
        31 * result + (deprecatedbynationallaw != null ? deprecatedbynationallaw.hashCode() : 0);
    result =
        31 * result + (recognisedatnationallevel != null ? recognisedatnationallevel.hashCode()
            : 0);
    result =
        31 * result + (deprecatedatnationallevel != null ? deprecatedatnationallevel.hashCode()
            : 0);
    return result;
  }

  public String getGranted() {
    return granted;
  }

  public void setGranted(String granted) {
    this.granted = granted;
  }

  public String getWithdrawn() {
    return withdrawn;
  }

  public void setWithdrawn(String withdrawn) {
    this.withdrawn = withdrawn;
  }

  public String getRecognisedatnationallevel() {
    return recognisedatnationallevel;
  }

  public void setRecognisedatnationallevel(String recognisedatnationallevel) {
    this.recognisedatnationallevel = recognisedatnationallevel;
  }

  public String getDeprecatedatnationallevel() {
    return deprecatedatnationallevel;
  }

  public void setDeprecatedatnationallevel(String deprecatedatnationallevel) {
    this.deprecatedatnationallevel = deprecatedatnationallevel;
  }

  public String getSetbynationallaw() {
    return setbynationallaw;
  }

  public void setSetbynationallaw(String setbynationallaw) {
    this.setbynationallaw = setbynationallaw;
  }

  public String getDeprecatedbynationallaw() {
    return deprecatedbynationallaw;
  }

  public void setDeprecatedbynationallaw(String deprecatedbynationallaw) {
    this.deprecatedbynationallaw = deprecatedbynationallaw;
  }
}
