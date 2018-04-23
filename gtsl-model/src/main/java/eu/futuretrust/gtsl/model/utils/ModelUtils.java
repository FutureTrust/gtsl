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

import eu.futuretrust.gtsl.jaxb.tsl.AnyTypeJAXB;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.common.Lang;
import eu.futuretrust.gtsl.model.data.common.NonEmptyNormalizedString;
import eu.futuretrust.gtsl.model.data.enums.MimeType;
import eu.futuretrust.gtsl.model.data.extension.AdditionalServiceInformationType;
import eu.futuretrust.gtsl.model.data.extension.ServiceInformationExtensionListType;
import eu.futuretrust.gtsl.model.data.extension.ServiceInformationExtensionType;
import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.x509.CertificateToken;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.security.auth.x500.X500Principal;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.x509.extension.X509ExtensionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public final class ModelUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModelUtils.class);

  public static final String VAT_PREFIX = "VAT";
  public static final String NTR_PREFIX = "NTR";
  public static final String PAS_PREFIX = "PAS";
  public static final String IDC_PREFIX = "IDC";
  public static final String PNO_PREFIX = "PNO";
  public static final String TIN_PREFIX = "TIN";

  public static final Lang LANG_EN = new Lang("en");

  public static final String XML_MEDIA_TYPE = "application/vnd.etsi.tsl+xml";
  public static final String PDF_MEDIA_TYPE = "application/pdf";

  public static String extractCNName(X500Principal principal) {
    String name = principal.getName();
    int index = name.indexOf("CN=") + 3;
    if (index == -1) {
      return name;
    } else {
      int stop = name.indexOf(",", index);
      return stop == -1 ? name.substring(index) : name.substring(index, stop);
    }
  }

  public static byte[] getSki(final CertificateToken certificateToken) throws DSSException {
    try {
      byte[] sKI = certificateToken.getCertificate()
          .getExtensionValue(Extension.subjectKeyIdentifier.getId());
      if (ArrayUtils.isNotEmpty(sKI)) {
        ASN1Primitive extension = X509ExtensionUtil.fromExtensionValue(sKI);
        SubjectKeyIdentifier skiBC = SubjectKeyIdentifier.getInstance(extension);
        return skiBC.getKeyIdentifier();
      } else {
        DLSequence seq1 = (DLSequence) DERSequence
            .fromByteArray(certificateToken.getPublicKey().getEncoded());
        DERBitString item2 = (DERBitString) seq1.getObjectAt(1);

        return DSSUtils.digest(DigestAlgorithm.SHA1, item2.getOctets());
      }
    } catch (Exception e) {
      throw new DSSException(e);
    }
  }

  public static String getTspEnglishName(InternationalNamesType names) {
    List<String> englishNames = names.getValues().stream()
        .filter(name -> name.getLang().equals(LANG_EN))
        .map(NonEmptyNormalizedString::getValue)
        .collect(Collectors.toList());
    if (CollectionUtils.isNotEmpty(englishNames)) {
      return englishNames.get(0);
    } else {
      return null;
    }
  }

  public static String getTspTradeNameByPrefix(InternationalNamesType names, String prefix) {
    List<String> tradeName = names.getValues().stream()
        .filter(name -> StringUtils.startsWith(name.getValue(), prefix))
        .map(NonEmptyNormalizedString::getValue)
        .collect(Collectors.toList());
    if (CollectionUtils.isNotEmpty(tradeName)) {
      return tradeName.get(0);
    } else {
      return null;
    }
  }

  public static List<AdditionalServiceInformationType> getValidAdditionalServiceInformation(
      ServiceInformationExtensionListType extensions) {
    if (extensions != null && CollectionUtils.isNotEmpty(extensions.getValues())) {
      return extensions.getValues().stream()
          .filter(ext -> Objects.nonNull(ext)
              && Objects.nonNull(ext.getAdditionalServiceInfo())
              && Objects.nonNull(ext.getAdditionalServiceInfo().getUri())
              && StringUtils.isNotEmpty(ext.getAdditionalServiceInfo().getUri().getValue()))
          .map(ServiceInformationExtensionType::getAdditionalServiceInfo)
          .collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }


  public static MimeType convert(String mimeTypeProp) {
    if (StringUtils.endsWithIgnoreCase(XML_MEDIA_TYPE, mimeTypeProp)) {
      return MimeType.XML;
    } else if (StringUtils.endsWithIgnoreCase(PDF_MEDIA_TYPE, mimeTypeProp)) {
      return MimeType.PDF;
    } else {
      return null;
    }
  }

  public static String convert(MimeType mimeType) {
    if (MimeType.XML == mimeType) {
      return XML_MEDIA_TYPE;
    } else if (MimeType.PDF == mimeType) {
      return PDF_MEDIA_TYPE;
    } else {
      return "";
    }
  }

  public static XMLGregorianCalendar toXMLGregorianDate(LocalDateTime date) {
    // https://stackoverflow.com/questions/30914901/java8-localdatetime-to-xmlgregoriancalender-remove-0530-portion
    LocalDateTime dateWithoutNano = date.withNano(0); // remove nano
    String iso = dateWithoutNano.toString();
    if (dateWithoutNano.getSecond() == 0) {
      iso += ":00Z"; // necessary hack because the second part is not optional in XML
    } else {
      iso += "Z";
    }
    XMLGregorianCalendar xml = null;
    try {
      xml = DatatypeFactory.newInstance().newXMLGregorianCalendar(iso);
    } catch (DatatypeConfigurationException e) {
      LOGGER.error("Unable to parse Date to XMLGregorianCalendar : " + e.getMessage());
    }
    return xml;
  }

  // from TL-Manager
  public static Map<String, Object> extractAsMap(
      List<Object> textualInformationOrOtherInformation) {
    Map<String, Object> properties = new HashMap<>();
    for (Object info : textualInformationOrOtherInformation) {
      if (info instanceof AnyTypeJAXB) {
        AnyTypeJAXB anyInfo = (AnyTypeJAXB) info;
        for (Object content : anyInfo.getContent()) {
          if (content instanceof JAXBElement) {
            JAXBElement jaxbElement = (JAXBElement) content;
            properties.put(jaxbElement.getName().toString(), jaxbElement.getValue());
          } else if (content instanceof Element) {
            Element element = (Element) content;
            properties.put("{" + element.getNamespaceURI() + "}" + element.getLocalName(), element);
          }
        }
      }
    }
    return properties;
  }

  public static String getTrustMark(InternationalNamesType tspTradeName) {
    String trustmark = getTspTradeNameByPrefix(tspTradeName, VAT_PREFIX);
    if (trustmark != null) {
      return trustmark;
    }
    trustmark = getTspTradeNameByPrefix(tspTradeName, NTR_PREFIX);
    if (trustmark != null) {
      return trustmark;
    }
    trustmark = getTspTradeNameByPrefix(tspTradeName, PAS_PREFIX);
    if (trustmark != null) {
      return trustmark;
    }
    trustmark = getTspTradeNameByPrefix(tspTradeName, IDC_PREFIX);
    if (trustmark != null) {
      return trustmark;
    }
    trustmark = getTspTradeNameByPrefix(tspTradeName, PNO_PREFIX);
    if (trustmark != null) {
      return trustmark;
    }
    trustmark = getTspTradeNameByPrefix(tspTradeName, TIN_PREFIX);
    if (trustmark != null) {
      return trustmark;
    }
    return null;
  }
}
