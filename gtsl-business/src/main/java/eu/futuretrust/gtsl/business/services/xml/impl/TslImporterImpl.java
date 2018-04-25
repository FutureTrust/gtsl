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

import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.services.ledger.TslLedgerService;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.business.services.version.VersionService;
import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.business.services.xml.TslImporter;
import eu.futuretrust.gtsl.business.utils.DebugUtils;
import eu.futuretrust.gtsl.business.utils.TslUtils;
import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TslImporterImpl implements TslImporter {

  private static final Logger LOGGER = LoggerFactory.getLogger(TslImporterImpl.class);

  private final JaxbService jaxbService;
  private final TslService tslService;
  private final TslLedgerService tslLedgerService;
  private final VersionService versionService;

  @Autowired
  public TslImporterImpl(JaxbService jaxbService, TslService tslService,
      TslLedgerService tslLedgerService, VersionService versionService) {
    this.jaxbService = jaxbService;
    this.tslService = tslService;
    this.tslLedgerService = tslLedgerService;
    this.versionService = versionService;
  }

  @Override
  public ReportDTO importTsl(MultipartFile file) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "importTsl");

    if (file != null && !file.isEmpty()) {
      TrustStatusListTypeJAXB jaxb = jaxbService.unmarshallTsl(file.getInputStream());
      TrustStatusListType tsl = new TrustStatusListType(jaxb);

      String tslIdentifier = TslUtils.extractTerritoryCode(tsl);
      String action;
      ReportDTO report;
      if (tslLedgerService.exists(tslIdentifier)) {
        action = "update";
        report = tslService.update(tsl);
      } else {
        action = "create";
        report = tslService.create(tsl);
      }
      if (LOGGER.isInfoEnabled()) {
        if (report.isValid()) {
          LOGGER.info("TSL {} has been imported successfully ({})", tslIdentifier, action);
        } else {
          LOGGER.info("TSL {} has not been imported because it contains error(s) ({})",
              tslIdentifier, action);
        }
      }
      return report;
    } else {
      throw new InvalidParameterException("No file provided");
    }
  }

  @Override
  public Optional<byte[]> exportTsl(String tlIdentifier) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "exportTsl");

    return tslService.readBytes(tlIdentifier);
  }

  @Override
  public Optional<byte[]> exportTsl(String tlIdentifier, BigInteger sequenceNumber)
      throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "exportTsl");

    return versionService.retrieveBytes(tlIdentifier, sequenceNumber);
  }

}
