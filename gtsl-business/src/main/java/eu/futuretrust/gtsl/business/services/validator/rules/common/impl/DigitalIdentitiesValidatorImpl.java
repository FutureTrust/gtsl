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

import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.futuretrust.gtsl.business.services.validator.rules.common.DigitalIdentitiesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.signature.CertificateValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.common.MultiLangNormStringType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import eu.futuretrust.gtsl.model.data.digitalidentity.DigitalIdentityType;
import eu.futuretrust.gtsl.model.data.digitalidentity.ServiceDigitalIdentityListType;
import eu.futuretrust.gtsl.model.data.digitalidentity.ServiceDigitalIdentityType;
import eu.futuretrust.gtsl.model.data.ts.CertificateType;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.x509.X500Name;

@Service
public class DigitalIdentitiesValidatorImpl implements DigitalIdentitiesValidator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DigitalIdentitiesValidatorImpl.class);

    private final CertificateValidator certificateValidator;

    @Autowired
    public DigitalIdentitiesValidatorImpl(
            CertificateValidator certificateValidator) {
        this.certificateValidator = certificateValidator;
    }

    @Override
    public void isValidCertificate(ValidationContext validationContext, ViolationConstant violation,
                                   DigitalIdentityType digitalIdentity) {
        if ((digitalIdentity != null) && (digitalIdentity.getCertificateList() != null)) {
            for (CertificateType cert : digitalIdentity.getCertificateList()) {
                if (!isBase64Certificate(cert)) {
                    validationContext.addViolation(new Violation(violation, cert));
                }
            }
        }
    }

    @Override
    public void isSubjectNameMatch(ValidationContext validationContext, ViolationConstant violation,
                                   DigitalIdentityType digitalIdentity) {
        if ((digitalIdentity != null) && CollectionUtils
                .isNotEmpty(digitalIdentity.getCertificateList()) && (StringUtils
                .isNotEmpty(digitalIdentity.getSubjectName()))) {
            for (CertificateType cert : digitalIdentity.getCertificateList()) {
                if (cert.getToken() == null) {
                    cert.setTokenFromEncoded();
                }
                if (!isCorrectX509SubjectName(digitalIdentity.getSubjectName(), cert.getToken())) {
                    validationContext
                            .addViolation(new Violation(violation, digitalIdentity.getSubjectName()));
                }
            }
        }
    }

    @Override
    public void isX509SkiMatch(ValidationContext validationContext, ViolationConstant violation,
                               DigitalIdentityType digitalIdentity) {
        if ((digitalIdentity != null) && CollectionUtils
                .isNotEmpty(digitalIdentity.getCertificateList()) && (ArrayUtils
                .isNotEmpty(digitalIdentity.getX509ski()))) {
            for (CertificateType cert : digitalIdentity.getCertificateList()) {
                if (cert.getToken() == null) {
                    cert.setTokenFromEncoded();
                }
                if (!isCorrectX509SKI(digitalIdentity.getX509ski(), cert.getToken())) {
                    validationContext
                            .addViolation(new Violation(violation, new String(digitalIdentity.getX509ski())));
                }
            }
        }
    }

    @Override
    public void isServiceDigitalIdentityOrganizationMatch(ValidationContext validationContext,
                                                          ViolationConstant violation, DigitalIdentityType digitalIdentity) {
        if (digitalIdentity != null && CollectionUtils
                .isNotEmpty(digitalIdentity.getCertificateList())) {
            for (CertificateType cert : digitalIdentity.getCertificateList()) {
                if (cert.getToken() == null) {
                    cert.setTokenFromEncoded();
                }
                try
                {
                  String organization = getOrganization(cert.getToken());
                  InternationalNamesType listToCheck = new InternationalNamesType(new ArrayList<>());
                  validationContext.getAttributes().getTspAttributes().getTspName().getValues()
                          .forEach(listToCheck.getValues()::add);
                  validationContext.getAttributes().getTspAttributes().getTspTradeName().getValues()
                          .forEach(listToCheck.getValues()::add);
                  if (! isOrganizationNameMatch(listToCheck, organization))
                  {
                    validationContext.addViolation(new Violation(
                            ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_X509CERTIFICATE_ORGANIZATION_MATCH,
                            listToCheck.getValues().stream().map(MultiLangNormStringType::getValue)
                                    .collect(Collectors.joining(", "))));
                  }
                } catch (IOException e)
                {
                  validationContext.addViolation(new Violation(
                          ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_X509CERTIFICATE_VALID));
                }
            }
        }
    }

    @Override
    public void isListServiceDigitalIdentityCorrect(ValidationContext validationContext,
                                                    ViolationConstant violation, ServiceDigitalIdentityType serviceDigitalIdentity,
                                                    NonEmptyURIType serviceTypeIdentifier) {
    /*serviceDigitalIdentity.getValues()
        .forEach(
            digitalId -> isServiceDigitalIdentityCorrect(validationContext, violation, digitalId,
                serviceTypeIdentifier));*/
        if (serviceDigitalIdentity.getValues().stream().noneMatch(
                digitalIdentity -> CollectionUtils.isNotEmpty(digitalIdentity.getCertificateList()))) {
            validationContext
                    .addViolation(new Violation(ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_CORRECT));
        }
    }

    @Override
    public void isServiceDigitalIdentitiesValid(ValidationContext validationContext,
                                                ServiceDigitalIdentityListType serviceDigitalIdentities) {
        if (serviceDigitalIdentities != null
                && CollectionUtils.isNotEmpty(serviceDigitalIdentities.getValues())) {
            serviceDigitalIdentities.getValues().forEach(
                    serviceDigitalIdentity -> isServiceDigitalIdentityValid(validationContext,
                            serviceDigitalIdentity));
        }
    }

    private void isServiceDigitalIdentityValid(ValidationContext validationContext,
                                               ServiceDigitalIdentityType serviceDigitalIdentity) {
        if (serviceDigitalIdentity == null
                || CollectionUtils.isEmpty(serviceDigitalIdentity.getValues())) {
            validationContext
                    .addViolation(new Violation(ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_CORRECT));
        } else {
            serviceDigitalIdentity.getValues()
                    .forEach(digitalId -> isDigitalIdentityValid(validationContext, digitalId));
        }
    }

    private void isDigitalIdentityValid(ValidationContext validationContext,
                                        DigitalIdentityType digitalId) {
        if (digitalId == null) {
            validationContext
                    .addViolation(new Violation(ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_CORRECT));
        } else {
            if ((CollectionUtils.isNotEmpty(digitalId.getCertificateList()) && (digitalId.getCertificateList().get(0).getCertEncoded() == null || digitalId.getCertificateList().get(0).getCertEncoded().length == 0))
                    && StringUtils.isEmpty(digitalId.getSubjectName())
                    && (digitalId.getX509ski() == null || digitalId.getX509ski().length == 0)) {
                validationContext
                        .addViolation(new Violation(ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_CORRECT));
            }
        }
    }

    @Override
    public void isCertificateExpired(ValidationContext validationContext, ViolationConstant
            violation,
                                     DigitalIdentityType digitalIdentity) {
        if ((digitalIdentity != null) && CollectionUtils
                .isNotEmpty(digitalIdentity.getCertificateList()) && (ArrayUtils
                .isNotEmpty(digitalIdentity.getX509ski()))) {
            for (CertificateType cert : digitalIdentity.getCertificateList()) {
                if (cert.getToken() == null) {
                    cert.setTokenFromEncoded();
                }
                if (isExpired(cert.getCertAfter())) {
                    validationContext
                            .addViolation(new Violation(violation, cert.getCertB64()));
                }
            }
        }
    }

    private boolean isBase64Certificate(CertificateType certificate) {
        return certificate != null && certificate.getToken() != null && Base64.isBase64(certificate.getCertB64());
    }

    private boolean isCorrectX509SKI(byte[] ski, CertificateToken certificate) {
        try {
            byte[] expectedSki = ModelUtils.getSki(certificate);
            if (!ArrayUtils.isEquals(expectedSki, ski)) {
                LOGGER.debug(
                        "Wrong X509SKI detected " + Base64.encodeBase64String(ski) + " should be " + Base64
                                .encodeBase64String(expectedSki));
            } else {
                return true;
            }
        } catch (Exception e) {
            //LOGGER.debug("Unable to compute SKI for Certificate " + certificate.getBase64Encoded());
            return false;
        }
        return false;
    }

    private boolean isCorrectX509SubjectName(String subjectName, CertificateToken certificate) {
        X500Principal x500Principal;
        try {
            x500Principal = new X500Principal(subjectName);
        } catch (Exception e) {
            return false;
        }
        if (!DSSUtils.x500PrincipalAreEquals(certificate.getSubjectX500Principal(), x500Principal)) {
            if (!certificate.getSubjectX500Principal().toString().equalsIgnoreCase(subjectName)) {
                LOGGER.debug("Wrong SubjectName detected " + x500Principal + " should be " + certificate
                        .getSubjectX500Principal());
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean isExpired(Date certExpirationDate) {
        // TODO: currentDate can be corrupted by user
        Date currentDate = new Date();
        if (certExpirationDate.before(currentDate)) {
            return false;
        }
        return true;
    }

    private String getOrganization (final CertificateToken certificateToken) throws IOException
    {
      X500Name name = new X500Name(certificateToken.getSubjectX500Principal().getName());
      return name.getOrganization();
    }

    private boolean isOrganizationNameMatch(final InternationalNamesType names, final String orgName) {
        return (null != orgName && orgName.length() > 0) ? names.getValues().stream()
                .anyMatch(n -> n.getValue().equalsIgnoreCase(orgName)) : false;
    }
}
