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
import javax.xml.datatype.XMLGregorianCalendar;

public final class XmlGregorianCalendarUtils {

  /**
   * Private constructor to prevent instantiation.
   */
  private XmlGregorianCalendarUtils() {
  }

  /**
   * Converts from an XMLGregorianCalendar to a LocalDateTime i.e. represents a date and time without timezone
   * inform.ation
   *
   * @param calendar XMLGregorianCalendar object.
   * @return The calendar converted to a LocalDateTime.
   */
  public static LocalDateTime toLocalDateTime(XMLGregorianCalendar calendar) {
    if (calendar == null) {
      return null;
    }
    int year = calendar.getYear() > 0 ? calendar.getYear() : 0;
    int hour = calendar.getHour() > 0 ? calendar.getHour() : 0;
    int minute = calendar.getMinute() > 0 ? calendar.getMinute() : 0;
    int second = calendar.getSecond() > 0 ? calendar.getSecond() : 0;
    int millisecond = calendar.getMillisecond() > 0 ? calendar.getMillisecond() : 0;

    return LocalDateTime.of(year, calendar.getMonth(), calendar.getDay(), hour, minute, second, millisecond);
  }

}
