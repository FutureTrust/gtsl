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

public class QualifiedServiceTypeProperties {

  @NotEmpty
  private String caQC;
  @NotEmpty
  private String ocspQC;
  @NotEmpty
  private String crlQC;
  @NotEmpty
  private String tsaQTST;
  @NotEmpty
  private String edsQ;
  @NotEmpty
  private String edsRemQ;
  @NotEmpty
  private String psesQ;
  @NotEmpty
  private String qesValidationQ;
  @NotEmpty
  private String remoteQSCDManagementQ;

  @Override
  public String toString() {
    return "QualifiedServiceTypeProperties{" +
        "caQC='" + caQC + '\'' +
        ", ocspQC='" + ocspQC + '\'' +
        ", crlQC='" + crlQC + '\'' +
        ", tsaQTST='" + tsaQTST + '\'' +
        ", edsQ='" + edsQ + '\'' +
        ", edsRemQ='" + edsRemQ + '\'' +
        ", psesQ='" + psesQ + '\'' +
        ", qesValidationQ='" + qesValidationQ + '\'' +
        ", remoteQSCDManagementQ='" + remoteQSCDManagementQ + '\'' +
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

    QualifiedServiceTypeProperties that = (QualifiedServiceTypeProperties) o;

    if (caQC != null ? !caQC.equals(that.caQC) : that.caQC != null) {
      return false;
    }
    if (ocspQC != null ? !ocspQC.equals(that.ocspQC) : that.ocspQC != null) {
      return false;
    }
    if (crlQC != null ? !crlQC.equals(that.crlQC) : that.crlQC != null) {
      return false;
    }
    if (tsaQTST != null ? !tsaQTST.equals(that.tsaQTST) : that.tsaQTST != null) {
      return false;
    }
    if (edsQ != null ? !edsQ.equals(that.edsQ) : that.edsQ != null) {
      return false;
    }
    if (edsRemQ != null ? !edsRemQ.equals(that.edsRemQ) : that.edsRemQ != null) {
      return false;
    }
    if (psesQ != null ? !psesQ.equals(that.psesQ) : that.psesQ != null) {
      return false;
    }
    if (qesValidationQ != null ? !qesValidationQ.equals(that.qesValidationQ)
        : that.qesValidationQ != null) {
      return false;
    }
    return remoteQSCDManagementQ != null ? remoteQSCDManagementQ.equals(that.remoteQSCDManagementQ)
        : that.remoteQSCDManagementQ == null;
  }

  @Override
  public int hashCode() {
    int result = caQC != null ? caQC.hashCode() : 0;
    result = 31 * result + (ocspQC != null ? ocspQC.hashCode() : 0);
    result = 31 * result + (crlQC != null ? crlQC.hashCode() : 0);
    result = 31 * result + (tsaQTST != null ? tsaQTST.hashCode() : 0);
    result = 31 * result + (edsQ != null ? edsQ.hashCode() : 0);
    result = 31 * result + (edsRemQ != null ? edsRemQ.hashCode() : 0);
    result = 31 * result + (psesQ != null ? psesQ.hashCode() : 0);
    result = 31 * result + (qesValidationQ != null ? qesValidationQ.hashCode() : 0);
    result = 31 * result + (remoteQSCDManagementQ != null ? remoteQSCDManagementQ.hashCode() : 0);
    return result;
  }

  public String getCaQC() {
    return caQC;
  }

  public void setCaQC(String caQC) {
    this.caQC = caQC;
  }

  public String getOcspQC() {
    return ocspQC;
  }

  public void setOcspQC(String ocspQC) {
    this.ocspQC = ocspQC;
  }

  public String getCrlQC() {
    return crlQC;
  }

  public void setCrlQC(String crlQC) {
    this.crlQC = crlQC;
  }

  public String getTsaQTST() {
    return tsaQTST;
  }

  public void setTsaQTST(String tsaQTST) {
    this.tsaQTST = tsaQTST;
  }

  public String getEdsQ() {
    return edsQ;
  }

  public void setEdsQ(String edsQ) {
    this.edsQ = edsQ;
  }

  public String getEdsRemQ() {
    return edsRemQ;
  }

  public void setEdsRemQ(String edsRemQ) {
    this.edsRemQ = edsRemQ;
  }

  public String getPsesQ() {
    return psesQ;
  }

  public void setPsesQ(String psesQ) {
    this.psesQ = psesQ;
  }

  public String getQesValidationQ() {
    return qesValidationQ;
  }

  public void setQesValidationQ(String qesValidationQ) {
    this.qesValidationQ = qesValidationQ;
  }

  public String getRemoteQSCDManagementQ() {
    return remoteQSCDManagementQ;
  }

  public void setRemoteQSCDManagementQ(String remoteQSCDManagementQ) {
    this.remoteQSCDManagementQ = remoteQSCDManagementQ;
  }
}
