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

import eu.europa.esig.dss.tsl.TSLValidationResult;
import java.io.File;

/**
 * Trusted Lists signature validator interface.
 */
public interface TLSignatureValidator {

  /**
   * Validates the signature of a Trusted List, based on its type and the authorised certificates
   * published in the List of Trusted Lists
   * @param tl a {@link File} containing the TSL to validate
   * @param tslType the type of the TSL, i.e. the value of the TSLType node contained within the
   *                TSL's SchemeInformation node.
   * @param countryCode the country code of the TSL
   * @return a {@link TSLValidationResult}, indicating the validation results
   * @throws Exception
   */
  TSLValidationResult validateTSL(final File tl, final String tslType, final String
      countryCode) throws Exception;
}
