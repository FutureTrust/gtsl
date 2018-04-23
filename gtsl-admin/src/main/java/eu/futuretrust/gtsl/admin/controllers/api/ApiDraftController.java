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
@RequestMapping("/api/draft")
public class ApiDraftController {

  private final ApiService apiService;
  private final DraftService draftService;

  @Autowired
  public ApiDraftController(
      @Qualifier("apiAuthorized") ApiService apiService,
      DraftService draftService) {
    this.apiService = apiService;
    this.draftService = draftService;
  }

  /**
   * Create a new Draft An authenticated and authorised user should be able to create a draft of
   * aTSL for a recognized territory or an application-specific purpose. Primary Actor:
   * Administrator User Preconditions: None Postconditions: A new draft of TSL is created.
   *
   * @param tsl tsl to be created as a draft
   * @return report containing the errors/warnings/info which occurred when validating the TSL
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<ReportDTO>> create(@RequestBody TrustStatusListType tsl) {
    return apiService.execute(() -> Optional.ofNullable(draftService.create(tsl)));
  }

  /**
   * Update a Draft An authenticated and authorised user should be able to update a draft of TSL for
   * a recognized territory or an application-specific purpose. Primary Actor: Administrator User
   * Preconditions: The draft of TSL should exist on the database. Postconditions: The changes are
   * stored.
   *
   * @param dbId Id in database
   * @param tsl tsl to be updated
   * @return report containing the errors/warnings/info which occurred when validating the TSL
   */
  @RequestMapping(value = "db/{dbId}", method = RequestMethod.PUT)
  public ResponseEntity<ResultDTO<ReportDTO>> update(@PathVariable String dbId,
      @RequestBody TrustStatusListType tsl) {
    return apiService.execute(() -> Optional.ofNullable(draftService.update(dbId, tsl)));
  }

  /**
   * Delete a Draft An authenticated and authorised user should be able to delete a draft of TSL.
   * Primary Actor: Administrator User Preconditions: The draft of TSL should exist on the database.
   * Postconditions: The draft is deleted.
   *
   * @param dbId Id in database
   * @return dbId
   */
  @RequestMapping(value = "db/{dbId}", method = RequestMethod.DELETE)
  public ResponseEntity<ResultDTO<String>> delete(@PathVariable String dbId) {
    return apiService.execute(() -> {
      draftService.delete(dbId);
      return Optional.ofNullable(dbId);
    });
  }

  /**
   * Delete all Drafts for the specified country An authenticated and authorised user should be able
   * to delete all drafts for the specified country. Primary Actor: Administrator User
   * Preconditions: The draft of TSL should exist on the database. Postconditions: The draft is
   * deleted.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @return territoryCode
   */
  @RequestMapping(value = "{territoryCode}", method = RequestMethod.DELETE)
  public ResponseEntity<ResultDTO<String>> deleteAll(@PathVariable String territoryCode) {
    return apiService.execute(() -> {
      draftService.deleteAll(territoryCode);
      return Optional.ofNullable(territoryCode);
    });
  }

  /**
   * Validate a Draft The user validates a Draft in order to correct the errors. Primary Actor:
   * External User Preconditions: The TSL should exist on the database. Postconditions: Return a
   * report containing the errors/warnings/info.
   *
   * @return report containing the errors/warnings/info which occurred when validating the TSL
   */
  @RequestMapping(value = "validate/db/{dbId}", method = RequestMethod.GET)
  public ResponseEntity<ResultDTO<ReportDTO>> validate(@PathVariable String dbId) {
    return apiService.execute(() -> draftService.validate(dbId));
  }

  /**
   * Read all drafts existing in the database User reads drafts information. Primary Actor: External
   * User Preconditions: None Postconditions: Return the list of drafts in the database.
   *
   * @return List of drafts in the database
   */
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<ResultDTO<List<DraftVO>>> readAll() {
    return apiService.execute(() -> Optional.ofNullable(draftService.readAll()));
  }

  /**
   * Read all drafts for the specified territory User reads drafts information. Primary Actor:
   * External User Preconditions: Requested territory should exist on the database. Postconditions:
   * Return the list of drafts for the specified territory.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @return List of drafts for the specified territory
   */
  @RequestMapping(value = "{territoryCode}", method = RequestMethod.GET)
  public ResponseEntity<ResultDTO<List<DraftVO>>> readAll(@PathVariable String territoryCode) {
    return apiService.execute(() -> Optional.ofNullable(draftService.readAll(territoryCode)));
  }

  /**
   * Read a draft for the specified Id User reads draft information. Primary Actor: External User
   * Preconditions: Requested Id should exist on the database. Postconditions: Return the draft for
   * the specified Id.
   *
   * @param dbId Id in database
   * @return draft for the specified Id
   */
  @RequestMapping(value = "db/{dbId}", method = RequestMethod.GET)
  public ResponseEntity<ResultDTO<DraftVO>> read(@PathVariable String dbId) {
    return apiService.execute(() -> draftService.read(dbId));
  }

}
