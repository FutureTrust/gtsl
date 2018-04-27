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

package eu.futuretrust.gtsl.business.services.wallet.impl;

import eu.futuretrust.gtsl.business.properties.EthereumProperties;
import eu.futuretrust.gtsl.business.services.wallet.WalletService;
import eu.futuretrust.gtsl.ledger.utils.WalletFileUtils;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.web3j.crypto.WalletFile;

@Service
public class WalletServiceImpl implements WalletService {

  private static final Logger LOGGER = LoggerFactory.getLogger(WalletServiceImpl.class);

  private final EthereumProperties ethereumProperties;

  @Autowired
  public WalletServiceImpl(
      EthereumProperties ethereumProperties) {
    this.ethereumProperties = ethereumProperties;
  }

  @Override
  public Optional<WalletFile> retrieve() {
    try {
      return Optional.ofNullable(WalletFileUtils.retrieveWallet(
          new ClassPathResource(ethereumProperties.getKeystorePath()).getInputStream()));
    } catch (IOException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("No keystore found");
      }
      return Optional.empty();
    }
  }

}
