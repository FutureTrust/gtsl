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

public class SchemeRulesProperties {

  @NotEmpty
  private String euListofthelists;
  @NotEmpty
  private String euCommon;
  @NotEmpty
  private String cc;

  @Override
  public String toString() {
    return "SchemeRulesProperties{" +
        "euListofthelists='" + euListofthelists + '\'' +
        ", euCommon='" + euCommon + '\'' +
        ", cc='" + cc + '\'' +
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

    SchemeRulesProperties that = (SchemeRulesProperties) o;

    if (euListofthelists != null ? !euListofthelists.equals(that.euListofthelists)
        : that.euListofthelists != null) {
      return false;
    }
    if (euCommon != null ? !euCommon.equals(that.euCommon) : that.euCommon != null) {
      return false;
    }
    return cc != null ? cc.equals(that.cc) : that.cc == null;
  }

  @Override
  public int hashCode() {
    int result = euListofthelists != null ? euListofthelists.hashCode() : 0;
    result = 31 * result + (euCommon != null ? euCommon.hashCode() : 0);
    result = 31 * result + (cc != null ? cc.hashCode() : 0);
    return result;
  }

  public String getEuListofthelists() {
    return euListofthelists;
  }

  public void setEuListofthelists(String euListofthelists) {
    this.euListofthelists = euListofthelists;
  }

  public String getEuCommon() {
    return euCommon;
  }

  public void setEuCommon(String euCommon) {
    this.euCommon = euCommon;
  }

  public String getCc() {
    return cc;
  }

  public void setCc(String cc) {
    this.cc = cc;
  }
}
