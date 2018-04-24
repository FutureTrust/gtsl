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

package eu.futuretrust.gtsl.web.controllers.webapp.browser;

import eu.futuretrust.gtsl.business.dto.search.SearchInformationDTO;
import eu.futuretrust.gtsl.business.dto.search.SearchInputDTO;
import eu.futuretrust.gtsl.business.dto.search.SearchResultDTO;
import eu.futuretrust.gtsl.business.dto.tsl.CountryDTO;
import eu.futuretrust.gtsl.business.services.search.SearchService;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.business.services.webapp.WebappWithPropertiesService;
import eu.futuretrust.gtsl.model.data.enums.ServiceType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.properties.rules.Country;
import eu.futuretrust.gtsl.properties.rules.SearchType;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/search")
public class SearchController {

  private final WebappWithPropertiesService webAppService;
  private final TslService tslService;
  private final SearchService searchService;

  @Autowired
  public SearchController(
      @Qualifier("webappPublicWithProperties") WebappWithPropertiesService webAppService,
      TslService tslService,
      SearchService searchService) {
    this.webAppService = webAppService;
    this.tslService = tslService;
    this.searchService = searchService;
  }

  @GetMapping()
  public String searchForm(Model model, RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> this
        .fillSearchForm(model, tslService.readAll(), properties.getSearchTypes(),
            properties.getCountries()), redirectAttributes);
  }

  @PostMapping()
  public String searchSubmit(@ModelAttribute SearchInputDTO searchInput, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      List<SearchResultDTO> result = searchService
          .search(searchInput.getCountries(), searchInput.getServiceTypes());
      List<CountryDTO> countries = searchInput.getCountries().stream().map(
          countryCode -> new CountryDTO(properties.getCountries().get(countryCode).getName() != null
              ? properties.getCountries().get(countryCode).getName()
              : countryCode, countryCode)).collect(Collectors.toList());
      List<ServiceType> searchTypes = searchInput.getServiceTypes().stream()
          .map(type -> {
            Optional<SearchType> optSearchType = properties.getSearchTypes().stream()
                .filter(s -> s.getIdentifier().equals(type))
                .findFirst();
            String identifier =
                optSearchType.isPresent() ? optSearchType.get().getIdentifier() : type;
            return ServiceType.fromString(identifier);
          })
          .collect(Collectors.toList());

      return this.fillSearchResult(model, result, searchTypes, countries);
    }, redirectAttributes);
  }

  private String fillSearchForm(Model model, List<TrustStatusListType> listOfTsl,
      List<SearchType> searchTypes, Map<String, Country> countries) {
    model.addAttribute("qualifiedTypes",
        searchTypes.stream().filter(SearchType::isQualified).collect(
            Collectors.toList()));
    model.addAttribute("nonQualifiedTypes",
        searchTypes.stream().filter(type -> !type.isQualified()).collect(
            Collectors.toList()));
    model.addAttribute("countries", listOfTsl.stream()
        .map(tsl -> new CountryDTO(
            (countries.get(tsl.getSchemeInformation().getSchemeTerritory().getValue())
                .getName() != null) ? countries
                .get(tsl.getSchemeInformation().getSchemeTerritory().getValue()).getName()
                : tsl.getSchemeInformation().getSchemeTerritory().getValue(),
            tsl.getSchemeInformation().getSchemeTerritory().getValue()))
        .sorted(Comparator.comparing(CountryDTO::getCountryName))
        .collect(Collectors.toList()));
    model.addAttribute("searchInput", new SearchInputDTO());
    return "browser/search";
  }

  private String fillSearchResult(Model model, List<SearchResultDTO> result,
      List<ServiceType> searchTypes, List<CountryDTO> countries) {
    model.addAttribute("result", result);
    model.addAttribute("searchInfo", new SearchInformationDTO(countries, searchTypes));
    return "browser/search_result";
  }

}
