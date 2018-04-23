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

package eu.futuretrust.gtsl.business.vo.draft;

import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;

public class DraftVO {

  private String dbId;
  private TrustStatusListType tsl;

  public DraftVO() {
  }

  public DraftVO(String dbId, TrustStatusListType tsl) {
    this.dbId = dbId;
    this.tsl = tsl;
  }

  public String getDbId() {
    return dbId;
  }

  public void setDbId(String dbId) {
    this.dbId = dbId;
  }

  public TrustStatusListType getTsl() {
    return tsl;
  }

  public void setTsl(TrustStatusListType tsl) {
    this.tsl = tsl;
  }
}
