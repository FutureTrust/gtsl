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

package eu.futuretrust.gtsl.model.data.tsl;

import eu.futuretrust.gtsl.jaxb.tsl.PolicyOrLegalnoticeTypeJAXB;
import eu.futuretrust.gtsl.model.data.common.MultiLangStringType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIType;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.11 TSL policy/legal notice
 *
 * <xsd:complexType name="PolicyOrLegalnoticeType"> <xsd:choice> <xsd:element name="TSLPolicy"
 * type="tsl:NonEmptyMultiLangURIType" maxOccurs="unbounded"/> <xsd:element name="TSLLegalNotice"
 * type="tsl:MultiLangStringType" maxOccurs="unbounded"/> </xsd:choice> </xsd:complexType>
 *
 * XML Schema choice element allows only one of the elements contained in the <choice> declaration
 * to be present within the containing element.
 */
public class PolicyOrLegalnoticeType {

  protected TSLPolicy policy;

  protected TSLLegalNotice legalNotice;

  public PolicyOrLegalnoticeType() {
    this.legalNotice = new TSLLegalNotice();
    this.policy = new TSLPolicy();
  }

  public PolicyOrLegalnoticeType(PolicyOrLegalnoticeTypeJAXB policyOrLegalNotice) {
    if (policyOrLegalNotice != null) {
      if (CollectionUtils.isNotEmpty(policyOrLegalNotice.getTSLLegalNotice())) {
        this.legalNotice = new TSLLegalNotice(policyOrLegalNotice.getTSLLegalNotice().stream().map(
            MultiLangStringType::new).collect(Collectors.toList()));
      } else {
        this.legalNotice = new TSLLegalNotice();
      }
      if (CollectionUtils.isNotEmpty(policyOrLegalNotice.getTSLPolicy())) {
        this.policy = new TSLPolicy(policyOrLegalNotice.getTSLPolicy().stream().map(
            NonEmptyMultiLangURIType::new).collect(Collectors.toList()));
      } else {
        this.policy = new TSLPolicy();
      }
    }
  }

  public PolicyOrLegalnoticeTypeJAXB asJAXB() {
    PolicyOrLegalnoticeTypeJAXB policyOrLegalnotice = new PolicyOrLegalnoticeTypeJAXB();
    if (legalNotice != null && CollectionUtils.isNotEmpty(legalNotice.getValues())) {
      policyOrLegalnotice.getTSLLegalNotice().addAll(legalNotice.asJAXB());
    } else if (policy != null && CollectionUtils.isNotEmpty(policy.getValues())) {
      policyOrLegalnotice.getTSLPolicy().addAll(policy.asJAXB());
    }
    return policyOrLegalnotice;
  }

  public TSLPolicy getPolicy() {
    return policy;
  }

  public void setPolicy(TSLPolicy policy) {
    this.policy = policy;
  }

  public TSLLegalNotice getLegalNotice() {
    return legalNotice;
  }

  public void setLegalNotice(
      TSLLegalNotice legalNotice) {
    this.legalNotice = legalNotice;
  }
}
