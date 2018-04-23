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

public class StatusDetnProperties {

  @NotEmpty
  private String euAppropriate;
  @NotEmpty
  private String euListofthelists;
  @NotEmpty
  private String ccDetermination;

  @Override
  public String toString() {
    return "StatusDetnProperties{" +
        "euAppropriate='" + euAppropriate + '\'' +
        ", euListofthelists='" + euListofthelists + '\'' +
        ", ccDetermination='" + ccDetermination + '\'' +
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

    StatusDetnProperties that = (StatusDetnProperties) o;

    if (euAppropriate != null ? !euAppropriate.equals(that.euAppropriate)
        : that.euAppropriate != null) {
      return false;
    }
    if (euListofthelists != null ? !euListofthelists.equals(that.euListofthelists)
        : that.euListofthelists != null) {
      return false;
    }
    return ccDetermination != null ? ccDetermination.equals(that.ccDetermination)
        : that.ccDetermination == null;
  }

  @Override
  public int hashCode() {
    int result = euAppropriate != null ? euAppropriate.hashCode() : 0;
    result = 31 * result + (euListofthelists != null ? euListofthelists.hashCode() : 0);
    result = 31 * result + (ccDetermination != null ? ccDetermination.hashCode() : 0);
    return result;
  }

  public String getEuAppropriate() {
    return euAppropriate;
  }

  public void setEuAppropriate(String euAppropriate) {
    this.euAppropriate = euAppropriate;
  }

  public String getEuListofthelists() {
    return euListofthelists;
  }

  public void setEuListofthelists(String euListofthelists) {
    this.euListofthelists = euListofthelists;
  }

  public String getCcDetermination() {
    return ccDetermination;
  }

  public void setCcDetermination(String ccDetermination) {
    this.ccDetermination = ccDetermination;
  }
}
