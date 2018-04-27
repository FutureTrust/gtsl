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

package eu.futuretrust.gtsl.business.services.ledger.impl;

import eu.futuretrust.gtsl.business.services.ledger.TslLedgerService;
import eu.futuretrust.gtsl.business.utils.DebugUtils;
import eu.futuretrust.gtsl.ledger.TslLedger;
import eu.futuretrust.gtsl.ledger.vo.Tsl;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TslLedgerServiceImpl implements TslLedgerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TslLedgerServiceImpl.class);

  private final TslLedger tslLedger;

  @Autowired
  public TslLedgerServiceImpl(TslLedger tslLedger) {
    this.tslLedger = tslLedger;
  }

  @Override
  public void add(String code, String address) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "add");

    tslLedger.add(code, address);
  }

  @Override
  public void update(String code, String address) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "update");

    tslLedger.update(code, address);
  }

  @Override
  public void remove(String code) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "remove");

    tslLedger.remove(code);
  }

  @Override
  public Optional<Tsl> findByCountryCode(String code) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "findByCountryCode");

    return tslLedger.findByCountryCode(code);
  }

  @Override
  public Optional<Tsl> findById(BigInteger index) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "findById");

    return tslLedger.findById(index);
  }

  @Override
  public List<Tsl> findAll() throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "findAll");

    return tslLedger.findAll();
  }

  @Override
  public BigInteger length() throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "length");

    return tslLedger.length();
  }

  @Override
  public boolean exists(String code) throws Exception {
    DebugUtils.debug(LOGGER, this.getClass(), "exists");

    return tslLedger.exists(code);
  }

}
