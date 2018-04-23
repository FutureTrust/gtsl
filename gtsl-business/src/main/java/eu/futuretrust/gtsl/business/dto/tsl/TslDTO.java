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

package eu.futuretrust.gtsl.business.dto.tsl;

import java.math.BigInteger;
import java.util.Map;

public class TslDTO {

  private String countryName;
  private String issueDate;
  private String name;
  private String nextUpdate;
  private BigInteger sequenceNumber;
  private String signature;
  private String territoryCode;
  private String tlType;

  private Map<String, String> urls;

  public TslDTO(String countryName,
      String issueDate,
      String name,
      String nextUpdate,
      BigInteger sequenceNumber,
      String signature,
      String territoryCode,
      String tlType, Map<String, String> urls) {
    this.countryName = countryName;
    this.name = name;
    this.nextUpdate = nextUpdate;
    this.sequenceNumber = sequenceNumber;
    this.signature = signature;
    this.territoryCode = territoryCode;
    this.tlType = tlType;
    this.issueDate = issueDate;
    this.urls = urls;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigInteger getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(BigInteger sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public String getTerritoryCode() {
    return territoryCode;
  }

  public void setTerritoryCode(String territoryCode) {
    this.territoryCode = territoryCode;
  }

  public String getTlType() {
    return tlType;
  }

  public void setTlType(String tlType) {
    this.tlType = tlType;
  }


  public String getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(String issueDate) {
    this.issueDate = issueDate;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public String getNextUpdate() {
    return nextUpdate;
  }

  public void setNextUpdate(String nextUpdate) {
    this.nextUpdate = nextUpdate;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public Map<String, String> getUrls() {
    return urls;
  }

  public void setUrls(Map<String, String> urls) {
    this.urls = urls;
  }
}
