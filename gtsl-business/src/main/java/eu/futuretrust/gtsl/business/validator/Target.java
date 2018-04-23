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

package eu.futuretrust.gtsl.business.validator;

public enum Target {

  // TSL
  TSL("tsl"),
  TSL_TAG(TSL.text + ".tslTag"),
  TSL_SCHEME_INFORMATION(TSL.text + ".schemeInformation"),
  TSL_TSP_LIST(TSL.text + ".trustServiceProviderList"),

  // SCHEME INFORMATION
  TSL_VERSION_IDENTIFIER(TSL_SCHEME_INFORMATION.text + ".tslVersionIdentifier"),
  TSL_SEQUENCE_NUMBER(TSL_SCHEME_INFORMATION.text + ".tslSequenceNumber"),
  TSL_TYPE(TSL_SCHEME_INFORMATION.text + ".tslType"),
  TSL_SCHEME_OPERATOR_NAME(TSL_SCHEME_INFORMATION.text + ".schemeOperatorName"),
  TSL_SCHEME_OPERATOR_ADDRESS(TSL_SCHEME_INFORMATION.text + ".schemeOperatorAddress"),
  TSL_SCHEME_NAME(TSL_SCHEME_INFORMATION.text + ".schemeName"),
  TSL_SCHEME_INFORMATION_URI(TSL_SCHEME_INFORMATION.text + ".schemeInformationURI"),
  TSL_STATUS_DETN(TSL_SCHEME_INFORMATION.text + ".statusDeterminationApproach"),
  TSL_SCHEME_RULES(TSL_SCHEME_INFORMATION.text + ".schemeTypeCommunityRules"),
  TSL_SCHEME_TERRITORY(TSL_SCHEME_INFORMATION.text + ".schemeTerritory"),
  TSL_POLICY_OR_LEGAL_NOTICE(TSL_SCHEME_INFORMATION.text + ".policyOrLegalNotice"),
  TSL_HISTORICAL_INFORMATION_PERIOD(TSL_SCHEME_INFORMATION.text + ".historicalInformationPeriod"),
  TSL_POINTERS_TO_OTHER_TSL(TSL_SCHEME_INFORMATION.text + ".pointersToOtherTSL"),
  TSL_ISSUE_DATE(TSL_SCHEME_INFORMATION.text + ".listIssueDateTime"),
  TSL_NEXT_UPDATE(TSL_SCHEME_INFORMATION.text + ".nextUpdate"),
  TSL_DISTRIBUTION_POINTS(TSL_SCHEME_INFORMATION.text + ".distributionPoints"),
  TSL_SCHEME_EXTENSIONS(TSL_SCHEME_INFORMATION.text + ".schemeExtensions"),

  // SCHEME OPERATOR ADDRESS
  SCHEME_OPERATOR_POSTAL_ADDRESSES(TSL_SCHEME_OPERATOR_ADDRESS.text + ".postalAddresses"),
  SCHEME_OPERATOR_ELECTRONIC_ADDRESS(TSL_SCHEME_OPERATOR_ADDRESS.text + ".electronicAddress"),

  // POINTER TO OTHER TSL
  POINTER(TSL_POINTERS_TO_OTHER_TSL.text + ".pointer[%s]"),
  POINTER_TSL_LOCATION(POINTER.text + ".tslLocation"),
  POINTER_SERVICE_DIGITAL_IDENTITIES(POINTER.text + ".serviceDigitalIdentities"),
  POINTER_TSL_TYPE(POINTER.text + ".tslType"),
  POINTER_SCHEME_OPERATOR_NAME(POINTER.text + ".schemeOperatorName"),
  POINTER_SCHEME_RULES(POINTER.text + ".schemeRules"),
  POINTER_SCHEME_TERRITORY(POINTER.text + ".schemeTerritory"),
  POINTER_MIME_TYPE(POINTER.text + ".mimeType"),

  // TSP
  TSP(TSL_TSP_LIST.text + ".tsp[%s]"),
  TSP_INFORMATION(TSP.text + ".tspInformation"),
  TSP_SERVICE_LIST(TSP.text + ".tspServices"),

  // TSP INFORMATION
  TSP_NAME(TSP_INFORMATION.text + ".tspName"),
  TSP_TRADE_NAME(TSP_INFORMATION.text + ".tspTradeName"),
  TSP_ADDRESS(TSP_INFORMATION.text + ".tspAddress"),
  TSP_INFORMATION_URI(TSP_INFORMATION.text + ".tspInformationURI"),
  TSP_INFORMATION_EXTENSIONS(TSP_INFORMATION.text + ".tspInformationExtensions"),

  // SCHEME OPERATOR ADDRESS
  TSP_POSTAL_ADDRESSES(TSP_ADDRESS.text + ".postalAddresses"),
  TSP_ELECTRONIC_ADDRESS(TSP_ADDRESS.text + ".electronicAddress"),

  // SERVICE
  SERVICE(TSP_SERVICE_LIST.text + ".service[%s]"),
  SERVICE_INFORMATION(SERVICE.text + ".serviceInformation"),
  SERVICE_HISTORY_LIST(SERVICE.text + ".serviceHistory"),

  // SERVICE INFORMATION
  SERVICE_TYPE_IDENTIFIER(SERVICE_INFORMATION.text + ".serviceTypeIdentifier"),
  SERVICE_NAME(SERVICE_INFORMATION.text + ".serviceName"),
  SERVICE_DIGITAL_IDENTITY(SERVICE_INFORMATION.text + ".serviceDigitalIdentity"),
  SERVICE_CURRENT_STATUS(SERVICE_INFORMATION.text + ".serviceStatus"),
  SERVICE_STARTING_DATE_AND_TIME(SERVICE_INFORMATION.text + ".statusStartingTime"),
  SERVICE_SCHEME_DEFINITION_URI(SERVICE_INFORMATION.text + ".schemeServiceDefinitionURI"),
  SERVICE_SUPPLY_POINTS(SERVICE_INFORMATION.text + ".serviceSupplyPoints"),
  SERVICE_DEFINITION_URI(SERVICE_INFORMATION.text + ".tspServiceDefinitionURI"),
  SERVICE_INFORMATION_EXTENSIONS(SERVICE_INFORMATION.text + ".serviceInformationExtensions"),

  // SERVICE HISTORY
  HISTORY_INSTANCE(SERVICE_HISTORY_LIST.text + ".serviceHistoryInstance[%s]"),
  HISTORY_SERVICE_TYPE_IDENTIFIER(HISTORY_INSTANCE.text + ".serviceTypeIdentifier"),
  HISTORY_SERVICE_NAME(HISTORY_INSTANCE.text + ".serviceName"),
  HISTORY_SERVICE_DIGITAL_IDENTITY(HISTORY_INSTANCE.text + ".serviceDigitalIdentity"),
  HISTORY_PREVIOUS_STATUS(HISTORY_INSTANCE.text + ".serviceStatus"),
  HISTORY_STARTING_DATE_AND_TIME(HISTORY_INSTANCE.text + ".statusStartingTime"),
  HISTORY_SERVICE_INFORMATION_EXTENSIONS(HISTORY_INSTANCE.text + ".serviceInformationExtensions");


  private final String text;

  /**
   * @param text
   */
  Target(final String text) {
    this.text = text;
  }

  public String value(Object... args) {
    return String.format(this.text, args);
  }
}
