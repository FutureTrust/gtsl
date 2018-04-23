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
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPinScheduler extends AbstractScheduler {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPinScheduler.class);

  protected final StorageService storageService;
  protected final IPFSProperties ipfsProperties;
  protected final TslLedgerService tslLedgerService;

  protected AbstractPinScheduler(
      StorageService storageService,
      IPFSProperties ipfsProperties,
      TslLedgerService tslLedgerService) {
    this.storageService = storageService;
    this.ipfsProperties = ipfsProperties;
    this.tslLedgerService = tslLedgerService;
  }

  @Override
  public abstract void run();

  protected void scheludePin(Function<String, Boolean> pinFunction, boolean enabled) {
    if (enabled) {
      try {
        tslLedgerService.findAll().forEach(tsl -> {
          boolean success = pinFunction.apply(tsl.getAddress());
          if (LOGGER.isInfoEnabled()) {
            if (success) {
              LOGGER.info("TSL {} has been successfully pinned at address {}", tsl.getCountryCode(), tsl.getAddress());
            } else {
              LOGGER.info("TSL {} has not been pinned at address {}", tsl.getCountryCode(), tsl.getAddress());
            }
          }
        });
      } catch (Exception e) {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error("Error when pinning: {}", e.getMessage());
        }
      }
    }
  }

}
