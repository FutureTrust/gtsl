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

import eu.futuretrust.gtsl.admin.controllers.api.ApiRulesPropertiesImporterController;
import eu.futuretrust.gtsl.business.services.rules.RulesPropertiesService;
import eu.futuretrust.gtsl.business.services.webapp.WebappService;
import eu.futuretrust.gtsl.properties.RulesProperties;
import java.security.InvalidParameterException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/properties")
public class RulesPropertiesController {

  private final WebappService webAppService;
  private final RulesPropertiesService rulesPropertiesService;

  @Autowired
  public RulesPropertiesController(
      @Qualifier("webappAuthorized") WebappService webAppService,
      RulesPropertiesService rulesPropertiesService) {
    this.webAppService = webAppService;
    this.rulesPropertiesService = rulesPropertiesService;
  }

  @GetMapping()
  public String properties(Model model, RedirectAttributes redirectAttributes) {
    return webAppService.execute(() -> {
      Optional<RulesProperties> properties = rulesPropertiesService.retrieve();
      if (properties.isPresent()) {
        model.addAttribute("rulesProperties", properties.get());
        model.addAttribute("export", MvcUriComponentsBuilder
            .fromMethodName(ApiRulesPropertiesImporterController.class, "exportJson")
            .build()
            .toString());
      }
      return "manager/properties";
    }, redirectAttributes);
  }

  @RequestMapping(value = "import", method = RequestMethod.POST)
  public String importJSON(@RequestParam("fileToUpload") MultipartFile file,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute(() -> {
      if (file == null || file.isEmpty()) {
        throw new InvalidParameterException("Invalid properties file: null or empty");
      }
      rulesPropertiesService.update(file.getInputStream());
      return "redirect:/properties";
    }, redirectAttributes);
  }


}
