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

public enum MessageCode {

  SERVER_ERROR("ServerError.value"),
  UNKNOWN_ERROR("UnknownError.value"),

  PROPERTIES_CANNOT_BE_LOADED("PropertiesError.value"),

  HAS_POINTER_TO_LOTL("pointersToOtherTSL.pointerToLotl"),
  IS_POINTER_PRESENT_FOR_EU_MS("pointersToOtherTSL.pointerPresentForEuMs"),
  IS_POINTER_SCHEME_RULES_CORRECT_VALUES("pointerToOtherTSL.schemeRulesCorrect"),
  IS_POINTER_SCHEME_TERRITORY_CORRECT_VALUE("pointerToOtherTSL.schemeTerritoryCorrect"),
  IS_POINTER_CERTIFICATE_EXPIRED("pointerToOtherTSL.certificateExpired"),
  IS_POINTER_DIGITAL_IDS_ALLOWED("pointerToOtherTSL.digitalIdAllowed"),
  IS_POINTER_DIGITAL_ALL_LOTL_CERTIFICATES("pointerToOtherTSL.allLotlCertificates"),
  IS_POINTER_SUBJECT_NAME_MATCH("pointerToOtherTSL.subjectNameMatch"),
  IS_POINTER_VALID_CERTIFICATE("pointerToOtherTSL.validCertificate"),
  IS_POINTER_X509CERTIFICATE_CONTAINS_BASIC_CONSTRAINT_CA_FALSE(
      "pointerToOtherTSL.basicConstraintCaFalse"),
  IS_POINTER_X509CERTIFICATE_CONTAINS_CORRECT_KEY_USAGES("pointerToOtherTSL.keyUsagesCorrect"),
  IS_POINTER_X509CERTIFICATE_CONTAINS_SUBJECT_KEY_IDENTIFIER(
      "pointerToOtherTSL.subjectKeyIdentifier"),
  IS_POINTER_X509CERTIFICATE_CONTAINS_TSLSIGNING_EXT_KEY_USAGE(
      "pointerToOtherTSL.tslSigning"),
  IS_POINTER_X509CERTIFICATE_COUNTRY_CODE_MATCH("pointerToOtherTSL.countryCodeMatch"),
  IS_POINTER_X509CERTIFICATE_ORGANIZATION_MATCH("pointerToOtherTSL.organizationMatch"),
  IS_POINTER_X509SKI_MATCH("pointerToOtherTSL.x509skiMatch"),
  IS_POINTER_TSL_TYPE_CORRECT_VALUE("pointerToOtherTSL.tslTypeCorrect"),

