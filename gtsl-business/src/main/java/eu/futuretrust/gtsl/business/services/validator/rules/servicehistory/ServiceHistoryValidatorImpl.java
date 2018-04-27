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

package eu.futuretrust.gtsl.business.services.validator.rules.servicehistory;

import eu.futuretrust.gtsl.business.services.validator.rules.RulesValidator;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.model.data.ts.ServiceHistoryInstanceType;
import eu.futuretrust.gtsl.model.data.ts.ServiceHistoryType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceHistoryValidatorImpl implements RulesValidator<ServiceHistoryType> {

  private final RulesValidator<ServiceHistoryInstanceType> serviceHistoryInstanceValidator;

  @Autowired
  public ServiceHistoryValidatorImpl(
      RulesValidator<ServiceHistoryInstanceType> serviceHistoryInstanceValidator) {
    this.serviceHistoryInstanceValidator = serviceHistoryInstanceValidator;
  }

  @Override
  public void validate(ValidationContext validationContext, ServiceHistoryType serviceHistory) {

    if (serviceHistory != null) {
      int i = 0;
      List<Object> currentArgs = validationContext.getArgs();
      for (ServiceHistoryInstanceType historyInstance : serviceHistory.getValues()) {
        // Add service index in the context
        List<Object> args = new ArrayList<>(currentArgs);
        args.add(i);
        validationContext.setArgs(args);

        serviceHistoryInstanceValidator.validate(validationContext, historyInstance);
        i++;
      }
    }
  }

}
