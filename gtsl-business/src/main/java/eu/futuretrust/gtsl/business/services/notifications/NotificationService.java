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

package eu.futuretrust.gtsl.business.services.notifications;

public interface NotificationService {

  /**
   * Subscribe to the TSL
   *
   * @param territoryCode country code of the TSL to subscribe
   * @param email email of the user
   * @return true if the user successfully subscribed to the TSL
   */
  boolean subscribe(String territoryCode, String email) throws Exception;

  void unsubscribe(String territoryCode, String email) throws Exception;

  void unsubscribe(String email);

  void notifyUsers();
}
