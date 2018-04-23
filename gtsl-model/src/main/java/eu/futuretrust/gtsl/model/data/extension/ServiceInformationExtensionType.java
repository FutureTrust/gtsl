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

package eu.futuretrust.gtsl.model.data.extension;

import eu.futuretrust.gtsl.jaxb.additionaltypes.TakenOverByTypeJAXB;
import eu.futuretrust.gtsl.jaxb.sie.QualificationElementTypeJAXB;
import eu.futuretrust.gtsl.jaxb.sie.QualificationsTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.AdditionalServiceInformationTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.ExtensionTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.utils.DateUtils;
import eu.futuretrust.gtsl.model.utils.JaxbGregorianCalendarZulu;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import eu.futuretrust.gtsl.model.utils.XmlGregorianCalendarUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.w3c.dom.Element;

/**
 * 5.5.9 Service information extensions 5.5.9.0 General 5.5.9.1 expiredCertsRevocationInfo Extension
 * 5.5.9.2 Qualifications Extension 5.5.9.3 TakenOverBy Extension 5.5.9.4
 * additionalServiceInformation Extension
 *
 * <xsd:element name="Qualifications" type="tns:QualificationsType"/> <xsd:complexType
 * name="QualificationsType"> <xsd:sequence maxOccurs="unbounded"> <!--Specified in TS 119 612
 * v2.1.1 clause 5.5.9.2.1 QualificationElement--> <xsd:element name="QualificationElement"
 * type="tns:QualificationElementType"/> </xsd:sequence> </xsd:complexType>
 */
