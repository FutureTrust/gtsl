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

public class NonQualifiedServiceTypeProperties {

  @NotEmpty
  private String caPKC;
  @NotEmpty
  private String ocsp;
  @NotEmpty
  private String crl;
  @NotEmpty
  private String tsa;
  @NotEmpty
  private String tsaTSSQC;
  @NotEmpty
  private String tsaTSSAdes;
  @NotEmpty
  private String eds;
  @NotEmpty
  private String edsRem;
  @NotEmpty
  private String pses;
  @NotEmpty
  private String adesValidation;
  @NotEmpty
  private String adesGeneration;

  @Override
  public String toString() {
    return "NonQualifiedServiceTypeProperties{" +
        "caPKC='" + caPKC + '\'' +
        ", ocsp='" + ocsp + '\'' +
        ", crl='" + crl + '\'' +
        ", tsa='" + tsa + '\'' +
        ", tsaTSSQC='" + tsaTSSQC + '\'' +
        ", tsaTSSAdes='" + tsaTSSAdes + '\'' +
        ", eds='" + eds + '\'' +
        ", edsRem='" + edsRem + '\'' +
        ", pses='" + pses + '\'' +
        ", adesValidation='" + adesValidation + '\'' +
        ", adesGeneration='" + adesGeneration + '\'' +
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

    NonQualifiedServiceTypeProperties that = (NonQualifiedServiceTypeProperties) o;

    if (caPKC != null ? !caPKC.equals(that.caPKC) : that.caPKC != null) {
      return false;
    }
    if (ocsp != null ? !ocsp.equals(that.ocsp) : that.ocsp != null) {
      return false;
    }
    if (crl != null ? !crl.equals(that.crl) : that.crl != null) {
      return false;
    }
    if (tsa != null ? !tsa.equals(that.tsa) : that.tsa != null) {
      return false;
    }
    if (tsaTSSQC != null ? !tsaTSSQC.equals(that.tsaTSSQC) : that.tsaTSSQC != null) {
      return false;
    }
    if (tsaTSSAdes != null ? !tsaTSSAdes.equals(that.tsaTSSAdes) : that.tsaTSSAdes != null) {
      return false;
    }
    if (eds != null ? !eds.equals(that.eds) : that.eds != null) {
      return false;
    }
    if (edsRem != null ? !edsRem.equals(that.edsRem) : that.edsRem != null) {
      return false;
    }
    if (pses != null ? !pses.equals(that.pses) : that.pses != null) {
      return false;
    }
    if (adesValidation != null ? !adesValidation.equals(that.adesValidation)
        : that.adesValidation != null) {
      return false;
    }
    return adesGeneration != null ? adesGeneration.equals(that.adesGeneration)
        : that.adesGeneration == null;
  }

  @Override
  public int hashCode() {
    int result = caPKC != null ? caPKC.hashCode() : 0;
    result = 31 * result + (ocsp != null ? ocsp.hashCode() : 0);
    result = 31 * result + (crl != null ? crl.hashCode() : 0);
    result = 31 * result + (tsa != null ? tsa.hashCode() : 0);
    result = 31 * result + (tsaTSSQC != null ? tsaTSSQC.hashCode() : 0);
    result = 31 * result + (tsaTSSAdes != null ? tsaTSSAdes.hashCode() : 0);
    result = 31 * result + (eds != null ? eds.hashCode() : 0);
    result = 31 * result + (edsRem != null ? edsRem.hashCode() : 0);
    result = 31 * result + (pses != null ? pses.hashCode() : 0);
    result = 31 * result + (adesValidation != null ? adesValidation.hashCode() : 0);
    result = 31 * result + (adesGeneration != null ? adesGeneration.hashCode() : 0);
    return result;
  }

  public String getCaPKC() {
    return caPKC;
  }

  public void setCaPKC(String caPKC) {
    this.caPKC = caPKC;
  }

  public String getOcsp() {
    return ocsp;
  }

  public void setOcsp(String ocsp) {
    this.ocsp = ocsp;
  }

  public String getCrl() {
    return crl;
  }

  public void setCrl(String crl) {
    this.crl = crl;
  }

  public String getTsa() {
    return tsa;
  }

  public void setTsa(String tsa) {
    this.tsa = tsa;
  }

  public String getTsaTSSQC() {
    return tsaTSSQC;
  }

  public void setTsaTSSQC(String tsaTSSQC) {
    this.tsaTSSQC = tsaTSSQC;
  }

  public String getTsaTSSAdes() {
    return tsaTSSAdes;
  }

  public void setTsaTSSAdes(String tsaTSSAdes) {
    this.tsaTSSAdes = tsaTSSAdes;
  }

  public String getEds() {
    return eds;
  }

  public void setEds(String eds) {
    this.eds = eds;
  }

  public String getEdsRem() {
    return edsRem;
  }

  public void setEdsRem(String edsRem) {
    this.edsRem = edsRem;
  }

  public String getPses() {
    return pses;
  }

  public void setPses(String pses) {
    this.pses = pses;
  }

  public String getAdesValidation() {
    return adesValidation;
  }

  public void setAdesValidation(String adesValidation) {
    this.adesValidation = adesValidation;
  }

  public String getAdesGeneration() {
    return adesGeneration;
  }

  public void setAdesGeneration(String adesGeneration) {
    this.adesGeneration = adesGeneration;
  }
}
