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

package eu.futuretrust.gtsl.business.services.rules.impl;

import eu.futuretrust.gtsl.business.services.rules.RulesPropertiesService;
import eu.futuretrust.gtsl.business.utils.DebugUtils;
import eu.futuretrust.gtsl.properties.RulesProperties;
import eu.futuretrust.gtsl.properties.RulesPropertiesHandler;
import java.io.InputStream;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RulesPropertiesServiceImpl implements RulesPropertiesService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RulesPropertiesServiceImpl.class);

  private final RulesPropertiesHandler rulesPropertiesHandler;

  @Autowired
  public RulesPropertiesServiceImpl(RulesPropertiesHandler rulesPropertiesHandler) {
    this.rulesPropertiesHandler = rulesPropertiesHandler;
  }

  @Override
  public Optional<RulesProperties> retrieve() throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "retrieve");

    return rulesPropertiesHandler.retrieve();
  }

  @Override
  public void update(RulesProperties rulesProperties) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "update");

    rulesPropertiesHandler.update(rulesProperties);
  }

  @Override
  public void update(InputStream file) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "update");

    rulesPropertiesHandler.update(file);
  }

}
