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

import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.business.services.webapp.WebappWithPropertiesService;
import eu.futuretrust.gtsl.model.data.ts.ServiceHistoryInstanceType;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import eu.futuretrust.gtsl.model.data.tsl.OtherTSLPointerType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.model.data.tsp.TSPType;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tl")
public class TlController {

  private final WebappWithPropertiesService webAppService;
  private final TslService tslService;

  @Autowired
  public TlController(
      @Qualifier("webappPublicWithProperties") WebappWithPropertiesService webAppService,
      TslService tslService) {
    this.webAppService = webAppService;
    this.tslService = tslService;
  }

  @GetMapping(value = "{territoryCode}")
  public String getTsl(@PathVariable String territoryCode, Model model,
      RedirectAttributes redirectAttributes) {
    return webAppService.execute((properties) -> {
      Optional<TrustStatusListType> tsl = tslService.read(territoryCode);
      if (tsl.isPresent()) {
        model.addAttribute("tsl", tsl.get());
        model.addAttribute("country", properties.getCountries().get(territoryCode).getName());
        if (tsl.get().getTrustServiceProviderList() != null && CollectionUtils
            .isNotEmpty(tsl.get().getTrustServiceProviderList().getValues())) {
          model.addAttribute("allTspAreActive",
              tsl.get().getTrustServiceProviderList().getValues().stream()
                  .allMatch(TSPType::isActive));
        }
        return "browser/tsl";
      } else {
        redirectAttributes.addFlashAttribute("errorMessage",
            "The Trust Status List " + territoryCode + " has not been found.");
        return "redirect:/error";
      }
    }, redirectAttributes);
  }

