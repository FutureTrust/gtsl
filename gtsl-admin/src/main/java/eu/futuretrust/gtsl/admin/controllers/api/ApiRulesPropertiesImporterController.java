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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.futuretrust.gtsl.business.utils.ExportUtils;
import eu.futuretrust.gtsl.business.dto.helper.ResultDTO;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.business.services.rules.RulesPropertiesService;
import eu.futuretrust.gtsl.properties.RulesProperties;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tsl/properties")
public class ApiRulesPropertiesImporterController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ApiRulesPropertiesImporterController.class);

  private final ApiService apiService;
  private final RulesPropertiesService rulesPropertiesService;

  public ApiRulesPropertiesImporterController(
      @Qualifier("apiAuthorized") ApiService apiService,
      RulesPropertiesService rulesPropertiesService) {
    this.apiService = apiService;
    this.rulesPropertiesService = rulesPropertiesService;
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<Boolean>> importJson(
      @RequestParam("fileToUpload") MultipartFile file) {
    return apiService.execute(() -> {
      rulesPropertiesService.update(file.getInputStream());
      return Optional.of(true);
    });
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<Resource> exportJson() {
    try {
      return rulesPropertiesService.retrieve()
          .map(properties -> {
            try {
              return new ObjectMapper().writeValueAsBytes(properties);
            } catch (JsonProcessingException e) {
              if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Unable to convert RulesProperties object into JSON object");
              }
              return null;
            }
          })
          .map(data -> ExportUtils.create(data, "rules.json"))
          .orElse(ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

}
