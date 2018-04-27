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
import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
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
@RequestMapping("/api/tsl")
public class ApiTslController {

  private final ApiService apiService;
  private final TslService tslService;

  @Autowired
  public ApiTslController(
      @Qualifier("apiPublic") ApiService apiService,
      TslService tslService) {
    this.apiService = apiService;
    this.tslService = tslService;
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
    return apiService.execute(() -> tslService.read(territoryCode));
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
    return apiService.execute(() -> Optional.ofNullable(tslService.readAll()));
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
    return apiService.execute(() -> tslService.validate(territoryCode));
  }

}
