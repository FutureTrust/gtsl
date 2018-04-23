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

package eu.futuretrust.gtsl.model.data.extension.qualification;

import eu.futuretrust.gtsl.jaxb.additionaltypes.CertSubjectDNAttributeTypeJAXB;
import eu.futuretrust.gtsl.jaxb.additionaltypes.ExtendedKeyUsageTypeJAXB;
import eu.futuretrust.gtsl.jaxb.xades.AnyTypeJAXB;
import eu.futuretrust.gtsl.jaxb.xades.ObjectIdentifierTypeJAXB;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class OtherCriteriaType {

  private static final Logger LOGGER = LoggerFactory.getLogger(OtherCriteriaType.class);

  private List<PoliciesBitType> extendedKeyUsageList;
  private List<PoliciesBitType> certDnaList;

  public OtherCriteriaType() {
    this.extendedKeyUsageList = new ArrayList<>();
    this.certDnaList = new ArrayList<>();
  }

  // from TL-Manager
  public OtherCriteriaType(AnyTypeJAXB otherCriteriaList) {
    this();
    if (otherCriteriaList != null && CollectionUtils.isNotEmpty(otherCriteriaList.getContent())) {
      if (CollectionUtils.isNotEmpty(otherCriteriaList.getContent())) {
        List<PoliciesBitType> extended = new ArrayList<>();
        List<PoliciesBitType> certDna = new ArrayList<>();
        for (Object obj : otherCriteriaList.getContent()) {
          if (obj instanceof JAXBElement) {
            JAXBElement jaxbElement = (JAXBElement) obj;
            filterTypeV5(jaxbElement.getValue(), extended, certDna);
          } else if (obj instanceof Element) {
            Element element = (Element) obj;
            try {
              JAXBContext jaxbContext = JAXBContext
                  .newInstance(CertSubjectDNAttributeTypeJAXB.class,
                      ExtendedKeyUsageTypeJAXB.class);
              Object object = jaxbContext.createUnmarshaller().unmarshal(element);
              filterTypeV5(object, extended, certDna);
            } catch (Exception e) {
              e.printStackTrace();
              LOGGER.warn("Cannot read object AnyTypeJAXB(OtherCriteriaType) : " + e.getMessage());
            }
          }
        }
        if (CollectionUtils.isNotEmpty(extended)) {
          this.extendedKeyUsageList = extended;
        }
        if (CollectionUtils.isNotEmpty(certDna)) {
          this.certDnaList = certDna;
        }
      }
    }
  }

  private void filterTypeV5(Object obj,
      List<PoliciesBitType> extended,
      List<PoliciesBitType> certDna) {
    if (obj instanceof CertSubjectDNAttributeTypeJAXB) {
      CertSubjectDNAttributeTypeJAXB certSubjectJAXB = (CertSubjectDNAttributeTypeJAXB) obj;
      if (CollectionUtils.isNotEmpty(certSubjectJAXB.getAttributeOID())) {
        for (ObjectIdentifierTypeJAXB objectIdentifierJAXB : certSubjectJAXB
            .getAttributeOID()) {
          certDna.add(new PoliciesBitType(objectIdentifierJAXB));
        }
      }
    } else if (obj instanceof ExtendedKeyUsageTypeJAXB) {
      ExtendedKeyUsageTypeJAXB extendedKeyUsageJAXB = (ExtendedKeyUsageTypeJAXB) obj;
      if (CollectionUtils.isNotEmpty(extendedKeyUsageJAXB.getKeyPurposeId())) {
        for (ObjectIdentifierTypeJAXB objectIdentifierJAXB : extendedKeyUsageJAXB
            .getKeyPurposeId()) {
          extended.add(new PoliciesBitType(objectIdentifierJAXB));
        }
      }
    } else {
      LOGGER.warn("Cannot read object " + obj.getClass());
    }
  }

  public List<PoliciesBitType> getExtendedKeyUsageList() {
    return extendedKeyUsageList;
  }

  public void setExtendedKeyUsageList(
      List<PoliciesBitType> extendedKeyUsageList) {
    this.extendedKeyUsageList = extendedKeyUsageList;
  }

  public List<PoliciesBitType> getCertDnaList() {
    return certDnaList;
  }

  public void setCertDnaList(
      List<PoliciesBitType> certDnaList) {
    this.certDnaList = certDnaList;
  }
}
