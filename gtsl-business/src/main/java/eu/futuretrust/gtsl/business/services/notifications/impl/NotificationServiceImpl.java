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

package eu.futuretrust.gtsl.business.services.notifications.impl;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import eu.futuretrust.gtsl.business.repositories.SubscriberRepository;
import eu.futuretrust.gtsl.business.repositories.TerritoryRepository;
import eu.futuretrust.gtsl.business.repositories.TslRepository;
import eu.futuretrust.gtsl.business.services.ledger.TslLedgerService;
import eu.futuretrust.gtsl.business.services.notifications.MailingService;
import eu.futuretrust.gtsl.business.services.notifications.NotificationService;
import eu.futuretrust.gtsl.ledger.vo.Tsl;
import eu.futuretrust.gtsl.persistence.entities.SubscriberEntity;
import eu.futuretrust.gtsl.persistence.entities.TerritoryEntity;
import eu.futuretrust.gtsl.persistence.entities.TslEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

  private final TerritoryRepository territoryRepository;
  private final SubscriberRepository subscriberRepository;
  private final TslRepository tslRepository;
  private final TslLedgerService tslLedgerService;
  private final MailingService mailingService;
  private final MongoTemplate mongoTemplate;

  @Autowired
  public NotificationServiceImpl(
      TerritoryRepository territoryRepository,
      SubscriberRepository subscriberRepository,
      TslRepository tslRepository,
      TslLedgerService tslLedgerService,
      MailingService mailingService,
      MongoTemplate mongoTemplate) {
    this.territoryRepository = territoryRepository;
    this.subscriberRepository = subscriberRepository;
    this.tslRepository = tslRepository;
    this.tslLedgerService = tslLedgerService;
    this.mailingService = mailingService;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public boolean subscribe(String territoryCode, String email) throws Exception {
    if (StringUtils.isBlank(email)) {
      return false;
    }

    if (tslLedgerService.exists(territoryCode)) {
      String trimmedEmail = email.trim();

      TerritoryEntity territory = territoryRepository.findByCode(territoryCode);
      if (territory == null) {
        if (LOGGER.isWarnEnabled()) {
          LOGGER.warn(
              "Tsl {} has not been found in local database when the user {} tried to subscribe",
              territoryCode, trimmedEmail);
        }
        territory = territoryRepository.save(new TerritoryEntity(territoryCode));
        if (LOGGER.isInfoEnabled()) {
          LOGGER.info("Creation of the territory {} with ID {}", territory.getCode(),
              territory.getId());
        }
      }

      SubscriberEntity subscriber = subscriberRepository.findByEmail(trimmedEmail);
      if (subscriber == null) {
        subscriber = subscriberRepository.save(new SubscriberEntity(trimmedEmail));
        if (LOGGER.isInfoEnabled()) {
          LOGGER.info("Creation of the subscriber {} with ID {}", subscriber.getEmail(),
              subscriber.getId());
        }
      }

      subscriber.getTerritories().add(territory);
      subscriberRepository.save(subscriber);

      // TODO: send email confirmation

      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("Subscriber {} has subscribed to {}", trimmedEmail, territoryCode);
      }
      return true;
    }

    if (LOGGER.isWarnEnabled()) {
      LOGGER.warn("Tsl {} has not been found", territoryCode);
    }
    return false;
  }

  @Override
  public void unsubscribe(String territoryCode, String email) throws Exception {
    if (StringUtils.isBlank(email)) {
      return;
    }

    if (tslLedgerService.exists(territoryCode)) {
      String trimmedEmail = email.trim();

      TerritoryEntity territory = territoryRepository.findByCode(territoryCode);
      if (territory == null) {
        if (LOGGER.isWarnEnabled()) {
          LOGGER
              .warn(
                  "Tsl {} has not been found in local database when the user {} tried to unsubscribe",
                  territoryCode, trimmedEmail);
        }
        return;
      }

      SubscriberEntity subscriber = subscriberRepository.findByEmail(trimmedEmail);
      if (subscriber == null) {
        if (LOGGER.isWarnEnabled()) {
          LOGGER
              .warn("Subscriber {} has not been found in local database when trying to unsubscribe",
                  territoryCode, trimmedEmail);
        }
        return;
      }

      subscriber.getTerritories().remove(territory);
      subscriberRepository.save(subscriber);

      // TODO: send email confirmation unsubscription

      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("Subscriber {} has unsubscribed to {}", trimmedEmail, territoryCode);
      }
    }
  }

  @Override
  public void unsubscribe(String email) {
    String trimmedEmail = email.trim();
    subscriberRepository.deleteByEmail(trimmedEmail);

    // TODO: send email confirmation unsubscription

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Subscriber {} has unsubscribed to all TSL", trimmedEmail);
    }
  }

  @Override
  public void notifyUsers() {
    try {
      List<Tsl> allTsl = tslLedgerService.findAll();
      for (Tsl tsl : allTsl) {
        TerritoryEntity territory = territoryRepository.findByCode(tsl.getCountryCode());
        if (territory == null) {
          territory = territoryRepository.save(new TerritoryEntity(tsl.getCountryCode()));
        }

        TslEntity tslEntity = tslRepository.findByTerritory(territory);
        if (tslEntity == null) {
          // Tsl is not registered in the database
          tslRepository.save(new TslEntity(territory, tsl.getAddress()));
          sendEmailToSubscribers(tsl.getCountryCode());
        } else {
          // Tsl is already registered in the database
          String hashInDb = tslEntity.getHash();
          if (!hashInDb.equals(tsl.getAddress())) {
            // Tsl contains changes
            tslEntity.setHash(tsl.getAddress());
            tslRepository.save(tslEntity);
            sendEmailToSubscribers(tsl.getCountryCode());
          } else {
            LOGGER.info("No changes detected on TSL \"" + tsl.getCountryCode() + "\"");
          }
        }
      }
    } catch (Exception e) {
      LOGGER.error("Unable to notify users : " + e.getMessage());
    }
  }

  private void sendEmailToSubscribers(String territoryCode) {
    TerritoryEntity territory = territoryRepository.findByCode(territoryCode.trim());
    if (territory == null) {
      return;
    }

    Query query = new Query();
    query.addCriteria(
        Criteria.where("territories").in(territory)
    );
    List<SubscriberEntity> subscribers = mongoTemplate.find(query, SubscriberEntity.class);
    mailingService.send(territoryCode,
        subscribers.stream().map(SubscriberEntity::getEmail).collect(Collectors.toSet()));
  }

}
