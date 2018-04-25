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

package eu.futuretrust.gtsl.admin.services.impl;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.SignatureLevel;
import eu.europa.esig.dss.SignaturePackaging;
import eu.europa.esig.dss.SignatureValue;
import eu.europa.esig.dss.ToBeSigned;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.xades.DSSReference;
import eu.europa.esig.dss.xades.DSSTransform;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;
import eu.futuretrust.gtsl.admin.services.XAdESSignatureService;
import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.properties.SignatureProperties;
import eu.futuretrust.gtsl.business.services.draft.DraftService;
import eu.futuretrust.gtsl.business.services.ledger.ConsortiumService;
import eu.futuretrust.gtsl.business.services.ledger.TslLedgerService;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.business.services.validator.exception.TslValidationException;
import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.business.utils.TslUtils;
import eu.futuretrust.gtsl.business.vo.draft.DraftVO;
import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.ledger.exceptions.UnauthorizedException;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import eu.futuretrust.gtsl.web.nexu.SignatureInformation;
import eu.futuretrust.gtsl.web.nexu.SignatureRequest;
import eu.futuretrust.gtsl.web.nexu.TokenId;
import eu.futuretrust.gtsl.web.utils.SignatureUtils;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XAdESSignatureServiceImpl implements XAdESSignatureService {

  private final static Logger LOGGER = LoggerFactory.getLogger(XAdESSignatureService.class);

  private final TslLedgerService tslLedgerService;
  private final DraftService draftService;
  private final ConsortiumService consortiumService;
  private final JaxbService jaxbService;
  private final SignatureProperties signatureProperties;
  private final TslService tslService;

  @Autowired
  public XAdESSignatureServiceImpl(
      TslLedgerService tslLedgerService,
      final DraftService draftService,
      final ConsortiumService consortiumService,
      final JaxbService jaxbService,
      final TslService tslService,
      final SignatureProperties signatureProperties) {
    this.tslLedgerService = tslLedgerService;
    this.draftService = draftService;
    this.consortiumService = consortiumService;
    this.jaxbService = jaxbService;
    this.tslService = tslService;
    this.signatureProperties = signatureProperties;
  }

  @Override
  public Map<String, Object> getSignatureRequestMap(final String draftId, final String keyId,
      final String tokenId, final String base64Cert, final String[] certificateChain)
      throws Exception {
    if (consortiumService.isAuthorized()) {
      Optional<DraftVO> draft = draftService.read(draftId);
      if (draft.isPresent()) {
        draft.get().getTsl().setSignature(null);
        byte[] bytes = jaxbService.marshallTslToBytes(draft.get().getTsl());
        final DSSDocument doc = new InMemoryDocument(bytes);
        final CertificateToken signingToken = SignatureUtils
            .getCertificateTokenFromBase64String(base64Cert);
        final DigestAlgorithm digestAlgorithm = DigestAlgorithm.forName
            (signatureProperties.getDigestAlgorithm());

        final XAdESSignatureParameters xAdESParams = getXAdESSignatureParameters
            (signingToken, new Date(), digestAlgorithm, doc);
        final CertificateVerifier verifier = new CommonCertificateVerifier();
        final XAdESService xAdESService = new XAdESService(verifier);
        final ToBeSigned toBeSigned = xAdESService.getDataToSign(doc, xAdESParams);

        final SignatureRequest sigRequest = getSignatureRequest(keyId, new
            TokenId(tokenId), toBeSigned, digestAlgorithm);

        final Map<String, Object> map = new HashMap<>();
        map.put("signatureRequest", sigRequest);
        map.put("nexuScheme", signatureProperties.getNexuScheme());
        map.put("nexuPort", signatureProperties.getNexuPort());
        map.put("signingDate", xAdESParams.bLevel().getSigningDate());
        return map;
      }
      return null;
    } else {
      throw new UnauthorizedException("Entity not authorized to run smart "
          + "contract");
    }
  }

  private SignatureRequest getSignatureRequest(final String keyId,
      final TokenId tokenId,
      final ToBeSigned toBeSigned,
      final DigestAlgorithm digestAlgorithm) {

    SignatureRequest signatureRequest = new SignatureRequest();
    eu.futuretrust.gtsl.web.nexu.ToBeSigned tbs = new eu.futuretrust.gtsl.web.nexu.ToBeSigned(
        toBeSigned);
    signatureRequest.setToBeSigned(tbs);
    signatureRequest.setKeyId(keyId);
    signatureRequest.setTokenId(tokenId);
    signatureRequest.setDigestAlgorithm(digestAlgorithm);

    return signatureRequest;
  }

  @Override
  public ReportDTO doSign(String draftId, SignatureInformation signatureInformation)
      throws Exception {
    if (consortiumService.isAuthorized()) {
      Optional<DraftVO> draft = draftService.read(draftId);
      if (draft.isPresent()) {
        draft.get().getTsl().setSignature(null);
        byte[] bytes = jaxbService.marshallTslToBytes(draft.get().getTsl());

        final DSSDocument doc = new InMemoryDocument(bytes);
        XAdESSignatureParameters params = getXAdESSignatureParameters(
            signatureInformation, doc);

        final CertificateVerifier verifier = new CommonCertificateVerifier();
        final XAdESService xAdESService = new XAdESService(verifier);
        final SignatureValue signatureValue = new SignatureValue(
            signatureInformation.getSignatureAlgorithm(),
            signatureInformation.getSignatureValue());

        final DSSDocument signedDoc = xAdESService.signDocument(doc, params,
            signatureValue);

        TrustStatusListTypeJAXB trustStatusListTypeJAXB = jaxbService
            .unmarshallTsl(signedDoc.openStream());
        TrustStatusListType tsl = new TrustStatusListType(trustStatusListTypeJAXB);

        String tslIdentifier = TslUtils.extractTerritoryCode(tsl);
        ReportDTO report;
        if (tslLedgerService.exists(tslIdentifier)) {
          report = tslService.update(tsl);
        } else {
          report = tslService.create(tsl);
        }
        if (report.isValid()) {
          if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Draft {} has been created successfully", draftId);
          }
          return report;
        } else {
          if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Draft {} has not been created because it contains error(s)", draftId);
          }
          throw new TslValidationException(
              "The draft " + draftId + " has not been created because it contains error(s)");
        }
      } else {
        throw new InvalidParameterException("Draft has not been found");
      }
    } else {
      throw new UnauthorizedException("Entity not authorized to run smart"
          + "contract");
    }
  }

  private XAdESSignatureParameters getXAdESSignatureParameters(final SignatureInformation
      signatureInformation, final DSSDocument document) {

    return getXAdESSignatureParameters(signatureInformation.getCertificate(),
        signatureInformation.getSigningDate(),
        DigestAlgorithm.forName(signatureProperties.getDigestAlgorithm()),
        document);
  }

  private XAdESSignatureParameters getXAdESSignatureParameters(
      final CertificateToken signingToken,
      final Date signingDate,
      final DigestAlgorithm digestAlgorithm,
      final DSSDocument toSignDocument) {

    XAdESSignatureParameters params = new XAdESSignatureParameters();

    params.setSignatureLevel(SignatureLevel.XAdES_BASELINE_B);
    params.setSignaturePackaging(SignaturePackaging.ENVELOPED);
    params.setSigningCertificate(signingToken);
    params.bLevel().setSigningDate(signingDate);
    params.setDigestAlgorithm(digestAlgorithm);

    final List<DSSReference> references = new ArrayList<>();

    DSSReference dssReference = new DSSReference();
    dssReference.setId("xml_ref_id");
    dssReference.setUri("");
    dssReference.setContents(toSignDocument);
    dssReference.setDigestMethodAlgorithm(params.getDigestAlgorithm());

    final List<DSSTransform> transforms = new ArrayList<>();

    DSSTransform dssTransform = new DSSTransform();
    dssTransform.setAlgorithm(CanonicalizationMethod.ENVELOPED);
    transforms.add(dssTransform);

    dssTransform = new DSSTransform();
    dssTransform.setAlgorithm(CanonicalizationMethod.EXCLUSIVE);
    transforms.add(dssTransform);

    dssReference.setTransforms(transforms);
    references.add(dssReference);

    params.setReferences(references);

    return params;
  }

}