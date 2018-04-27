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

package eu.futuretrust.gtsl.web.controllers.api;

import eu.futuretrust.gtsl.business.dto.helper.ResultDTO;
import eu.futuretrust.gtsl.business.dto.notifications.EmailDTO;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.business.services.notifications.NotificationService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class ApiNotificationController {

  private final ApiService apiService;
  private final NotificationService notificationService;

  public ApiNotificationController(
      @Qualifier("apiPublic") ApiService apiService,
      NotificationService notificationService) {
    this.apiService = apiService;
    this.notificationService = notificationService;
  }

  /**
   * Subscribe to receive notifications when changes are detected on the TSL The system allows users
   * to subscribe to different notification channels. Primary Actor: External User Preconditions:
   * None Postconditions: User is subscribed to a notification channel.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @param emailDTO object containing email of the user
   */
  @RequestMapping(value = "{territoryCode}", method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<Boolean>> subscribe(@PathVariable String territoryCode,
      @RequestBody EmailDTO emailDTO) {
    return apiService.execute(
        () -> Optional.of(notificationService.subscribe(territoryCode, emailDTO.getEmail())));
  }

  /**
   * Unsubscribe to not receive notifications anymore for the specified territory The system allows
   * users to unsubscribe to subscribed notification channels. Primary Actor: External User
   * Preconditions: None Postconditions: User is unsubscribed to a notification channel.
   *
   * @param territoryCode Scheme Territory of the TSL
   * @param emailDTO object containing email of the user
   */
  @RequestMapping(value = "{territoryCode}", method = RequestMethod.DELETE)
  public ResponseEntity<ResultDTO<Boolean>> unsubscribe(@PathVariable String territoryCode,
      @RequestBody EmailDTO emailDTO) {
    return apiService.execute(() -> {
      notificationService.unsubscribe(territoryCode, emailDTO.getEmail());
      return Optional.of(true);
    });
  }

  /**
   * Unsubscribe to not receive notifications anymore for all territories The system allows users to
   * unsubscribe to subscribed notification channels. Primary Actor: External User Preconditions:
   * None Postconditions: User is unsubscribed to a notification channel.
   *
   * @param emailDTO object containing email of the user
   */
  @RequestMapping(method = RequestMethod.DELETE)
  public ResponseEntity<ResultDTO<Boolean>> unsubscribe(@RequestBody EmailDTO emailDTO) {
    return apiService.execute(() -> {
      notificationService.unsubscribe(emailDTO.getEmail());
      return Optional.of(true);
    });
  }

}
