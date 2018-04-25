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
@PropertySource("classpath:properties/lotl.properties")
@ConfigurationProperties(prefix = "lotl")
public class LotlProperties {

  private static final Logger LOGGER = LoggerFactory.getLogger(LotlProperties.class);

  @PostConstruct
  public void init() throws Exception {
    if (!this.isValid()) {
      String errorMessage = "Lotl properties must be configured";
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(errorMessage);
      }
      throw new PropertiesException(errorMessage);
    }
  }

  private String keystorePassword;
  private String keystorePath;
  private String keystoreType;
  private String xmlUrl;
  private Long cacheValidityWindow;
  private String fileCachePath;

  public boolean isValid() {
    return StringUtils.isNotBlank(keystorePassword)
        && StringUtils.isNotBlank(keystorePath)
        && StringUtils.isNotBlank(keystorePassword)
        && StringUtils.isNotBlank(keystoreType)
        && StringUtils.isNotBlank(xmlUrl)
        && cacheValidityWindow != null
        && cacheValidityWindow.compareTo(0L) > 0;
  }

  public String getKeystorePassword() {
    return keystorePassword;
  }

  public void setKeystorePassword(String keystorePassword) {
    this.keystorePassword = keystorePassword;
  }

  public String getKeystorePath() {
    return keystorePath;
  }

  public void setKeystorePath(String keystorePath) {
    this.keystorePath = keystorePath;
  }

  public String getKeystoreType() {
    return keystoreType;
  }

  public void setKeystoreType(String keystoreType) {
    this.keystoreType = keystoreType;
  }

  public String getXmlUrl() {
    return xmlUrl;
  }

  public void setXmlUrl(String xmlUrl) {
    this.xmlUrl = xmlUrl;
  }

  public Long getCacheValidityWindow() {
    return cacheValidityWindow;
  }

  public void setCacheValidityWindow(final Long cacheValidityWindow) {
    this.cacheValidityWindow = cacheValidityWindow;
  }

  public String getFileCachePath() {
    return fileCachePath;
  }

  public void setFileCachePath(String fileCachePath) {
    this.fileCachePath = fileCachePath;
  }

}
