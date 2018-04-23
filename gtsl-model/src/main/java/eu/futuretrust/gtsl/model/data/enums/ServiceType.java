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

package eu.futuretrust.gtsl.model.data.enums;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum ServiceType {

  Q_CERT_ESIG("QCertESig", true, "Qualified certificate for electronic signature"),
  Q_CERT_ESEAL("QCertESeal", true, "Qualified certificate for electronic seal"),
  Q_CERT_WAC("QWAC", true, "Qualified certificate for website authentication"),
  Q_CERT_ROOTCA("QRootCA", true, "Qualified Root Certification Authority"),
  Q_VAL_ESIG("QValQESig", true, "Qualified validation service for qualified electronic signature"),
  Q_PRES_ESIG("QPresQESig", true,
      "Qualified preservation service for qualified electronic signature"),
  Q_VAL_ESEAL("QValQESeal", true, "Qualified validation service for qualified electronic seal"),
  Q_PRES_ESEAL("QPresQESeal", true, "Qualified preservation service for qualified electronic seal"),
  Q_TIMESTAMP("QTimestamp", true, "Qualified time stamp"),
  Q_ERDS("QeRDS", true, "Qualified electronic registrered delivery service"),
  Q_QSCDMANAGEMENT("QSCDManagement", true,
      "Qualified service for remote signature/seal creation device management"),

  NQ_CERT_ESIG("CertESig", false, "Certificate for electronic signature"),
  NQ_CERT_ESEAL("CertESeal", false, "Certificate for electronic seal"),
  NQ_CERT_WAC("WAC", false, "Certificate for website authentication"),
  NQ_VAL_ESIG("ValESig", false, "Validation service for electronic signature"),
  NQ_GEN_ESIG("GenESig", false, "Generation service for electronic signature"),
  NQ_PRES_ESIG("PresESig", false, "Preservation service for electronic signature"),
  NQ_VAL_ESEAL("ValESeal", false, "Validation service for electronic seal"),
  NQ_GEN_ESEAL("GenESeal", false, "Generation service for electronic seal"),
  NQ_PRES_ESEAL("PresESeal", false, "Preservation service for electronic seal"),
  NQ_TIMESTAMP("Timestamp", false, "Time stamp service"),
  NQ_ERDS("eRDS", false, "Electronic registrered delivery service"),

  NON_REGULATORY("NonRegulatory", false, "Non-regulatory, nationally defined trust service"),
  CERT_UNDEFINED("CertUndefined", false, "Undefined type");


  private final String identifier;
  private final boolean qualified;
  private final String name;

  /**
   * @param identifier
   * @param qualified
   * @param name
   */
  ServiceType(final String identifier, boolean qualified, String name) {
    this.identifier = identifier;
    this.qualified = qualified;
    this.name = name;
  }

  public String value() {
    return this.identifier;
  }

  public String getIdentifier() {
    return identifier;
  }

  public boolean isQualified() {
    return qualified;
  }

  public String getName() {
    return name;
  }

  public static ServiceType fromString(String text) {
    for (ServiceType val : ServiceType.values()) {
      if (val.identifier.equals(text)) {
        return val;
      }
    }
    return null;
  }

  public static List<ServiceType> fromStringAsList(Collection<String> stringCollection) {
    return stringCollection.stream()
        .filter(Objects::nonNull)
        .map(searchType -> {
          try {
            return fromString(searchType);
          } catch (IllegalArgumentException ignored) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

}
