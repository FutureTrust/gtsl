package eu.futuretrust.gtsl.business.utils;

import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.security.InvalidParameterException;
import org.apache.commons.lang.StringUtils;

public final class TslUtils {

  private TslUtils() {
  }

  public static String extractTerritoryCode(TrustStatusListType tsl)
      throws InvalidParameterException {
    if (tsl == null
        || tsl.getSchemeInformation() == null
        || tsl.getSchemeInformation().getSchemeTerritory() == null
        || tsl.getSchemeInformation().getSchemeTerritory().getValue() == null) {
      throw new InvalidParameterException("The Scheme Territory cannot be retrieved, a value is a null");
    }
    return tsl.getSchemeInformation().getSchemeTerritory().getValue();
  }

  public static void checkDraftId(String dbId) throws InvalidParameterException {
    if (StringUtils.isBlank(dbId)) {
      throw new InvalidParameterException("The draft id is invalid: null or empty");
    }
  }

  public static void checkTerritoryCode(String territoryCode) throws InvalidParameterException {
    if (StringUtils.isBlank(territoryCode)) {
      throw new InvalidParameterException("The territoryCode is invalid: null or empty");
    }
  }

}
