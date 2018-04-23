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

import eu.futuretrust.gtsl.business.services.ledger.ConsortiumService;
import eu.futuretrust.gtsl.business.services.webapp.WebappService;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UsersController {

  private final ConsortiumService consortiumService;
  private final WebappService webAppService;

  @Autowired
  public UsersController(ConsortiumService consortiumService,
      @Qualifier("webappAuthorized") WebappService webAppService) {
    this.consortiumService = consortiumService;
    this.webAppService = webAppService;
  }

  @GetMapping()
  public String users(Model model, RedirectAttributes redirectAttributes) {
    return webAppService.execute(() -> {
      model.addAttribute("members", consortiumService.findAllMembers());
      return "manager/users";
    }, redirectAttributes);
  }

  @RequestMapping(value = "add", method = RequestMethod.POST)
  public String requestAddMember(@RequestParam("memberAddress") String memberAddress,
      @RequestParam("tslIdentifier") String tslIdentifier,
      @RequestParam("duration") BigInteger duration,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute(() -> {
      consortiumService.requestAddMember(memberAddress, tslIdentifier, duration);
      return "redirect:/proposals";
    }, redirectAttributes);
  }

  @RequestMapping(value = "remove", method = RequestMethod.POST)
  public String requestRemoveMember(@RequestParam("memberAddress") String memberAddress,
      @RequestParam("duration") BigInteger duration,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute(() -> {
      consortiumService.requestRemoveMember(memberAddress, duration);
      return "redirect:/proposals";
    }, redirectAttributes);
  }

}
