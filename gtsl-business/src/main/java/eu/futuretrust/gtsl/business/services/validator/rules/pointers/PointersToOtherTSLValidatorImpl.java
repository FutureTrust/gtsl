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

package eu.futuretrust.gtsl.business.services.validator.rules.pointers;

import eu.futuretrust.gtsl.business.services.validator.rules.RulesValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.tsl.OtherTSLPointerType;
import eu.futuretrust.gtsl.model.data.tsl.OtherTSLPointersType;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointersToOtherTSLValidatorImpl implements RulesValidator<OtherTSLPointersType> {

  private final RulesValidator<OtherTSLPointerType> pointerValidator;

  @Autowired
  public PointersToOtherTSLValidatorImpl(RulesValidator<OtherTSLPointerType> pointerValidator) {
    this.pointerValidator = pointerValidator;
  }

  @Override
  public void validate(ValidationContext validationContext,
      OtherTSLPointersType pointersToOtherTSL) {

    if (validationContext.isEuTsl() || validationContext.isLotl()) {
      isPointersToOtherTslPresent(validationContext, pointersToOtherTSL);
      hasPointerToLOTL(validationContext, pointersToOtherTSL);
    }

    if (pointersToOtherTSL != null) {
      int i = 0;

      for (OtherTSLPointerType pointer : pointersToOtherTSL.getValues()) {
        validationContext.setArgs(new ArrayList<>(Collections.singletonList(i)));
        pointerValidator.validate(validationContext, pointer);
        i++;
      }
    }
  }

  private void isPointersToOtherTslPresent(ValidationContext validationContext,
      OtherTSLPointersType pointersToOtherTSL) {
    if (pointersToOtherTSL == null) {
      validationContext.addViolation(new Violation(ViolationConstant.IS_POINTER_PRESENT_FOR_EU_MS));
    }
  }

  private void hasPointerToLOTL(ValidationContext validationContext,
      OtherTSLPointersType pointersToOtherTSL) {
    if (pointersToOtherTSL != null && pointersToOtherTSL.getValues().stream().noneMatch(
        pointer -> pointer.getTslLocation().getValue()
            .equals(validationContext.getProperties().getLotl().getXml()))) {
      validationContext.addViolation(new Violation(ViolationConstant.HAS_POINTER_TO_LOTL));
    }
  }

}