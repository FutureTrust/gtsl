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

package eu.futuretrust.gtsl.business.services.validator.rules.tsp;

import eu.futuretrust.gtsl.business.services.validator.rules.RulesValidator;
import eu.futuretrust.gtsl.business.vo.validator.TspAttributesForValidation;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.model.data.tsl.TrustServiceProviderListType;
import eu.futuretrust.gtsl.model.data.tsp.TSPInformationType;
import eu.futuretrust.gtsl.model.data.tsp.TSPServicesListType;
import eu.futuretrust.gtsl.model.data.tsp.TSPType;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrustServiceProvidersValidatorImpl implements
    RulesValidator<TrustServiceProviderListType> {

  private final RulesValidator<TSPInformationType> tspInformationValidator;
  private final RulesValidator<TSPServicesListType> tspServicesValidator;

  @Autowired
  public TrustServiceProvidersValidatorImpl(
      RulesValidator<TSPInformationType> tspInformationValidator,
      RulesValidator<TSPServicesListType> tspServicesValidator) {
    this.tspInformationValidator = tspInformationValidator;
    this.tspServicesValidator = tspServicesValidator;
  }

  @Override
  public void validate(ValidationContext validationContext,
      TrustServiceProviderListType trustServiceProviderList) {
    if (trustServiceProviderList != null) {
      int i = 0;
      for (TSPType tsp : trustServiceProviderList.getValues()) {
        // Add tsp index in the context
        validationContext.setArgs(new ArrayList<>(Collections.singletonList(i)));
        validationContext.getAttributes().setTspAttributes(
            new TspAttributesForValidation(tsp.getTspInformation().getTspName(),
                tsp.getTspInformation().getTspTradeName()));
        // Run validation on TSP Information
        tspInformationValidator.validate(validationContext, tsp.getTspInformation());
        // Run validation on Services
        tspServicesValidator.validate(validationContext, tsp.getTspServices());
        i++;
      }
    }

  }

}
