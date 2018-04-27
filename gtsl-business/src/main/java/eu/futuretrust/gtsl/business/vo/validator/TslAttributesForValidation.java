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

package eu.futuretrust.gtsl.business.vo.validator;

import eu.futuretrust.gtsl.model.data.common.CountryCode;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIListType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import java.math.BigInteger;

public class TslAttributesForValidation {

  private CountryCode schemeTerritory;
  private NonEmptyURIType tslType;
  private NonEmptyMultiLangURIListType schemeInformationURI;
  private BigInteger currentSequenceNumber;

  private TspAttributesForValidation tspAttributes;

  public TslAttributesForValidation(CountryCode schemeTerritory,
      NonEmptyURIType tslType, NonEmptyMultiLangURIListType schemeInformationURI,
      BigInteger currentSequenceNumber) {
    this.schemeTerritory = schemeTerritory;
    this.tslType = tslType;
    this.schemeInformationURI = schemeInformationURI;
    this.currentSequenceNumber = currentSequenceNumber;
  }

  public CountryCode getSchemeTerritory() {
    return schemeTerritory;
  }

  public void setSchemeTerritory(CountryCode schemeTerritory) {
    this.schemeTerritory = schemeTerritory;
  }

  public NonEmptyURIType getTslType() {
    return tslType;
  }

  public void setTslType(NonEmptyURIType tslType) {
    this.tslType = tslType;
  }

  public NonEmptyMultiLangURIListType getSchemeInformationURI() {
    return schemeInformationURI;
  }

  public void setSchemeInformationURI(
      NonEmptyMultiLangURIListType schemeInformationURI) {
    this.schemeInformationURI = schemeInformationURI;
  }

  public TspAttributesForValidation getTspAttributes() {
    return tspAttributes;
  }

  public void setTspAttributes(
      TspAttributesForValidation tspAttributes) {
    this.tspAttributes = tspAttributes;
  }

  public BigInteger getCurrentSequenceNumber() {
    return currentSequenceNumber;
  }

  public void setCurrentSequenceNumber(BigInteger currentSequenceNumber) {
    this.currentSequenceNumber = currentSequenceNumber;
  }
}
