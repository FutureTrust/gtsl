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

package eu.futuretrust.gtsl.admin.controllers.api;

import eu.futuretrust.gtsl.business.dto.helper.ResultDTO;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.business.services.responder.ResponderService;
import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/responder")
public class ApiResponderController {

  private final ApiService apiService;
  private final ResponderService responderService;
  private final JaxbService jaxbService;

  public ApiResponderController(
      @Qualifier("apiPublic") ApiService apiService,
      ResponderService responderService,
      JaxbService jaxbService) {
    this.apiService = apiService;
    this.responderService = responderService;
    this.jaxbService = jaxbService;
  }

  /**
   * Check if the certificate in present in one of the Trusted Lists
   *
   * @param base64Certificate base64 encoded certificate
   * @return Trusted Service in which the certificate has been found
   */
  @RequestMapping(value = "/json", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResultDTO<TSPServiceType>> responderJson(
      @RequestBody String base64Certificate) {
    return apiService.execute(() -> responderService.validate(base64Certificate));
  }

  @RequestMapping(value = "/xml", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
  public ResponseEntity<ResultDTO<byte[]>> responderXml(@RequestBody String base64Certificate) {
    return apiService.execute(() -> responderService.validate(base64Certificate)
        .map(jaxbService::marshallServiceToBytes));
  }

}
