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

package eu.futuretrust.gtsl.business.services.draft.impl;

import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.repositories.DraftRepository;
import eu.futuretrust.gtsl.business.repositories.TerritoryRepository;
import eu.futuretrust.gtsl.business.services.draft.DraftService;
import eu.futuretrust.gtsl.business.services.storage.StorageService;
import eu.futuretrust.gtsl.business.services.validator.TslValidator;
import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.business.utils.DebugUtils;
import eu.futuretrust.gtsl.business.utils.TslUtils;
import eu.futuretrust.gtsl.business.vo.draft.DraftVO;
import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.persistence.entities.DraftEntity;
import eu.futuretrust.gtsl.persistence.entities.TerritoryEntity;
import eu.futuretrust.gtsl.storage.exceptions.StorageException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "draftService")
public class DraftServiceImpl implements DraftService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DraftServiceImpl.class);

  private final DraftRepository draftRepository;
  private final TerritoryRepository territoryRepository;
  private final TslValidator tslValidator;
  private final JaxbService jaxbService;
  private final StorageService storageService;

  private final static Comparator<DraftVO> COMPARATOR_ISSUE_DATE = (draftVO1, draftVO2) -> {
    if (draftVO1.getTsl() == null || draftVO1.getTsl().getSchemeInformation() == null
        || draftVO1.getTsl().getSchemeInformation().getListIssueDateTime() == null) {
      return 1;
    }
    if (draftVO2.getTsl() == null || draftVO2.getTsl().getSchemeInformation() == null
        || draftVO2.getTsl().getSchemeInformation().getListIssueDateTime() == null) {
      return -1;
    }
    return draftVO2.getTsl().getSchemeInformation().getListIssueDateTime()
        .compareTo(draftVO1.getTsl().getSchemeInformation().getListIssueDateTime());
  };

  @Autowired
  public DraftServiceImpl(DraftRepository draftRepository,
      TerritoryRepository territoryRepository,
      TslValidator tslValidator, JaxbService jaxbService,
      StorageService storageService) {
    this.draftRepository = draftRepository;
    this.territoryRepository = territoryRepository;
    this.tslValidator = tslValidator;
    this.jaxbService = jaxbService;
    this.storageService = storageService;
  }

  @Override
  public ReportDTO create(TrustStatusListType tsl) throws Exception {
    return save(null, tsl);
  }

  @Override
  public ReportDTO update(String dbId, TrustStatusListType tsl) throws Exception {
    return save(dbId, tsl);
  }

  @Override
  public void delete(String dbId) {
    DebugUtils.debug(LOGGER, this.getClass(), "delete");

    TslUtils.checkDraftId(dbId);
    safeDelete(draftRepository.findById(dbId));
  }

  @Override
  public void deleteAll(String territoryCode) {
    DebugUtils.debug(LOGGER, this.getClass(), "deleteAll");

    TslUtils.checkTerritoryCode(territoryCode);
    String trimmedTerritoryCode = territoryCode.trim();
    TerritoryEntity territory = territoryRepository.findByCode(trimmedTerritoryCode);
    if (territory != null) {
      draftRepository.findByTerritory(territory).forEach(this::safeDelete);
    }
  }

  @Override
  public Optional<DraftVO> read(String dbId) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "read");

    TslUtils.checkDraftId(dbId);
    DraftEntity draft = draftRepository.findById(dbId);
    return getDraftFromEntity(draft);
  }

  @Override
  public Optional<byte[]> readBytes(String dbId) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "readBytes");

    TslUtils.checkDraftId(dbId);
    DraftEntity draft = draftRepository.findById(dbId);
    return getBytesFromEntity(draft);
  }

  @Override
  public List<DraftVO> readAll(String territoryCode) {
    DebugUtils.debug(LOGGER, this.getClass(), "readAll");

    TslUtils.checkTerritoryCode(territoryCode);
    String trimmedTerritoryCode = territoryCode.trim();
    TerritoryEntity territory = territoryRepository.findByCode(trimmedTerritoryCode);
    if (territory == null) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("TerritoryCode {} cannot be retrieved in the local database", territoryCode);
      }
      return Collections.emptyList();
    }

    return getDraftFromEntity(draftRepository.findByTerritory(territory));
  }

  @Override
  public List<DraftVO> readAll() {
    return getDraftFromEntity(draftRepository.findAll());
  }

  @Override
  public Optional<ReportDTO> validate(String dbId) throws Exception {
    Optional<DraftVO> draft = this.read(dbId);
    if (draft.isPresent()) {
      return Optional.ofNullable(tslValidator.validate(draft.get().getTsl(), null));
    } else {
      return Optional.empty();
    }
  }

  private void safeDelete(DraftEntity draft) {
    if (draft != null) {
      List<DraftEntity> draftsSharingHash = draftRepository.findByHash(draft.getHash());
      if (draftsSharingHash.size() == 1) {
        // be sure that it does not exist a copy of this draft
        storageService.unpin(draft.getHash());
      }
      draftRepository.delete(draft);

      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("Tsl {} has been deleted from the local database", draft.getId());
      }
    }
  }

  private Optional<byte[]> getBytesFromEntity(DraftEntity draft) throws Exception {
    if (draft == null) {
      return Optional.empty();
    }

    Optional<String> dataAddress = storageService.resolve(draft.getHash());
    if (!dataAddress.isPresent()) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Commit at address {} cannot be retrieved from IPFS", draft.getHash());
      }
      throw new StorageException(
          "Commit at address " + draft.getHash() + " cannot be retrieved from IPFS");
    }

    Optional<byte[]> data = storageService.get(dataAddress.get());
    if (!data.isPresent()) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Data at address {} cannot be retrieved from IPFS", dataAddress.get());
      }
      throw new StorageException(
          "Data at address " + dataAddress.get() + " cannot be retrieved from IPFS");
    }

    return data;
  }

  private Optional<DraftVO> getDraftFromBytes(String dbId, byte[] data) throws Exception {
    TrustStatusListTypeJAXB tslJAXB = jaxbService.unmarshallTsl(data);
    DraftVO draftVO = new DraftVO(dbId, new TrustStatusListType(tslJAXB));

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Tsl {} draft with id {} has been retrieved from the local database",
          draftVO.getTsl().getSchemeInformation().getSchemeTerritory().getValue(), dbId);
    }
    return Optional.of(draftVO);
  }

  private Optional<DraftVO> getDraftFromEntity(DraftEntity draftEntity) throws Exception {
    Optional<byte[]> data = getBytesFromEntity(draftEntity);
    if (data.isPresent()) {
      return getDraftFromBytes(draftEntity.getId(), data.get());
    } else {
      return Optional.empty();
    }
  }

  private List<DraftVO> getDraftFromEntity(List<DraftEntity> entities) {
    List<DraftVO> drafts = entities.stream()
        .map(entity -> {
          try {
            return getDraftFromEntity(entity);
          } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
              LOGGER.error("Error while retrieving draft {} of the TSL {} at address {}: {}",
                  entity.getId(), entity.getTerritory(), entity.getHash(), e.getMessage());
            }
            return Optional.<DraftVO>empty();
          }
        })
        .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
        .sorted(COMPARATOR_ISSUE_DATE)
        .collect(Collectors.toList());

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("{} TSL draft(s) has/have been retrieved from the local database", drafts.size());
    }
    return drafts;
  }

  private ReportDTO save(String dbId, TrustStatusListType tsl) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "save");

    String territoryCode = TslUtils.extractTerritoryCode(tsl);

    // transform tsl into xml
    byte[] tslXml = jaxbService.marshallTslToBytes(tsl);

    // store xml into IPFS
    String createEntity = storageService
        .create(tslXml, tsl.getSchemeInformation().getTslSequenceNumber());

    TerritoryEntity territory = territoryRepository.findByCode(territoryCode);
    if (territory == null) {
      territory = territoryRepository.save(new TerritoryEntity(territoryCode));
    }

    // store hash in DB
    DraftEntity draft = new DraftEntity(dbId, territory, createEntity);
    draft = draftRepository.save(draft);

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Draft \"" + draft.getId() + "\" has been saved in the local database");
    }

    return tslValidator.validate(tsl, null);
  }

}
