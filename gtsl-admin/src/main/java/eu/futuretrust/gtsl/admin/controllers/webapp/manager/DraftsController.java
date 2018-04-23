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


import eu.futuretrust.gtsl.admin.binder.ByteArrayBase64Editor;
import eu.futuretrust.gtsl.admin.controllers.api.ApiDraftImporterController;
import eu.futuretrust.gtsl.admin.helpers.PropertiesUtils;
import eu.futuretrust.gtsl.business.dto.drafts.DraftDTO;
import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.dto.tsl.TslDTO;
import eu.futuretrust.gtsl.business.services.draft.DraftService;
import eu.futuretrust.gtsl.business.services.ledger.ConsortiumService;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.business.services.webapp.WebappWithPropertiesService;
import eu.futuretrust.gtsl.business.services.xml.DraftImporter;
import eu.futuretrust.gtsl.business.vo.draft.DraftVO;
import eu.futuretrust.gtsl.model.data.common.CountryCode;
import eu.futuretrust.gtsl.model.data.enums.MimeType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.model.utils.DateUtils;
import eu.futuretrust.gtsl.properties.rules.Country;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/drafts")
public class DraftsController {

  private final WebappWithPropertiesService webAppService;
  private final ConsortiumService consortiumService;
  private final TslService tslService;
  private final DraftService draftService;
  private final DraftImporter draftImporter;

  @Autowired
  public DraftsController(
      @Qualifier("webappAuthorizedWithProperties") WebappWithPropertiesService webAppService,
      ConsortiumService consortiumService,
      TslService tslService,
      DraftService draftService,
      DraftImporter draftImporter) {
    this.consortiumService = consortiumService;
    this.webAppService = webAppService;
    this.tslService = tslService;
    this.draftService = draftService;
    this.draftImporter = draftImporter;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(byte[].class, new ByteArrayBase64Editor());
  }

