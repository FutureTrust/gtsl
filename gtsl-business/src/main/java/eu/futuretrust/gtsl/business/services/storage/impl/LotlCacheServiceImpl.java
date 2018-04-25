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

package eu.futuretrust.gtsl.business.services.storage.impl;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.client.http.NativeHTTPDataLoader;
import eu.europa.esig.dss.client.http.commons.FileCacheDataLoader;
import eu.futuretrust.gtsl.business.properties.LotlProperties;
import eu.futuretrust.gtsl.business.services.storage.LotlCacheService;
import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LotlCacheServiceImpl implements LotlCacheService {

  private final JaxbService jaxbService;
  private final LotlProperties lotlProperties;
  private final FileCacheDataLoader fileCache;

  @Autowired
  public LotlCacheServiceImpl(JaxbService jaxbService, final LotlProperties lotlProperties) {
    this.jaxbService = jaxbService;
    this.lotlProperties = lotlProperties;
    this.fileCache = new FileCacheDataLoader();
    this.fileCache.setCacheExpirationTime(lotlProperties.getCacheValidityWindow());
    this.fileCache.addToBeLoaded(lotlProperties.getXmlUrl());
    this.fileCache.setDataLoader(new NativeHTTPDataLoader());
    if (StringUtils.isNotBlank(lotlProperties.getFileCachePath())) {
      this.fileCache.setFileCacheDirectory(new File(lotlProperties.getFileCachePath()));
    }
  }

  public DSSDocument getLotlAsDSSDocument() {

    byte[] bytes = fileCache.get(lotlProperties.getXmlUrl());
    return new InMemoryDocument(bytes);
  }

  public TrustStatusListType getLotlAsTSLType() throws IOException {

    byte[] bytes = fileCache.get(lotlProperties.getXmlUrl());
    return new TrustStatusListType(jaxbService.unmarshallTsl(bytes));
  }
}
