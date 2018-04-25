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

package eu.futuretrust.gtsl.business.services.validator.rules.service;

import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.futuretrust.gtsl.business.services.validator.rules.RulesValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.digitalidentity.DigitalIdentityType;
import eu.futuretrust.gtsl.model.data.ts.CertificateType;
import eu.futuretrust.gtsl.model.data.ts.ServiceHistoryInstanceType;
import eu.futuretrust.gtsl.model.data.ts.ServiceHistoryType;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceInformationType;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import eu.futuretrust.gtsl.model.data.tsp.TSPServicesListType;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicesValidatorImpl implements RulesValidator<TSPServicesListType> {

  private final RulesValidator<TSPServiceInformationType> serviceInformationValidator;
  private final RulesValidator<ServiceHistoryType> serviceHistoryValidator;

  @Autowired
  public ServicesValidatorImpl(
      RulesValidator<TSPServiceInformationType> serviceInformationValidator,
      RulesValidator<ServiceHistoryType> serviceHistoryValidator) {
    this.serviceInformationValidator = serviceInformationValidator;
    this.serviceHistoryValidator = serviceHistoryValidator;
  }

  @Override
  public void validate(ValidationContext validationContext, TSPServicesListType tspServicesList) {

    int i = 0;
    List<Object> currentArgs = validationContext.getArgs();
    for (TSPServiceType service : tspServicesList.getValues()) {
      // Add service index in the context
      List<Object> args = new ArrayList<>(currentArgs);
      args.add(i);
      validationContext.setArgs(args);

      serviceInformationValidator.validate(validationContext, service.getServiceInformation());
      serviceHistoryValidator.validate(validationContext, service.getServiceHistory());
      if (service.getServiceHistory() != null) {
        isServiceStatusStartingTimeOrder(validationContext, service);
        isServiceAndHistoryHaveSameSubjectName(validationContext, service);
        isServiceAndHistoryHaveSameTypeIdentifier(validationContext, service);
        isServiceAndHistoryHaveSameX509SKI(validationContext, service);
      }
      i++;
    }
  }

  private void isServiceStatusStartingTimeOrder(ValidationContext validationContext,
      TSPServiceType service) {
    List<LocalDateTime> dates = new ArrayList<>();
    dates.add(service.getServiceInformation().getStatusStartingTime());
    dates.addAll(service.getServiceHistory().getValues().stream()
        .map(ServiceHistoryInstanceType::getStatusStartingTime)
        .collect(Collectors.toList()));

    List<LocalDateTime> sortedDates = new ArrayList<>(dates);
    Collections.sort(sortedDates);
    Collections.reverse(sortedDates);

    if (!dates.equals(sortedDates)) {
      validationContext
          .addViolation(new Violation(ViolationConstant.IS_SERVICE_STATUS_STARTING_TIME_ORDER));
    }
  }

  private void isServiceAndHistoryHaveSameTypeIdentifier(ValidationContext validationContext,
      TSPServiceType service) {
    String typeId = service.getServiceInformation().getServiceTypeIdentifier().getValue();
    if (service.getServiceHistory().getValues().stream()
        .anyMatch(history -> !history.getServiceTypeIdentifier().getValue().equals(typeId))) {
      validationContext.addViolation(
          new Violation(ViolationConstant.IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_TYPE_IDENTIFIER,
              service.getServiceInformation().getServiceTypeIdentifier()));
    }
  }

  // from TL-Manager
  private void isServiceAndHistoryHaveSameSubjectName(ValidationContext validationContext,
      TSPServiceType service) {
    X500Principal expectedSubjectX500Principal = getSubjectX500Principal(
        service.getServiceInformation().getServiceDigitalIdentity().getValues());
    if (expectedSubjectX500Principal == null) {
      validationContext.addViolation(
          new Violation(ViolationConstant.IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_SUBJECT_NAME));
    } else {
      ServiceHistoryType history = service.getServiceHistory();
      boolean hasOneHistoryFalse = false;
      if (CollectionUtils.isNotEmpty(history.getValues())) {
        for (ServiceHistoryInstanceType serviceHistory : history.getValues()) {
          X500Principal historySubjectX500Principal = getSubjectX500Principal(
              serviceHistory.getServiceDigitalIdentity().getValues());
          if (historySubjectX500Principal != null) {
            boolean valid = DSSUtils
                .x500PrincipalAreEquals(expectedSubjectX500Principal, historySubjectX500Principal);
            if (!valid) {
              hasOneHistoryFalse = true;
            }
          }
        }
      }
      if (hasOneHistoryFalse) {
        validationContext.addViolation(
            new Violation(ViolationConstant.IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_SUBJECT_NAME));
      }
    }
  }

  // from TL-Manager
  private X500Principal getSubjectX500Principal(List<DigitalIdentityType> digitalIds) {
    if (CollectionUtils.isNotEmpty(digitalIds)) {
      for (DigitalIdentityType identity : digitalIds) {
        if (CollectionUtils.isNotEmpty(identity.getCertificateList())) {
          for (CertificateType cert : identity.getCertificateList()) {
            if (cert.getToken() == null) {
              cert.setTokenFromEncoded();
            }
            return cert.getToken().getSubjectX500Principal();
          }
        } else if (StringUtils.isNotEmpty(identity.getSubjectName())) {
          return DSSUtils.getX500PrincipalOrNull(identity.getSubjectName());
        }
      }
    }
    return null;
  }

  // from TL-Manager
  private void isServiceAndHistoryHaveSameX509SKI(ValidationContext validationContext,
      TSPServiceType service) {
    byte[] expectedSKI = getX509SKI(
        service.getServiceInformation().getServiceDigitalIdentity().getValues());
    if (ArrayUtils.isNotEmpty(expectedSKI)) {
      List<ServiceHistoryInstanceType> history = service.getServiceHistory().getValues();
      boolean hasOneHistoryFalse = false;
      if (CollectionUtils.isNotEmpty(history)) {
        for (ServiceHistoryInstanceType serviceHistory : history) {
          byte[] ski = getX509SKI(serviceHistory.getServiceDigitalIdentity().getValues());
          if (ArrayUtils.isNotEmpty(ski)) {
            boolean valid = ArrayUtils.isEquals(expectedSKI, ski);
            if (!valid) {
              hasOneHistoryFalse = true;
            }
          }
        }
      }
      if (hasOneHistoryFalse) {
        validationContext.addViolation(
            new Violation(ViolationConstant.IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_X509SKI));
      }
    }
  }

  // from TL-Manager
  private byte[] getX509SKI(List<DigitalIdentityType> digitalIdentifications) {
    if (CollectionUtils.isNotEmpty(digitalIdentifications)) {
      for (DigitalIdentityType identification : digitalIdentifications) {
        if (CollectionUtils.isNotEmpty(identification.getCertificateList())) {
          for (CertificateType cert : identification.getCertificateList()) {
            if (cert.getToken() == null) {
              cert.setTokenFromEncoded();
            }
            CertificateToken certificate = cert.getToken();
            return ModelUtils.getSki(certificate);
          }
        } else if (ArrayUtils.isNotEmpty(identification.getX509ski())) {
          return identification.getX509ski();
        }
      }
    }
    return null;
  }

}
