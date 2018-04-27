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

package eu.futuretrust.gtsl.ledger.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.ObjectMapperFactory;

public final class WalletFileUtils {

  private WalletFileUtils() {}

  public static WalletFile retrieveWallet(InputStream keystore) throws IOException {
    ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    return objectMapper.readValue(keystore, WalletFile.class);
  }

  public static WalletFile retrieveWallet(byte[] keystore) throws IOException {
    ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    return objectMapper.readValue(keystore, WalletFile.class);
  }

}
