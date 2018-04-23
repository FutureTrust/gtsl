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

package eu.futuretrust.gtsl.model.data.digitalidentity;

import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.futuretrust.gtsl.jaxb.tsl.AnyTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.DigitalIdentityListTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.DigitalIdentityTypeJAXB;
import eu.futuretrust.gtsl.model.data.ts.CertificateType;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.5.3 Service digital identity
 *
 * <xsd:complexType name="DigitalIdentityType"> <xsd:choice> <xsd:element name="X509Certificate"
 * type="xsd:base64Binary"/> <xsd:element name="X509SubjectName" type="xsd:string"/> <xsd:element
 * ref="ds:KeyValue"/> <xsd:element name="X509SKI" type="xsd:base64Binary"/> <xsd:element
 * name="Other" type="tsl:AnyType" minOccurs="0"/> </xsd:choice> </xsd:complexType>
 */
public class DigitalIdentityType {

  private List<CertificateType> certificateList;

  private String subjectName;

  private byte[] x509ski;
  private List<Object> other;

  public DigitalIdentityType() {
  }

  public String getX509skiAsString() {
    return Base64.encodeBase64String(x509ski);
  }

  // from TL-Manager
  public DigitalIdentityType(DigitalIdentityTypeJAXB digitalIdentity) {
    if (digitalIdentity != null) {
      List<CertificateType> certificateList = new ArrayList<>();
      List<Object> other = new ArrayList<>();
      if (digitalIdentity.getX509Certificate() != null) {
        certificateList.add(new CertificateType(digitalIdentity.getX509Certificate()));
      } else if (digitalIdentity.getOther() != null) {
        List<Object> objList = digitalIdentity.getOther().getContent();
        for (Object obj : objList) {
          if (obj != null) {
            if (obj instanceof String) {
              String t = (String) obj;
              if (!StringUtils.isEmpty(t.trim())) {
                other.add(obj);
              }
            } else {
              other.add(obj);
            }
          }
        }
      } else if (digitalIdentity.getX509SubjectName() != null) {
        this.subjectName = digitalIdentity.getX509SubjectName();
      } else if (digitalIdentity.getX509SKI() != null) {
        this.x509ski = digitalIdentity.getX509SKI();
      }
      if (CollectionUtils.isNotEmpty(other)) {
        this.setOther(other);
      }
      if (CollectionUtils.isNotEmpty(certificateList)) {
        this.setCertificateList(certificateList);
      }
    }
  }

  public DigitalIdentityType(byte[] input) {
    List<CertificateType> list = new ArrayList<>();
    CertificateType certificate = new CertificateType(input);

    if (certificate.getToken() != null) {
      list.add(certificate);
      this.setCertificateList(list);
      this.setSubjectName(certificate.getToken().getSubjectX500Principal().toString());
      this.setX509ski(ModelUtils.getSki(certificate.getToken()));
    } else {
      this.setCertificateList(null);
    }
  }

