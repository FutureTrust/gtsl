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

import eu.futuretrust.gtsl.business.properties.IPFSProperties;
import eu.futuretrust.gtsl.business.properties.MailProperties;
import eu.futuretrust.gtsl.business.properties.helper.Cron;
import eu.futuretrust.gtsl.business.scheduled.NotificationScheduler;
import eu.futuretrust.gtsl.business.scheduled.PinAllScheduler;
import eu.futuretrust.gtsl.business.scheduled.PinCurrentScheduler;
import eu.futuretrust.gtsl.business.services.wallet.WalletService;
import eu.futuretrust.gtsl.business.services.webapp.WebappService;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/configuration")
public class ConfigurationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationController.class);

  private final WebappService webAppService;

  private final WalletService walletService;

  private final MailProperties mailProperties;
  private final IPFSProperties ipfsProperties;

  private final PinAllScheduler pinAllScheduler;
  private final PinCurrentScheduler pinCurrentScheduler;
  private final NotificationScheduler notificationScheduler;


  @Autowired
  public ConfigurationController(
      @Qualifier("webappAuthorized") WebappService webAppService,
      WalletService walletService,
      MailProperties mailProperties,
      IPFSProperties ipfsProperties,
      PinAllScheduler pinAllScheduler,
      PinCurrentScheduler pinCurrentScheduler,
      NotificationScheduler notificationScheduler) {
    this.webAppService = webAppService;
    this.walletService = walletService;
    this.mailProperties = mailProperties;
    this.ipfsProperties = ipfsProperties;
    this.pinAllScheduler = pinAllScheduler;
    this.pinCurrentScheduler = pinCurrentScheduler;
    this.notificationScheduler = notificationScheduler;
  }

  @GetMapping()
  public String configuration(@ModelAttribute("successMessage") String successMessage, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute(() -> {
      model.addAttribute("cronNotifications", mailProperties.getCron());
      model.addAttribute("cronPinCurrent", ipfsProperties.getCronCurrent());
      model.addAttribute("cronPinAll", ipfsProperties.getCronAll());
      model.addAttribute("successMessage", successMessage);
      walletService.retrieve()
          .ifPresent(wallet -> model.addAttribute("address", wallet.getAddress()));
      return "manager/configuration";
    }, redirectAttributes);
  }

  @PostMapping(value = "/notifications")
  public String submitCronNotifications(@ModelAttribute("cronNotifications") Cron cronInput,
      RedirectAttributes redirectAttributes) {
    return updateCron(cron -> {
      notificationScheduler.reSchedule(cron);
      mailProperties.getCron().updateProperly(cron);
    }, cronInput, redirectAttributes, "notifications");
  }

  @PostMapping(value = "/pin/current")
  public String submitCronPinCurrent(@ModelAttribute("cronPinCurrent") Cron cronInput,
      RedirectAttributes redirectAttributes) {
    return updateCron(cron -> {
      pinCurrentScheduler.reSchedule(cron);
      ipfsProperties.getCronCurrent().updateProperly(cron);
    }, cronInput, redirectAttributes, "pinning current");
  }

  @PostMapping(value = "/pin/all")
  public String submitCronPinAll(@ModelAttribute("cronPinAll") Cron cronInput,
      RedirectAttributes redirectAttributes) {
    return updateCron(cron -> {
      pinAllScheduler.reSchedule(cron);
      ipfsProperties.getCronAll().updateProperly(cron);
    }, cronInput, redirectAttributes, "pinning all versions");
  }

  private String updateCron(Consumer<Cron> cronConsumer, Cron cron,
      RedirectAttributes redirectAttributes, String cronName) {
    return webAppService.execute(() -> {
      cronConsumer.accept(cron);
      redirectAttributes.addFlashAttribute("successMessage",
          "You have successfully updated the " + cronName + " configuration");
      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("CronJob {} has been updated to {}, {}", cronName, cron.getValue(),
            cron.isEnabled() ? "enabled" : "disabled");
      }
      return "redirect:/configuration";
    }, redirectAttributes);
  }

}
