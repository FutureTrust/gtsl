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

public class NationalServiceTypeProperties {

  @NotEmpty
  private String ra;
  @NotEmpty
  private String raNotHavingPKIid;
  @NotEmpty
  private String aca;
  @NotEmpty
  private String signaturePolicyAuthority;
  @NotEmpty
  private String archiv;
  @NotEmpty
  private String archivNotHavingPKIid;
  @NotEmpty
  private String idv;
  @NotEmpty
  private String idvNotHavingPKIid;
  @NotEmpty
  private String kEscrow;
  @NotEmpty
  private String kEscrowNotHavingPKIid;
  @NotEmpty
  private String ppwd;
  @NotEmpty
  private String ppwdNotHavingPKIid;
  @NotEmpty
  private String tlIssuer;
  @NotEmpty
  private String nationalRootCAQC;
  @NotEmpty
  private String unspecified;

  @Override
  public String toString() {
    return "NationalServiceTypeProperties{" +
        "ra='" + ra + '\'' +
        ", raNotHavingPKIid='" + raNotHavingPKIid + '\'' +
        ", aca='" + aca + '\'' +
        ", signaturePolicyAuthority='" + signaturePolicyAuthority + '\'' +
        ", archiv='" + archiv + '\'' +
        ", archivNotHavingPKIid='" + archivNotHavingPKIid + '\'' +
        ", idv='" + idv + '\'' +
        ", idvNotHavingPKIid='" + idvNotHavingPKIid + '\'' +
        ", kEscrow='" + kEscrow + '\'' +
        ", kEscrowNotHavingPKIid='" + kEscrowNotHavingPKIid + '\'' +
        ", ppwd='" + ppwd + '\'' +
        ", ppwdNotHavingPKIid='" + ppwdNotHavingPKIid + '\'' +
        ", tlIssuer='" + tlIssuer + '\'' +
        ", nationalRootCAQC='" + nationalRootCAQC + '\'' +
        ", unspecified='" + unspecified + '\'' +
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

    NationalServiceTypeProperties that = (NationalServiceTypeProperties) o;

    if (ra != null ? !ra.equals(that.ra) : that.ra != null) {
      return false;
    }
    if (raNotHavingPKIid != null ? !raNotHavingPKIid.equals(that.raNotHavingPKIid)
        : that.raNotHavingPKIid != null) {
      return false;
    }
    if (aca != null ? !aca.equals(that.aca) : that.aca != null) {
      return false;
    }
    if (signaturePolicyAuthority != null ? !signaturePolicyAuthority
        .equals(that.signaturePolicyAuthority) : that.signaturePolicyAuthority != null) {
      return false;
    }
    if (archiv != null ? !archiv.equals(that.archiv) : that.archiv != null) {
      return false;
    }
    if (archivNotHavingPKIid != null ? !archivNotHavingPKIid.equals(that.archivNotHavingPKIid)
        : that.archivNotHavingPKIid != null) {
      return false;
    }
    if (idv != null ? !idv.equals(that.idv) : that.idv != null) {
      return false;
    }
    if (idvNotHavingPKIid != null ? !idvNotHavingPKIid.equals(that.idvNotHavingPKIid)
        : that.idvNotHavingPKIid != null) {
      return false;
    }
    if (kEscrow != null ? !kEscrow.equals(that.kEscrow) : that.kEscrow != null) {
      return false;
    }
    if (kEscrowNotHavingPKIid != null ? !kEscrowNotHavingPKIid.equals(that.kEscrowNotHavingPKIid)
        : that.kEscrowNotHavingPKIid != null) {
      return false;
    }
    if (ppwd != null ? !ppwd.equals(that.ppwd) : that.ppwd != null) {
      return false;
    }
    if (ppwdNotHavingPKIid != null ? !ppwdNotHavingPKIid.equals(that.ppwdNotHavingPKIid)
        : that.ppwdNotHavingPKIid != null) {
      return false;
    }
    if (tlIssuer != null ? !tlIssuer.equals(that.tlIssuer) : that.tlIssuer != null) {
      return false;
    }
    if (nationalRootCAQC != null ? !nationalRootCAQC.equals(that.nationalRootCAQC)
        : that.nationalRootCAQC != null) {
      return false;
    }
    return unspecified != null ? unspecified.equals(that.unspecified) : that.unspecified == null;
  }

