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

package eu.futuretrust.gtsl.admin;

import eu.futuretrust.gtsl.business.properties.ContractProperties;
import eu.futuretrust.gtsl.business.properties.EthereumProperties;
import eu.futuretrust.gtsl.business.properties.IPFSProperties;
import eu.futuretrust.gtsl.jaxb.additionaltypes.ObjectFactory;
import eu.futuretrust.gtsl.ledger.Consortium;
import eu.futuretrust.gtsl.ledger.RulesPropertiesWrapper;
import eu.futuretrust.gtsl.ledger.TslLedger;
import eu.futuretrust.gtsl.model.utils.JaxbGregorianCalendarZulu;
import eu.futuretrust.gtsl.properties.RulesPropertiesHandler;
import eu.futuretrust.gtsl.storage.Storage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"eu.futuretrust.gtsl.admin", "eu.futuretrust.gtsl.business"})
public class GtslAdminApplication {

  public static void main(String[] args) {
    SpringApplication.run(GtslAdminApplication.class, args);
  }

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("rules_messages");
    return messageSource;
  }

  @Bean
  public Storage initStorage(IPFSProperties ipfsProperties) {
    return new Storage(ipfsProperties.getEndpoint());
  }

  @Bean
  public Consortium initConsortiumContract(EthereumProperties ethereumProperties,
      ContractProperties contractProperties) throws Exception {
    Consortium consortium = new Consortium(ethereumProperties.getEndpoint(),
        loadKeystore(ethereumProperties.getKeystorePath()), ethereumProperties.getPassword());
    consortium.load(contractProperties.getConsortium());
    return consortium;
  }

  @Bean
  public TslLedger initLedgerContract(EthereumProperties ethereumProperties,
      ContractProperties contractProperties) throws Exception {
    TslLedger tslLedger = new TslLedger(ethereumProperties.getEndpoint(),
        loadKeystore(ethereumProperties.getKeystorePath()), ethereumProperties.getPassword());
    tslLedger.load(contractProperties.getTslStore());
    return tslLedger;
  }

  @Bean
  public RulesPropertiesHandler initPropertiesHandler(EthereumProperties ethereumProperties,
      ContractProperties contractProperties, Storage storage) throws Exception {
    RulesPropertiesWrapper rulesPropertiesWrapper = new RulesPropertiesWrapper(
        ethereumProperties.getEndpoint(),
        loadKeystore(ethereumProperties.getKeystorePath()),
        ethereumProperties.getPassword());
    rulesPropertiesWrapper.load(contractProperties.getRulesProperties());
    return new RulesPropertiesHandler(storage, rulesPropertiesWrapper);
  }

  private InputStream loadKeystore(String keystorePath) throws IOException {
    return new ClassPathResource(keystorePath).getInputStream();
  }

  @Bean
  @Qualifier(value = "tslMarshaller")
  public Jaxb2Marshaller tslJaxb2MarshallerV5() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

    marshaller.setAdapters(new JaxbGregorianCalendarZulu());
    Map<String, Object> map = new HashMap<>();
    map.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, false);
    map.put(javax.xml.bind.Marshaller.JAXB_FRAGMENT, true);
    map.put("com.sun.xml.bind.xmlHeaders",
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    marshaller.setMarshallerProperties(map);
    marshaller.setClassesToBeBound(eu.futuretrust.gtsl.jaxb.tsl.ObjectFactory.class,
        ObjectFactory.class,
        eu.futuretrust.gtsl.jaxb.sie.ObjectFactory.class,
        eu.futuretrust.gtsl.jaxb.xades.ObjectFactory.class,
        eu.futuretrust.gtsl.jaxb.xmldsig.ObjectFactory.class);
    return marshaller;
  }

}
