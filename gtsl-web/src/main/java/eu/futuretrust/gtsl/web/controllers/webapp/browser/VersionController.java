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

import eu.futuretrust.gtsl.business.dto.tsl.TslDTO;
import eu.futuretrust.gtsl.business.services.version.VersionService;
import eu.futuretrust.gtsl.business.services.webapp.WebappWithPropertiesService;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.model.utils.DateUtils;
import eu.futuretrust.gtsl.properties.RulesProperties;
import eu.futuretrust.gtsl.properties.rules.Country;
import eu.futuretrust.gtsl.web.controllers.api.ApiVersionImporterController;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tl/versions")
public class VersionController {

  private final WebappWithPropertiesService webAppService;
  private final VersionService versionService;

  @Autowired
  public VersionController(
      @Qualifier("webappPublicWithProperties") WebappWithPropertiesService webAppService,
      VersionService versionService) {
    this.webAppService = webAppService;
    this.versionService = versionService;
  }

  @GetMapping(value = "{territoryCode}")
  public String getVersions(@PathVariable String territoryCode, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      List<TslDTO> versionsDTO = convertToDTO(model, redirectAttributes, properties.getCountries(),
          versionService.retrieveAllVersions(territoryCode));

      return this.fillVersions(model, territoryCode, properties, versionsDTO);
    }, redirectAttributes);
  }

  private String fillVersions(Model model, String territoryCode, RulesProperties properties,
      List<TslDTO> versionsDTO) {
    model.addAttribute("territoryCode", territoryCode);
    model.addAttribute("country", properties.getCountries().get(territoryCode).getName());
    model.addAttribute("versions", versionsDTO);

    return "browser/versions";
  }

  private List<TslDTO> convertToDTO(Model model, RedirectAttributes redirectAttributes,
      Map<String, Country> countriesProperties, List<TrustStatusListType> listOfTsl) {
    return listOfTsl.stream()
        .map(tsl ->
            new TslDTO(
                (countriesProperties.get(tsl.getSchemeInformation().getSchemeTerritory().getValue())
                    .getName() != null) ? countriesProperties
                    .get(tsl.getSchemeInformation().getSchemeTerritory().getValue()).getName()
                    : tsl.getSchemeInformation().getSchemeTerritory().getValue(), DateUtils
                .localDateTimeToPrettyString(tsl.getSchemeInformation().getListIssueDateTime()),
                "TL - " + tsl.getSchemeInformation().getSchemeTerritory().getValue(),
                DateUtils.localDateTimeToPrettyString(tsl.getSchemeInformation().getNextUpdate()),
                tsl.getSchemeInformation().getTslSequenceNumber(), "VALID",
                tsl.getSchemeInformation().getSchemeTerritory().getValue(),
                tsl.getSchemeInformation().getTslType().getValue(),
                createMapUrls(model, redirectAttributes, tsl)))
        .sorted(Comparator.comparing(TslDTO::getCountryName))
        .collect(Collectors.toList());
  }

  private Map<String, String> createMapUrls(Model model, RedirectAttributes redirectAttributes,
      TrustStatusListType tsl) {
    Map<String, String> urls = new HashMap<>();
    urls.put("export", MvcUriComponentsBuilder
        .fromMethodName(ApiVersionImporterController.class, "exportXmlVersion",
            tsl.getSchemeInformation().getSchemeTerritory().getValue(),
            tsl.getSchemeInformation().getTslSequenceNumber())
        .build()
        .toString());
    return urls;
  }

}
