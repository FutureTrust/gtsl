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

package eu.futuretrust.gtsl.business.services.version.impl;

import eu.futuretrust.gtsl.business.services.additional.TslAdditionalInformationService;
import eu.futuretrust.gtsl.business.services.ledger.TslLedgerService;
import eu.futuretrust.gtsl.business.services.storage.StorageService;
import eu.futuretrust.gtsl.business.services.version.VersionService;
import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.business.utils.DebugUtils;
import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.ledger.vo.Tsl;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.properties.RulesProperties;
import eu.futuretrust.gtsl.properties.RulesPropertiesHandler;
import eu.futuretrust.gtsl.storage.Commit;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersionServiceImpl implements VersionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(VersionServiceImpl.class);

  private final TslLedgerService tslLedgerService;
  private final StorageService storageService;
  private final JaxbService jaxbService;
  private final TslAdditionalInformationService tslAdditionalInformationService;
  private final RulesPropertiesHandler rulesPropertiesHandler;

  @Autowired
  public VersionServiceImpl(TslLedgerService tslLedgerService, StorageService storageService,
      JaxbService jaxbService,
      TslAdditionalInformationService tslAdditionalInformationService,
      RulesPropertiesHandler rulesPropertiesHandler) {
    this.tslLedgerService = tslLedgerService;
    this.storageService = storageService;
    this.jaxbService = jaxbService;
    this.tslAdditionalInformationService = tslAdditionalInformationService;
    this.rulesPropertiesHandler = rulesPropertiesHandler;
  }


  @Override
  public Optional<byte[]> retrieveBytes(String tlIdentifier, BigInteger versionNumber)
      throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "retrieveBytes");

    if (StringUtils.isEmpty(tlIdentifier)) {
      throw new InvalidParameterException("Tsl identifier is invalid: empty or null");
    } else if (versionNumber == null) {
      throw new InvalidParameterException("Version number is invalid: null");
    } else if (versionNumber.signum() < 1) {
      throw new InvalidParameterException("Version number is invalid: must be greater than 0");
    }

    // retrieve the current TSL from Ethereum
    Optional<Tsl> tsl = tslLedgerService.findByCountryCode(tlIdentifier);
    if (!tsl.isPresent()) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Version cannot be retrieved, no TSL \"{}\" found in Ethereum", tlIdentifier);
      }
      return Optional.empty();
    }

    String nextVersionAddress = tsl.get().getAddress();
    while (StringUtils.isNotEmpty(nextVersionAddress)) {
      // retrieve the commit in IPFS from the address
      Optional<Commit> commit = storageService.getCommit(nextVersionAddress);
      if (!commit.isPresent()) {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error("Commit \"" + nextVersionAddress + "\" cannot be retrieved from IPFS");
        }
        return Optional.empty();
      }

      if (commit.get().getVersionNumber().compareTo(versionNumber) == 0) {
        // version found, retrieve the data in IPFS
        return storageService.get(commit.get().getDataAddress());
      }

      nextVersionAddress = commit.get().getParent();
    }

    return Optional.empty();
  }


  @Override
  public Optional<TrustStatusListType> retrieveTsl(String tlIdentifier, BigInteger versionNumber)
      throws Exception {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Service [" + VersionServiceImpl.class.getName() + ".retrieveTsl] called");
    }

    Optional<byte[]> data = retrieveBytes(tlIdentifier, versionNumber);
    if (!data.isPresent()) {
      return Optional.empty();
    }

    // transform the (bytes) data into Java object
    TrustStatusListTypeJAXB tslJAXB = jaxbService.unmarshallTsl(data.get());
    TrustStatusListType tsl = new TrustStatusListType(tslJAXB);

    rulesPropertiesHandler.retrieve().ifPresent(rulesProperties -> tslAdditionalInformationService
        .appendAdditionalInformation(rulesProperties, tsl));

    return Optional.of(tsl);
  }

  @Override
  public List<TrustStatusListType> retrieveAllVersions(String tlIdentifier) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "retrieveAllVersions");

    // retrieve the IPFS address of the last commit for the specified tsl
    Optional<Tsl> tslInfo = tslLedgerService.findByCountryCode(tlIdentifier);
    if (!tslInfo.isPresent()) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Versions cannot be retrieved, no TSL \"{}\" found in Ethereum", tlIdentifier);
      }
      return Collections.emptyList();
    }

    Optional<RulesProperties> rulesPropertiesOptional = rulesPropertiesHandler.retrieve();

    List<TrustStatusListType> list = new ArrayList<>();
    String nextVersionAddress = tslInfo.get().getAddress();
    while (StringUtils.isNotEmpty(nextVersionAddress)) {
      // retrieve the commit in IPFS from the address
      Optional<Commit> commit = storageService.getCommit(nextVersionAddress);
      if (!commit.isPresent()) {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error("Commit at address {} cannot be retrieved from IPFS", nextVersionAddress);
        }
        return list;
      }

      // retrieve the data in IPFS
      Optional<byte[]> data = storageService.get(commit.get().getDataAddress());
      if (!data.isPresent()) {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error("Data at address {} cannot be retrieved from IPFS",
              commit.get().getDataAddress());
        }
        return list;
      }

      // transform the (bytes) data into Java object
      TrustStatusListTypeJAXB tslJAXB = jaxbService.unmarshallTsl(data.get());
      TrustStatusListType tsl = new TrustStatusListType(tslJAXB);
      rulesPropertiesOptional.ifPresent(rulesProperties -> tslAdditionalInformationService
          .appendAdditionalInformation(rulesProperties, tsl));
      list.add(tsl);

      nextVersionAddress = commit.get().getParent();
    }

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("{} versions have been retrieved for TSL {}", list.size(), tlIdentifier);
    }
    return list;
  }
}