  IS_HISTORICAL_PERIOD_CORRECT_VALUE("schemeInformation.historicalPeriodCorrect"),
  IS_ISSUE_DATE_IN_THE_PAST("schemeInformation.issueDateInThePast"),
  IS_NEXT_UPDATE_IN_THE_FUTURE("schemeInformation.nextUpdateInThePresent"),
  IS_NEXT_UPDATE_MAX_6_MONTHS_AFTER_ISSUE_DATE("schemeInformation.nextUpdateMax6Months"),
  IS_LEGAL_NOTICE_ENGLISH_CORRECT_VALUE("schemeInformation.legalNoticeEnglishCorrect"),
  IS_POLICY_OR_LEGAL_NOTICE_LANG_ALLOWED("schemeInformation.policyOrLegalNoticeLangAllowed"),
  IS_POLICY_OR_LEGAL_NOTICE_CONTAIN_LANG_EN("schemeInformation.policyOrLegalNoticeContainEn"),
  IS_POLICY_OR_LEGAL_NOTICE_EMPTY("schemeInformation.policyOrLegalNoticeEmpty"),
  IS_SCHEME_EXTENSIONS_NOT_PRESENT_FOR_EU_MS("schemeInformation.extensionsNotPresentForEuMs"),
  IS_SCHEME_INFORMATION_URI_LANG_ALLOWED("schemeInformation.schemeInformationUriLangAllowed"),
  IS_SCHEME_INFORMATION_URI_CONTAIN_LANG_EN("schemeInformation.schemeInformationUriContainEn"),
  IS_SCHEME_NAME_START_WITH_COUNTRY_CODE("schemeInformation.schemeNameStartWithCC"),
  IS_SCHEME_NAME_ENGLISH_CORRECT_VALUE("schemeInformation.schemeNameEnglishCorrect"),
  IS_SCHEME_NAME_LANG_ALLOWED("schemeInformation.schemeNameLangAllowed"),
  IS_SCHEME_NAME_CONTAIN_LANG_EN("schemeInformation.schemeNameContainEn"),
  IS_SCHEME_ELECTRONIC_ADDRESS_CONTAIN_LANG_EN("schemeInformation.electronicAddressContainEn"),
  IS_SCHEME_ELECTRONIC_ADDRESS_LANG_ALLOWED("schemeInformation.electronicAddressLangAllowed"),
  IS_SCHEME_POSTAL_ADDRESSES_CONTAIN_LANG_EN("schemeInformation.postalAddressesContainEn"),
  IS_SCHEME_POSTAL_ADDRESSES_COUNTRY_ALLOWED("schemeInformation.postalAddressesCountryAllowed"),
  IS_SCHEME_POSTAL_ADDRESSES_LANG_ALLOWED("schemeInformation.postalAddressesLangAllowed"),
  IS_SCHEME_OPERATOR_NAME_LANG_ALLOWED("schemeInformation.schemeOperatorNameLangAllowed"),
  IS_SCHEME_OPERATOR_NAME_CONTAIN_LANG_EN("schemeInformation.schemeOperatorNameContainEn"),
  IS_SCHEME_TERRITORY_CORRECT_VALUE("schemeInformation.schemeTerritoryCorrect"),
  IS_SCHEME_TYPE_COMMUNITY_RULES_CORRECT_VALUES("schemeInformation.schemeRulesCorrect"),
  IS_SCHEME_TYPE_COMMUNITY_RULES_LANG_ALLOWED("schemeInformation.schemeRulesLangAllowed"),
  IS_SCHEME_TYPE_COMMUNITY_RULES_CONTAIN_LANG_EN("schemeInformation.schemeRulesContainEn"),
  IS_STATUS_DETERMINATION_APPROACH_CORRECT_VALUE("schemeInformation.statusDetnCorrect"),
  IS_SEQUENCE_NUMBER_INCREMENTED("schemeInformation.sequenceNumberIncremented"),
  IS_TSL_TYPE_CORRECT_VALUE("schemeInformation.tslTypeCorrect"),
  IS_TSL_VERSION_IDENTIFIER_CORRECT_VALUE("schemeInformation.tslVersionIdentifierCorrect"),

