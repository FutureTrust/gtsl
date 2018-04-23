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

package eu.futuretrust.gtsl.business.utils.validator;

import eu.futuretrust.gtsl.business.services.message.MessageService;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.business.dto.report.ConstraintViolationDTO;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import org.springframework.context.NoSuchMessageException;

public final class ReportUtils {

  public static <T> List<ConstraintViolationDTO> generateReport(
      Set<ConstraintViolation<T>> constraintViolations) {
    List<ConstraintViolationDTO> report = new ArrayList<>();

    for (ConstraintViolation<?> constraintViolation : constraintViolations) {
      String message;
      try {
        message = constraintViolation.getMessage();
      } catch (NoSuchMessageException exception) {
        message = "No message available.";
      }

      String impact = "Unknown";
      String severity = "Unknown";
      for (Class<? extends Payload> p : constraintViolation.getConstraintDescriptor()
          .getPayload()) {
        if (p.getSuperclass().equals(Impact.class)) {
          impact = p.getSimpleName();
        } else if (p.getSuperclass().equals(Severity.class)) {
          severity = p.getSimpleName();
        }
      }

      ConstraintViolationDTO violationDTO = new ConstraintViolationDTO(impact, severity,
          constraintViolation.getPropertyPath().toString(), message,
          constraintViolation.getInvalidValue());
      report.add(violationDTO);
    }

    report.sort(Comparator.comparing(ConstraintViolationDTO::getTarget));
    return report;
  }

  public static List<ConstraintViolationDTO> generateReport(
      List<Violation> constraintViolations,
      MessageService messageService) {
    List<ConstraintViolationDTO> report = new ArrayList<>();

    for (Violation violation : constraintViolations) {
      ViolationConstant violationConstant = violation.getViolation();
      String impact = violationConstant.getImpact();
      String severity = violationConstant.getSeverity();
      String message = messageService.getMessage(violationConstant.getDescription());
      String target = violationConstant.getTarget().value(violation.getArgs());
      ConstraintViolationDTO violationDTO = new ConstraintViolationDTO(impact, severity, target,
          message, violation.getCurrentValue());
      report.add(violationDTO);
    }

    report.sort(Comparator.comparing(ConstraintViolationDTO::getTarget));
    return report;
  }

}
