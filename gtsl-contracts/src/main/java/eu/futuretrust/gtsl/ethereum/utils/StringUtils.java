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

package eu.futuretrust.gtsl.ethereum.utils;

import eu.futuretrust.gtsl.ethereum.exceptions.BytesLengthException;

public final class StringUtils {

  private StringUtils() {
  }

  public static String safeString(byte[] bytes) {
    return bytes == null ? "" : new String(bytes).trim();
  }

  public static byte[] safeBytes(String str, int length) {
    byte[] bytes = new byte[length];
    String strTrimmed = str.trim();
    if (strTrimmed.getBytes().length > length) {
      throw new BytesLengthException("String variable must be in range 0 < length <= " + length);
    }
    System.arraycopy(strTrimmed.getBytes(), 0, bytes, 0, strTrimmed.getBytes().length);
    return bytes;
  }

  public static boolean isEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

}
