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

package eu.futuretrust.gtsl.admin.controllers.api;

import eu.futuretrust.gtsl.business.dto.consortium.ProposalDTO;
import eu.futuretrust.gtsl.business.dto.helper.ResultDTO;
import eu.futuretrust.gtsl.business.services.api.ApiService;
import eu.futuretrust.gtsl.business.services.ledger.ConsortiumService;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class ApiUsersController {

  private final ApiService apiService;
  private final ConsortiumService consortiumService;

  @Autowired
  public ApiUsersController(
      @Qualifier("apiAuthorized") ApiService apiService,
      ConsortiumService consortiumService) {
    this.apiService = apiService;
    this.consortiumService = consortiumService;
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<BigInteger>> requestAddMember(
      @RequestBody ProposalDTO proposalDTO) {
    return apiService.execute(() -> Optional.ofNullable(consortiumService
        .requestAddMember(proposalDTO.getMemberAddress(), proposalDTO.getTslIdentifier(),
            proposalDTO.getDuration())));
  }

  @RequestMapping(method = RequestMethod.DELETE)
  public ResponseEntity<ResultDTO<BigInteger>> requestRemoveMember(
      @RequestBody ProposalDTO proposalDTO) {
    return apiService.execute(() -> Optional.ofNullable(consortiumService
        .requestRemoveMember(proposalDTO.getMemberAddress(), proposalDTO.getDuration())));
  }

  @RequestMapping(value = "execute/{proposalId}", method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<Boolean>> executeProposal(
      @PathVariable BigInteger proposalId) {
    return apiService.execute(() -> {
      consortiumService.executeProposal(proposalId);
      return Optional.of(true);
    });
  }

  @RequestMapping(value = "vote/{proposalId}", method = RequestMethod.POST)
  public ResponseEntity<ResultDTO<Boolean>> vote(@PathVariable BigInteger proposalId,
      @RequestBody boolean inSupport) {
    return apiService.execute(() -> {
      consortiumService.vote(proposalId, inSupport);
      return Optional.of(true);
    });
  }

}
