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
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.model.data.tsp.TSPType;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tsl/tsp")
public class ApiTspController {

  private final ApiService apiService;
  private final TslService tslService;

  @Autowired
  public ApiTspController(
      @Qualifier("apiPublic") ApiService apiService,
      TslService tslService) {
    this.apiService = apiService;
    this.tslService = tslService;
  }

  /**
   * Read a specific Trust Service Provider of a Trust Service List; Primary Actor: External User;
   * Preconditions: Requested territory should exist on the ledger, Requested tspTradeName should
   * exist in the TSL; Postconditions: Return the Trust Service Provider of the TSL for the
   * specified territory.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @param tspTradeName one of the Trade Names of the TSP
   * @return Trust Service Provider of the TSL for the specified tspTradeName
   */
  @RequestMapping(value = "{territoryCode}/{tspTradeName}", method = RequestMethod.GET)
  public ResponseEntity<ResultDTO<TSPType>> read(@PathVariable String territoryCode,
      @PathVariable String tspTradeName) {
    return apiService.execute(
        () -> tslService.read(territoryCode)
            .flatMap(tsl -> tsl.getTrustServiceProviderList().getValues()
                .parallelStream()
                .filter(tsp -> tsp.getTspInformation().getTspTradeName().getValues().stream()
                    .anyMatch(multiLang -> multiLang.getValue().equals(tspTradeName.trim())))
                .findAny()));
  }

  /**
   * Read all Trust Service Providers of the Trust Service List User reads all TSPs. Primary Actor:
   * External User Preconditions: The TSL should exist on the ledger. Postconditions: Return all
   * Trust Service Provider of the TSL.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @return list of Trust Service Provider for the specified TSL
   */
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<ResultDTO<List<TSPType>>> readAll(@PathVariable String territoryCode) {
    return apiService.execute(() -> tslService.read(territoryCode)
        .map(tsl -> tsl.getTrustServiceProviderList().getValues()));
  }

}
