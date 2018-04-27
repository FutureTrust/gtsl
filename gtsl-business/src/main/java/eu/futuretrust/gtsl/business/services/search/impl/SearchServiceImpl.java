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

package eu.futuretrust.gtsl.business.services.search.impl;


import eu.futuretrust.gtsl.business.dto.search.SearchResultDTO;
import eu.futuretrust.gtsl.business.services.additional.TslAdditionalInformationService;
import eu.futuretrust.gtsl.business.services.rules.RulesPropertiesService;
import eu.futuretrust.gtsl.business.services.search.SearchService;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.business.utils.DebugUtils;
import eu.futuretrust.gtsl.model.data.enums.ServiceType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.model.data.tsp.TSPType;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import eu.futuretrust.gtsl.properties.RulesProperties;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

  private final TslService tslService;
  private final RulesPropertiesService rulesPropertiesService;
  private final TslAdditionalInformationService tslAdditionalInformationService;

  @Autowired
  public SearchServiceImpl(TslService tslService,
      RulesPropertiesService rulesPropertiesService,
      TslAdditionalInformationService tslAdditionalInformationService) {
    this.tslService = tslService;
    this.rulesPropertiesService = rulesPropertiesService;
    this.tslAdditionalInformationService = tslAdditionalInformationService;
  }

  @Override
  public List<SearchResultDTO> search(List<String> countries, List<String> searchTypes)
      throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "search");

    if (CollectionUtils.isEmpty(countries)) {
      throw new InvalidParameterException("You must provide at least one country");
    } else if (CollectionUtils.isEmpty(searchTypes)) {
      throw new InvalidParameterException("You must provide at least one service type");
    }

    Optional<RulesProperties> properties = rulesPropertiesService.retrieve();
    if (!properties.isPresent()) {
      throw new Exception("The rules properties cannot be found");
    }

    if (!countriesAllowed(properties.get(), countries)) {
      throw new InvalidParameterException("The set of countries provided contain unknown country");
    } else if (!serviceTypesAllowed(properties.get(), searchTypes)) {
      throw new InvalidParameterException(
          "The set of service types provided contain unknown service type");
    } else {
      List<SearchResultDTO> searchResultDTOSet = getTslListFromCountryList(countries).stream()
          .map(tsl -> searchThroughTsl(properties.get(), tsl,
              ServiceType.fromStringAsList(searchTypes)))
          .filter(Objects::nonNull)
          .flatMap(List::stream).collect(Collectors.toList());

      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(
            "Search has been proceeded for countries {} and types {} with the following result {}",
            countries.stream().collect(Collectors.joining(", ", "[", "]")),
            searchTypes.stream().collect(Collectors.joining(", ", "[", "]")),
            searchResultDTOSet.stream().map(SearchResultDTO::getName)
                .collect(Collectors.joining(", ", "[", "]"))
        );
      }
      if (LOGGER.isInfoEnabled()) {
        LOGGER.info(
            "Search has been proceeded for {} countries and {} types with {} results",
            countries.size(), searchTypes.size(), searchResultDTOSet.size()
        );
      }
      return searchResultDTOSet;
    }
  }

  private List<SearchResultDTO> searchThroughTsl(RulesProperties properties, TrustStatusListType
      tsl, List<ServiceType> serviceTypes) {
    if (tsl.getTrustServiceProviderList() != null && CollectionUtils
        .isNotEmpty(tsl.getTrustServiceProviderList().getValues())) {
      String countryCode = tsl.getSchemeInformation().getSchemeTerritory().getValue();
      List<SearchResultDTO> result = new ArrayList<>();
      List<TSPType> tspList = tsl.getTrustServiceProviderList().getValues();
      for (int index = 0; index < tspList.size(); index++) {
        SearchResultDTO res = searchThroughTsp(properties, serviceTypes, countryCode,
            tspList.get(index), index);
        if (res != null) {
          result.add(res);
        }
      }
      return result;
    }
    return null;
  }

  private SearchResultDTO searchThroughTsp(RulesProperties properties,
      List<ServiceType> serviceTypes,
      String countryCode, TSPType tsp, int indexTsp) {
    String tspName = ModelUtils.getTspEnglishName(tsp.getTspInformation().getTspName());
    String trustmark = ModelUtils.getTrustMark(tsp.getTspInformation().getTspTradeName());

    SearchResultDTO searchResult = new SearchResultDTO(indexTsp, countryCode, tspName, trustmark);
    searchResult.setActive(tslAdditionalInformationService.isTspActive(properties, tsp));
    tsp.getTspServices().getValues().forEach(service -> {
      searchResult
          .getqServiceTypes()
          .addAll(tslAdditionalInformationService.getServiceTypes(properties, service));
      searchResult.setqServiceTypes(
          tslAdditionalInformationService.sortServiceTypes(searchResult.getqServiceTypes()));
      searchResult.getServiceStatus().addAll(tslAdditionalInformationService
          .getServiceStatus(serviceTypes, service, searchResult.getqServiceTypes()));
    });
    return searchResult.validate() ? searchResult : null;
  }

  private List<TrustStatusListType> getTslListFromCountryList(List<String> countries) {
    return countries.stream()
        .map(country -> {
          try {
            return tslService.read(country);
          } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
              LOGGER.warn("Tsl {} has not been found", country);
            }
            return Optional.<TrustStatusListType>empty();
          }
        })
        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
        .collect(Collectors.toList());
  }

  private boolean countriesAllowed(RulesProperties properties, List<String> countries) {
    return properties.getCountries().keySet().containsAll(countries);
  }

  private boolean serviceTypesAllowed(RulesProperties properties, List<String> searchTypes) {
    return searchTypes.stream()
        .allMatch(s -> properties.getSearchTypes().stream()
            .anyMatch(prop -> prop.getIdentifier().equals(s)));
  }

}
