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

package eu.futuretrust.gtsl.business.scheduled;

import eu.futuretrust.gtsl.business.properties.IPFSProperties;
import eu.futuretrust.gtsl.business.services.ledger.TslLedgerService;
import eu.futuretrust.gtsl.business.services.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PinAllScheduler extends AbstractPinScheduler {

  private static final Logger LOGGER = LoggerFactory.getLogger(PinAllScheduler.class);

  @Autowired
  public PinAllScheduler(
      StorageService storageService,
      TslLedgerService tslLedgerService,
      IPFSProperties ipfsProperties) {
    super(storageService, ipfsProperties, tslLedgerService);
  }

  @Override
  public void run() {
    scheludePin(commitAddress -> {
      try {
        return storageService.pinAllVersions(commitAddress);
      } catch (Exception e) {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error("Error when pinning: {}", e.getMessage());
        }
        return false;
      }
    }, ipfsProperties.getCronAll().isEnabled());
  }

  @Override
  public void initializeScheduler() {
    this.reSchedule(ipfsProperties.getCronAll());
  }
}
