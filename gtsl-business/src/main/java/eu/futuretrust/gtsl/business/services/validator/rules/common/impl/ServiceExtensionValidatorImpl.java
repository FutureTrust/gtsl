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

import eu.futuretrust.gtsl.business.services.validator.rules.common.CommonAttributesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CountriesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.ServiceExtensionValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import eu.futuretrust.gtsl.model.data.extension.AdditionalServiceInformationType;
import eu.futuretrust.gtsl.model.data.extension.QualificationElementType;
import eu.futuretrust.gtsl.model.data.extension.ServiceInformationExtensionListType;
import eu.futuretrust.gtsl.model.data.extension.ServiceInformationExtensionType;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceExtensionValidatorImpl implements ServiceExtensionValidator {

	enum Check {
		ExpiredCertRevocationInfo,
		Fore,
		ForeSignatureOrSeal
	}

	private static final Map<Check, Predicate<String>> PREDICATES_FOR_CHECK;

	static {
		PREDICATES_FOR_CHECK = new HashMap<>();

		PREDICATES_FOR_CHECK.put(Check.ExpiredCertRevocationInfo, typeIdentifier ->
				StringUtils.endsWith(typeIdentifier, "CA/PKC")
						|| StringUtils.endsWith(typeIdentifier, "NationalRootCA-QC")
						|| StringUtils.endsWith(typeIdentifier, "Certstatus/OCSP")
						|| StringUtils.endsWith(typeIdentifier, "Certstatus/OCSP/QC")
						|| StringUtils.endsWith(typeIdentifier, "Certstatus/CRL")
						|| StringUtils.endsWith(typeIdentifier, "Certstatus/CRL/QC"));

		PREDICATES_FOR_CHECK.put(Check.Fore, typeIdentifier ->
				StringUtils.endsWith(typeIdentifier, "CA/PKC")
						|| StringUtils.endsWith(typeIdentifier, "OCSP")
						|| StringUtils.endsWith(typeIdentifier, "CRL")
						|| StringUtils.endsWith(typeIdentifier, "PSES")
						|| StringUtils.endsWith(typeIdentifier, "AdESValidation")
						|| StringUtils.endsWith(typeIdentifier, "AdESGeneration"));

		PREDICATES_FOR_CHECK.put(Check.ForeSignatureOrSeal, typeIdentifier ->
				StringUtils.endsWith(typeIdentifier, "OCSP/QC")
						|| StringUtils.endsWith(typeIdentifier, "CRL/QC")
						|| StringUtils.endsWith(typeIdentifier, "PSES/Q")
						|| StringUtils.endsWith(typeIdentifier, "QESValidation/Q"));
	}

	private final CommonAttributesValidator commonAttributesValidator;
	private final CountriesValidator countriesValidator;


	@Autowired
	public ServiceExtensionValidatorImpl(
			CommonAttributesValidator commonAttributesValidator,
			CountriesValidator countriesValidator) {
		this.commonAttributesValidator = commonAttributesValidator;
		this.countriesValidator = countriesValidator;
	}

	@Override
	public void validateExtension(ValidationContext validationContext,
			ServiceInformationExtensionListType extensions,
			NonEmptyURIType serviceTypeIdentifier,
			ViolationConstant violationQualificationElementPresent,
			ViolationConstant violationQualificationExtensionAllowed,
			ViolationConstant violationQualifiersPresent,
			ViolationConstant violationCriteriaAssertsNotNull,
			ViolationConstant violationQualifiersURICorrectValue,
			ViolationConstant violationExpiredCertsRevocationNotCritical,
			ViolationConstant violationExpiredCertsRevocationAllowed,
			ViolationConstant violationAsiAllowed,
			ViolationConstant violationAsiSignatureAllowed,
			ViolationConstant violationAsiSignatureOrSealAllowed,
			ViolationConstant violationAsiLangAllowed) {
		if (extensions != null && CollectionUtils.isNotEmpty(extensions.getValues())) {

			for (ServiceInformationExtensionType extension : extensions.getValues()) {
				if (extension != null) {
					if (CollectionUtils.isNotEmpty(extension.getQualificationsExtension())) {
            /*if (extension.getQualificationsExtension().isEmpty()) {
              validationContext
                  .addViolation(new Violation(violationQualificationElementPresent));
            } else {*/
						isQualificationExtensionAllowed(validationContext,
								violationQualificationExtensionAllowed, serviceTypeIdentifier);
						for (QualificationElementType qualification : extension
								.getQualificationsExtension()) {
							isQualifiersPresent(validationContext, violationQualifiersPresent, qualification);
							isCriteriaAssertsNotNull(validationContext, violationCriteriaAssertsNotNull,
									qualification);
							isQualifiersURICorrectValue(validationContext, violationQualifiersURICorrectValue,
									qualification);
						}
						//}
					}
					if (extension.getExpiredCertsRevocationDate() != null) {
						if (extension.isCritical()) {
							validationContext
									.addViolation(new Violation(violationExpiredCertsRevocationNotCritical));
						}
					}
				}
			}

			checkAllExpiredCertsRevocationInfo(validationContext, violationExpiredCertsRevocationAllowed,
					extensions, serviceTypeIdentifier);
			checkAllAsi(validationContext, violationAsiAllowed, extensions, serviceTypeIdentifier);
			checkAllAsiSignatureOrSeal(validationContext, violationAsiSignatureOrSealAllowed, extensions,
					serviceTypeIdentifier);
			checkAllAsiLangAllowed(validationContext, violationAsiLangAllowed, extensions);

		}
	}

	private void checkAllAsiLangAllowed(ValidationContext validationContext,
			ViolationConstant violationAsiLangAllowed,
			ServiceInformationExtensionListType extensions) {
		List<NonEmptyMultiLangURIType> multiLang = ModelUtils
				.getValidAdditionalServiceInformation(extensions)
				.stream().map(AdditionalServiceInformationType::getUri)
				.collect(Collectors.toList());
		countriesValidator
				.isListAttributeLangAllowed(validationContext, violationAsiLangAllowed, multiLang);
	}

	private void checkAllAsi(ValidationContext validationContext,
			ViolationConstant violation, ServiceInformationExtensionListType extensions,
			NonEmptyURIType serviceTypeIdentifier) {
		List<String> allowedServiceTypesForAsiFore = filterServiceType(validationContext,
				PREDICATES_FOR_CHECK.get(Check.Fore));
		if (allowedServiceTypesForAsiFore.contains(serviceTypeIdentifier.getValue())) {
			List<AdditionalServiceInformationType> asiList = ModelUtils
					.getValidAdditionalServiceInformation(extensions);
			// Check if service have an ASI with Fore*
			if (asiList.stream().noneMatch(
					asi -> asi.getUri().getValue().endsWith("ForeSignatures")
							|| asi.getUri().getValue().endsWith("ForeSeals")
							|| asi.getUri().getValue().endsWith("ForWebSiteAuthentication"))) {
				// ASI not found
				validationContext.addViolation(new Violation(violation, serviceTypeIdentifier.getValue()));
			}
		}
	}

	private void checkAllAsiSignatureOrSeal(ValidationContext validationContext,
			ViolationConstant violation, ServiceInformationExtensionListType extensions,
			NonEmptyURIType serviceTypeIdentifier) {
		List<String> allowedServiceTypesForAsiForeSignatureOrSeals = filterServiceType(
				validationContext,
				PREDICATES_FOR_CHECK.get(Check.ForeSignatureOrSeal));
		if (allowedServiceTypesForAsiForeSignatureOrSeals.contains(serviceTypeIdentifier.getValue())) {
			List<AdditionalServiceInformationType> asiList = ModelUtils
					.getValidAdditionalServiceInformation(extensions);
			// Check if service have an ASI with ForeSignature or ForeSeal
			if (asiList.stream()
					.noneMatch(asi -> asi.getUri().getValue().endsWith("ForeSignatures")
							|| asi.getUri().getValue().endsWith("ForeSeals"))) {
				validationContext
						.addViolation(new Violation(violation, serviceTypeIdentifier.getValue()));
			}
		}
	}

	private void checkAllExpiredCertsRevocationInfo(ValidationContext validationContext,
			ViolationConstant violation, ServiceInformationExtensionListType extensions,
			NonEmptyURIType serviceTypeIdentifier) {
		List<String> allowedServiceTypesForCertRevocationInfo = filterServiceType(validationContext,
				PREDICATES_FOR_CHECK.get(Check.ExpiredCertRevocationInfo));
		List<LocalDateTime> expiredCertsRevocationInfo = getValidExpiredCertsRevocationInformation(
				extensions);
		// Check if the list contains at least one expiredCertsRevocationInfo Extension
		if (CollectionUtils.isNotEmpty(expiredCertsRevocationInfo)) {
			// Check if the serviceTypeIdentifier is allowed for expiredCertsRevocationInfo Extension
			if (!allowedServiceTypesForCertRevocationInfo.contains(serviceTypeIdentifier.getValue())) {
				validationContext.addViolation(new Violation(violation, serviceTypeIdentifier.getValue()));
			}
		}
	}

	private void isQualifiersURICorrectValue(ValidationContext validationContext,
			ViolationConstant violation, QualificationElementType qualification) {
		List<String> qualifiers = qualification.getQualificationList();
		if (CollectionUtils.isNotEmpty(qualifiers)) {
			List<String> allowedQualifiers = commonAttributesValidator
					.propertiesAsList(validationContext.getProperties().getQualifier());
			if (qualifiers.stream().anyMatch(qualifier -> !allowedQualifiers.contains(qualifier))) {
				validationContext.addViolation(new Violation(violation));
			}
		}
	}

	private void isQualificationExtensionAllowed(ValidationContext validationContext,
			ViolationConstant violation, NonEmptyURIType serviceTypeIdentifier) {
		if (!StringUtils.endsWith(serviceTypeIdentifier.getValue(), "CA/QC")) {
			validationContext.addViolation(new Violation(violation, serviceTypeIdentifier.getValue()));
		}
	}

	private void isQualifiersPresent(ValidationContext validationContext,
			ViolationConstant violation, QualificationElementType qualification) {
		if ((qualification != null) && ((qualification.getQualificationList() == null) || (
				qualification.getQualificationList().isEmpty()))) {
			validationContext.addViolation(new Violation(violation));
		}
	}

	private void isCriteriaAssertsNotNull(ValidationContext validationContext,
			ViolationConstant violation, QualificationElementType qualification) {
		if ((qualification != null) && (qualification.getCriteria() != null)
				&& (((qualification.getCriteria().getAsserts() == null) || qualification
				.getCriteria().getAsserts().equals("")))) {
			validationContext.addViolation(new Violation(violation));
		}
	}

	private List<LocalDateTime> getValidExpiredCertsRevocationInformation(
			ServiceInformationExtensionListType extensions) {
		if (extensions != null && CollectionUtils.isNotEmpty(extensions.getValues())) {
			return extensions.getValues().stream()
					.filter(extension -> extension.getExpiredCertsRevocationDate() != null)
					.map(ServiceInformationExtensionType::getExpiredCertsRevocationDate)
					.collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

	private List<String> filterServiceType(ValidationContext validationContext,
			Predicate<String> predicate) {
		List<String> serviceTypeIdentifiers = getServiceTypeIdentifiers(validationContext);
		if (CollectionUtils.isNotEmpty(serviceTypeIdentifiers)) {
			return serviceTypeIdentifiers.stream().filter(predicate).collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

	private List<String> getServiceTypeIdentifiers(ValidationContext validationContext) {
		List<String> listServiceTypeIdentifiers = new ArrayList<>();
		listServiceTypeIdentifiers.addAll(commonAttributesValidator
				.propertiesAsList(validationContext.getProperties().getNationalServiceType()));
		listServiceTypeIdentifiers.addAll(commonAttributesValidator
				.propertiesAsList(validationContext.getProperties().getQualifiedServiceType()));
		listServiceTypeIdentifiers.addAll(commonAttributesValidator
				.propertiesAsList(validationContext.getProperties().getNonQualifiedServiceType()));
		return listServiceTypeIdentifiers;
	}

}
