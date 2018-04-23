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

package eu.futuretrust.gtsl.business.config;

import eu.futuretrust.gtsl.business.properties.VatProperties;
import eu.futuretrust.gtsl.vat.CheckVatService;
import java.net.URL;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(VatProperties.class)
public class VATCheckerConfig {

  private final String wsdlLocation;

  public VATCheckerConfig(VatProperties vatProperties) {
    this.wsdlLocation = vatProperties.getLocation();
  }

  @Bean
  public CheckVatService checkVatService() {
    try {
      return new CheckVatService(new URL(wsdlLocation));
    } catch (Exception e) {
      return null;
    }
  }

}
