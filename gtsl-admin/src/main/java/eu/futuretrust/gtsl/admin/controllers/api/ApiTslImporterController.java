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
import eu.futuretrust.gtsl.business.dto.helper.ResultDTO;
import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.business.services.xml.TslImporter;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tsl/xml")
public class ApiTslImporterController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiTslImporterController.class);

  private final ApiService apiAuthorizedService;
  private final TslImporter tslImporter;

  public ApiTslImporterController(
      @Qualifier("apiAuthorized") ApiService apiAuthorizedService,
      TslImporter tslImporter) {
    this.apiAuthorizedService = apiAuthorizedService;
    this.tslImporter = tslImporter;
  }

  /**
   * Import a Trust Service List An authenticated and authorised user should be able to create a TSL
   * for a recognized territory or an application-specific purpose by importing a XML file. Primary
   * Actor: Administrator User Preconditions: None Postconditions: A signed/unsigned TSL is
   * created/updated.
   *
   * @param file XML file representing the TSL to be created
   * @return report containing the errors/warnings/info which occurred when validating the TSL
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<ReportDTO>> importXml(
      @RequestParam("fileToUpload") MultipartFile file) {
    return apiAuthorizedService.execute(() -> Optional.ofNullable(tslImporter.importTsl(file)));
  }

  /**
   * Export a Trust Service List User exports TSL information. Primary Actor: External User
   * Preconditions: Requested territory should exist on the ledger. Postconditions: Return the XML
   * file representing the TSL for the specified territory.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @return XML file representing the TSL for the specified territory
   */
  @RequestMapping(value = "/{territoryCode}", method = RequestMethod.GET)
  public ResponseEntity<Resource> exportXml(@PathVariable String territoryCode) {
    try {
      return tslImporter.exportTsl(territoryCode)
          .map(data -> ExportUtils.create(data, "TL_" + territoryCode + ".xml"))
          .orElse(ResponseEntity.notFound().build());
    } catch (Exception e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Unable to export TSL {}: {}", territoryCode, e.getMessage());
      }
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

}
