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

import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;

public enum ViolationConstant {

  // CONFIGURATION
  PROPERTIES_CANNOT_BE_LOADED(Impact.TrustBackbone.class, Severity.Error.class, Target.TSL,
      MessageCode.PROPERTIES_CANNOT_BE_LOADED),

  // POINTERS TO OTHER TSL
  HAS_POINTER_TO_LOTL(Impact.Legal.class, Severity.Error.class,
      Target.TSL_POINTERS_TO_OTHER_TSL, MessageCode.HAS_POINTER_TO_LOTL),
  IS_POINTER_PRESENT_FOR_EU_MS(Impact.Legal.class, Severity.Error.class,
      Target.TSL_POINTERS_TO_OTHER_TSL, MessageCode.IS_POINTER_PRESENT_FOR_EU_MS),
  IS_POINTER_SCHEME_RULES_CORRECT_VALUES(Impact.Legal.class, Severity.Error.class,
      Target.POINTER_SCHEME_RULES, MessageCode.IS_POINTER_SCHEME_RULES_CORRECT_VALUES),
  IS_POINTER_SCHEME_TERRITORY_CORRECT_VALUE(Impact.Legal.class, Severity.Warning.class,
      Target.POINTER_SCHEME_TERRITORY, MessageCode.IS_POINTER_SCHEME_TERRITORY_CORRECT_VALUE),
  IS_POINTER_CERTIFICATE_EXPIRED(Impact.Legal.class, Severity.Warning.class,
      Target.POINTER_SERVICE_DIGITAL_IDENTITIES, MessageCode.IS_POINTER_CERTIFICATE_EXPIRED),
  IS_POINTER_DIGITAL_IDS_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.POINTER_SERVICE_DIGITAL_IDENTITIES, MessageCode.IS_POINTER_DIGITAL_IDS_ALLOWED),
  IS_POINTER_DIGITAL_ALL_LOTL_CERTIFICATES(Impact.Legal.class, Severity.Error.class,
      Target.POINTER_SERVICE_DIGITAL_IDENTITIES,
      MessageCode.IS_POINTER_DIGITAL_ALL_LOTL_CERTIFICATES),
  IS_POINTER_SUBJECT_NAME_MATCH(Impact.Legal.class, Severity.Error.class,
      Target.POINTER_SERVICE_DIGITAL_IDENTITIES, MessageCode.IS_POINTER_SUBJECT_NAME_MATCH),
  IS_POINTER_VALID_CERTIFICATE(Impact.Legal.class, Severity.Error.class,
      Target.POINTER_SERVICE_DIGITAL_IDENTITIES, MessageCode.IS_POINTER_VALID_CERTIFICATE),
  IS_POINTER_X509CERTIFICATE_CONTAINS_BASIC_CONSTRAINT_CA_FALSE(Impact.Legal.class,
      Severity.Warning.class, Target.POINTER_SERVICE_DIGITAL_IDENTITIES,
      MessageCode.IS_POINTER_X509CERTIFICATE_CONTAINS_BASIC_CONSTRAINT_CA_FALSE),
  IS_POINTER_X509CERTIFICATE_CONTAINS_CORRECT_KEY_USAGES(Impact.Legal.class, Severity.Warning.class,
      Target.POINTER_SERVICE_DIGITAL_IDENTITIES,
      MessageCode.IS_POINTER_X509CERTIFICATE_CONTAINS_CORRECT_KEY_USAGES),
  IS_POINTER_X509CERTIFICATE_CONTAINS_SUBJECT_KEY_IDENTIFIER(Impact.Legal.class,
      Severity.Warning.class, Target.POINTER_SERVICE_DIGITAL_IDENTITIES,
      MessageCode.IS_POINTER_X509CERTIFICATE_CONTAINS_SUBJECT_KEY_IDENTIFIER),
  IS_POINTER_X509CERTIFICATE_CONTAINS_TSLSIGNING_EXT_KEY_USAGE(Impact.Legal.class,
      Severity.Info.class, Target.POINTER_SERVICE_DIGITAL_IDENTITIES,
      MessageCode.IS_POINTER_X509CERTIFICATE_CONTAINS_TSLSIGNING_EXT_KEY_USAGE),
  IS_POINTER_X509CERTIFICATE_COUNTRY_CODE_MATCH(Impact.Legal.class, Severity.Warning.class,
      Target.POINTER_SERVICE_DIGITAL_IDENTITIES,
      MessageCode.IS_POINTER_X509CERTIFICATE_COUNTRY_CODE_MATCH),
  IS_POINTER_X509CERTIFICATE_ORGANIZATION_MATCH(Impact.Legal.class, Severity.Warning.class,
      Target.POINTER_SERVICE_DIGITAL_IDENTITIES,
      MessageCode.IS_POINTER_X509CERTIFICATE_ORGANIZATION_MATCH),
  IS_POINTER_X509SKI_MATCH(Impact.Legal.class, Severity.Error.class,
      Target.POINTER_SERVICE_DIGITAL_IDENTITIES, MessageCode.IS_POINTER_X509SKI_MATCH),
  IS_POINTER_TSL_TYPE_CORRECT_VALUE(Impact.Legal.class, Severity.Error.class,
      Target.POINTER_TSL_TYPE, MessageCode.IS_POINTER_TSL_TYPE_CORRECT_VALUE),

  // SCHEME INFORMATION
  IS_HISTORICAL_PERIOD_CORRECT_VALUE(Impact.Legal.class, Severity.Error.class,
      Target.TSL_HISTORICAL_INFORMATION_PERIOD, MessageCode.IS_HISTORICAL_PERIOD_CORRECT_VALUE),
  IS_ISSUE_DATE_IN_THE_PAST(Impact.Legal.class, Severity.Error.class,
      Target.TSL_ISSUE_DATE, MessageCode.IS_ISSUE_DATE_IN_THE_PAST),
  IS_NEXT_UPDATE_IN_THE_FUTURE(Impact.Legal.class, Severity.Error.class,
      Target.TSL_NEXT_UPDATE, MessageCode.IS_NEXT_UPDATE_IN_THE_FUTURE),
  IS_NEXT_UPDATE_MAX_6_MONTHS_AFTER_ISSUE_DATE(Impact.Legal.class, Severity.Error.class,
      Target.TSL_NEXT_UPDATE, MessageCode.IS_NEXT_UPDATE_MAX_6_MONTHS_AFTER_ISSUE_DATE),
  IS_LEGAL_NOTICE_ENGLISH_CORRECT_VALUE(Impact.Legal.class, Severity.Warning.class,
      Target.TSL_POLICY_OR_LEGAL_NOTICE, MessageCode.IS_LEGAL_NOTICE_ENGLISH_CORRECT_VALUE),
  IS_POLICY_OR_LEGAL_NOTICE_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.TSL_POLICY_OR_LEGAL_NOTICE, MessageCode.IS_POLICY_OR_LEGAL_NOTICE_LANG_ALLOWED),
  IS_POLICY_OR_LEGAL_NOTICE_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.TSL_POLICY_OR_LEGAL_NOTICE, MessageCode.IS_POLICY_OR_LEGAL_NOTICE_CONTAIN_LANG_EN),
  IS_POLICY_OR_LEGAL_NOTICE_EMPTY(Impact.Legal.class, Severity.Error.class,
      Target.TSL_POLICY_OR_LEGAL_NOTICE, MessageCode.IS_POLICY_OR_LEGAL_NOTICE_EMPTY),
  IS_SCHEME_EXTENSIONS_NOT_PRESENT_FOR_EU_MS(Impact.Legal.class, Severity.Error.class,
      Target.TSL_SCHEME_EXTENSIONS, MessageCode.IS_SCHEME_EXTENSIONS_NOT_PRESENT_FOR_EU_MS),
  IS_SCHEME_INFORMATION_URI_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.TSL_SCHEME_INFORMATION_URI, MessageCode.IS_SCHEME_INFORMATION_URI_LANG_ALLOWED),
  IS_SCHEME_INFORMATION_URI_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.TSL_SCHEME_INFORMATION_URI, MessageCode.IS_SCHEME_INFORMATION_URI_CONTAIN_LANG_EN),
  IS_SCHEME_NAME_START_WITH_COUNTRY_CODE(Impact.Legal.class, Severity.Warning.class,
      Target.TSL_SCHEME_NAME, MessageCode.IS_SCHEME_NAME_START_WITH_COUNTRY_CODE),
  IS_SCHEME_NAME_ENGLISH_CORRECT_VALUE(Impact.Legal.class, Severity.Warning.class,
      Target.TSL_SCHEME_NAME, MessageCode.IS_SCHEME_NAME_ENGLISH_CORRECT_VALUE),
  IS_SCHEME_NAME_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.TSL_SCHEME_NAME, MessageCode.IS_SCHEME_NAME_LANG_ALLOWED),
  IS_SCHEME_NAME_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.TSL_SCHEME_NAME, MessageCode.IS_SCHEME_NAME_CONTAIN_LANG_EN),
  IS_SCHEME_ELECTRONIC_ADDRESS_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.SCHEME_OPERATOR_ELECTRONIC_ADDRESS,
      MessageCode.IS_SCHEME_ELECTRONIC_ADDRESS_CONTAIN_LANG_EN),
  IS_SCHEME_ELECTRONIC_ADDRESS_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.SCHEME_OPERATOR_ELECTRONIC_ADDRESS,
      MessageCode.IS_SCHEME_ELECTRONIC_ADDRESS_LANG_ALLOWED),
  IS_SCHEME_POSTAL_ADDRESSES_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.SCHEME_OPERATOR_POSTAL_ADDRESSES,
      MessageCode.IS_SCHEME_POSTAL_ADDRESSES_CONTAIN_LANG_EN),
  IS_SCHEME_POSTAL_ADDRESSES_COUNTRY_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.SCHEME_OPERATOR_POSTAL_ADDRESSES,
      MessageCode.IS_SCHEME_POSTAL_ADDRESSES_COUNTRY_ALLOWED),
  IS_SCHEME_POSTAL_ADDRESSES_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.SCHEME_OPERATOR_POSTAL_ADDRESSES, MessageCode.IS_SCHEME_POSTAL_ADDRESSES_LANG_ALLOWED),
  IS_SCHEME_OPERATOR_NAME_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.TSL_SCHEME_OPERATOR_NAME, MessageCode.IS_SCHEME_OPERATOR_NAME_LANG_ALLOWED),
  IS_SCHEME_OPERATOR_NAME_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.TSL_SCHEME_OPERATOR_NAME, MessageCode.IS_SCHEME_OPERATOR_NAME_CONTAIN_LANG_EN),
  IS_SCHEME_TERRITORY_CORRECT_VALUE(Impact.Legal.class, Severity.Error.class,
      Target.TSL_SCHEME_TERRITORY, MessageCode.IS_SCHEME_TERRITORY_CORRECT_VALUE),
  IS_SCHEME_TYPE_COMMUNITY_RULES_CORRECT_VALUES(Impact.Legal.class, Severity.Warning.class,
      Target.TSL_SCHEME_RULES, MessageCode.IS_SCHEME_TYPE_COMMUNITY_RULES_CORRECT_VALUES),
  IS_SCHEME_TYPE_COMMUNITY_RULES_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.TSL_SCHEME_RULES, MessageCode.IS_SCHEME_TYPE_COMMUNITY_RULES_LANG_ALLOWED),
  IS_SCHEME_TYPE_COMMUNITY_RULES_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.TSL_SCHEME_RULES, MessageCode.IS_SCHEME_TYPE_COMMUNITY_RULES_CONTAIN_LANG_EN),
  IS_STATUS_DETERMINATION_APPROACH_CORRECT_VALUE(Impact.Legal.class, Severity.Warning.class,
      Target.TSL_STATUS_DETN, MessageCode.IS_STATUS_DETERMINATION_APPROACH_CORRECT_VALUE),
  IS_SEQUENCE_NUMBER_INCREMENTED(Impact.Legal.class, Severity.Error.class,
      Target.TSL_SEQUENCE_NUMBER, MessageCode.IS_SEQUENCE_NUMBER_INCREMENTED),
  IS_SEQUENCE_NUMBER_WELL_INCREMENTED(Impact.Legal.class, Severity.Warning.class,
      Target.TSL_SEQUENCE_NUMBER, MessageCode.IS_SEQUENCE_NUMBER_INCREMENTED),
  IS_TSL_TYPE_CORRECT_VALUE(Impact.Legal.class, Severity.Warning.class,
      Target.TSL_TYPE, MessageCode.IS_TSL_TYPE_CORRECT_VALUE),
  IS_TSL_VERSION_IDENTIFIER_CORRECT_VALUE(Impact.Legal.class, Severity.Error.class,
      Target.TSL_VERSION_IDENTIFIER, MessageCode.IS_TSL_VERSION_IDENTIFIER_CORRECT_VALUE),

  // SERVICE HISTORY
  IS_HISTORY_CRITERIA_ASSERTS_NOT_NULL(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_INFORMATION_EXTENSIONS,
      MessageCode.IS_HISTORY_CRITERIA_ASSERTS_NOT_NULL),
  IS_HISTORY_EXPIRED_CERTS_REVOCATION_ALLOWED(Impact.Legal.class, Severity.Warning.class,
      Target.HISTORY_SERVICE_INFORMATION_EXTENSIONS,
      MessageCode.IS_HISTORY_EXPIRED_CERTS_REVOCATION_ALLOWED),
  IS_HISTORY_EXPIRED_CERTS_REVOCATION_NOT_CRITICAL(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_INFORMATION_EXTENSIONS,
      MessageCode.IS_HISTORY_EXPIRED_CERTS_REVOCATION_NOT_CRITICAL),
  IS_HISTORY_QUALIFICATION_ELEMENT_PRESENT(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_INFORMATION_EXTENSIONS,
      MessageCode.IS_HISTORY_QUALIFICATION_ELEMENT_PRESENT),
  IS_HISTORY_QUALIFICATION_EXTENSION_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_INFORMATION_EXTENSIONS,
      MessageCode.IS_HISTORY_QUALIFICATION_EXTENSION_ALLOWED),
  IS_HISTORY_QUALIFIER_URI_CORRECT_VALUE(Impact.Legal.class, Severity.Warning.class,
      Target.HISTORY_SERVICE_INFORMATION_EXTENSIONS,
      MessageCode.IS_HISTORY_QUALIFIER_URI_CORRECT_VALUE),
  IS_HISTORY_QUALIFIERS_PRESENT(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_INFORMATION_EXTENSIONS, MessageCode.IS_HISTORY_QUALIFIERS_PRESENT),
  IS_HISTORY_SERVICE_EXTENSION_ASI_ALLOWED(Impact.Legal.class, Severity.Warning.class,
      Target.HISTORY_SERVICE_INFORMATION_EXTENSIONS,
      MessageCode.IS_HISTORY_SERVICE_EXTENSION_ASI_ALLOWED),
  IS_HISTORY_SERVICE_EXTENSION_ASI_SIG_ALLOWED(Impact.Legal.class, Severity.Warning.class,
      Target.HISTORY_SERVICE_INFORMATION_EXTENSIONS,
      MessageCode.IS_HISTORY_SERVICE_EXTENSION_ASI_SIG_ALLOWED),
  IS_HISTORY_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED(Impact.Legal.class, Severity.Warning.class,
      Target.HISTORY_SERVICE_INFORMATION_EXTENSIONS,
      MessageCode.IS_HISTORY_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED),
  IS_HISTORY_SERVICE_EXTENSION_ASI_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_INFORMATION_EXTENSIONS,
      MessageCode.IS_HISTORY_SERVICE_EXTENSION_ASI_LANG_ALLOWED),
  IS_HISTORY_DIGITAL_IDENTITY_CERTIFICATE_EXPIRED(Impact.Legal.class, Severity.Warning.class,
      Target.HISTORY_SERVICE_DIGITAL_IDENTITY,
      MessageCode.IS_HISTORY_DIGITAL_IDENTITY_CERTIFICATE_EXPIRED),
  IS_HISTORY_DIGITAL_IDENTITY_SUBJECT_NAME_MATCH(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_DIGITAL_IDENTITY,
      MessageCode.IS_HISTORY_DIGITAL_IDENTITY_SUBJECT_NAME_MATCH),
  IS_HISTORY_DIGITAL_IDENTITY_VALID_CERTIFICATE(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_DIGITAL_IDENTITY,
      MessageCode.IS_HISTORY_DIGITAL_IDENTITY_VALID_CERTIFICATE),
  IS_HISTORY_DIGITAL_IDENTITY_X509SKI_MATCH(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_DIGITAL_IDENTITY,
      MessageCode.IS_HISTORY_DIGITAL_IDENTITY_X509SKI_MATCH),
  IS_HISTORY_SERVICE_NAME_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_NAME, MessageCode.IS_HISTORY_SERVICE_NAME_CONTAIN_LANG_EN),
  IS_HISTORY_SERVICE_NAME_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_NAME, MessageCode.IS_HISTORY_SERVICE_NAME_LANG_ALLOWED),
  IS_HISTORY_SERVICE_STATUS_BY_NATIONAL_LAW_ALLOWED(Impact.Legal.class, Severity.Warning.class,
      Target.HISTORY_PREVIOUS_STATUS,
      MessageCode.IS_HISTORY_SERVICE_STATUS_BY_NATIONAL_LAW_ALLOWED),
  IS_HISTORY_SERVICE_STATUS_CORRECT_VALUE(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_PREVIOUS_STATUS, MessageCode.IS_HISTORY_SERVICE_STATUS_CORRECT_VALUE),
  IS_HISTORY_TYPE_IDENTIFIER_REGISTERED(Impact.Legal.class, Severity.Error.class,
      Target.HISTORY_SERVICE_TYPE_IDENTIFIER, MessageCode.IS_HISTORY_TYPE_IDENTIFIER_REGISTERED),

  // SERVICE INFORMATION
  IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_SUBJECT_NAME(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_INFORMATION, MessageCode.IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_SUBJECT_NAME),
  IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_TYPE_IDENTIFIER(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_INFORMATION, MessageCode.IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_TYPE_IDENTIFIER),
  IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_X509SKI(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_INFORMATION, MessageCode.IS_ALL_SERVICE_AND_HISTORY_HAVE_SAME_X509SKI),
  IS_SCHEME_SERVICE_DEFINITION_URI_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_SCHEME_DEFINITION_URI,
      MessageCode.IS_SCHEME_SERVICE_DEFINITION_URI_CONTAIN_LANG_EN),
  IS_SCHEME_SERVICE_DEFINITION_URI_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_SCHEME_DEFINITION_URI,
      MessageCode.IS_SCHEME_SERVICE_DEFINITION_URI_LANG_ALLOWED),
  IS_SERVICE_DEFINITION_URI_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_DEFINITION_URI, MessageCode.IS_SERVICE_DEFINITION_URI_CONTAIN_LANG_EN),
  IS_SERVICE_DEFINITION_URI_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_DEFINITION_URI, MessageCode.IS_SERVICE_DEFINITION_URI_LANG_ALLOWED),
  IS_SERVICE_DEFINITION_URI_PRESENT_FOR_NATIONAL_ROOT_CA_QC(Impact.Legal.class,
      Severity.Warning.class, Target.SERVICE_DEFINITION_URI,
      MessageCode.IS_SERVICE_DEFINITION_URI_PRESENT_FOR_NATIONAL_ROOT_CA_QC),
  IS_SERVICE_DEFINITION_URI_LIST_NOT_EMPTY_FOR_NATIONAL_ROOT_CA_QC(Impact.Legal.class,
      Severity.Warning.class, Target.SERVICE_DEFINITION_URI,
      MessageCode.IS_SERVICE_DEFINITION_URI_LIST_NOT_EMPTY_FOR_NATIONAL_ROOT_CA_QC),
  IS_SERVICE_DIGITAL_IDENTITY_X509CERTIFICATE_VALID(Impact.Legal.class, Severity.Error.class,
          Target.SERVICE_DIGITAL_IDENTITY, MessageCode.IS_SERVICE_DIGITAL_IDENTITY_CERTIFICATE_VALID),
  IS_SERVICE_DIGITAL_IDENTITY_CERTIFICATE_EXPIRED(Impact.Legal.class, Severity.Warning.class,
      Target.SERVICE_DIGITAL_IDENTITY, MessageCode.IS_SERVICE_DIGITAL_IDENTITY_CERTIFICATE_EXPIRED),
  IS_SERVICE_DIGITAL_IDENTITY_CORRECT(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_DIGITAL_IDENTITY, MessageCode.IS_SERVICE_DIGITAL_IDENTITY_CORRECT),
  IS_SERVICE_DIGITAL_IDENTITY_SUBJECT_NAME_MATCH(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_DIGITAL_IDENTITY, MessageCode.IS_SERVICE_DIGITAL_IDENTITY_SUBJECT_NAME_MATCH),
  IS_SERVICE_DIGITAL_IDENTITY_VALID_CERTIFICATE(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_DIGITAL_IDENTITY, MessageCode.IS_SERVICE_DIGITAL_IDENTITY_VALID_CERTIFICATE),
  IS_SERVICE_DIGITAL_IDENTITY_X509SKI_MATCH(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_DIGITAL_IDENTITY, MessageCode.IS_SERVICE_DIGITAL_IDENTITY_X509SKI_MATCH),
  IS_SERVICE_DIGITAL_IDENTITY_X509CERTIFICATE_ORGANIZATION_MATCH(Impact.Legal.class,
      Severity.Warning.class, Target.SERVICE_DIGITAL_IDENTITY,
      MessageCode.IS_SERVICE_DIGITAL_IDENTITY_X509CERTIFICATE_ORGANIZATION_MATCH),
  IS_CRITERIA_ASSERTS_NOT_NULL(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_INFORMATION_EXTENSIONS, MessageCode.IS_CRITERIA_ASSERTS_NOT_NULL),
  IS_EXPIRED_CERTS_REVOCATION_ALLOWED(Impact.Legal.class, Severity.Warning.class,
      Target.SERVICE_INFORMATION_EXTENSIONS,
      MessageCode.IS_EXPIRED_CERTS_REVOCATION_ALLOWED),
  IS_EXPIRED_CERTS_REVOCATION_NOT_CRITICAL(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_INFORMATION_EXTENSIONS, MessageCode.IS_EXPIRED_CERTS_REVOCATION_NOT_CRITICAL),
  IS_QUALIFIER_URI_CORRECT_VALUE(Impact.Legal.class, Severity.Warning.class,
      Target.SERVICE_INFORMATION_EXTENSIONS, MessageCode.IS_QUALIFIER_URI_CORRECT_VALUE),
  IS_SERVICE_EXTENSION_ASI_ALLOWED(Impact.Legal.class, Severity.Warning.class,
      Target.SERVICE_INFORMATION_EXTENSIONS, MessageCode.IS_SERVICE_EXTENSION_ASI_ALLOWED),
  IS_SERVICE_EXTENSION_ASI_SIG_ALLOWED(Impact.Legal.class, Severity.Warning.class,
      Target.SERVICE_INFORMATION_EXTENSIONS, MessageCode.IS_SERVICE_EXTENSION_ASI_SIG_ALLOWED),
  IS_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED(Impact.Legal.class, Severity.Warning.class,
      Target.SERVICE_INFORMATION_EXTENSIONS, MessageCode.IS_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED),
  IS_SERVICE_EXTENSION_ASI_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_INFORMATION_EXTENSIONS, MessageCode.IS_SERVICE_EXTENSION_ASI_LANG_ALLOWED),
  IS_QUALIFICATION_ELEMENT_PRESENT(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_INFORMATION_EXTENSIONS, MessageCode.IS_QUALIFICATION_ELEMENT_PRESENT),
  IS_QUALIFICATION_EXTENSION_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_INFORMATION_EXTENSIONS, MessageCode.IS_QUALIFICATION_EXTENSION_ALLOWED),
  IS_QUALIFIERS_PRESENT(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_INFORMATION_EXTENSIONS, MessageCode.IS_QUALIFIERS_PRESENT),
  IS_SERVICE_NAME_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_NAME, MessageCode.IS_SERVICE_NAME_CONTAIN_LANG_EN),
  IS_SERVICE_NAME_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_NAME, MessageCode.IS_SERVICE_NAME_LANG_ALLOWED),
  IS_SERVICE_STATUS_CORRECT_VALUE_FOR_EU_MS(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_CURRENT_STATUS, MessageCode.IS_SERVICE_STATUS_CORRECT_VALUE_FOR_EU_MS),
  IS_SERVICE_STATUS_CORRECT_VALUE_FOR_NON_EU(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_CURRENT_STATUS, MessageCode.IS_SERVICE_STATUS_CORRECT_VALUE_FOR_NON_EU),
  IS_SERVICE_STATUS_BY_NATIONAL_LAW_ALLOWED(Impact.Legal.class, Severity.Warning.class,
      Target.SERVICE_CURRENT_STATUS, MessageCode.IS_SERVICE_STATUS_BY_NATIONAL_LAW_ALLOWED),
  IS_SERVICE_TYPE_IDENTIFIER_REGISTERED(Impact.Legal.class, Severity.Warning.class,
      Target.SERVICE_TYPE_IDENTIFIER, MessageCode.IS_SERVICE_TYPE_IDENTIFIER_REGISTERED),
  IS_SERVICE_STATUS_STARTING_TIME_ORDER(Impact.Legal.class, Severity.Error.class,
      Target.SERVICE_STARTING_DATE_AND_TIME, MessageCode.IS_SERVICE_STATUS_STARTING_TIME_ORDER),

  // TSL
  IS_TSL_TAG_CORRECT_VALUE(Impact.Legal.class, Severity.Error.class, Target.TSL_TAG,
      MessageCode.IS_TSL_TAG_CORRECT_VALUE),

  // TSP INFORMATION
  IS_TSP_ELECTRONIC_ADDRESS_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.TSP_ELECTRONIC_ADDRESS, MessageCode.IS_TSP_ELECTRONIC_ADDRESS_CONTAIN_LANG_EN),
  IS_TSP_ELECTRONIC_ADDRESS_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.TSP_ELECTRONIC_ADDRESS, MessageCode.IS_TSP_ELECTRONIC_ADDRESS_LANG_ALLOWED),
  IS_TSP_POSTAL_ADDRESSES_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.TSP_POSTAL_ADDRESSES, MessageCode.IS_TSP_POSTAL_ADDRESSES_CONTAIN_LANG_EN),
  IS_TSP_POSTAL_ADDRESSES_COUNTRY_ALLOWED(Impact.Legal.class, Severity.Warning.class,
      Target.TSP_POSTAL_ADDRESSES, MessageCode.IS_TSP_POSTAL_ADDRESSES_COUNTRY_ALLOWED),
  IS_TSP_POSTAL_ADDRESSES_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.TSP_POSTAL_ADDRESSES, MessageCode.IS_TSP_POSTAL_ADDRESSES_LANG_ALLOWED),
  IS_TSP_INFORMATION_EXTENSIONS_NOT_CRITICAL_FOR_EU_MS(Impact.Legal.class, Severity.Error.class,
      Target.TSP_INFORMATION_EXTENSIONS,
      MessageCode.IS_TSP_INFORMATION_EXTENSIONS_NOT_CRITICAL_FOR_EU_MS),
  IS_TSP_INFORMATION_URI_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.TSP_INFORMATION_URI, MessageCode.IS_TSP_INFORMATION_URI_CONTAIN_LANG_EN),
  IS_TSP_INFORMATION_URI_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.TSP_INFORMATION_URI, MessageCode.IS_TSP_INFORMATION_URI_LANG_ALLOWED),
  IS_TSP_NAME_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.TSP_NAME, MessageCode.IS_TSP_NAME_CONTAIN_LANG_EN),
  IS_TSP_NAME_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.TSP_NAME, MessageCode.IS_TSP_NAME_LANG_ALLOWED),
  IS_TSP_TRADE_NAME_CONTAIN_LANG_EN(Impact.Legal.class, Severity.Error.class,
      Target.TSP_TRADE_NAME, MessageCode.IS_TSP_TRADE_NAME_CONTAIN_LANG_EN),
  IS_TSP_TRADE_NAME_LANG_ALLOWED(Impact.Legal.class, Severity.Error.class,
      Target.TSP_TRADE_NAME, MessageCode.IS_TSP_TRADE_NAME_LANG_ALLOWED),
  IS_TSP_TRADE_NAME_VAT_NUMBER_VALID(Impact.Legal.class, Severity.Warning.class,
      Target.TSP_TRADE_NAME, MessageCode.IS_TSP_TRADE_NAME_VAT_NUMBER_VALID),
  IS_TSP_TRADE_NAME_CORRECT_VALUE(Impact.Legal.class, Severity.Error.class,
      Target.TSP_TRADE_NAME, MessageCode.IS_TSP_TRADE_NAME_CORRECT_VALUE),

  //SIGNING CERTIFICATE
  IS_SIGNING_CERTIFICATE_ISSUER_TLSO_OR_TSP(Impact.Legal.class, Severity.Error.class, Target
      .X509_CERTIFICATE, MessageCode.IS_SIGNING_CERTIFICATE_ISSUER_TLSO_OR_TSP),
  IS_SIGNING_CERTIFICATE_COUNTRY_CODE_VALID(Impact.Legal.class, Severity.Error.class, Target
      .X509_CERTIFICATE, MessageCode.IS_SIGNING_CERTIFICATE_COUNTRY_CODE_VALID),
  IS_SIGNING_CERTIFICATE_ORGANIZATION_VALID(Impact.Legal.class, Severity.Error.class, Target
      .X509_CERTIFICATE, MessageCode.IS_SIGNING_CERTIFICATE_ORGANIZATION_VALID),
  IS_SIGNING_CERTIFICATE_KEY_USAGE_VALID(Impact.Legal.class, Severity.Error.class, Target
      .X509_CERTIFICATE, MessageCode.IS_SIGNING_CERTIFICATE_KEY_USAGE_VALID),
  IS_SIGNING_CERTIFICATE_EXTENDED_KEY_USAGE_PRESENT(Impact.Legal.class, Severity.Warning.class,
      Target.X509_CERTIFICATE, MessageCode.IS_SIGNING_CERTIFICATE_EXTENDED_KEY_USAGE_PRESENT),
  IS_SIGNING_CERTIFICATE_KEY_USAGE_CONSISTENT(Impact.Legal.class, Severity.Error.class, Target
      .X509_CERTIFICATE, MessageCode.IS_SIGNING_CERTIFICATE_KEY_USAGE_CONSISTENT),
  IS_SIGNING_CERTIFICATE_SKI_PRESENT(Impact.Legal.class, Severity.Error.class, Target.X509_CERTIFICATE,
      MessageCode.IS_SIGNING_CERTIFICATE_SKI_PRESENT),
  IS_SIGNING_CERTIFICATE_BASIC_CONSTRAINTS_EXTENSION_VALID(Impact.Legal.class, Severity.Error
      .class, Target.X509_CERTIFICATE, MessageCode
      .IS_SIGNING_CERTIFICATE_BASIC_CONSTRAINTS_EXTENSION_VALID),
  IS_SIGNING_CERTIFICATE_TLSO(Impact.Legal.class, Severity.Error.class, Target.X509_CERTIFICATE,
      MessageCode.IS_SIGNING_CERTIFICATE_TLSO),
  IS_SIGNING_CERTIFICATE_VALID(Impact.Legal.class, Severity.Error.class, Target.X509_CERTIFICATE,
          MessageCode.IS_SIGNING_CERTIFICATE_VALID),
  IS_SIGNING_CERTIFICATE_KEY_RESILIENT(Impact.Legal.class, Severity.Error.class, Target.X509_CERTIFICATE, MessageCode.IS_SIGNING_CERTIFICATE_KEY_RESILIENT),

  //SIGNATURE
  HAS_SIGNATURE(Impact.Legal.class, Severity.Error.class, Target.SIGNATURE, MessageCode.HAS_SIGNATURE),
  HAS_SIGNATURE_KEYINFO_SINGLE_CERTIFICATE(Impact.Legal.class, Severity.Error.class, Target.SIGNATURE, MessageCode.HAS_SIGNATURE_KEYINFO_SINGLE_CERTIFICATE),
  IS_SIGNATURE_INVALID(Impact.Legal.class, Severity.Error.class, Target.SIGNATURE, MessageCode.IS_SIGNATURE_INVALID),
  IS_SIGNATURE_INDETERMINATE(Impact.Legal.class, Severity.Error.class, Target.SIGNATURE, MessageCode.IS_SIGNATURE_INDETERMINATE);


  private final String impact;
  private final String severity;
  private final Target target;
  private final MessageCode description;

  ViolationConstant(Class<? extends Impact> impact, Class<? extends Severity> severity,
      Target target, MessageCode description) {
    this.impact = impact.getSimpleName();
    this.severity = severity.getSimpleName();
    this.target = target;
    this.description = description;
  }

  public String getImpact() {
    return impact;
  }

  public String getSeverity() {
    return severity;
  }

  public Target getTarget() {
    return target;
  }

  public MessageCode getDescription() {
    return description;
  }

}
