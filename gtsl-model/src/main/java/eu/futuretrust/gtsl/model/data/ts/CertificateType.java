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

package eu.futuretrust.gtsl.model.data.ts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.tsl.KeyUsageBit;
import eu.europa.esig.dss.x509.CertificateToken;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.util.ASN1Dump;

public class CertificateType {

  private byte[] certEncoded;

  @JsonIgnore
  private CertificateToken token;
  @JsonIgnore
  private String certB64;
  @JsonIgnore
  private String certSubjectShortName;
  @JsonIgnore
  private String certSubject;
  @JsonIgnore
  private String certSerial;
  @JsonIgnore
  private String certIssuer;
  @JsonIgnore
  private String certDigestAlgo;
  @JsonIgnore
  private Date certNotBefore;
  @JsonIgnore
  private Date certAfter;
  @JsonIgnore
  private String certificateInfo;
  @JsonIgnore
  private List<String> keyUsageList;
  @JsonIgnore
  private List<String> extendedKeyUsageList;
  @JsonIgnore
  private byte[] certSki;
  @JsonIgnore
  private String certSkiB64;

  public CertificateType() {
  }

  public CertificateType(byte[] encodedCertificate) {
    initCertificateToken(encodedCertificate);
  }

  private void initCertificateToken(byte[] encodedCertificate) {
    if (encodedCertificate != null) {

      try {
        CertificateToken cert = DSSUtils.loadCertificate(encodedCertificate);
        this.certEncoded = cert.getEncoded();
        this.token = cert;
        this.certB64 = DatatypeConverter.printBase64Binary(cert.getEncoded());
        this.certSubject = cert.getSubjectX500Principal().toString();
        this.certSerial = cert.getSerialNumber().toString();
        this.certIssuer = cert.getIssuerX500Principal().toString();
        this.certSubjectShortName = ModelUtils.extractCNName(cert.getSubjectX500Principal());
        this.certDigestAlgo = cert.getDigestAlgorithm().getName();
        this.certAfter = cert.getNotAfter();
        this.certNotBefore = cert.getNotBefore();
        try {
          this.certificateInfo = cert.getCertificate().toString();
        } catch (IllegalStateException e) {
          this.certificateInfo = "";
        }
        byte[] ski = ModelUtils.getSki(cert);
        if (ArrayUtils.isNotEmpty(ski)) {
          this.certSki = ski;
          this.certSkiB64 = Base64.encodeBase64String(ski);
        }
        this.setKeyUsageList(new ArrayList<>());
        if (cert.getKeyUsageBits() != null) {
          for (KeyUsageBit kub : cert.getKeyUsageBits()) {
            this.getKeyUsageList().add(kub.name());
          }
        }

        this.setExtendedKeyUsageList(new ArrayList<>());
        if (cert.getCertificate().getExtendedKeyUsage() != null) {
          for (String str : cert.getCertificate().getExtendedKeyUsage()) {
            this.getExtendedKeyUsageList().add(str);
          }
        }

      } catch (Exception e) {
        this.token = null;
      }
    }
  }

  public String asASN1Tree() {
    if (certEncoded != null) {
      ASN1InputStream bIn = new ASN1InputStream(new ByteArrayInputStream(certEncoded));
      try {
        ASN1Primitive obj = bIn.readObject();
        return ASN1Dump.dumpAsString(obj, true);
      } catch (IOException e) {
        return "";
      }
    }
    return "";
  }

  public void setTokenFromEncoded() {
    setToken(DSSUtils.loadCertificate(getCertEncoded()));
  }

  public CertificateToken getToken() {
    return token;
  }

  public void setToken(CertificateToken token) {
    this.token = token;
  }

  public byte[] getCertEncoded() {
    return certEncoded;
  }

  public void setCertEncoded(byte[] certEncoded) {
    initCertificateToken(certEncoded);
  }

  public String getCertB64() {
    return certB64;
  }

  public void setCertB64(String certB64) {
    this.certB64 = certB64;
  }

  public String getCertSubjectShortName() {
    return certSubjectShortName;
  }

  public void setCertSubjectShortName(String certSubjectShortName) {
    this.certSubjectShortName = certSubjectShortName;
  }

  public String getCertSubject() {
    return certSubject;
  }

  public void setCertSubject(String certSubject) {
    this.certSubject = certSubject;
  }

  public String getCertSerial() {
    return certSerial;
  }

  public void setCertSerial(String certSerial) {
    this.certSerial = certSerial;
  }

  public String getCertIssuer() {
    return certIssuer;
  }

  public void setCertIssuer(String certIssuer) {
    this.certIssuer = certIssuer;
  }

  public String getCertDigestAlgo() {
    return certDigestAlgo;
  }

  public void setCertDigestAlgo(String certDigestAlgo) {
    this.certDigestAlgo = certDigestAlgo;
  }

  public Date getCertNotBefore() {
    return certNotBefore;
  }

  public void setCertNotBefore(Date certNotBefore) {
    this.certNotBefore = certNotBefore;
  }

  public Date getCertAfter() {
    return certAfter;
  }

  public void setCertAfter(Date certAfter) {
    this.certAfter = certAfter;
  }

  public String getCertificateInfo() {
    return certificateInfo;
  }

  public void setCertificateInfo(String certificateInfo) {
    this.certificateInfo = certificateInfo;
  }

  public List<String> getKeyUsageList() {
    return keyUsageList;
  }

  public void setKeyUsageList(List<String> keyUsageList) {
    this.keyUsageList = keyUsageList;
  }

  public List<String> getExtendedKeyUsageList() {
    return extendedKeyUsageList;
  }

  public void setExtendedKeyUsageList(List<String> extendedKeyUsageList) {
    this.extendedKeyUsageList = extendedKeyUsageList;
  }

  public byte[] getCertSki() {
    return certSki;
  }

  public void setCertSki(byte[] certSki) {
    this.certSki = certSki;
  }

  public String getCertSkiB64() {
    return certSkiB64;
  }

  public void setCertSkiB64(String certSkiB64) {
    this.certSkiB64 = certSkiB64;
  }
}
