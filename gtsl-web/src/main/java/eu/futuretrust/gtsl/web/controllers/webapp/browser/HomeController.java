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

import eu.futuretrust.gtsl.business.dto.notifications.EmailDTO;
import eu.futuretrust.gtsl.business.dto.tsl.TslDTO;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.business.services.webapp.WebappWithPropertiesService;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.model.utils.DateUtils;
import eu.futuretrust.gtsl.properties.rules.Country;
import eu.futuretrust.gtsl.web.controllers.api.ApiTslImporterController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class HomeController {

  private final WebappWithPropertiesService webAppService;
  private final TslService tslService;

  @Autowired
  public HomeController(
      @Qualifier("webappPublicWithProperties") WebappWithPropertiesService webAppService,
      TslService tslService) {
    this.webAppService = webAppService;
    this.tslService = tslService;
  }

  @GetMapping()
  public String home(Model model, RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      List<TslDTO> tslDTO = convertToDTO(model, redirectAttributes, properties.getCountries(),
          tslService.readAll());

      return this.fillHome(model, tslDTO);
    }, redirectAttributes);
  }

  private String fillHome(Model model, List<TslDTO> tslDTO) {
    model.addAttribute("listOfTsl", tslDTO);
    model.addAttribute("subscribeForm", new EmailDTO());
    return "browser/home";
  }

  private List<TslDTO> convertToDTO(Model model, RedirectAttributes redirectAttributes,
      Map<String, Country> countriesProperties, List<TrustStatusListType> listOfTsl) {
    return listOfTsl.stream().map(tsl -> {
      Map<String, String> urls = createMapUrls(model, redirectAttributes, tsl);
      return new TslDTO(
          (countriesProperties
              .get(tsl.getSchemeInformation().getSchemeTerritory().getValue())
              .getName() != null) ? countriesProperties
              .get(tsl.getSchemeInformation().getSchemeTerritory().getValue())
              .getName() : tsl.getSchemeInformation().getSchemeTerritory().getValue(),
          DateUtils.localDateTimeToPrettyString(
              tsl.getSchemeInformation().getListIssueDateTime()),
          "TL - " + tsl.getSchemeInformation().getSchemeTerritory()
              .getValue(),
          DateUtils.localDateTimeToPrettyString(
              tsl.getSchemeInformation().getNextUpdate()),
          tsl.getSchemeInformation().getTslSequenceNumber(),
          "VALID",
          tsl.getSchemeInformation().getSchemeTerritory().getValue(),
          tsl.getSchemeInformation().getTslType().getValue(),
          urls);
    }).collect(Collectors.toList());
  }

  private Map<String, String> createMapUrls(Model model, RedirectAttributes redirectAttributes,
      TrustStatusListType tsl) {
    Map<String, String> urls = new HashMap<>();
    urls.put("view", MvcUriComponentsBuilder
        .fromMethodName(TlController.class, "getTsl",
            tsl.getSchemeInformation().getSchemeTerritory().getValue(), model,
            redirectAttributes)
        .build()
        .toString());
    urls.put("export", MvcUriComponentsBuilder
        .fromMethodName(ApiTslImporterController.class, "exportXml",
            tsl.getSchemeInformation().getSchemeTerritory().getValue())
        .build()
        .toString());
    urls.put("versions", MvcUriComponentsBuilder
        .fromMethodName(VersionController.class, "getVersions",
            tsl.getSchemeInformation().getSchemeTerritory().getValue(), model,
            redirectAttributes)
        .build()
        .toString());
    return urls;
  }

}
