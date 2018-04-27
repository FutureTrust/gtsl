package eu.futuretrust.gtsl.business.services.webapp.impl;

import eu.futuretrust.gtsl.business.services.ledger.ConsortiumService;
import eu.futuretrust.gtsl.business.services.webapp.WebappService;
import eu.futuretrust.gtsl.business.wrappers.ThrowingSupplier;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import java.security.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service("webappAuthorized")
public class WebappAuthorizedServiceImpl implements WebappService {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebappAuthorizedServiceImpl.class);

  private final ConsortiumService consortiumService;

  @Autowired
  public WebappAuthorizedServiceImpl(ConsortiumService consortiumService) {
    this.consortiumService = consortiumService;
  }

  @Override
  public String execute(ThrowingSupplier<String, Exception> supplier,
      RedirectAttributes redirectAttributes) {
    try {
      if (consortiumService.isAuthorized()) {
        return supplier.get();
      } else {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error("You are not authorized to manage the trusted lists");
        }
        return "redirect:/unauthorized";
      }
    } catch (UnauthorizedException e) {
      redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to proceed this action.");
      redirectAttributes.addFlashAttribute("message", e.getMessage());
      return "redirect:/error";
    } catch (InvalidParameterException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("{}: {}", e.getClass(), e.getMessage());
      }
      redirectAttributes.addFlashAttribute("errorMessage", "The provided input is invalid.");
      redirectAttributes.addFlashAttribute("message", e.getMessage());
      return "redirect:/error";
    } catch (Exception e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("{}: {}", e.getClass(), e.getMessage());
      }
      redirectAttributes.addFlashAttribute("errorMessage", "An error occurred on the server.");
      redirectAttributes.addFlashAttribute("message", e.getMessage());
      return "redirect:/error";
    }
  }

}
