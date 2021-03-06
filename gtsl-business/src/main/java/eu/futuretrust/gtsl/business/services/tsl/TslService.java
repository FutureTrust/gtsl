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

package eu.futuretrust.gtsl.business.services.tsl;

import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.vo.draft.DraftVO;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.util.List;
import java.util.Optional;

public interface TslService {

  ReportDTO create(TrustStatusListType tsl) throws Exception;

  ReportDTO update(TrustStatusListType tsl) throws Exception;

  void remove(String tlIdentifier) throws Exception;

  Optional<byte[]> readBytes(String tlIdentifier) throws Exception;

  Optional<TrustStatusListType> read(String tlIdentifier) throws Exception;

  List<TrustStatusListType> readAll() throws Exception;

  Optional<ReportDTO> validate(String territoryCode) throws Exception;

  ReportDTO push(DraftVO draft) throws Exception;
}
