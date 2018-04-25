package eu.futuretrust.gtsl.web.nexu;

import eu.europa.esig.dss.DigestAlgorithm;
import eu.futuretrust.gtsl.web.nexu.ToBeSigned;

public class SignatureRequest extends NexuRequest {

  private TokenId tokenId;

  private ToBeSigned toBeSigned;

  private DigestAlgorithm digestAlgorithm;

  private String keyId;

  public SignatureRequest() {
  }

  public TokenId getTokenId() {
    return tokenId;
  }

  public void setTokenId(TokenId tokenId) {
    this.tokenId = tokenId;
  }

  public ToBeSigned getToBeSigned() {
    return toBeSigned;
  }

  public void setToBeSigned(ToBeSigned toBeSigned) {
    this.toBeSigned = toBeSigned;
  }

  public DigestAlgorithm getDigestAlgorithm() {
    return digestAlgorithm;
  }

  public void setDigestAlgorithm(DigestAlgorithm digestAlgorithm) {
    this.digestAlgorithm = digestAlgorithm;
  }

  public String getKeyId() {
    return keyId;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

}
