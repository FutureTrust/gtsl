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
import eu.futuretrust.gtsl.ledger.vo.Proposal;
import java.math.BigInteger;
import java.util.List;
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
@RequestMapping("/proposals")
public class ProposalsController {

  private final WebappService webAppService;
  private final ConsortiumService consortiumService;

  @Autowired
  public ProposalsController(
      @Qualifier("webappAuthorized") WebappService webAppService,
      ConsortiumService consortiumService) {
    this.webAppService = webAppService;
    this.consortiumService = consortiumService;
  }

  @GetMapping()
  public String proposals(Model model, RedirectAttributes redirectAttributes) {
    return webAppService.execute(() -> {
      List<Proposal> proposals = consortiumService.findAllProposals();
      proposals.sort((p1, p2) -> p2.getIndex().compareTo(p1.getIndex()));
      model.addAttribute("proposals", proposals);
      return "manager/proposals";
    }, redirectAttributes);
  }

  @RequestMapping(value = "vote", method = RequestMethod.POST)
  public String vote(@RequestParam("proposalId") BigInteger proposalId,
      @RequestParam("inSupport") boolean inSupport, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute(() -> {
      consortiumService.vote(proposalId, inSupport);
      model.addAttribute("proposalId", proposalId);
      model.addAttribute("inSupport", inSupport);
      return "manager/proposals/vote_success";
    }, redirectAttributes);
  }

  @RequestMapping(value = "execute", method = RequestMethod.POST)
  public String execute(@RequestParam("proposalId") BigInteger proposalId, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute(() -> {
      consortiumService.executeProposal(proposalId);
      model.addAttribute("proposalId", proposalId);
      return "manager/proposals/execute_success";
    }, redirectAttributes);
  }

}
