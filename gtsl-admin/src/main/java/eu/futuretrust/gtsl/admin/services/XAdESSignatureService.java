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

package eu.futuretrust.gtsl.admin.services;

import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.admin.nexu.SignatureInformation;
import java.util.Map;

public interface XAdESSignatureService {

  ReportDTO doSign(final String draftId,
      final SignatureInformation signatureInformation) throws Exception;

  Map<String, Object> getSignatureRequestMap(final String draftId, final String keyId,
      final String tokenId, final String base64Cert, final String[] base64Chain) throws Exception;
}
