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
import eu.futuretrust.gtsl.jaxb.sie.CriteriaListTypeJAXB;
import eu.futuretrust.gtsl.jaxb.xades.AnyTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.ValidAssert;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.apache.commons.collections.CollectionUtils;

/**
 * 5.5.9.2.2 CriteriaList 5.5.9.2.2.0 General 5.5.9.2.2.1 KeyUsage 5.5.9.2.2.2 PolicySet 5.5.9.2.2.3
 * OtherCriteria
 *
 * <xsd:complexType name="CriteriaListType"> <xsd:sequence> <!--Specified in TS 119 612 v2.1.1
 * clause 5.5.9.2.2.1 KeyUsage--> <xsd:element name="KeyUsage" type="tns:KeyUsageType" minOccurs="0"
 * maxOccurs="unbounded"/> <!--Specified in TS 119 612 v2.1.1 clause 5.5.9.2.2.2 PolicySet-->
 * <xsd:element name="PolicySet" type="tns:PoliciesListType" minOccurs="0" maxOccurs="unbounded"/>
 * <xsd:element name="CriteriaList" type="tns:CriteriaListType" minOccurs="0"
 * maxOccurs="unbounded"/> <xsd:element name="Description" type="xsd:string" minOccurs="0"/>
 * <xsd:element name="otherCriteriaList" type="xades:AnyType" minOccurs="0"/> </xsd:sequence>
 * <xsd:attribute name="assert"> <xsd:simpleType> <xsd:restriction base="xsd:string">
 * <xsd:enumeration value="all"/> <xsd:enumeration value="atLeastOne"/> <xsd:enumeration
 * value="none"/> </xsd:restriction> </xsd:simpleType> </xsd:attribute> </xsd:complexType>
 */
public class CriteriaType {

  @ValidAssert
  private String asserts;

  private String description;
  private List<KeyUsageType> keyUsage;
  private List<PoliciesListType> policyList;
  private List<CriteriaType> criteriaList;
  private OtherCriteriaType otherList;

  public CriteriaType() {
    this.keyUsage = new ArrayList<>();
    this.policyList = new ArrayList<>();
    this.criteriaList = new ArrayList<>();
    this.otherList = new OtherCriteriaType();
  }

  public CriteriaType(CriteriaListTypeJAXB criteriaList) {
    this();
    if (criteriaList != null) {
      this.asserts = criteriaList.getAssert();
      this.description = criteriaList.getDescription();
      if (CollectionUtils.isNotEmpty(criteriaList.getKeyUsage())) {
        this.keyUsage = criteriaList.getKeyUsage().stream().map(KeyUsageType::new)
            .collect(Collectors.toList());
      }
      if (CollectionUtils.isNotEmpty(criteriaList.getCriteriaList())) {
        this.criteriaList = criteriaList.getCriteriaList().stream().map(CriteriaType::new)
            .collect(Collectors.toList());
      }
      if (CollectionUtils.isNotEmpty(criteriaList.getPolicySet())) {
        this.policyList = criteriaList.getPolicySet().stream().map(PoliciesListType::new)
            .collect(Collectors.toList());
      }
      if (criteriaList.getOtherCriteriaList() != null) {
        this.otherList = new OtherCriteriaType(criteriaList.getOtherCriteriaList());
      }
    }
  }

  // from TL-Manager
  public CriteriaListTypeJAXB asJAXB() {
    CriteriaListTypeJAXB criteriaListJAXB = new CriteriaListTypeJAXB();

    if (asserts != null) {
      criteriaListJAXB.setAssert(asserts);
    }
    if (description != null) {
      criteriaListJAXB.setDescription(description);
    }

    if (CollectionUtils.isNotEmpty(this.keyUsage)) {
      criteriaListJAXB.getKeyUsage().addAll(keyUsage.stream().map(KeyUsageType::asJAXB).collect(
          Collectors.toList()));
    }

    if (CollectionUtils.isNotEmpty(this.policyList)) {
      criteriaListJAXB.getPolicySet()
          .addAll(policyList.stream().map(PoliciesListType::asJAXB).collect(Collectors.toList()));
    }

    if (CollectionUtils.isNotEmpty(this.criteriaList)) {
      criteriaListJAXB.getCriteriaList()
          .addAll(criteriaList.stream().map(CriteriaType::asJAXB).collect(Collectors.toList()));
    }

    if (this.otherList != null) {
      if (CollectionUtils.isNotEmpty(this.otherList.getCertDnaList())) {
        CertSubjectDNAttributeTypeJAXB certSubjectDNAttrJAXB = new CertSubjectDNAttributeTypeJAXB();
        certSubjectDNAttrJAXB.getAttributeOID().addAll(
            otherList.getCertDnaList().stream().map(PoliciesBitType::asJAXB)
                .collect(Collectors.toList()));
        if (criteriaListJAXB.getOtherCriteriaList() == null) {
          criteriaListJAXB.setOtherCriteriaList(new AnyTypeJAXB());
        }
        criteriaListJAXB.getOtherCriteriaList().getContent()
            .add(new JAXBElement<>(new QName("http://uri.etsi.org/02231/v2/additionaltypes#",
                "CertSubjectDNAttribute"), CertSubjectDNAttributeTypeJAXB.class,
                certSubjectDNAttrJAXB));
      }

      if (CollectionUtils.isNotEmpty(this.otherList.getExtendedKeyUsageList())) {
        ExtendedKeyUsageTypeJAXB extendedKeyUsageJAXB = new ExtendedKeyUsageTypeJAXB();
        extendedKeyUsageJAXB.getKeyPurposeId().addAll(
            otherList.getExtendedKeyUsageList().stream().map(PoliciesBitType::asJAXB)
                .collect(Collectors.toList()));
        if (criteriaListJAXB.getOtherCriteriaList() == null) {
          criteriaListJAXB.setOtherCriteriaList(new AnyTypeJAXB());
        }
        criteriaListJAXB.getOtherCriteriaList().getContent().add(
            new JAXBElement<>(
                new QName("http://uri.etsi.org/02231/v2/additionaltypes#", "ExtendedKeyUsage"),
                ExtendedKeyUsageTypeJAXB.class, extendedKeyUsageJAXB));
      }
    }
    return criteriaListJAXB;
  }

  public String getAsserts() {
    return asserts;
  }

  public void setAsserts(String asserts) {
    this.asserts = asserts;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<KeyUsageType> getKeyUsage() {
    return keyUsage;
  }

  public void setKeyUsage(
      List<KeyUsageType> keyUsage) {
    this.keyUsage = keyUsage;
  }

  public List<PoliciesListType> getPolicyList() {
    return policyList;
  }

  public void setPolicyList(
      List<PoliciesListType> policyList) {
    this.policyList = policyList;
  }

  public List<CriteriaType> getCriteriaList() {
    return criteriaList;
  }

  public void setCriteriaList(
      List<CriteriaType> criteriaList) {
    this.criteriaList = criteriaList;
  }

  public OtherCriteriaType getOtherList() {
    return otherList;
  }

  public void setOtherList(OtherCriteriaType otherList) {
    this.otherList = otherList;
  }

}
