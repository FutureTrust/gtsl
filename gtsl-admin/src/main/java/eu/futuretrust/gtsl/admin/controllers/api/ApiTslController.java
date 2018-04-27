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
import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.business.services.draft.DraftService;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.business.vo.draft.DraftVO;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tsl")
public class ApiTslController {

  private final ApiService apiAuthorizedService;
  private final ApiService apiPublicService;
  private final TslService tslService;
  private final DraftService draftService;

  @Autowired
  public ApiTslController(
      @Qualifier("apiAuthorized") ApiService apiAuthorizedService,
      @Qualifier("apiPublic") ApiService apiPublicService,
      TslService tslService,
      DraftService draftService) {
    this.apiAuthorizedService = apiAuthorizedService;
    this.apiPublicService = apiPublicService;
    this.tslService = tslService;
    this.draftService = draftService;
  }

  /**
   * Create a new Trust Service List An authenticated and authorised user should be able to create a
   * TSL for a recognized territory or an application-specific purpose. Primary Actor: Administrator
   * User Preconditions: None Postconditions: A new unsigned TSL is created.
   *
   * @param tsl tsl to be created
   * @return report containing the errors/warnings/info which occurred when validating the TSL
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<ReportDTO>> create(@RequestBody TrustStatusListType tsl) {
    return apiAuthorizedService.execute(() -> Optional.ofNullable(tslService.create(tsl)));
  }

  /**
   * Create a Trust Service List from an existing Draft An authenticated and authorised user should
   * be able to create a TSL for a recognized territory or an application-specific purpose. Primary
   * Actor: Administrator User Preconditions: The TSL should exist on the database. Postconditions:
   * The changes are stored / A new unsigned TSL is created.
   *
   * @param dbId Id in database
   * @return report containing the errors/warnings/info which occurred when validating the TSL
   */
  @RequestMapping(value = "db/{dbId}", method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<ReportDTO>> push(@PathVariable String dbId) {
    return apiAuthorizedService
        .execute(() -> {
          Optional<DraftVO> draft = draftService.read(dbId);
          if (draft.isPresent()) {
            return Optional.ofNullable(tslService.push(draft.get()));
          } else {
            return Optional.empty();
          }
        });
  }

  /**
   * Sign a Trust Service List The actor wants to sign a TSL in order to allow the publication. The
   * (XAdES) signature is only used for XML document Primary Actor: Administrator User
   * Preconditions: A signature was created. Postconditions: Signature is stored with-in a TSL
   * document.
   *
   * @param dbId Id in database
   * @return ?????
   */
  @RequestMapping(value = "sign/{dbId}", method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<String>> sign(@PathVariable String dbId) {
    /*
    Basic Flow of Events:
      1. The actor provides a signature for the respective TSL document.
      2. The system checks if the signature is valid.
      3. The system stores the document.
      4. A success message is returned.

    Error Flow:
      2a. Return an error message, if the signature is not valid.
     */
    // TODO: attach the signature provided to the respective TSL draft (XML) document if the signature is valid
    return null;
  }

  /**
   * Update a Trust Service List An authenticated and authorised user should be able to update a TSL
   * for a recognized territory or an application-specific purpose. Primary Actor: Administrator
   * User Preconditions: The TSL should exist on the ledger. Postconditions: The changes are
   * stored.
   *
   * @param tsl tsl to be updated
   * @return report containing the errors/warnings/info which occurred when validating the TSL
   */
  @RequestMapping(method = RequestMethod.PUT)
  public ResponseEntity<ResultDTO<ReportDTO>> update(@RequestBody TrustStatusListType tsl) {
    return apiAuthorizedService.execute(() -> Optional.ofNullable(tslService.update(tsl)));
  }

  /**
   * Read a Trust Service List User reads TSL information. Primary Actor: External User
   * Preconditions: Requested territory should exist on the ledger. Postconditions: Return the TSL
   * for the specified territory.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @return TSL for the specified territory
   */
  @RequestMapping(value = "{territoryCode}", method = RequestMethod.GET)
  public ResponseEntity<ResultDTO<TrustStatusListType>> read(@PathVariable String territoryCode) {
    return apiPublicService.execute(() -> tslService.read(territoryCode));
  }

  /**
   * Delete a Trust Service List An authenticated and authorised user should be able to delete a
   * TSL, this TSL is repealed but not definitely deleted. Primary Actor: Administrator User
   * Preconditions: The TSL should exist on the ledger. Postconditions: The TSL is repealed.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @return territoryCode
   */
  @RequestMapping(value = "{territoryCode}", method = RequestMethod.DELETE)
  public ResponseEntity<ResultDTO<Boolean>> delete(@PathVariable String territoryCode) {
    return apiAuthorizedService.execute(() -> {
      tslService.remove(territoryCode);
      return Optional.of(true);
    });
  }

  /**
   * List all Trust Service Lists The user lists all TSL in order to have a global view of the Trust
   * Lists available on gTSL. Primary Actor: External User Preconditions: The TSL should exist on
   * the ledger. Postconditions: Return all TSLs available.
   *
   * @return list of TSLs available
   */
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<ResultDTO<List<TrustStatusListType>>> readAll() {
    return apiPublicService.execute(() -> Optional.ofNullable(tslService.readAll()));
  }

  /**
   * Validate a Trust Service List The user validates a TSL in order to correct the errors. Primary
   * Actor: External User Preconditions: The TSL should exist on the ledger. Postconditions: Return
   * a report containing the errors/warnings/info.
   *
   * @return report containing the errors/warnings/info which occurred when validating the TSL
   */
  @RequestMapping(value = "validate/{territoryCode}", method = RequestMethod.GET)
  public ResponseEntity<ResultDTO<ReportDTO>> validate(@PathVariable String territoryCode) {
    return apiPublicService.execute(() -> tslService.validate(territoryCode));
  }

}