  @GetMapping(value = "{territoryCode}/pointer/{index}")
  public String getPointer(@PathVariable String territoryCode, @PathVariable Integer index,
      Model model, RedirectAttributes redirectAttributes) {
    if (index < 0) {
      redirectAttributes
          .addFlashAttribute("errorMessage", "Invalid request pointer index is lower than 0");
      return "redirect:/error";
    }

    return webAppService.execute((properties) -> {
      Optional<TrustStatusListType> tsl = tslService.read(territoryCode);
      if (tsl.isPresent()) {
        if (this.pointerExists(index, tsl.get())) {
          OtherTSLPointerType pointer = tsl.get().getSchemeInformation().getPointersToOtherTSL()
              .getValues().get(index);
          if (this.isPointerValid(pointer)) {
            model.addAttribute("pointer", pointer);
            model.addAttribute("country", properties.getCountries().get(territoryCode).getName());
            model.addAttribute("territoryCode", territoryCode);
            return "browser/pointer";
          } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Pointer is invalid.");
            return "redirect:/error";
          }
        } else {
          redirectAttributes.addFlashAttribute("errorMessage", "Pointer has not been found.");
          return "redirect:/error";
        }
      } else {
        redirectAttributes.addFlashAttribute("errorMessage",
            "The Trust Status List " + territoryCode + " has not been found.");
        return "redirect:/error";
      }
    }, redirectAttributes);
  }

  @GetMapping(value = "{territoryCode}/{index}")
  public String getTsp(@PathVariable String territoryCode, @PathVariable Integer index,
      Model model, RedirectAttributes redirectAttributes) {
    if (index < 0) {
      redirectAttributes
          .addFlashAttribute("errorMessage", "Invalid request tsp index is lower than 0");
      return "redirect:/error";
    }

    return webAppService.execute((properties) -> {
      Optional<TrustStatusListType> tsl = tslService.read(territoryCode);
      if (tsl.isPresent()) {
        if (this.tspExists(index, tsl.get())) {
          TSPType tsp = tsl.get().getTrustServiceProviderList().getValues().get(index);
          if (this.isTspValid(tsp)) {
            model.addAttribute("tsp", tsp);
            model.addAttribute("country", properties.getCountries().get(territoryCode).getName());
            model.addAttribute("territoryCode", territoryCode);
            model.addAttribute("index", index);
            return "browser/tsp";
          } else {
            redirectAttributes
                .addFlashAttribute("errorMessage", "Trust service provider is invalid");
            return "redirect:/error";
          }
        } else {
          redirectAttributes
              .addFlashAttribute("errorMessage", "Trust service provider has not been found");
          return "redirect:/error";
        }
      } else {
        redirectAttributes.addFlashAttribute("errorMessage",
            "The Trust Status List " + territoryCode + " has not been found.");
        return "redirect:/error";
      }
    }, redirectAttributes);
  }

  @GetMapping(value = "{territoryCode}/{indexTsp}/{indexService}")
  public String getService(@PathVariable String territoryCode, @PathVariable Integer indexTsp,
      @PathVariable Integer indexService, Model model, RedirectAttributes redirectAttributes) {
    if (indexTsp < 0) {
      redirectAttributes
          .addFlashAttribute("errorMessage", "Invalid request tsp index is lower than 0");
      return "redirect:/error";
    }
    if (indexService < 0) {
      redirectAttributes
          .addFlashAttribute("errorMessage", "Invalid request service index is lower than 0");
      return "redirect:/error";
    }

    return webAppService.execute((properties) -> {
      Optional<TrustStatusListType> tsl = tslService.read(territoryCode);
      if (tsl.isPresent()) {
        if (this.tspExists(indexTsp, tsl.get())) {
          TSPType tsp = tsl.get().getTrustServiceProviderList().getValues().get(indexTsp);
          if (this.isTspValid(tsp)) {
            if (this.serviceExists(indexService, tsp)) {
              TSPServiceType service = tsp.getTspServices().getValues().get(indexService);
              if (this.isServiceValid(service)) {
                model.addAttribute("tspName",
                    tsp.getTspInformation().getTspName().getValues().get(0).getValue());
                model.addAttribute("service", service);
                model.addAttribute("country",
                    properties.getCountries().get(territoryCode).getName());
                model.addAttribute("territoryCode", territoryCode);
                model.addAttribute("indexTsp", indexTsp);
                model.addAttribute("indexService", indexService);
                return "browser/service";
              } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Trust service is invalid");
                return "redirect:/error";
              }
            } else {
              redirectAttributes.addFlashAttribute("errorMessage", "Trust service has not been found");
              return "redirect:/error";
            }
          } else {
            redirectAttributes
                .addFlashAttribute("errorMessage", "Trust service provider is invalid");
            return "redirect:/error";
          }
        } else {
          redirectAttributes
              .addFlashAttribute("errorMessage", "Trust service provider has not been found");
          return "redirect:/error";
        }
      } else {
        redirectAttributes.addFlashAttribute("errorMessage",
            "The Trust Status List " + territoryCode + " has not been found.");
        return "redirect:/error";
      }
    }, redirectAttributes);
  }

  @GetMapping(value = "{territoryCode}/{indexTsp}/{indexService}/{indexHistory}")
  public String getHistory(@PathVariable String territoryCode, @PathVariable Integer indexTsp,
      @PathVariable Integer indexService, @PathVariable Integer indexHistory, Model model,
      RedirectAttributes redirectAttributes) {
    if (indexTsp < 0) {
      redirectAttributes
          .addFlashAttribute("errorMessage", "Invalid request tsp index is lower than 0");
      return "redirect:/error";
    }
    if (indexService < 0) {
      redirectAttributes
          .addFlashAttribute("errorMessage", "Invalid request service index is lower than 0");
      return "redirect:/error";
    }
    if (indexHistory < 0) {
      redirectAttributes.addFlashAttribute("errorMessage",
          "Invalid request service history index is lower than 0");
      return "redirect:/error";
    }

    return webAppService.execute((properties) -> {
      Optional<TrustStatusListType> tsl = tslService.read(territoryCode);
      if (tsl.isPresent()) {
        if (this.tspExists(indexTsp, tsl.get())) {
          TSPType tsp = tsl.get().getTrustServiceProviderList().getValues().get(indexTsp);
          if (this.isTspValid(tsp)) {
            if (this.serviceExists(indexService, tsp)) {
              TSPServiceType service = tsp.getTspServices().getValues().get(indexService);
              if (this.isServiceValid(service)) {
                if (this.historyExists(indexHistory, service)) {
                  ServiceHistoryInstanceType history = service.getServiceHistory().getValues()
                      .get(indexHistory);
                  if (this.isHistoryValid(history)) {
                    model.addAttribute("tspName",
                        tsp.getTspInformation().getTspName().getValues().get(0).getValue());
                    model.addAttribute("serviceName",
                        service.getServiceInformation().getServiceName().getValues().get(0)
                            .getValue());
                    model.addAttribute("history", history);
                    model.addAttribute("country",
                        properties.getCountries().get(territoryCode).getName());
                    model.addAttribute("territoryCode", territoryCode);
                    model.addAttribute("indexTsp", indexTsp);
                    model.addAttribute("indexService", indexService);
                    model.addAttribute("indexHistory", indexHistory);
                    return "browser/history";
                  } else {
                    redirectAttributes
                        .addFlashAttribute("errorMessage", "Trust service history is invalid");
                    return "redirect:/error";
                  }
                } else {
                  redirectAttributes
                      .addFlashAttribute("errorMessage", "Trust service history has not been found");
                  return "redirect:/error";
                }
              } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Trust service is invalid");
                return "redirect:/error";
              }
            } else {
              redirectAttributes.addFlashAttribute("errorMessage", "Trust service has not been found");
              return "redirect:/error";
            }
          } else {
            redirectAttributes
                .addFlashAttribute("errorMessage", "Trust service provider is invalid");
            return "redirect:/error";
          }
        } else {
          redirectAttributes
              .addFlashAttribute("errorMessage", "Trust service provider has not been found");
          return "redirect:/error";
        }
      } else {
        redirectAttributes.addFlashAttribute("errorMessage",
            "The Trust Status List " + territoryCode + " has not been found.");
        return "redirect:/error";
      }
    }, redirectAttributes);
  }

  private boolean isPointerValid(OtherTSLPointerType pointer) {
    return pointer != null && pointer.getMimeType() != null
        && pointer.getSchemeTerritory() != null
        && pointer.getSchemeTerritory().getValue() != null;
  }

  private boolean pointerExists(@PathVariable Integer index, TrustStatusListType tsl) {
    return tsl.getSchemeInformation() != null
        && tsl.getSchemeInformation().getPointersToOtherTSL() != null && CollectionUtils
        .isNotEmpty(tsl.getSchemeInformation().getPointersToOtherTSL().getValues())
        && index < tsl.getSchemeInformation().getPointersToOtherTSL().getValues()
        .size();
  }

  private boolean isTspValid(TSPType tsp) {
    return tsp != null && tsp.getTspInformation() != null
        && tsp.getTspInformation().getTspName() != null
        && CollectionUtils.isNotEmpty(tsp.getTspInformation().getTspName().getValues());
  }

  private boolean tspExists(@PathVariable Integer index, TrustStatusListType tsl) {
    return tsl.getTrustServiceProviderList() != null
        && CollectionUtils.isNotEmpty(tsl.getTrustServiceProviderList().getValues())
        && index < tsl.getTrustServiceProviderList().getValues().size();
  }

  private boolean isServiceValid(TSPServiceType service) {
    return service != null && service.getServiceInformation() != null
        && service.getServiceInformation().getServiceName() != null
        && CollectionUtils.isNotEmpty(service.getServiceInformation().getServiceName().getValues());
  }

  private boolean serviceExists(@PathVariable Integer index, TSPType tsp) {
    return tsp.getTspServices() != null
        && CollectionUtils.isNotEmpty(tsp.getTspServices().getValues())
        && index < tsp.getTspServices().getValues().size();
  }

  private boolean isHistoryValid(ServiceHistoryInstanceType history) {
    return history != null
        && history.getServiceName() != null
        && CollectionUtils.isNotEmpty(history.getServiceName().getValues());
  }

  private boolean historyExists(@PathVariable Integer index, TSPServiceType service) {
    return service.getServiceHistory() != null
        && CollectionUtils.isNotEmpty(service.getServiceHistory().getValues())
        && index < service.getServiceHistory().getValues().size();
  }

}
