package eu.futuretrust.gtsl.business.services.webapp;

import eu.futuretrust.gtsl.business.wrappers.ThrowingSupplier;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface WebappService {

  String execute(ThrowingSupplier<String, Exception> supplier,
      RedirectAttributes redirectAttributes);

}