  @GetMapping()
  public String drafts(Model model, RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      String countryCode = consortiumService.authorizedTsl();
      String countryName = PropertiesUtils.extractCountry(countryCode, properties).getName();
      List<DraftDTO> draftsDTO = createTslDTO(model, redirectAttributes, countryName,
          draftService.readAll(countryCode));

      return fillDrafts(model, draftsDTO);
    }, redirectAttributes);
  }

  @GetMapping(value = "/new")
  public String newDraft(Model model, RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      String countryCode = consortiumService.authorizedTsl();
      Country country = PropertiesUtils.extractCountry(countryCode, properties);
      TrustStatusListType tsl = new TrustStatusListType();
      tsl.getSchemeInformation().setSchemeTerritory(new CountryCode(countryCode));
      String action = MvcUriComponentsBuilder
          .fromMethodName(DraftsController.class, "createDraft",
              tsl, model, redirectAttributes).build().toString();

      return fillDraft(model, country, tsl, action);
    }, redirectAttributes);
  }

  @GetMapping(value = "{dbId}")
  public String editDraft(@PathVariable("dbId") String dbId, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      String countryCode = consortiumService.authorizedTsl();
      Country country = PropertiesUtils.extractCountry(countryCode, properties);
      DraftVO draft = draftService.read(dbId)
          .orElseThrow(() -> new InvalidParameterException("Draft not found"));
      draft.getTsl().getSchemeInformation().setSchemeTerritory(new CountryCode(countryCode));
      String action = MvcUriComponentsBuilder
          .fromMethodName(DraftsController.class, "updateDraft",
              draft.getDbId(), draft.getTsl(), model, redirectAttributes).build().toString();

      return fillDraft(model, country, draft.getTsl(), action);
    }, redirectAttributes);
  }

  @RequestMapping(value = "import", method = RequestMethod.POST)
  public String importXml(@RequestParam("fileToUpload") MultipartFile file, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      ReportDTO report = draftImporter.importTsl(file);
      return fillReport(model, report,
          "The Trusted List can be pushed into production.",
          "The Trusted List cannot be pushed into production because it contains error(s).");
    }, redirectAttributes);
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public String createDraft(@ModelAttribute TrustStatusListType tsl, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      // set the issue time to now
      tsl.getSchemeInformation().setListIssueDateTime(LocalDateTime.now());
      ReportDTO report = draftService.create(tsl);
      return fillReport(model, report,
          "The Trusted List can be pushed into production.",
          "The Trusted List cannot be pushed into production because it contains error(s).");
    }, redirectAttributes);
  }

  @RequestMapping(value = "/update/{dbId}", method = RequestMethod.POST)
  public String updateDraft(@PathVariable("dbId") String dbId,
      @ModelAttribute TrustStatusListType tsl, Model model, RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      // set the issue time to now
      tsl.getSchemeInformation().setListIssueDateTime(LocalDateTime.now());
      ReportDTO report = draftService.update(dbId, tsl);
      return fillReport(model, report,
          "The Trusted List can be pushed into production.",
          "The Trusted List cannot be pushed into production because it contains error(s).");
    }, redirectAttributes);
  }

  @RequestMapping(value = "/delete/{dbId}", method = RequestMethod.POST)
  public String deleteDraft(@PathVariable("dbId") String dbId,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      draftService.delete(dbId);
      return "redirect:/drafts";
    }, redirectAttributes);
  }

  @RequestMapping(value = "/push/{dbId}", method = RequestMethod.POST)
  public String pushDraft(@PathVariable("dbId") String dbId, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      DraftVO draft = draftService.read(dbId)
          .orElseThrow(() -> new InvalidParameterException("Draft not found"));
      ReportDTO report = tslService.push(draft);
      return fillReport(model, report,
          "The Trusted List has been pushed into production.",
          "The Trusted List has not been pushed into production because it contains error(s).");
    }, redirectAttributes);
  }

  @GetMapping(value = "/validate/{dbId}")
  public String validateDraft(@PathVariable("dbId") String dbId, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      ReportDTO report = draftService.validate(dbId)
          .orElseThrow(() -> new InvalidParameterException("Draft not found"));
      return fillReport(model, report,
          "The Trusted List can be pushed into production.",
          "The Trusted List cannot be pushed into production because it contains error(s).");
    }, redirectAttributes);
  }

  private String fillDrafts(Model model, List<DraftDTO> draftsDTO) {
    model.addAttribute("drafts", draftsDTO);
    return "manager/drafts";
  }

  private String fillDraft(Model model, Country country, TrustStatusListType tsl, String action) {
    model.addAttribute("country", country);
    model.addAttribute("mimeTypes", MimeType.values());
    model.addAttribute("tsl", tsl);
    model.addAttribute("action", action);
    return "manager/draft";
  }

  private String fillReport(Model model, ReportDTO report, String successMessage,
      String errorMessage) {
    model.addAttribute("report", report.getReport());
    model.addAttribute("valid", report.isValid());
    model.addAttribute("successMessage", successMessage);
    model.addAttribute("errorMessage", errorMessage);
    return "manager/report";
  }

  private List<DraftDTO> createTslDTO(Model model, RedirectAttributes redirectAttributes,
      String countryName, List<DraftVO> drafts) {
    return drafts.stream().map(draftVO -> {
      Map<String, String> urls = createMapUrls(model, redirectAttributes, draftVO);
      TslDTO tslDTO = new TslDTO(
          countryName,
          DateUtils.localDateTimeToPrettyString(
              draftVO.getTsl().getSchemeInformation().getListIssueDateTime()),
          "TL - " + draftVO.getTsl().getSchemeInformation().getSchemeTerritory()
              .getValue(),
          DateUtils.localDateTimeToPrettyString(
              draftVO.getTsl().getSchemeInformation().getNextUpdate()),
          draftVO.getTsl().getSchemeInformation().getTslSequenceNumber(),
          "VALID",
          draftVO.getTsl().getSchemeInformation().getSchemeTerritory().getValue(),
          draftVO.getTsl().getSchemeInformation().getTslType().getValue(),
          urls);
      return new DraftDTO(draftVO.getDbId(), tslDTO);
    }).collect(Collectors.toList());
  }

  private Map<String, String> createMapUrls(Model model, RedirectAttributes redirectAttributes,
      DraftVO draftVO) {
    Map<String, String> urls = new HashMap<>();
    urls.put("edit", MvcUriComponentsBuilder
        .fromMethodName(DraftsController.class, "editDraft",
            draftVO.getDbId(), model, redirectAttributes).build().toString());
    urls.put("export", MvcUriComponentsBuilder
        .fromMethodName(ApiDraftImporterController.class, "exportXml",
            draftVO.getDbId()).build().toString());
    urls.put("delete", MvcUriComponentsBuilder
        .fromMethodName(DraftsController.class, "deleteDraft",
            draftVO.getDbId(), redirectAttributes).build().toString());
    urls.put("push", MvcUriComponentsBuilder
        .fromMethodName(DraftsController.class, "pushDraft",
            draftVO.getDbId(), model, redirectAttributes).build().toString());
    urls.put("validate", MvcUriComponentsBuilder
        .fromMethodName(DraftsController.class, "validateDraft",
            draftVO.getDbId(), model, redirectAttributes).build().toString());
    return urls;
  }

}