public class ServiceInformationExtensionType {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ServiceInformationExtensionType.class);

  @NotNull(message = "{NotNull.extension.critical}", payload = {Severity.Error.class,
      Impact.Legal.class})
  private Boolean critical;

  private List<QualificationElementType> qualificationsExtension;
  private TakenOverByType takenOverBy;
  private AdditionalServiceInformationType additionalServiceInfo;

  @DateTimeFormat(pattern = DateUtils.SPRING_PATTERN)
  private LocalDateTime expiredCertsRevocationDate;

  public ServiceInformationExtensionType() {
    this.critical = false;
    this.qualificationsExtension = new ArrayList<>();
    this.takenOverBy = new TakenOverByType();
    this.additionalServiceInfo = new AdditionalServiceInformationType();
  }

  // from TL-Manager
  public ServiceInformationExtensionType(ExtensionTypeJAXB extension) {
    this();
    if (extension != null) {
      this.setCritical(extension.isCritical());
      if (CollectionUtils.isNotEmpty(extension.getContent())) {
        for (Object objectJAXB : extension.getContent()) {
          if (objectJAXB instanceof JAXBElement) {
            JAXBElement jaxbElement = (JAXBElement) objectJAXB;
            filterTypeV5(jaxbElement);
          } else if (objectJAXB instanceof Element) {
            Element element = (Element) objectJAXB;
            try {
              JAXBContext jaxbContext = JAXBContext
                  .newInstance(TakenOverByTypeJAXB.class,
                      AdditionalServiceInformationTypeJAXB.class,
                      QualificationElementTypeJAXB.class);
              Object object = jaxbContext.createUnmarshaller().unmarshal(element);
              filterTypeV5(object);
            } catch (Exception e) {
              LOGGER.warn("Cannot read object ExtensionTypeJAXB : " + e.getMessage());
            }
          }
        }
      }
    }
  }

  // from TL-Manager
  private void filterTypeV5(Object obj) {
    if (obj instanceof QualificationsTypeJAXB) {
      QualificationsTypeJAXB qt = (QualificationsTypeJAXB) obj;
      List<QualificationElementType> qualificationsExtension = new ArrayList<>();
      for (QualificationElementTypeJAXB qualificationElementJAXB : qt.getQualificationElement()) {
        qualificationsExtension.add(new QualificationElementType(qualificationElementJAXB));
      }
      this.setQualificationsExtension(qualificationsExtension);
    } else if (obj instanceof AdditionalServiceInformationTypeJAXB) {
      AdditionalServiceInformationTypeJAXB asiJAXB = (AdditionalServiceInformationTypeJAXB) obj;
      this.setAdditionalServiceInfo(new AdditionalServiceInformationType(asiJAXB));
    } else if (obj instanceof TakenOverByTypeJAXB) {
      TakenOverByTypeJAXB takenOverByJAXB = (TakenOverByTypeJAXB) obj;
      this.setTakenOverBy(new TakenOverByType(takenOverByJAXB));
    } else if (obj instanceof JAXBElement) {
      JAXBElement obj2 = (JAXBElement) obj;
      filterTypeV5(obj2.getValue());
    } else if (obj instanceof XMLGregorianCalendar) {
      this.expiredCertsRevocationDate = XmlGregorianCalendarUtils
          .toLocalDateTime((XMLGregorianCalendar) obj);
    } else {
      LOGGER.warn("Cannot read object " + obj.getClass());
    }
  }

  // from TL-Manager
  public ExtensionTypeJAXB asJAXB() {
    if (isNotValidExtension()) {
      return null;
    }

    ExtensionTypeJAXB extensionJAXB = new ExtensionTypeJAXB();

    extensionJAXB.setCritical(this.isCritical());

    // QualificationsType
    if (isValidQualifications()) {
      QualificationsTypeJAXB qualificationsJAXB = new QualificationsTypeJAXB();
      qualificationsJAXB.getQualificationElement().addAll(
          qualificationsExtension.stream().map(QualificationElementType::asJAXB)
              .collect(Collectors.toList()));
      extensionJAXB.getContent().add(new JAXBElement<>(
          new QName("http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#",
              "Qualifications"), QualificationsTypeJAXB.class, qualificationsJAXB));
    }

    // AdditionalServiceInformationType
    if (isValidAdditionalServiceInfo()) {
      extensionJAXB.getContent().add(new JAXBElement<>(
          new QName("http://uri.etsi.org/02231/v2#", "AdditionalServiceInformation"),
          AdditionalServiceInformationTypeJAXB.class, additionalServiceInfo.asJAXB()));
    }

    if (isValidTakenOverBy()) {
      extensionJAXB.getContent().add(new JAXBElement<>(
          new QName("http://uri.etsi.org/02231/v2/additionaltypes#", "TakenOverBy"),
          TakenOverByTypeJAXB.class, takenOverBy.asJAXB()));
    }

    if (isValidExpiredCertsRevocationDate()) {
      try {
        extensionJAXB.getContent().add(new JAXBElement<>(
            new QName("http://uri.etsi.org/02231/v2#", "ExpiredCertsRevocationInfo"), XMLGregorianCalendar.class,
            ModelUtils.toXMLGregorianDate(expiredCertsRevocationDate)));
      } catch (Exception e1) {
        LOGGER.error(e1.getMessage());
      }
    }

    return extensionJAXB;
  }

  private boolean isNotValidExtension() {
    return !isValidQualifications() && !isValidAdditionalServiceInfo() && !isValidTakenOverBy()
        && !isValidExpiredCertsRevocationDate();
  }

  private boolean isValidQualifications() {
    return CollectionUtils.isNotEmpty(qualificationsExtension);
  }

  private boolean isValidAdditionalServiceInfo() {
    return additionalServiceInfo != null
        && ((additionalServiceInfo.getUri() != null && StringUtils
        .isNotEmpty(additionalServiceInfo.getUri().getValue()))
        || (StringUtils.isNotEmpty(additionalServiceInfo.getInformationValue())));
  }

  private boolean isValidTakenOverBy() {
    return takenOverBy != null
        && ((takenOverBy.getOperatorName() != null && CollectionUtils
        .isNotEmpty(takenOverBy.getOperatorName().getValues()))
        || (takenOverBy.getTspName() != null && CollectionUtils
        .isNotEmpty(takenOverBy.getTspName().getValues()))
        || (takenOverBy.getUri() != null && StringUtils
        .isNotEmpty(takenOverBy.getUri().getValue()))
        || (StringUtils.isNotEmpty(takenOverBy.getTerritory())));
  }

  private boolean isValidExpiredCertsRevocationDate() {
    return expiredCertsRevocationDate != null;
  }

  public boolean isCritical() {
    return critical;
  }

  public void setCritical(boolean critical) {
    this.critical = critical;
  }

  public LocalDateTime getExpiredCertsRevocationDate() {
    return expiredCertsRevocationDate;
  }

  public void setExpiredCertsRevocationDate(LocalDateTime expiredCertsRevocationDate) {
    this.expiredCertsRevocationDate = expiredCertsRevocationDate;
  }

  public List<QualificationElementType> getQualificationsExtension() {
    return qualificationsExtension;
  }

  public void setQualificationsExtension(
      List<QualificationElementType> qualificationsExtension) {
    this.qualificationsExtension = qualificationsExtension;
  }

  public TakenOverByType getTakenOverBy() {
    return takenOverBy;
  }

  public void setTakenOverBy(TakenOverByType takenOverBy) {
    this.takenOverBy = takenOverBy;
  }

  public AdditionalServiceInformationType getAdditionalServiceInfo() {
    return additionalServiceInfo;
  }

  public void setAdditionalServiceInfo(
      AdditionalServiceInformationType additionalServiceInfo) {
    this.additionalServiceInfo = additionalServiceInfo;
  }
}
