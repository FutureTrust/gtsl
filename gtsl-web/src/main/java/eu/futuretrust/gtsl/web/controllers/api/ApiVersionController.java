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

package eu.futuretrust.gtsl.web.controllers.api;

import eu.futuretrust.gtsl.business.dto.helper.ResultDTO;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.business.services.version.VersionService;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tsl/version")
public class ApiVersionController {

  private final ApiService apiService;
  private final VersionService versionService;

  @Autowired
  public ApiVersionController(
      @Qualifier("apiPublic") ApiService apiService,
      VersionService versionService) {
    this.apiService = apiService;
    this.versionService = versionService;
  }

  /**
   * Read a Version of TSL User reads TSL information. Primary Actor: External User Preconditions:
   * Requested territory should exist on the ledger. Postconditions: Return the TSL for the
   * specified territory and version number.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @param versionNumber Version number of the TSL
   * @return TSL for the specified territory and version number
   */
  @RequestMapping(value = "{territoryCode}/{versionNumber}", method = RequestMethod.GET)
  public ResponseEntity<ResultDTO<TrustStatusListType>> retrieveByVersionNumber(
      @PathVariable String territoryCode, @PathVariable BigInteger versionNumber) {
    return apiService.execute(() -> versionService.retrieveTsl(territoryCode, versionNumber));
  }

  /**
   * List all Versions of TSL User reads TSL information. Primary Actor: External User
   * Preconditions:Requested territory should exist on the ledger. Postconditions: Return the list
   * of TSL for the specified territory.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @return List of TSL for the specified territory
   */
  @RequestMapping(value = "{territoryCode}", method = RequestMethod.GET)
  public ResponseEntity<ResultDTO<List<TrustStatusListType>>> retrieveAllVersions(
      @PathVariable String territoryCode) {
    return apiService
        .execute(() -> Optional.ofNullable(versionService.retrieveAllVersions(territoryCode)));
  }

}
