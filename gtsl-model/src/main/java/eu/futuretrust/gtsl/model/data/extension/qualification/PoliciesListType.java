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

import eu.futuretrust.gtsl.jaxb.sie.PoliciesListTypeJAXB;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class PoliciesListType {

  private List<PoliciesBitType> policyBitList;

  public PoliciesListType() {
  }

  public PoliciesListType(PoliciesListTypeJAXB policiesListJAXB) {
    this.policyBitList = policiesListJAXB.getPolicyIdentifier().stream().map(PoliciesBitType::new)
        .collect(Collectors.toList());
  }

  public List<PoliciesBitType> getPolicyBitList() {
    return policyBitList;
  }

  public void setPolicyBitList(List<PoliciesBitType> policyBitList) {
    this.policyBitList = policyBitList;
  }

  public PoliciesListTypeJAXB asJAXB() {
    PoliciesListTypeJAXB policiesListJAXB = new PoliciesListTypeJAXB();
    if (CollectionUtils.isNotEmpty(policyBitList)) {
      policiesListJAXB.getPolicyIdentifier()
          .addAll(policyBitList.stream().map(PoliciesBitType::asJAXB).collect(Collectors.toList()));
    }
    return policiesListJAXB;
  }
}
