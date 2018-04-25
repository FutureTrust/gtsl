/**
 * © Nowina Solutions, 2015-2015
 *
 * Concédée sous licence EUPL, version 1.1 ou – dès leur approbation par la Commission européenne - versions ultérieures de l’EUPL (la «Licence»).
 * Vous ne pouvez utiliser la présente œuvre que conformément à la Licence.
 * Vous pouvez obtenir une copie de la Licence à l’adresse suivante:
 *
 * http://ec.europa.eu/idabc/eupl5
 *
 * Sauf obligation légale ou contractuelle écrite, le logiciel distribué sous la Licence est distribué «en l’état»,
 * SANS GARANTIES OU CONDITIONS QUELLES QU’ELLES SOIENT, expresses ou implicites.
 * Consultez la Licence pour les autorisations et les restrictions linguistiques spécifiques relevant de la Licence.
 */
package eu.futuretrust.gtsl.web.nexu;

import java.util.List;

import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.EncryptionAlgorithm;
import eu.europa.esig.dss.x509.CertificateToken;

public class GetCertificateResponse {

  private TokenId tokenId;

  private String keyId;

  private CertificateToken certificate;

  private CertificateToken[] certificateChain;

  private EncryptionAlgorithm encryptionAlgorithm;

  private List<DigestAlgorithm> supportedDigests;

  private DigestAlgorithm preferredDigest;

  public GetCertificateResponse() {
    super();
  }

  public TokenId getTokenId() {
    return tokenId;
  }

  public void setTokenId(TokenId tokenId) {
    this.tokenId = tokenId;
  }

  public String getKeyId() {
    return keyId;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

  public CertificateToken getCertificate() {
    return certificate;
  }

  public void setCertificate(CertificateToken certificate) {
    this.certificate = certificate;
  }

  public CertificateToken[] getCertificateChain() {
    return certificateChain;
  }

  public void setCertificateChain(CertificateToken[] certificateChain) {
    this.certificateChain = certificateChain;
  }

  public EncryptionAlgorithm getEncryptionAlgorithm() {
    return encryptionAlgorithm;
  }

  public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
    this.encryptionAlgorithm = encryptionAlgorithm;
  }

  public List<DigestAlgorithm> getSupportedDigests() {
    return supportedDigests;
  }

  public void setSupportedDigests(List<DigestAlgorithm> supportedDigests) {
    this.supportedDigests = supportedDigests;
  }

  public DigestAlgorithm getPreferredDigest() {
    return preferredDigest;
  }

  public void setPreferredDigest(DigestAlgorithm preferredDigest) {
    this.preferredDigest = preferredDigest;
  }
}