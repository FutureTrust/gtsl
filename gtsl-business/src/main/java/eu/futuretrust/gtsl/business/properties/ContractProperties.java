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

package eu.futuretrust.gtsl.business.properties;

import eu.futuretrust.gtsl.business.properties.exceptions.PropertiesException;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/contract.properties")
@ConfigurationProperties(prefix = "contract")
public class ContractProperties {

  private static final Logger LOGGER = LoggerFactory.getLogger(ContractProperties.class);

  @PostConstruct
  public void init() throws Exception {
    if (!this.isValid()) {
      String errorMessage = "Contract properties must be configured";
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(errorMessage);
      }
      throw new PropertiesException(errorMessage);
    }
  }

  public boolean isValid() {
    return StringUtils.isNotBlank(consortium)
        && StringUtils.isNotBlank(rulesProperties)
        && StringUtils.isNotBlank(tslStore);
  }

  private String consortium;
  private String rulesProperties;
  private String tslStore;

  public String getConsortium() {
    return consortium;
  }

  public void setConsortium(String consortium) {
    this.consortium = consortium;
  }

  public String getRulesProperties() {
    return rulesProperties;
  }

  public void setRulesProperties(String rulesProperties) {
    this.rulesProperties = rulesProperties;
  }

  public String getTslStore() {
    return tslStore;
  }

  public void setTslStore(String tslStore) {
    this.tslStore = tslStore;
  }

}
