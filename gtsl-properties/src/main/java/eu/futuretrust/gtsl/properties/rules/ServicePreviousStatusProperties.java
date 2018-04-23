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

public class ServicePreviousStatusProperties {

  @NotEmpty
  private String undersupervision;
  @NotEmpty
  private String supervisionincessation;
  @NotEmpty
  private String supervisionceased;
  @NotEmpty
  private String supervisionrevoked;
  @NotEmpty
  private String accredited;
  @NotEmpty
  private String accreditationceased;
  @NotEmpty
  private String accreditationrevoked;

  @Override
  public String toString() {
    return "ServicePreviousStatusProperties{" +
        "undersupervision='" + undersupervision + '\'' +
        ", supervisionincessation='" + supervisionincessation + '\'' +
        ", supervisionceased='" + supervisionceased + '\'' +
        ", supervisionrevoked='" + supervisionrevoked + '\'' +
        ", accredited='" + accredited + '\'' +
        ", accreditationceased='" + accreditationceased + '\'' +
        ", accreditationrevoked='" + accreditationrevoked + '\'' +
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

    ServicePreviousStatusProperties that = (ServicePreviousStatusProperties) o;

    if (undersupervision != null ? !undersupervision.equals(that.undersupervision)
        : that.undersupervision != null) {
      return false;
    }
    if (supervisionincessation != null ? !supervisionincessation.equals(that.supervisionincessation)
        : that.supervisionincessation != null) {
      return false;
    }
    if (supervisionceased != null ? !supervisionceased.equals(that.supervisionceased)
        : that.supervisionceased != null) {
      return false;
    }
    if (supervisionrevoked != null ? !supervisionrevoked.equals(that.supervisionrevoked)
        : that.supervisionrevoked != null) {
      return false;
    }
    if (accredited != null ? !accredited.equals(that.accredited) : that.accredited != null) {
      return false;
    }
    if (accreditationceased != null ? !accreditationceased.equals(that.accreditationceased)
        : that.accreditationceased != null) {
      return false;
    }
    return accreditationrevoked != null ? accreditationrevoked.equals(that.accreditationrevoked)
        : that.accreditationrevoked == null;
  }

  @Override
  public int hashCode() {
    int result = undersupervision != null ? undersupervision.hashCode() : 0;
    result = 31 * result + (supervisionincessation != null ? supervisionincessation.hashCode() : 0);
    result = 31 * result + (supervisionceased != null ? supervisionceased.hashCode() : 0);
    result = 31 * result + (supervisionrevoked != null ? supervisionrevoked.hashCode() : 0);
    result = 31 * result + (accredited != null ? accredited.hashCode() : 0);
    result = 31 * result + (accreditationceased != null ? accreditationceased.hashCode() : 0);
    result = 31 * result + (accreditationrevoked != null ? accreditationrevoked.hashCode() : 0);
    return result;
  }

  public String getUndersupervision() {
    return undersupervision;
  }

  public void setUndersupervision(String undersupervision) {
    this.undersupervision = undersupervision;
  }

  public String getSupervisionincessation() {
    return supervisionincessation;
  }

  public void setSupervisionincessation(String supervisionincessation) {
    this.supervisionincessation = supervisionincessation;
  }

  public String getSupervisionceased() {
    return supervisionceased;
  }

  public void setSupervisionceased(String supervisionceased) {
    this.supervisionceased = supervisionceased;
  }

  public String getSupervisionrevoked() {
    return supervisionrevoked;
  }

  public void setSupervisionrevoked(String supervisionrevoked) {
    this.supervisionrevoked = supervisionrevoked;
  }

  public String getAccredited() {
    return accredited;
  }

  public void setAccredited(String accredited) {
    this.accredited = accredited;
  }

  public String getAccreditationceased() {
    return accreditationceased;
  }

  public void setAccreditationceased(String accreditationceased) {
    this.accreditationceased = accreditationceased;
  }

  public String getAccreditationrevoked() {
    return accreditationrevoked;
  }

  public void setAccreditationrevoked(String accreditationrevoked) {
    this.accreditationrevoked = accreditationrevoked;
  }

}
