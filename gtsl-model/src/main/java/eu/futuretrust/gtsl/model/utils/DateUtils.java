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

package eu.futuretrust.gtsl.model.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  public static final String SPRING_PATTERN = "dd/MM/yyyy HH:mm";

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter
      .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
  private static final DateTimeFormatter FORMATTER2 = DateTimeFormatter
      .ofPattern("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
  private static final DateTimeFormatter FORMATTER3 = DateTimeFormatter
      .ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  private static final DateTimeFormatter PRETTY_FORMATTER = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss");

  public static String localDateTimeToString(LocalDateTime dateTime) {
    String str = null;

    try {
      str = dateTime.format(FORMATTER);
    } catch (Exception ignored) {
    }

    if (str == null) {
      try {
        str = dateTime.format(FORMATTER2);
      } catch (Exception ignored) {
      }
    }

    if (str == null) {
      try {
        str = dateTime.format(FORMATTER3);
      } catch (Exception ignored) {
      }
    }

    return str;
  }

  public static String localDateTimeToPrettyString(LocalDateTime dateTime) {
    String str = null;
    try {
      str = dateTime.format(PRETTY_FORMATTER);
    } catch (Exception ignored) {
    }
    return str;
  }

  public static LocalDateTime stringToLocalDateTime(String str) {
    LocalDateTime date = null;

    try {
      date = LocalDateTime.parse(str, FORMATTER);
    } catch (Exception ignored) {
    }

    if (date == null) {
      try {
        date = LocalDateTime.parse(str, FORMATTER);
      } catch (Exception ignored) {
      }
    }

    if (date == null) {
      try {
        date = LocalDateTime.parse(str, FORMATTER);
      } catch (Exception ignored) {
      }
    }

    return date;
  }

}
