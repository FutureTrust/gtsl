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

package eu.futuretrust.gtsl.business.services.xml.impl;

import eu.futuretrust.gtsl.business.services.draft.DraftService;
import eu.futuretrust.gtsl.business.services.xml.DraftImporter;
import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.business.utils.DebugUtils;
import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.security.InvalidParameterException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DraftImporterImpl implements DraftImporter {

  private static final Logger LOGGER = LoggerFactory.getLogger(DraftImporterImpl.class);

  private final JaxbService jaxbService;
  private final DraftService draftService;

  @Autowired
  public DraftImporterImpl(JaxbService jaxbService, DraftService draftService) {
    this.jaxbService = jaxbService;
    this.draftService = draftService;
  }

  @Override
  public ReportDTO importTsl(MultipartFile file) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "importTsl");

    if (file != null && !file.isEmpty()) {
      TrustStatusListTypeJAXB jaxb = jaxbService.unmarshallTsl(file.getInputStream());
      TrustStatusListType tsl = new TrustStatusListType(jaxb);
      return draftService.create(tsl);
    } else {
      throw new InvalidParameterException("No file provided");
    }
  }

  @Override
  public Optional<byte[]> exportTsl(String dbId) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "exportTsl");

    return draftService.readBytes(dbId);
  }

}
