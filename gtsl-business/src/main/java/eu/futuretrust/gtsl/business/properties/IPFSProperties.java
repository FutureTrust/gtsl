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
import eu.futuretrust.gtsl.business.properties.helper.Cron;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/ipfs.properties")
@ConfigurationProperties(prefix="ipfs")
public class IPFSProperties {

  private static final Logger LOGGER = LoggerFactory.getLogger(IPFSProperties.class);

  @PostConstruct
  public void init() throws Exception {
    if (!this.isValid()) {
      String errorMessage = "IPFS properties must be configured";
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(errorMessage);
      }
      throw new PropertiesException(errorMessage);
    }
  }

  public boolean isValid() {
    return StringUtils.isNotBlank(endpoint)
        && cronAll != null && StringUtils.isNotBlank(cronAll.getValue())
        && cronCurrent != null && StringUtils.isNotBlank(cronCurrent.getValue());
  }

  /**
   * IPFS endpoint for storing files
   */
  private String endpoint;

  /**
   * Cron for pinning current version
   */
  private Cron cronCurrent;

  /**
   * Cron for pinning all versions
   */
  private Cron cronAll;

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public Cron getCronCurrent() {
    return cronCurrent;
  }

  public void setCronCurrent(Cron cronCurrent) {
    this.cronCurrent = cronCurrent;
  }

  public Cron getCronAll() {
    return cronAll;
  }

  public void setCronAll(Cron cronAll) {
    this.cronAll = cronAll;
  }

}