  IS_HISTORY_DIGITAL_IDENTITY_CERTIFICATE_EXPIRED("serviceHistory.certificateExpired"),
  IS_HISTORY_DIGITAL_IDENTITY_SUBJECT_NAME_MATCH("serviceHistory.subjectNameMatch"),
  IS_HISTORY_DIGITAL_IDENTITY_VALID_CERTIFICATE("serviceHistory.validCertificate"),
  IS_HISTORY_DIGITAL_IDENTITY_X509SKI_MATCH("serviceHistory.x509skiMatch"),
  IS_HISTORY_CRITERIA_ASSERTS_NOT_NULL("serviceHistory.assertsNotNull"),
  IS_HISTORY_EXPIRED_CERTS_REVOCATION_ALLOWED("serviceHistory.expiredCertsRevocationAllowed"),
  IS_HISTORY_EXPIRED_CERTS_REVOCATION_NOT_CRITICAL(
      "serviceHistory.expiredCertsRevocationNotCritical"),
  IS_HISTORY_QUALIFICATION_ELEMENT_PRESENT("serviceHistory.qualificationElementPresent"),
  IS_HISTORY_QUALIFICATION_EXTENSION_ALLOWED("serviceHistory.qualificationExtensionsAllowed"),
  IS_HISTORY_QUALIFIER_URI_CORRECT_VALUE("serviceHistory.qualifierUriCorrect"),
  IS_HISTORY_QUALIFIERS_PRESENT("serviceHistory.qualifiersPresent"),
  IS_HISTORY_SERVICE_EXTENSION_ASI_ALLOWED("serviceHistory.serviceExtensionAsiAllowed"),
  IS_HISTORY_SERVICE_EXTENSION_ASI_SIG_ALLOWED("serviceHistory.serviceExtensionAsiSigAllowed"),
  IS_HISTORY_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED(
      "serviceHistory.serviceExtensionAsiSigSealAllowed"),
  IS_HISTORY_SERVICE_EXTENSION_ASI_LANG_ALLOWED("serviceHistory.serviceExtensionAsiLangAllowed"),
  IS_HISTORY_SERVICE_NAME_CONTAIN_LANG_EN("serviceHistory.serviceNameContainEn"),
  IS_HISTORY_SERVICE_NAME_LANG_ALLOWED("serviceHistory.serviceNameLangAllowed"),
  IS_HISTORY_SERVICE_STATUS_BY_NATIONAL_LAW_ALLOWED(
      "serviceHistory.serviceStatusByNationalLawAllowed"),
  IS_HISTORY_SERVICE_STATUS_CORRECT_VALUE("serviceHistory.serviceStatusCorrect"),
  IS_HISTORY_TYPE_IDENTIFIER_REGISTERED("serviceHistory.typeIdentifierRegistered"),

  IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_SUBJECT_NAME(
      "serviceInformation.allServiceAndHistoryHaveSameSubjectName"),
  IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_TYPE_IDENTIFIER(
      "serviceInformation.allServiceAndHistoryHaveSameTypeIdentifier"),
  IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_X509SKI(
      "serviceInformation.allServiceAndHistoryHaveSameX509ski"),
  IS_SCHEME_SERVICE_DEFINITION_URI_CONTAIN_LANG_EN(
      "serviceInformation.schemeServiceDefinitionUriContainEn"),
  IS_SCHEME_SERVICE_DEFINITION_URI_LANG_ALLOWED(
      "serviceInformation.schemeServiceDefinitionUriLangAllowed"),
  IS_SERVICE_DEFINITION_URI_CONTAIN_LANG_EN("serviceInformation.serviceDefinitionUriContainEn"),
  IS_SERVICE_DEFINITION_URI_LANG_ALLOWED("serviceInformation.serviceDefinitionUriLangAllowed"),
  IS_SERVICE_DEFINITION_URI_PRESENT_FOR_NATIONAL_ROOT_CA_QC(
      "serviceInformation.serviceDefinitionUriPresentForNational"),
  IS_SERVICE_DEFINITION_URI_LIST_NOT_EMPTY_FOR_NATIONAL_ROOT_CA_QC(
      "serviceInformation.serviceDefinitionUriListNotEmptyForNational"),
  IS_SERVICE_DIGITAL_IDENTITY_CERTIFICATE_EXPIRED("serviceInformation.certificateExpired"),
  IS_SERVICE_DIGITAL_IDENTITY_CORRECT("serviceInformation.digitalIdentityCorrect"),
  IS_SERVICE_DIGITAL_IDENTITY_SUBJECT_NAME_MATCH("serviceInformation.subjectNameMatch"),
  IS_SERVICE_DIGITAL_IDENTITY_VALID_CERTIFICATE("serviceInformation.validCertificate"),
  IS_SERVICE_DIGITAL_IDENTITY_X509SKI_MATCH("serviceInformation.x509skiMatch"),
  IS_SERVICE_DIGITAL_IDENTITY_X509CERTIFICATE_ORGANIZATION_MATCH(
      "serviceInformation.x509CertificateOrganizationMatch"),
  IS_CRITERIA_ASSERTS_NOT_NULL("serviceInformation.criteriaAssertsNotNull"),
  IS_EXPIRED_CERTS_REVOCATION_ALLOWED("serviceInformation.expiredCertsRevocationAllowed"),
  IS_EXPIRED_CERTS_REVOCATION_NOT_CRITICAL("serviceInformation.expiredCertsRevocationNotCritical"),
  IS_QUALIFIER_URI_CORRECT_VALUE("serviceInformation.qualifierUriCorrect"),
  IS_SERVICE_EXTENSION_ASI_ALLOWED("serviceInformation.serviceExtensionAsiAllowed"),
  IS_SERVICE_EXTENSION_ASI_SIG_ALLOWED("serviceInformation.serviceExtensionAsiSigAllowed"),
  IS_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED("serviceInformation.serviceExtensionAsiSigSealAllowed"),
  IS_SERVICE_EXTENSION_ASI_LANG_ALLOWED("serviceInformation.serviceExtensionAsiLangAllowed"),
  IS_QUALIFICATION_ELEMENT_PRESENT("serviceInformation.qualificationElementPresent"),
  IS_QUALIFICATION_EXTENSION_ALLOWED("serviceInformation.qualificationExtensionAllowed"),
  IS_QUALIFIERS_PRESENT("serviceInformation.qualifiersPresent"),
  IS_SERVICE_NAME_CONTAIN_LANG_EN("serviceInformation.serviceNameContainEn"),
  IS_SERVICE_NAME_LANG_ALLOWED("serviceInformation.serviceNameLangAllowed"),
  IS_SERVICE_STATUS_CORRECT_VALUE_FOR_EU_MS("serviceInformation.serviceStatusCorrectForEuMs"),
  IS_SERVICE_STATUS_CORRECT_VALUE_FOR_NON_EU("serviceInformation.serviceStatusCorrectForNonEu"),
  IS_SERVICE_STATUS_BY_NATIONAL_LAW_ALLOWED("serviceInformation.serviceStatusByNationalLawAllowed"),
  IS_SERVICE_TYPE_IDENTIFIER_REGISTERED("serviceInformation.serviceTypeIdentifierRegistered"),
  IS_SERVICE_STATUS_STARTING_TIME_ORDER("serviceInformation.serviceStatusStartingTimeOrder"),

