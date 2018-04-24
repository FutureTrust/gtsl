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
import eu.futuretrust.gtsl.business.dto.search.SearchInputDTO;
import eu.futuretrust.gtsl.business.dto.search.SearchResultDTO;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.business.services.search.SearchService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tsl/search")
public class ApiSearchController {

  private final ApiService apiService;
  private final SearchService searchService;

  @Autowired
  public ApiSearchController(
      @Qualifier("apiPublic") ApiService apiService,
      SearchService searchService) {
    this.apiService = apiService;
    this.searchService = searchService;
  }

  /**
   * Search through TSLs filtering by service types User searches Trust Services matching the
   * desired service types. Primary Actor: External User Preconditions: None Postconditions: TSPs
   * matching arguments are returned.
   *
   * @param searchInput countries and service types to be matched
   * @return Result object containing a set of TSPs matching the search input
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<List<SearchResultDTO>>> search(
      @RequestBody SearchInputDTO searchInput) {
    return apiService.execute(
        () -> Optional.ofNullable(
            searchService.search(searchInput.getCountries(), searchInput.getServiceTypes())));
  }

}
