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

package eu.futuretrust.gtsl.business.services.xml;

import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.oxm.XmlMappingException;

public interface JaxbService {

  TrustStatusListTypeJAXB unmarshallTsl(InputStream is)
      throws XmlMappingException, IOException;

  TrustStatusListTypeJAXB unmarshallTsl(File file) throws XmlMappingException, IOException;

  TrustStatusListTypeJAXB unmarshallTsl(byte[] content) throws XmlMappingException, IOException;

  byte[] marshallTslToBytes(TrustStatusListType tsl);

  byte[] marshallServiceToBytes(TSPServiceType service);

}

