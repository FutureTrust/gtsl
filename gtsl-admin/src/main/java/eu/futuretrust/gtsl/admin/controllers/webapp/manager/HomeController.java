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

package eu.futuretrust.gtsl.admin.controllers.webapp.manager;

import eu.futuretrust.gtsl.admin.controllers.api.ApiTslImporterController;
import eu.futuretrust.gtsl.admin.helpers.PropertiesUtils;
import eu.futuretrust.gtsl.business.dto.tsl.TslDTO;
import eu.futuretrust.gtsl.business.services.ledger.ConsortiumService;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.business.services.webapp.WebappWithPropertiesService;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.model.utils.DateUtils;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("")
public class HomeController {

  private final ConsortiumService consortiumService;
  private final WebappWithPropertiesService webAppService;
  private final TslService tslService;

  @Autowired
  public HomeController(
      @Qualifier("webappAuthorizedWithProperties") WebappWithPropertiesService webAppService,
      ConsortiumService consortiumService,
      TslService tslService) {
    this.consortiumService = consortiumService;
    this.webAppService = webAppService;
    this.tslService = tslService;
  }

  @GetMapping()
  public String manager(Model model, RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      String countryCode = consortiumService.authorizedTsl();
      String countryName = PropertiesUtils.extractCountry(countryCode, properties).getName();

      model.addAttribute("territoryCode", countryCode);
      model.addAttribute("countryName", countryName);

      tslService.read(countryCode)
          .ifPresent(tsl -> model.addAttribute("tsl",
              convertToDTO(model, redirectAttributes, countryName, tsl)));

      return "manager/home";
    }, redirectAttributes);
  }

  private TslDTO convertToDTO(Model model, RedirectAttributes redirectAttributes,
      String countryName, TrustStatusListType tsl) {
    Map<String, String> urls = createMapUrls(model, redirectAttributes, tsl);
    return new TslDTO(
        countryName,
        DateUtils.localDateTimeToPrettyString(
            tsl.getSchemeInformation().getListIssueDateTime()),
        "TL - " + tsl.getSchemeInformation().getSchemeTerritory().getValue(),
        DateUtils.localDateTimeToPrettyString(tsl.getSchemeInformation().getNextUpdate()),
        tsl.getSchemeInformation().getTslSequenceNumber(),
        "VALID",
        tsl.getSchemeInformation().getSchemeTerritory().getValue(),
        tsl.getSchemeInformation().getTslType().getValue(),
        urls);
  }

  private Map<String, String> createMapUrls(Model model, RedirectAttributes redirectAttributes,
      TrustStatusListType tsl) {
    Map<String, String> urls = new HashMap<>();
    String exportUrl = MvcUriComponentsBuilder
        .fromMethodName(ApiTslImporterController.class, "exportXml",
            tsl.getSchemeInformation().getSchemeTerritory().getValue()).build().getPath();
    System.out.println("------------------------------------------------------------\n"
        + "exportUrl=" + exportUrl + "\n"
        + "------------------------------------------------------------\n" );
    urls.put("export", exportUrl);
    return urls;
  }

}
