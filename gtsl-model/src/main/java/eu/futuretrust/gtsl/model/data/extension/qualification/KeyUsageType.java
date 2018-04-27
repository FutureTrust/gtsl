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

import eu.futuretrust.gtsl.jaxb.sie.KeyUsageTypeJAXB;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class KeyUsageType {

  private List<KeyUsageBitType> keyUsageBitList;

  public KeyUsageType() {
  }

  public KeyUsageType(KeyUsageTypeJAXB keyUsageJAXB) {
    if (keyUsageJAXB != null && CollectionUtils.isNotEmpty(keyUsageJAXB.getKeyUsageBit())) {
      this.keyUsageBitList = keyUsageJAXB.getKeyUsageBit().stream().map(KeyUsageBitType::new)
          .collect(Collectors.toList());
    }
  }

  public KeyUsageTypeJAXB asJAXB() {
    KeyUsageTypeJAXB keyUsageJAXB = new KeyUsageTypeJAXB();
    if (CollectionUtils.isNotEmpty(keyUsageBitList)) {
      keyUsageJAXB.getKeyUsageBit()
          .addAll(
              keyUsageBitList.stream().map(KeyUsageBitType::asJAXB).collect(Collectors.toList()));
    }
    return keyUsageJAXB;
  }

  public List<KeyUsageBitType> getKeyUsageBitList() {
    return keyUsageBitList;
  }

  public void setKeyUsageBitList(
      List<KeyUsageBitType> keyUsageBitList) {
    this.keyUsageBitList = keyUsageBitList;
  }

}