  @Override
  public int hashCode() {
    int result = ra != null ? ra.hashCode() : 0;
    result = 31 * result + (raNotHavingPKIid != null ? raNotHavingPKIid.hashCode() : 0);
    result = 31 * result + (aca != null ? aca.hashCode() : 0);
    result =
        31 * result + (signaturePolicyAuthority != null ? signaturePolicyAuthority.hashCode() : 0);
    result = 31 * result + (archiv != null ? archiv.hashCode() : 0);
    result = 31 * result + (archivNotHavingPKIid != null ? archivNotHavingPKIid.hashCode() : 0);
    result = 31 * result + (idv != null ? idv.hashCode() : 0);
    result = 31 * result + (idvNotHavingPKIid != null ? idvNotHavingPKIid.hashCode() : 0);
    result = 31 * result + (kEscrow != null ? kEscrow.hashCode() : 0);
    result = 31 * result + (kEscrowNotHavingPKIid != null ? kEscrowNotHavingPKIid.hashCode() : 0);
    result = 31 * result + (ppwd != null ? ppwd.hashCode() : 0);
    result = 31 * result + (ppwdNotHavingPKIid != null ? ppwdNotHavingPKIid.hashCode() : 0);
    result = 31 * result + (tlIssuer != null ? tlIssuer.hashCode() : 0);
    result = 31 * result + (nationalRootCAQC != null ? nationalRootCAQC.hashCode() : 0);
    result = 31 * result + (unspecified != null ? unspecified.hashCode() : 0);
    return result;
  }

  public String getRa() {
    return ra;
  }

  public void setRa(String ra) {
    this.ra = ra;
  }

  public String getRaNotHavingPKIid() {
    return raNotHavingPKIid;
  }

  public void setRaNotHavingPKIid(String raNotHavingPKIid) {
    this.raNotHavingPKIid = raNotHavingPKIid;
  }

  public String getAca() {
    return aca;
  }

  public void setAca(String aca) {
    this.aca = aca;
  }

  public String getSignaturePolicyAuthority() {
    return signaturePolicyAuthority;
  }

  public void setSignaturePolicyAuthority(String signaturePolicyAuthority) {
    this.signaturePolicyAuthority = signaturePolicyAuthority;
  }

  public String getArchiv() {
    return archiv;
  }

  public void setArchiv(String archiv) {
    this.archiv = archiv;
  }

  public String getArchivNotHavingPKIid() {
    return archivNotHavingPKIid;
  }

  public void setArchivNotHavingPKIid(String archivNotHavingPKIid) {
    this.archivNotHavingPKIid = archivNotHavingPKIid;
  }

  public String getIdv() {
    return idv;
  }

  public void setIdv(String idv) {
    this.idv = idv;
  }

  public String getIdvNotHavingPKIid() {
    return idvNotHavingPKIid;
  }

  public void setIdvNotHavingPKIid(String idvNotHavingPKIid) {
    this.idvNotHavingPKIid = idvNotHavingPKIid;
  }

  public String getkEscrow() {
    return kEscrow;
  }

  public void setkEscrow(String kEscrow) {
    this.kEscrow = kEscrow;
  }

  public String getkEscrowNotHavingPKIid() {
    return kEscrowNotHavingPKIid;
  }

  public void setkEscrowNotHavingPKIid(String kEscrowNotHavingPKIid) {
    this.kEscrowNotHavingPKIid = kEscrowNotHavingPKIid;
  }

  public String getPpwd() {
    return ppwd;
  }

  public void setPpwd(String ppwd) {
    this.ppwd = ppwd;
  }

  public String getPpwdNotHavingPKIid() {
    return ppwdNotHavingPKIid;
  }

  public void setPpwdNotHavingPKIid(String ppwdNotHavingPKIid) {
    this.ppwdNotHavingPKIid = ppwdNotHavingPKIid;
  }

  public String getTlIssuer() {
    return tlIssuer;
  }

  public void setTlIssuer(String tlIssuer) {
    this.tlIssuer = tlIssuer;
  }

  public String getNationalRootCAQC() {
    return nationalRootCAQC;
  }

  public void setNationalRootCAQC(String nationalRootCAQC) {
    this.nationalRootCAQC = nationalRootCAQC;
  }

  public String getUnspecified() {
    return unspecified;
  }

  public void setUnspecified(String unspecified) {
    this.unspecified = unspecified;
  }
}
