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

package eu.futuretrust.gtsl.admin.controllers.webapp.error;

import eu.futuretrust.gtsl.admin.controllers.webapp.manager.RulesPropertiesController;
import eu.futuretrust.gtsl.business.services.ledger.ConsortiumService;
import eu.futuretrust.gtsl.business.services.webapp.WebappService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/error/properties")
public class ErrorPropertiesController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorPropertiesController.class);

  private final WebappService webAppService;
  private final ConsortiumService consortiumService;

  @Autowired
  public ErrorPropertiesController(@Qualifier("webappPublic") WebappService webAppService,
      ConsortiumService consortiumService) {
    this.webAppService = webAppService;
    this.consortiumService = consortiumService;
  }

  @GetMapping()
  public String error(@ModelAttribute("errorMessage") String errorMessage, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute(() -> {
      model.addAttribute("errorMessage", errorMessage);
      model.addAttribute("isAuthorized", consortiumService.isAuthorized());
      model.addAttribute("url", MvcUriComponentsBuilder
          .fromMethodName(RulesPropertiesController.class, "properties", model,
              redirectAttributes)
          .build()
          .getPath());
      return "error/error_properties";
    }, redirectAttributes);
  }

}
