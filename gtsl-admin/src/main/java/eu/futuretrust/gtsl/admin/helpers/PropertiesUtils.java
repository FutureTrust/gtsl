package eu.futuretrust.gtsl.admin.helpers;

import eu.futuretrust.gtsl.properties.RulesProperties;
import eu.futuretrust.gtsl.properties.rules.Country;
import java.util.TreeSet;

public final class PropertiesUtils {

  private PropertiesUtils() {}

  public static Country extractCountry(String countryCode, RulesProperties properties)
      throws Exception {
    if (properties != null && properties.getCountries() != null) {
      Country country = properties.getCountries().get(countryCode);
      if (country != null) {
        // sorting languages
        country.setLang(new TreeSet<>(country.getLang()));
        return country;
      }
    }
    throw new Exception(
        "The country for which you are authorized is not present in the rules properties");
  }

}
