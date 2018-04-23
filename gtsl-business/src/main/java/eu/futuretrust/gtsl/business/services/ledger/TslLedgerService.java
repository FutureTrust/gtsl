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

package eu.futuretrust.gtsl.business.services.ledger;

import eu.futuretrust.gtsl.ledger.vo.Tsl;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface TslLedgerService {

  void add(String code, String address) throws Exception;

  void update(String code, String address) throws Exception;

  void remove(String code) throws Exception;

  Optional<Tsl> findByCountryCode(String code) throws Exception;

  Optional<Tsl> findById(BigInteger index) throws Exception;

  List<Tsl> findAll() throws Exception;

  BigInteger length() throws Exception;

  boolean exists(String code) throws Exception;
}
