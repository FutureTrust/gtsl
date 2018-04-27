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

package eu.futuretrust.gtsl.model.constraints.validator;

import eu.futuretrust.gtsl.model.constraints.Case;
import eu.futuretrust.gtsl.model.constraints.enums.CaseMode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CaseValidator implements ConstraintValidator<Case, String> {

    private CaseMode caseMode;

    @Override
    public void initialize(Case constraintAnnotation) {
        this.caseMode = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if ( object == null ) {
            return true;
        }

        switch (caseMode) {
            case UPPER:
                return object.equals(object.toUpperCase());
            case LOWER:
                return object.equals(object.toLowerCase());
            default:
                return true;
        }
    }
}