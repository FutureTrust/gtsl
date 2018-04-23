package eu.futuretrust.gtsl.business.services.webapp.impl;

import eu.futuretrust.gtsl.business.services.webapp.WebappService;
import eu.futuretrust.gtsl.business.wrappers.ThrowingSupplier;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import java.security.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service("webappPublic")
public class WebappPublicServiceImpl implements WebappService {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebappPublicServiceImpl.class);

  @Override
  public String execute(ThrowingSupplier<String, Exception> supplier,
      RedirectAttributes redirectAttributes) {
    try {
      return supplier.get();
    } catch (UnauthorizedException e) {
      redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to proceed this action.");
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
        e.printStackTrace();
      }
      redirectAttributes.addFlashAttribute("errorMessage", "An error occurred on the server.");
      redirectAttributes.addFlashAttribute("message", e.getMessage());
      return "redirect:/error";
    }
  }

}
