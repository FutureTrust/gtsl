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

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Contact;
import com.mailjet.client.resource.Email;
import eu.futuretrust.gtsl.business.properties.MailProperties;
import eu.futuretrust.gtsl.business.properties.helper.Sender;
import eu.futuretrust.gtsl.business.services.notifications.MailingService;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailingServiceImpl implements MailingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MailingServiceImpl.class);

  private final MailjetClient client;
  private final Sender sender;

  @Autowired
  public MailingServiceImpl(MailProperties mailProperties) {
    client = new MailjetClient(mailProperties.getPublicKey(), mailProperties.getSecretKey());
    sender = mailProperties.getSender();
  }

  public void send(String territoryCode, Set<String> recipients)
      throws MailjetException, MailjetSocketTimeoutException {
    if (CollectionUtils.isNotEmpty(recipients)) {
      JSONArray recipientsJson = new JSONArray();
      recipients.forEach(mail -> {
        try {
          recipientsJson.put(
              new JSONObject().put(Contact.EMAIL, mail));
        } catch (JSONException ignored) {
        }
      });

      MailjetRequest request = new MailjetRequest(Email.resource)
          .property(Email.FROMEMAIL, sender.getMail())
          .property(Email.FROMNAME, sender.getName())
          .property(Email.SUBJECT, "Update on TSL : " + territoryCode)
          .property(Email.TEXTPART, "Trusted List " + territoryCode
              + " had been updated !\n Take a look on https://webgate.ec.europa.eu/tl-browser/#/tl/"
              + territoryCode)
          .property(Email.HTMLPART, "Trusted List " + territoryCode
              + " had been updated !\n Take a look on https://webgate.ec.europa.eu/tl-browser/#/tl/"
              + territoryCode)
          .property(Email.RECIPIENTS, recipientsJson);
      MailjetResponse response = client.post(request);
      LOGGER.info(response.getData().toString());
    }
    LOGGER.info(recipients.size()+" subscriber(s) has/have been notified");
  }

}
