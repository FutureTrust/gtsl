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

import eu.futuretrust.gtsl.admin.services.XAdESSignatureService;
import eu.futuretrust.gtsl.business.dto.helper.ResultDTO;
import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.properties.SignatureProperties;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import eu.futuretrust.gtsl.admin.nexu.SignatureInformation;
import java.security.InvalidParameterException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sign")
public class ApiSignatureController {

  private final ApiService apiAuthorizedService;
  private final XAdESSignatureService signatureService;
  private final SignatureProperties signatureProperties;

  @Autowired
  public ApiSignatureController(
      @Qualifier("apiAuthorized") ApiService apiAuthorizedService,
      final XAdESSignatureService signatureService,
      final SignatureProperties signatureProperties) {
    this.apiAuthorizedService = apiAuthorizedService;
    this.signatureService = signatureService;
    this.signatureProperties = signatureProperties;
  }

  @RequestMapping(value = "/config", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> getNexuConfiguration() {

    Map<String, Object> nexuConfig = new HashMap<>();
    nexuConfig.put("nexuScheme", signatureProperties.getNexuScheme());
    nexuConfig.put("nexuPort", signatureProperties.getNexuPort());

    return ResponseEntity.ok(nexuConfig);
  }

  @RequestMapping(value = "{draftId}", method = RequestMethod.PUT)
  public ResponseEntity<ResultDTO<ReportDTO>> sign(@PathVariable final String draftId,
      @RequestBody SignatureInformation signatureInformation) {
    return apiAuthorizedService
        .execute(() -> Optional.of(signatureService.doSign(draftId, signatureInformation)));
  }

  /**
   * Retrieves the "toBeSigned" value for a given draft along with the configured nexu properties.
   *
   * @param draftId, which corresponds to the ID of the draft to sign
   * @return a Map containing: - The SignatureRequest object (wrapper around the toBeSigned, the
   * digest algorithm and other signature parameters); - The NexU properties, i.e. the NexU scheme
   * (HTTP or HTTPS) and the port on which the NexU component is listening.
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Map<String, Object>> getSignatureRequest(
      @RequestParam("draftId") final String draftId,
      @RequestParam("keyId") final String keyId,
      @RequestParam("tokenId") final String tokenId,
      @RequestParam("signingCert") final String base64Cert,
      @RequestParam("certChain") final String[] base64Chain) {
    try {
      final Map<String, Object> signatureRequest = signatureService
          .getSignatureRequestMap(draftId, keyId, tokenId, base64Cert, base64Chain);
      return ResponseEntity.ok(signatureRequest);
    } catch (UnauthorizedException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    } catch (InvalidParameterException | CertificateException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
