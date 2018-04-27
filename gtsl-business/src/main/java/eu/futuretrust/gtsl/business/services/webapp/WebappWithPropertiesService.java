package eu.futuretrust.gtsl.business.services.webapp;

import eu.futuretrust.gtsl.business.wrappers.ThrowingFunction;
import eu.futuretrust.gtsl.properties.RulesProperties;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface WebappWithPropertiesService {

  String execute(ThrowingFunction<RulesProperties, String, Exception> function,
      RedirectAttributes redirectAttributes);

}
