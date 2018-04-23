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
import eu.futuretrust.gtsl.jaxb.tsl.AnyTypeJAXB;
import eu.futuretrust.gtsl.model.data.common.AnyType;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class TakenOverByType {

  private NonEmptyMultiLangURIType uri;
  private InternationalNamesType tspName;
  private InternationalNamesType operatorName;
  private String territory;
  private List<AnyType> otherQualifier;

  public TakenOverByType() {
    this.uri = new NonEmptyMultiLangURIType();
    this.tspName =  new InternationalNamesType();
    this.operatorName = new InternationalNamesType();
    this.territory  = "";
    this.otherQualifier = new ArrayList<>();
  }

  public TakenOverByType(TakenOverByTypeJAXB takenOverByJAXB) {
    if (StringUtils.isNotEmpty(takenOverByJAXB.getSchemeTerritory())) {
      this.territory = takenOverByJAXB.getSchemeTerritory();
    } else {
      this.territory = "";
    }
    if (takenOverByJAXB.getURI() != null) {
      this.uri = new NonEmptyMultiLangURIType(takenOverByJAXB.getURI());
    } else {
      this.uri = new NonEmptyMultiLangURIType();
    }
    if (takenOverByJAXB.getTSPName() != null) {
      this.tspName = new InternationalNamesType(takenOverByJAXB.getTSPName());
    } else {
      this.tspName = new InternationalNamesType();
    }
    if (takenOverByJAXB.getSchemeOperatorName() != null) {
      this.operatorName = new InternationalNamesType(takenOverByJAXB.getSchemeOperatorName());
    } else {
      this.operatorName = new InternationalNamesType();
    }
    if (CollectionUtils.isNotEmpty(takenOverByJAXB.getOtherQualifier())) {
      this.setOtherQualifier(takenOverByJAXB.getOtherQualifier().stream()
          .map(anyJAXB -> new AnyType(anyJAXB.getContent())).collect(Collectors.toList()));
    } else {
      this.setOtherQualifier(Collections.emptyList());
    }
  }

  public TakenOverByTypeJAXB asJAXB() {
    TakenOverByTypeJAXB takenOverByJAXB = new TakenOverByTypeJAXB();

    if (uri != null) {
      takenOverByJAXB.setURI(uri.asJAXB());
    }
    if (tspName != null) {
      takenOverByJAXB.setTSPName(tspName.asJAXB());
    }
    if (operatorName != null) {
      takenOverByJAXB.setSchemeOperatorName(operatorName.asJAXB());
    }
    if (territory != null) {
      takenOverByJAXB.setSchemeTerritory(territory);
    }
    if (CollectionUtils.isNotEmpty(otherQualifier)) {
      List<AnyTypeJAXB> anyList = otherQualifier.stream().map(str -> {
        AnyTypeJAXB any = new AnyTypeJAXB();
        any.getContent().add(str);
        return any;
      }).collect(Collectors.toList());
      takenOverByJAXB.getOtherQualifier().addAll(anyList);
    }

    return takenOverByJAXB;
  }

  public NonEmptyMultiLangURIType getUri() {
    return uri;
  }

  public void setUri(NonEmptyMultiLangURIType uri) {
    this.uri = uri;
  }

  public InternationalNamesType getTspName() {
    return tspName;
  }

  public void setTspName(InternationalNamesType tspName) {
    this.tspName = tspName;
  }

  public InternationalNamesType getOperatorName() {
    return operatorName;
  }

  public void setOperatorName(InternationalNamesType operatorName) {
    this.operatorName = operatorName;
  }

  public String getTerritory() {
    return territory;
  }

  public void setTerritory(String territory) {
    this.territory = territory;
  }

  public List<AnyType> getOtherQualifier() {
    return otherQualifier;
  }

  public void setOtherQualifier(List<AnyType> otherQualifier) {
    this.otherQualifier = otherQualifier;
  }

}
