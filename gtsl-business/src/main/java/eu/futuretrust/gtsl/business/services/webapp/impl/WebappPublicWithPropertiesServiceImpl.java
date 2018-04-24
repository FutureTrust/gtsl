package eu.futuretrust.gtsl.business.services.webapp.impl;

import eu.futuretrust.gtsl.business.services.rules.RulesPropertiesService;
import eu.futuretrust.gtsl.business.services.webapp.WebappWithPropertiesService;
import eu.futuretrust.gtsl.business.wrappers.ThrowingFunction;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import eu.futuretrust.gtsl.properties.RulesProperties;
import java.security.InvalidParameterException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service("webappPublicWithProperties")
public class WebappPublicWithPropertiesServiceImpl implements WebappWithPropertiesService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(WebappPublicWithPropertiesServiceImpl.class);

  private final RulesPropertiesService rulesPropertiesService;

  @Autowired
  public WebappPublicWithPropertiesServiceImpl(RulesPropertiesService rulesPropertiesService) {
    this.rulesPropertiesService = rulesPropertiesService;
  }

  @Override
  public String execute(ThrowingFunction<RulesProperties, String, Exception> function,
      RedirectAttributes redirectAttributes) {
    try {
      Optional<RulesProperties> rulesProperties = rulesPropertiesService.retrieve();
      if (rulesProperties.isPresent()) {
        return function.apply(rulesProperties.get());
      } else {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error("Unable to retrieve the rules properties");
        }
        return "redirect:/error/properties";
      }
    } catch (UnauthorizedException e) {
      redirectAttributes
          .addFlashAttribute("errorMessage", "You are not authorized to proceed this action.");
      redirectAttributes.addFlashAttribute("message", e.getMessage());
      return "redirect:/error";
    } catch (InvalidParameterException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(e.getMessage());
      }
      redirectAttributes.addFlashAttribute("errorMessage", "The provided input is invalid.");
      redirectAttributes.addFlashAttribute("message", e.getMessage());
      return "redirect:/error";
    } catch (Exception e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(e.getMessage());
      }
      redirectAttributes.addFlashAttribute("errorMessage", "An error occurred on the server.");
      redirectAttributes.addFlashAttribute("message", e.getMessage());
      return "redirect:/error";
    }
  }

}
