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

package eu.futuretrust.gtsl.admin.controllers.api;

import eu.futuretrust.gtsl.business.utils.ExportUtils;
import eu.futuretrust.gtsl.business.services.xml.TslImporter;
import java.math.BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tsl/version/xml")
public class ApiVersionImporterController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiVersionImporterController.class);

  private final TslImporter tslImporter;

  public ApiVersionImporterController(TslImporter tslImporter) {
    this.tslImporter = tslImporter;
  }

  /**
   * Export a Trust Service List for the specified version User exports TSL information. Primary
   * Actor: External User Preconditions: Requested territory should exist on the ledger.
   * Postconditions: Return the XML file representing the TSL for the specified territory.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @return XML file representing the TSL for the specified territory
   */
  @RequestMapping(value = "{territoryCode}/{versionNumber}", method = RequestMethod.GET)
  public ResponseEntity<Resource> exportXmlVersion(@PathVariable String territoryCode,
      @PathVariable BigInteger versionNumber) {
    try {
      return tslImporter.exportTsl(territoryCode, versionNumber)
          .map(data -> ExportUtils
              .create(data, "TL_" + territoryCode + "_" + versionNumber + ".xml"))
          .orElse(ResponseEntity.notFound().build());
    } catch (Exception e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Unable to export version {} of TSL {}: {}", versionNumber, territoryCode,
            e.getMessage());
      }
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

}
