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
@PropertySource("classpath:properties/ethereum.properties")
@ConfigurationProperties(prefix = "ethereum")
public class EthereumProperties {

  private static final Logger LOGGER = LoggerFactory.getLogger(EthereumProperties.class);

  @PostConstruct
  public void init() throws Exception {
    if (!this.isValid()) {
      String errorMessage = "Ethereum properties must be configured";
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(errorMessage);
      }
      throw new PropertiesException(errorMessage);
    }
  }

  public boolean isValid() {
    return StringUtils.isNotBlank(endpoint)
        && StringUtils.isNotBlank(keystorePath)
        && StringUtils.isNotBlank(password);
  }

  /**
   * Ethereum endpoint to load smart contracts
   */
  private String endpoint;

  /**
   * Keystore location uses to be authenticated on the Ethereum chain
   */
  private String keystorePath;

  /**
   * Password associated to the keystorePath
   */
  private String password;

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getKeystorePath() {
    return keystorePath;
  }

  public void setKeystorePath(String keystorePath) {
    this.keystorePath = keystorePath;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
