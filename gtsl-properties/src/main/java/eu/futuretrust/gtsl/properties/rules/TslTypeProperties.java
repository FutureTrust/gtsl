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

public class TslTypeProperties {

  @NotEmpty
  private String euGeneric;
  @NotEmpty
  private String euListofthelists;
  @NotEmpty
  private String ccList;
  @NotEmpty
  private String ccListofthelists;

  @Override
  public String toString() {
    return "TslTypeProperties{" +
        "euGeneric='" + euGeneric + '\'' +
        ", euListofthelists='" + euListofthelists + '\'' +
        ", ccList='" + ccList + '\'' +
        ", ccListofthelists='" + ccListofthelists + '\'' +
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

    TslTypeProperties that = (TslTypeProperties) o;

    if (euGeneric != null ? !euGeneric.equals(that.euGeneric) : that.euGeneric != null) {
      return false;
    }
    if (euListofthelists != null ? !euListofthelists.equals(that.euListofthelists)
        : that.euListofthelists != null) {
      return false;
    }
    if (ccList != null ? !ccList.equals(that.ccList) : that.ccList != null) {
      return false;
    }
    return ccListofthelists != null ? ccListofthelists.equals(that.ccListofthelists)
        : that.ccListofthelists == null;
  }

  @Override
  public int hashCode() {
    int result = euGeneric != null ? euGeneric.hashCode() : 0;
    result = 31 * result + (euListofthelists != null ? euListofthelists.hashCode() : 0);
    result = 31 * result + (ccList != null ? ccList.hashCode() : 0);
    result = 31 * result + (ccListofthelists != null ? ccListofthelists.hashCode() : 0);
    return result;
  }

  public String getEuGeneric() {
    return euGeneric;
  }

  public void setEuGeneric(String euGeneric) {
    this.euGeneric = euGeneric;
  }

  public String getEuListofthelists() {
    return euListofthelists;
  }

  public void setEuListofthelists(String euListofthelists) {
    this.euListofthelists = euListofthelists;
  }

  public String getCcList() {
    return ccList;
  }

  public void setCcList(String ccList) {
    this.ccList = ccList;
  }

  public String getCcListofthelists() {
    return ccListofthelists;
  }

  public void setCcListofthelists(String ccListofthelists) {
    this.ccListofthelists = ccListofthelists;
  }
}
