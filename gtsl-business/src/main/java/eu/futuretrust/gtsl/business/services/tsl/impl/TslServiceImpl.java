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

package eu.futuretrust.gtsl.business.services.tsl.impl;

import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.services.additional.TslAdditionalInformationService;
import eu.futuretrust.gtsl.business.services.ledger.TslLedgerService;
import eu.futuretrust.gtsl.business.services.storage.StorageService;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.business.services.validator.TslValidator;
import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.business.utils.DebugUtils;
import eu.futuretrust.gtsl.business.utils.TslUtils;
import eu.futuretrust.gtsl.business.vo.draft.DraftVO;
import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.ledger.vo.Tsl;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.properties.RulesPropertiesHandler;
import eu.futuretrust.gtsl.storage.exceptions.StorageException;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TslServiceImpl implements TslService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TslServiceImpl.class);

  private final TslLedgerService tslLedgerService;
  private final StorageService storageService;
  private final TslValidator tslValidator;
  private final JaxbService jaxbService;
  private final TslAdditionalInformationService tslAdditionalInformationService;
  private final RulesPropertiesHandler rulesPropertiesHandler;

  @Autowired
  public TslServiceImpl(TslLedgerService tslLedgerService,
      StorageService storageService,
      TslValidator tslValidator,
      JaxbService jaxbService,
      TslAdditionalInformationService tslAdditionalInformationService,
      RulesPropertiesHandler rulesPropertiesHandler) {
    this.tslLedgerService = tslLedgerService;
    this.storageService = storageService;
    this.tslValidator = tslValidator;
    this.jaxbService = jaxbService;
    this.tslAdditionalInformationService = tslAdditionalInformationService;
    this.rulesPropertiesHandler = rulesPropertiesHandler;
  }

  @Override
  public ReportDTO create(TrustStatusListType tsl) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "create");

    ReportDTO report = tslValidator.validate(tsl, null);
    if (report.isValid()) {
      byte[] tslXml = jaxbService.marshallTslToBytes(tsl);
      BigInteger version = tsl.getSchemeInformation().getTslSequenceNumber();
      String hash = storageService.create(tslXml, version);
      String countryCode = tsl.getSchemeInformation().getSchemeTerritory().getValue();
      tslLedgerService.add(countryCode, hash);
    } else {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Tsl {} cannot be updated, because the Tsl contains error(s)",
            tsl.getSchemeInformation().getSchemeTerritory().getValue());
      }
    }
    return report;
  }

  @Override
  public ReportDTO update(TrustStatusListType tsl) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "update");

    // find the current TSL in the blockchain
    String tslIdentifier = TslUtils.extractTerritoryCode(tsl);
    Optional<Tsl> tslInfo = tslLedgerService.findByCountryCode(tslIdentifier);
    if (!tslInfo.isPresent()) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Tsl {} cannot be updated, because it cannot be retrieved", tslIdentifier);
      }
      throw new InvalidParameterException(
          "Tsl " + tslIdentifier + "cannot be updated, because it cannot be retrieved");
    }

    // resolve the address to which the data can be accessed
    String currentAddress = tslInfo.get().getAddress();
    Optional<String> dataAddress = storageService.resolve(currentAddress);
    if (!dataAddress.isPresent()) {
      // recreation of the complete TSL because something went wrong
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn(
            "The TSL {} need to be recreated because it cannot be retrieved from IPFS using the following hash {}",
            tslIdentifier, tslInfo.get().getAddress());
      }
      tslLedgerService.remove(tslIdentifier);
      return this.create(tsl);
    }

    // retrieve the data from IPFS
    Optional<byte[]> data = storageService.get(dataAddress.get());
    if (!data.isPresent()) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Data of the TSL {} cannot be retrieved from IPFS at the address {}",
            tslIdentifier, dataAddress.get());
      }
      throw new StorageException("Unable to retrieve the TSL " + tslIdentifier);
    }

    // convert the data into a Java object
    TrustStatusListType currentTsl = convert(data.get());

    // validate the TSL
    ReportDTO report = tslValidator.validate(tsl, currentTsl);
    if (report.isValid()) {
      // update the TSL
      byte[] tslXml = jaxbService.marshallTslToBytes(tsl);
      BigInteger version = tsl.getSchemeInformation().getTslSequenceNumber();
      String hash = storageService.update(currentAddress, tslXml, version);
      tslLedgerService.update(tslIdentifier, hash);
    } else {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Tsl {} cannot be updated, because the Tsl contains error(s)", tslIdentifier);
      }
    }

    return report;
  }

  @Override
  public void remove(String tlIdentifier) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "remove");

    tslLedgerService.remove(tlIdentifier);
  }

  @Override
  public ReportDTO push(DraftVO draft) throws Exception {
    TrustStatusListType tsl = draft.getTsl();
    String tslIdentifier = TslUtils.extractTerritoryCode(tsl);
    if (tslLedgerService.exists(tslIdentifier)) {
      return this.update(tsl);
    } else {
      return this.create(tsl);
    }
  }

  @Override
  public Optional<byte[]> readBytes(String tslIdentifier) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "readBytes");

    Optional<Tsl> tslInfo = tslLedgerService.findByCountryCode(tslIdentifier);
    if (tslInfo.isPresent()) {
      return Optional.of(read(tslInfo.get()));
    }

    if (LOGGER.isWarnEnabled()) {
      LOGGER.warn("Tsl {} cannot be read, because it cannot be retrieved", tslIdentifier);
    }
    return Optional.empty();
  }

  @Override
  public Optional<TrustStatusListType> read(String tlIdentifier) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "read");

    Optional<byte[]> data = this.readBytes(tlIdentifier);
    if (data.isPresent()) {
      return Optional.ofNullable(convert(data.get()));
    }
    return Optional.empty();
  }

  @Override
  public List<TrustStatusListType> readAll() throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "readAll");

    return tslLedgerService.findAll().stream()
        .map(tslInfo -> {
          try {
            return convert(read(tslInfo));
          } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
              LOGGER.error(e.getMessage());
            }
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<ReportDTO> validate(String territoryCode) throws Exception {
    Optional<TrustStatusListType> tsl = this.read(territoryCode);
    if (tsl.isPresent()) {
      return Optional.ofNullable(tslValidator.validate(tsl.get(), null));
    }
    return Optional.empty();
  }

  private byte[] read(Tsl tslInfo) throws Exception {
    // resolve the address to which the data can be accessed
    String currentAddress = tslInfo.getAddress();
    Optional<String> dataAddress = storageService.resolve(currentAddress);
    if (!dataAddress.isPresent()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn(
            "The TSL {} cannot be read, because it cannot be retrieved from IPFS using the following hash {}",
            tslInfo.getCountryCode(), tslInfo.getAddress());
      }
      throw new StorageException("Unable to retrieve the TSL " + tslInfo.getCountryCode());
    }

    // retrieve the data from IPFS
    Optional<byte[]> data = storageService.get(dataAddress.get());
    if (!data.isPresent()) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Data of the TSL {} cannot be retrieved from IPFS at the address {}",
            tslInfo.getCountryCode(), dataAddress.get());
      }
      throw new StorageException("Unable to retrieve the TSL " + tslInfo.getCountryCode());
    }

    return data.get();
  }

  private TrustStatusListType convert(byte[] data) throws Exception {
    TrustStatusListTypeJAXB tslJAXB = jaxbService.unmarshallTsl(data);
    return convert(tslJAXB);
  }

  private TrustStatusListType convert(TrustStatusListTypeJAXB tslJAXB) throws Exception {
    TrustStatusListType tsl = new TrustStatusListType(tslJAXB);
    rulesPropertiesHandler.retrieve()
        .ifPresent(rulesProperties -> tslAdditionalInformationService
            .appendAdditionalInformation(rulesProperties, tsl));
    return tsl;
  }

}
