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

package eu.futuretrust.gtsl.business.services.responder;

import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Optional;

public interface ResponderService {

  /**
   * Try to findAll the Base64 encoded certificate in one of the Trusted List
   *
   * @param base64Cert Base64 encoded certificate to be found in the Trusted Lists
   * @return service in which the certificate is present; or empty if not found
   * @throws CertificateException whenever the certificate is not valid
   */
  Optional<TSPServiceType> validate(String base64Cert) throws Exception;

}
