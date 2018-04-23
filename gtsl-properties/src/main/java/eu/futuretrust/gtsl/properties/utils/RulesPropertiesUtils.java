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

package eu.futuretrust.gtsl.properties.utils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public final class RulesPropertiesUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(RulesPropertiesUtils.class);

  public static <T> Set<T> getPropertiesAsSet(Object properties, Class<T> classResult) {
    Set<T> result = new HashSet<>();
    for (Field field : properties.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      T value = null;
      try {
        value = safeCast(field.get(properties), classResult);
      } catch (IllegalAccessException e) {
        LOGGER.warn("Unable to get field "+field+" : "+e.getMessage());
      }
      if (value != null) {
        result.add(value);
      }
    }
    return result;
  }

  private static <T> T safeCast(Object obj, Class<T> clazz) {
    try {
      return clazz.cast(obj);
    } catch (ClassCastException e) {
      return null;
    }
  }

}
