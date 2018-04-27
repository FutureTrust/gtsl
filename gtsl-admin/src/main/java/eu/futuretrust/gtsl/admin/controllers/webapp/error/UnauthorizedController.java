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

import eu.futuretrust.gtsl.business.services.wallet.WalletService;
import eu.futuretrust.gtsl.business.services.webapp.WebappService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/unauthorized")
public class UnauthorizedController {

  private final WebappService webAppService;
  private final WalletService walletService;

  @Autowired
  public UnauthorizedController(
      @Qualifier("webappPublic") WebappService webAppService,
      WalletService walletService) {
    this.webAppService = webAppService;
    this.walletService = walletService;
  }

  @GetMapping()
  public String unauthorized(Model model, RedirectAttributes redirectAttributes) {
    return webAppService.execute(() -> {
      walletService.retrieve()
          .ifPresent(wallet -> model.addAttribute("address", wallet.getAddress()));
      return "error/unauthorized";
    }, redirectAttributes);
  }

}
