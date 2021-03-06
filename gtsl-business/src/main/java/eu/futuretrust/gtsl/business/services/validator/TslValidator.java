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

package eu.futuretrust.gtsl.business.services.validator;

import eu.futuretrust.gtsl.business.dto.report.ConstraintViolationDTO;
import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.properties.LotlProperties;
import eu.futuretrust.gtsl.business.services.message.MessageService;
import eu.futuretrust.gtsl.business.services.validator.rules.RulesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.signature.TslSignatureValidatorImpl;
import eu.futuretrust.gtsl.business.utils.DebugUtils;
import eu.futuretrust.gtsl.business.utils.validator.ReportUtils;
import eu.futuretrust.gtsl.business.utils.validator.ValidatorUtils;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.TslAttributesForValidation;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.signature.SignatureType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.properties.RulesProperties;
import eu.futuretrust.gtsl.properties.RulesPropertiesHandler;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TslValidator {

  private static final Logger LOGGER = LoggerFactory.getLogger(TslValidator.class);

  private final MessageService messageService;
  private final RulesValidator<TrustStatusListType> rulesValidator;
  private final RulesValidator<SignatureType> signatureValidator;
  private final RulesPropertiesHandler rulesPropertiesHandler;
  private final LotlProperties lotlProperties;

  @Autowired
  public TslValidator(
      final MessageService messageService,
      final RulesPropertiesHandler rulesPropertiesHandler,
      final RulesValidator<TrustStatusListType> rulesValidator,
      final RulesValidator<SignatureType> signatureValidator,
      final LotlProperties lotlProperties) {
    this.messageService = messageService;
    this.rulesPropertiesHandler = rulesPropertiesHandler;
    this.rulesValidator = rulesValidator;
    this.signatureValidator = signatureValidator;
    this.lotlProperties = lotlProperties;
  }

  public boolean containsError(List<ConstraintViolationDTO> report) {
    return report.stream().anyMatch(
        violation -> violation.getSeverity().equals(Severity.Error.class.getSimpleName()));
  }

  public ReportDTO validate(final TrustStatusListType tsl, boolean isDraft) {
    return validate(tsl, null, isDraft);
  }

  public ReportDTO validate(final TrustStatusListType tsl, final TrustStatusListType currentTsl, boolean isDraft) {
    DebugUtils.debug(LOGGER, this.getClass(), "validate");
    List<ConstraintViolationDTO> report = validateFormat(tsl);
    if (!containsError(report)) {
      report.addAll(validateRules(tsl, currentTsl, isDraft));
    }
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Validation has been proceeded, report contains {} violations{}", report.size(),
          containsError(report) ? " including errors" : "");
      report.forEach(violation -> LOGGER.info("{}", violation.toString()));
    }
    return new ReportDTO(report, !containsError(report));
  }

  /**
   * Format validation using JSR 303 Bean Validation
   *
   * @param tsl trusted list to validate
   * @return report containing constraint violations
   */
  public List<ConstraintViolationDTO> validateFormat(TrustStatusListType tsl) {
    DebugUtils.debug(LOGGER, this.getClass(), "validateFormat");

    return ReportUtils.generateReport(ValidatorUtils.getValidator().validate(tsl));
  }

  public List<ConstraintViolationDTO> validateRules(final TrustStatusListType tsl,
      TrustStatusListType currentTsl, boolean isDraft) {
    DebugUtils.debug(LOGGER, this.getClass(), "validateRules");

    Optional<RulesProperties> optRulesProperties;
    try {
      optRulesProperties = rulesPropertiesHandler.retrieve();
      if (!optRulesProperties.isPresent()) {
        LOGGER.info("Properties cannot be loaded");
        return ReportUtils.generateReport(
            Collections.singletonList(new Violation(ViolationConstant.PROPERTIES_CANNOT_BE_LOADED)),
            messageService);
      } else {
        TslAttributesForValidation attributes = new TslAttributesForValidation(
            tsl.getSchemeInformation().getSchemeTerritory(),
            tsl.getSchemeInformation().getTslType(),
            tsl.getSchemeInformation().getSchemeInformationURI(), currentTsl == null ? null :
            currentTsl.getSchemeInformation().getTslSequenceNumber());
        ValidationContext validationContext = new ValidationContext(optRulesProperties.get(),
            attributes);
        rulesValidator.validate(validationContext, tsl);

        // Signature validation is not performed on drafts
        if (!isDraft) {
          ((TslSignatureValidatorImpl) signatureValidator).setTsl(tsl);
          ((TslSignatureValidatorImpl) signatureValidator).setLotlProperties(lotlProperties);
          signatureValidator.validate(validationContext, tsl.getSignature());
        }

        return ReportUtils.generateReport(validationContext.getReport(), messageService);
      }
    } catch (Exception e) {
      return ReportUtils.generateReport(
          Collections.singletonList(new Violation(ViolationConstant.PROPERTIES_CANNOT_BE_LOADED)),
          messageService);
    }
  }
}
