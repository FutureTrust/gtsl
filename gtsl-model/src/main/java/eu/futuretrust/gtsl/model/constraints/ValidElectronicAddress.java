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

package eu.futuretrust.gtsl.model.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.5.2 Scheme operator electronic address
 */
@Pattern(regexp = "^((http(s)?)|mailto:)(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?[^\\s]$")
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidElectronicAddress {

  String message() default "{ValidElectronicAddress.electronicAddressURI.value}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
