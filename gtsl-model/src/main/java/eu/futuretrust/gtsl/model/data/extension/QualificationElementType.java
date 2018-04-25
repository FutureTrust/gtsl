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

import eu.futuretrust.gtsl.jaxb.sie.CriteriaListTypeJAXB;
import eu.futuretrust.gtsl.jaxb.sie.QualificationElementTypeJAXB;
import eu.futuretrust.gtsl.jaxb.sie.QualifierTypeJAXB;
import eu.futuretrust.gtsl.jaxb.sie.QualifiersTypeJAXB;
import eu.futuretrust.gtsl.model.data.extension.qualification.CriteriaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <xsd:complexType name="QualificationElementType"> <xsd:sequence> <!--Specified in TS 119 612
 * v2.1.1 clause 5.5.9.2.3 Qualifier--> <xsd:element name="Qualifiers" type="tns:QualifiersType"/>
 * <!--Specified in TS 119 612 v2.1.1 clause 5.5.9.2.2 CriteriaList--> <xsd:element
 * name="CriteriaList" type="tns:CriteriaListType"/> </xsd:sequence> </xsd:complexType>
 */
public class QualificationElementType {

  private List<String> qualificationList;
  private CriteriaType criteria;

  public QualificationElementType() {
    this.qualificationList = new ArrayList<>();
    this.criteria = new CriteriaType();
  }

  public QualificationElementType(QualificationElementTypeJAXB qElementJAXB) {
    this();
    if (qElementJAXB != null) {
      if (qElementJAXB.getQualifiers() != null) {
        this.qualificationList = qElementJAXB.getQualifiers().getQualifier().stream()
          .map(QualifierTypeJAXB::getUri).collect(Collectors.toList());
      }
      if (qElementJAXB.getCriteriaList() != null) {
        this.criteria = new CriteriaType(qElementJAXB.getCriteriaList());
      }
    }
  }

  public QualificationElementTypeJAXB asJAXB() {
    QualificationElementTypeJAXB qualificationElementJAXB = new QualificationElementTypeJAXB();
    CriteriaListTypeJAXB criteriaListJAXB = new CriteriaListTypeJAXB();
    if (this.criteria != null) {
      criteriaListJAXB = criteria.asJAXB();
    }
    qualificationElementJAXB.setCriteriaList(criteriaListJAXB);
    if (this.qualificationList != null) {
      if (qualificationElementJAXB.getQualifiers() == null) {
        qualificationElementJAXB.setQualifiers(new QualifiersTypeJAXB());
      }
      for (String uri : this.qualificationList) {
        QualifierTypeJAXB qualifierJAXB = new QualifierTypeJAXB();
        qualifierJAXB.setUri(uri);
        qualificationElementJAXB.getQualifiers().getQualifier().add(qualifierJAXB);
      }
    }
    return qualificationElementJAXB;
  }

  public List<String> getQualificationList() {
    return qualificationList;
  }

  public void setQualificationList(List<String> qualificationList) {
    this.qualificationList = qualificationList;
  }

  public CriteriaType getCriteria() {
    return criteria;
  }

  public void setCriteria(CriteriaType criteria) {
    this.criteria = criteria;
  }

}
