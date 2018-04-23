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

import eu.futuretrust.gtsl.admin.controllers.api.helpers.ExportUtils;
import eu.futuretrust.gtsl.business.dto.helper.ResultDTO;
import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.business.services.xml.DraftImporter;
import java.util.Optional;
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
@RequestMapping("/api/draft/xml")
public class ApiDraftImporterController {

  private final ApiService apiService;
  private final DraftImporter draftImporter;

  public ApiDraftImporterController(
      @Qualifier("apiAuthorized") ApiService apiService,
      DraftImporter draftImporter) {
    this.apiService = apiService;
    this.draftImporter = draftImporter;
  }

  /**
   * Import a Draft An authenticated and authorised user should be able to create a draft of TSL for
   * a recognized territory or an application-specific purpose by importing a XML file. Primary
   * Actor: Administrator User Preconditions: None Postconditions: A signed/unsigned TSL is
   * created.
   *
   * @param file XML file representing the TSL to be created
   * @return report containing the errors/warnings/info which occurred when validating the TSL
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<ReportDTO>> importXml(
      @RequestParam("fileToUpload") MultipartFile file) {
    return apiService.execute(() -> Optional.ofNullable(draftImporter.importTsl(file)));
  }

  /**
   * Export a Draft User exports TSL information. Primary Actor: External User Preconditions:
   * Requested Id should exist on the database. Postconditions: Return the XML file representing the
   * TSL for the specified Id.
   *
   * @param dbId Id in database
   * @return XML file representing the TSL for the specified Id
   */
  @RequestMapping(value = "/db/{dbId}", method = RequestMethod.GET)
  public ResponseEntity<Resource> exportXml(@PathVariable String dbId) {
    try {
      return draftImporter.exportTsl(dbId)
          .map(data -> ExportUtils.create(data, "TL_" + dbId + ".xml"))
          .orElse(ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

}
