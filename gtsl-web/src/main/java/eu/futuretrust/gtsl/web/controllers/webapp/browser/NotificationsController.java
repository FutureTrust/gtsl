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
import eu.futuretrust.gtsl.business.services.notifications.NotificationService;
import eu.futuretrust.gtsl.business.services.webapp.WebappWithPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notifications")
public class NotificationsController {

  private final WebappWithPropertiesService webAppService;
  private final NotificationService notificationService;

  @Autowired
  public NotificationsController(
      @Qualifier("webappPublicWithProperties") WebappWithPropertiesService webAppService,
      NotificationService notificationService) {
    this.webAppService = webAppService;
    this.notificationService = notificationService;
  }

  @PostMapping(value = "{territoryCode}")
  public String notificationSubmit(@PathVariable String territoryCode,
      @ModelAttribute EmailDTO subscribeForm, Model model, RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      boolean valid = notificationService.subscribe(territoryCode, subscribeForm.getEmail());

      return this.fillNotificationsResult(model, territoryCode, subscribeForm.getEmail(),
          properties.getCountries().get(territoryCode).getName(), valid);
    }, redirectAttributes);
  }

  private String fillNotificationsResult(Model model, String territoryCode, String email,
      String country, boolean valid) {
    model.addAttribute("territoryCode", territoryCode);
    model.addAttribute("email", email);
    model.addAttribute("country", country);
    model.addAttribute("valid", valid);
    return "browser/notification_result";
  }

}
