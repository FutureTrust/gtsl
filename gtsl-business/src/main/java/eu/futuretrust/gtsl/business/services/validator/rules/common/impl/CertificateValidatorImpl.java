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

package eu.futuretrust.gtsl.business.services.validator.rules.common.impl;

import eu.europa.esig.dss.tsl.KeyUsageBit;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CertificateValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.CountryCode;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.common.MultiLangNormStringType;
import eu.futuretrust.gtsl.model.data.ts.CertificateType;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.DLSet;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateValidatorImpl implements CertificateValidator {

  private static final Logger LOGGER = LoggerFactory.getLogger(CertificateValidatorImpl.class);

  private static final String SUBJECT_KEY_IDENTIFIER_OID = "2.5.29.14";
  private static final String ID_TSL_KP_TSLSIGNING_OID = "0.4.0.2231.3.0";

  @Autowired
  protected CertificateValidatorImpl() {
  }

  // from TL-Manager
  @Override
  public void isX509CertificateContainsCorrectKeyUsages(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert) {
    if (!hasAllowedKeyUsagesBits(cert.getToken())) {
      validationContext.addViolation(new Violation(violationConstant, cert.getCertB64()));
    }
  }

  // from TL-Manager
  @Override
  public void isX509CertificateContainsBasicConstraintCaFalse(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert) {
    if (!isBasicConstraintCaFalse(cert.getToken())) {
      validationContext.addViolation(new Violation(violationConstant, cert.getCertB64()));
    }
  }

  // from TL-Manager
  @Override
  public void isX509CertificateContainsTslSigningExtKeyUsage(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert) {
    if (!hasTslSigningExtendedKeyUsage(cert.getToken())) {
      validationContext.addViolation(new Violation(violationConstant, cert.getCertB64()));
    }
  }

  // from TL-Manager
  @Override
  public void isX509CertificateContainsSubjectKeyIdentifier(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert) {
    byte[] subjectKeyIdentifier = getSubjectKeyIdentifier(cert.getToken());
    if (ArrayUtils.isEmpty(subjectKeyIdentifier)) {
      validationContext.addViolation(new Violation(violationConstant, cert.getCertB64()));
    }
  }

  // from TL-Manager
  @Override
  public void isX509CertificateCountryCodeMatch(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert, CountryCode expectedCountryCode) {
    String certificateCountryCode = getCountryCode(cert.getToken());
    if (!certificateCountryCode.equals(expectedCountryCode.getValue())) {
      validationContext.addViolation(new Violation(violationConstant, cert.getCertB64()));
    }
  }

  // from TL-Manager
  @Override
  public void isX509CertificateOrganizationMatch(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert,
      InternationalNamesType schemeOperatorNames) {
    String organization = getOrganization(cert.getToken());
    if (!isMatch(schemeOperatorNames, organization)) {
      validationContext.addViolation(new Violation(violationConstant, cert.getCertB64()));
    }
  }

  // from TL-Manager
  @Override
  public String getCountryCode(CertificateToken certificate) {
    return StringUtils.upperCase(getRDNValue(BCStyle.C, certificate));
  }

  // from TL-Manager
  @Override
  public String getOrganization(CertificateToken certificate) {
    return StringEscapeUtils.unescapeJava(getRDNValue(BCStyle.O, certificate));
  }

  private String getRDNValue(ASN1ObjectIdentifier oid, CertificateToken certificate) {
    String value = null;
    if (certificate != null) {
      try {

        X509Certificate x509Certificate = certificate.getCertificate();
        X500Name x500name = new JcaX509CertificateHolder(x509Certificate).getSubject();
        if (x500name != null) {
          Map<String, Object> pairs = new HashMap<>();

          DLSequence seq = (DLSequence) DERSequence.fromByteArray(x500name.getEncoded());
          for (int i = 0; i < seq.size(); i++) {
            DLSet set = (DLSet) seq.getObjectAt(i);
            for (int j = 0; j < set.size(); j++) {
              DLSequence pair = (DLSequence) set.getObjectAt(j);
              ASN1Encodable objectAt = pair.getObjectAt(1);
              pairs.put(((ASN1ObjectIdentifier) pair.getObjectAt(0)).getId(), objectAt);
            }
          }

          if (pairs.get(oid.toString()) instanceof ASN1String) {
            ASN1String o = (ASN1String) pairs.get(oid.toString());
            value = o.getString();
          } else if (pairs.get(oid.toString()) != null) {
            LOGGER.error("Type unknown " + pairs.get(oid.toString()).getClass());
          } else {
            LOGGER.debug("No 'Organization' in certificate -> x500Name is : " + x500name);
          }
        }
      } catch (Exception e) {
        LOGGER.debug("Unable to retrieve X500Name from certificate : " + e.getMessage(), e);
      }
    }
    return value;
  }

  private byte[] getSubjectKeyIdentifier(CertificateToken certificate) {
    byte[] subjectKeyIdentifier = null;
    if (certificate != null) {
      X509Certificate x509Certificate = certificate.getCertificate();
      subjectKeyIdentifier = x509Certificate.getExtensionValue(SUBJECT_KEY_IDENTIFIER_OID);
    }
    return subjectKeyIdentifier;
  }

  private boolean hasTslSigningExtendedKeyUsage(CertificateToken certificate) {
    if (certificate != null) {
      try {
        X509Certificate x509Certificate = certificate.getCertificate();
        List<String> extendedKeyUsages = x509Certificate.getExtendedKeyUsage();
        return CollectionUtils.isNotEmpty(extendedKeyUsages) && extendedKeyUsages
            .contains(ID_TSL_KP_TSLSIGNING_OID);
      } catch (Exception e) {
        return false;
      }
    }
    return false;
  }

  private boolean hasAllowedKeyUsagesBits(CertificateToken certificate) {
    if (certificate != null) {
      Set<KeyUsageBit> keyUsageBits = certificate.getKeyUsageBits();
      if (CollectionUtils.size(keyUsageBits) == 1) {
        return (keyUsageBits.contains(KeyUsageBit.digitalSignature) || keyUsageBits
            .contains(KeyUsageBit.nonRepudiation));
      } else if (CollectionUtils.size(keyUsageBits) == 2) {
        return (keyUsageBits.contains(KeyUsageBit.digitalSignature) && keyUsageBits
            .contains(KeyUsageBit.nonRepudiation));
      }
    }
    return false;
  }

  private boolean isBasicConstraintCaFalse(CertificateToken certificate) {
    if (certificate != null) {
      X509Certificate x509Certificate = certificate.getCertificate();
      int basicConstraints = x509Certificate.getBasicConstraints();
      return basicConstraints == -1;
    }
    return false;
  }

  // from TL-Manager
  @Override
  public boolean isMatch(InternationalNamesType names, String organization) {
    if (CollectionUtils.isNotEmpty(names.getValues()) && StringUtils
        .isNotEmpty(organization)) {
      for (MultiLangNormStringType schemeOperatorName : names.getValues()) {
        if (StringUtils.equalsIgnoreCase(schemeOperatorName.getValue(), organization)) {
          return true;
        }
      }
    }
    return false;
  }

}