  IS_TSL_TAG_CORRECT_VALUE("tsl.tslTagCorrect"),

  IS_TSP_ELECTRONIC_ADDRESS_CONTAIN_LANG_EN("tsp.electronicAddressContainEn"),
  IS_TSP_ELECTRONIC_ADDRESS_LANG_ALLOWED("tsp.electronicAddressLangAllowed"),
  IS_TSP_POSTAL_ADDRESSES_CONTAIN_LANG_EN("tsp.postalAddressesContainEn"),
  IS_TSP_POSTAL_ADDRESSES_COUNTRY_ALLOWED("tsp.postalAddressesCountryAllowed"),
  IS_TSP_POSTAL_ADDRESSES_LANG_ALLOWED("tsp.postalAddressesLangAllowed"),
  IS_TSP_INFORMATION_EXTENSIONS_NOT_CRITICAL_FOR_EU_MS("tsp.extensionsNotCriticalForEuMs"),
  IS_TSP_INFORMATION_URI_CONTAIN_LANG_EN("tsp.tspInformationUriContainEn"),
  IS_TSP_INFORMATION_URI_LANG_ALLOWED("tsp.tspInformationUriLangAllowed"),
  IS_TSP_NAME_CONTAIN_LANG_EN("tsp.tspNameContainEn"),
  IS_TSP_NAME_LANG_ALLOWED("tsp.tspNameLangAllowed"),
  IS_TSP_TRADE_NAME_CONTAIN_LANG_EN("tsp.tspTradeNameContainEn"),
  IS_TSP_TRADE_NAME_LANG_ALLOWED("tsp.tspTradeNameLangAllowed"),
  IS_TSP_TRADE_NAME_VAT_NUMBER_VALID("tsp.tspTradeNameVatNumberValid"),
  IS_TSP_TRADE_NAME_CORRECT_VALUE("tsp.tspTradeNameCorrectValue");

  private final String text;

  /**
   * @param text
   */
  MessageCode(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }

}
