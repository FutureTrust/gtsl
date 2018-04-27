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

package eu.futuretrust.gtsl.business.services.message.impl;

import eu.futuretrust.gtsl.business.services.message.MessageService;
import eu.futuretrust.gtsl.business.validator.MessageCode;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService, MessageSourceAware {

  private MessageSource messageSource;

  @Autowired
  public MessageServiceImpl(MessageSource messageSource) {
    this.setMessageSource(messageSource);
  }

  @Override
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public String getMessage(MessageCode code) {
    return messageSource.getMessage(code.toString(), null, Locale.ENGLISH);
  }

  @Override
  public String getMessage(MessageCode code, Object... args) {
    return messageSource.getMessage(code.toString(), args, Locale.ENGLISH);
  }

  @Override
  public String getMessage(MessageCode code, Locale locale, Object... args) {
    return messageSource.getMessage(code.toString(), args, locale);
  }
}
