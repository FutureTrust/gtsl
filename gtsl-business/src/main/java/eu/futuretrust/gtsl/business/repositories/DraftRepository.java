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

package eu.futuretrust.gtsl.business.repositories;

import eu.futuretrust.gtsl.persistence.entities.DraftEntity;
import eu.futuretrust.gtsl.persistence.entities.TerritoryEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DraftRepository extends MongoRepository<DraftEntity, String> {

  DraftEntity findById(String dbId);

  List<DraftEntity> findByHash(String hash);

  List<DraftEntity> findByTerritory(TerritoryEntity territoryEntity);

  Long deleteById(String dbId);

  Long deleteByTerritory(TerritoryEntity territoryEntity);

}