  public String getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }

  public byte[] getX509ski() {
    return x509ski;
  }

  public void setX509ski(byte[] x509ski) {
    this.x509ski = x509ski;
  }

  public List<Object> getOther() {
    return other;
  }

  public void setOther(List<Object> other) {
    this.other = other;
  }

  public List<CertificateType> getCertificateList() {
    return certificateList;
  }

  public void setCertificateList(List<CertificateType> certificateList) {
    this.certificateList = certificateList;
  }

  public DigitalIdentityListTypeJAXB asJAXB() {
    if (CollectionUtils.isNotEmpty(this.certificateList)
        && this.certificateList.stream()
        .allMatch(cert -> cert.getCertEncoded() != null && cert.getCertEncoded().length != 0)) {
      DigitalIdentityListTypeJAXB digitalIdentityList = new DigitalIdentityListTypeJAXB();
      for (CertificateType cert : this.getCertificateList()) {
        DigitalIdentityTypeJAXB tslDigitalId = new DigitalIdentityTypeJAXB();
        tslDigitalId.setX509Certificate(cert.getCertEncoded());
        digitalIdentityList.getDigitalId().add(tslDigitalId);
      }
      return digitalIdentityList;
    }

    if (!StringUtils.isEmpty(this.subjectName)) {
      DigitalIdentityListTypeJAXB digitalIdentityList = new DigitalIdentityListTypeJAXB();
      DigitalIdentityTypeJAXB tslDigitalId2 = new DigitalIdentityTypeJAXB();
      tslDigitalId2.setX509SubjectName(this.subjectName);
      digitalIdentityList.getDigitalId().add(tslDigitalId2);
      return digitalIdentityList;
    }

    if (this.x509ski != null && this.x509ski.length > 0) {
      DigitalIdentityListTypeJAXB digitalIdentityList = new DigitalIdentityListTypeJAXB();
      DigitalIdentityTypeJAXB tslDigitalId3 = new DigitalIdentityTypeJAXB();
      tslDigitalId3.setX509SKI(this.x509ski);
      digitalIdentityList.getDigitalId().add(tslDigitalId3);
      return digitalIdentityList;
    }

    if (this.getOther() != null) {
      DigitalIdentityListTypeJAXB digitalIdentityList = new DigitalIdentityListTypeJAXB();
      for (Object o : this.getOther()) {
        AnyTypeJAXB others = new AnyTypeJAXB();
        others.getContent().add(o);
        DigitalIdentityTypeJAXB tslDigitalId3 = new DigitalIdentityTypeJAXB();
        tslDigitalId3.setOther(others);
        digitalIdentityList.getDigitalId().add(tslDigitalId3);
      }
      return digitalIdentityList;
    }

    return null;
  }

  // from TL-Manager
  public DigitalIdentityListTypeJAXB asHistoricTSLTypeV5(CertificateType serviceCert) {
    DigitalIdentityListTypeJAXB digitalIdentityListJAXB = new DigitalIdentityListTypeJAXB();
    String tmpCertSubjectName = "";
    byte[] tmpCertSki = null;
    if (this.getCertificateList() != null) {

      if (serviceCert != null) {
        for (int i = 0; i < this.getCertificateList().size(); i++) {
          CertificateToken certT = DSSUtils.loadCertificate(serviceCert.getCertEncoded());
          tmpCertSubjectName = certT.getSubjectX500Principal().toString();
          tmpCertSki = ModelUtils.getSki(certT);
        }
      }

      if (!StringUtils.isEmpty(this.getSubjectName())) {
        DigitalIdentityTypeJAXB digitalIdentityJAXB = new DigitalIdentityTypeJAXB();
        digitalIdentityJAXB.setX509SubjectName(this.getSubjectName());
        digitalIdentityListJAXB.getDigitalId().add(digitalIdentityJAXB);
      } else {
        DigitalIdentityTypeJAXB digitalIdentityJAXB = new DigitalIdentityTypeJAXB();
        digitalIdentityJAXB.setX509SubjectName(tmpCertSubjectName);
        digitalIdentityListJAXB.getDigitalId().add(digitalIdentityJAXB);
      }

      if ((this.getX509ski() != null) && (this.getX509ski().length > 0)) {
        DigitalIdentityTypeJAXB digitalIdentityJAXB = new DigitalIdentityTypeJAXB();
        digitalIdentityJAXB.setX509SKI(this.getX509ski());
        digitalIdentityListJAXB.getDigitalId().add(digitalIdentityJAXB);
      } else {
        DigitalIdentityTypeJAXB digitalIdentityJAXB = new DigitalIdentityTypeJAXB();
        //LOGGER.debug("SKI to set : " + tmpCertSki);
        digitalIdentityJAXB.setX509SKI(tmpCertSki);
        digitalIdentityListJAXB.getDigitalId().add(digitalIdentityJAXB);

      }

    } else {
      // No certificate, but perhaps SKI and/Or SUbject Name --> Historic!
      // IF USER WANT TO PUBLISH IN MP XML
      if (serviceCert != null) {
        CertificateToken certT = DSSUtils.loadCertificate(serviceCert.getCertEncoded());
        tmpCertSubjectName = certT.getSubjectX500Principal().toString();
        tmpCertSki = ModelUtils.getSki(certT);
      }

      DigitalIdentityTypeJAXB tslDigitalIdSN = new DigitalIdentityTypeJAXB();
      if (!StringUtils.isEmpty(this.getSubjectName())) {
        tslDigitalIdSN.setX509SubjectName(this.getSubjectName());
        digitalIdentityListJAXB.getDigitalId().add(tslDigitalIdSN);
      } else {
        if (!StringUtils.isEmpty(tmpCertSubjectName)) {
          tslDigitalIdSN.setX509SubjectName(tmpCertSubjectName);
          digitalIdentityListJAXB.getDigitalId().add(tslDigitalIdSN);
        }
      }

      DigitalIdentityTypeJAXB tslDigitalIdSKI = new DigitalIdentityTypeJAXB();
      if ((this.getX509ski() != null) && (this.getX509ski().length > 0)) {
        tslDigitalIdSKI.setX509SKI(this.getX509ski());
        digitalIdentityListJAXB.getDigitalId().add(tslDigitalIdSKI);
      } else {
        if (tmpCertSki != null) {
          tslDigitalIdSKI.setX509SKI(tmpCertSki);
          digitalIdentityListJAXB.getDigitalId().add(tslDigitalIdSKI);
        }
      }
    }

    AnyTypeJAXB others = new AnyTypeJAXB();
    if (this.getOther() != null) {
      for (Object o : this.getOther()) {
        others.getContent().add(o);
        DigitalIdentityTypeJAXB tslDigitalId3 = new DigitalIdentityTypeJAXB();
        tslDigitalId3.setOther(others);
        digitalIdentityListJAXB.getDigitalId().add(tslDigitalId3);

      }
    }

    return digitalIdentityListJAXB;
  }

}
