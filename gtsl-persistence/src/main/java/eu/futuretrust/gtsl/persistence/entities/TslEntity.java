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

package eu.futuretrust.gtsl.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="tsl")
public class TslEntity {

  @Id
  private String id;

  @Indexed
  @DBRef
  private TerritoryEntity territory;

  private String hash;

  public TslEntity() {}

  public TslEntity(String id, TerritoryEntity territory, String hash) {
    this.id = id;
    this.hash = hash;
    this.territory = territory;
  }

  public TslEntity(TerritoryEntity territory, String hash) {
    this.hash = hash;
    this.territory = territory;
  }

  @Override
  public String toString() {
    return "SubscriberEntity{" +
        "id='" + id + '\'' +
        ", hash='" + hash + '\'' +
        ", territoryCode='" + territory + '\'' +
        '}';
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public TerritoryEntity getTerritory() {
    return territory;
  }

  public void setTerritory(TerritoryEntity territory) {
    this.territory = territory;
  }
}
