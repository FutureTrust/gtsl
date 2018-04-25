package eu.futuretrust.gtsl.business.services.validator.rules.signature.impl;

import eu.futuretrust.gtsl.business.services.validator.rules.signature.X500NameValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.MultiLangNormStringType;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import sun.security.x509.X500Name;

@Service
public class X500NameValidatorImpl implements X500NameValidator {

  @Override
  public void isX500NameCountryCodeValid(ValidationContext context,
                                         ViolationConstant violationConstant,
                                         X500Name name,
                                         String schemeTerritory)
  {
    try {
      if (schemeTerritory.equalsIgnoreCase("EU")) {
        return;
      }
      else if (!schemeTerritory.equalsIgnoreCase(name.getCountry())) {
        context.addViolation(new Violation(violationConstant));
      }
    } catch (final IOException e) {
      context.addViolation(new Violation(ViolationConstant.IS_SIGNING_CERTIFICATE_VALID));
    }
  }

  @Override
  public void isX500NameOrganizationValid(ValidationContext context,
                                          ViolationConstant violationConstant,
                                          X500Name name,
                                          List<MultiLangNormStringType> orgNames)
  {
    try
    {
      for (final MultiLangNormStringType value : orgNames) {
        if (value.getValue().equalsIgnoreCase(name.getOrganization())) {
          return;
        }
      }
      context.addViolation(new Violation(violationConstant));
    } catch (final IOException e) {
      context.addViolation(new Violation(ViolationConstant.IS_SIGNING_CERTIFICATE_VALID));
    }
  }
}
