package eu.futuretrust.gtsl.business.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/signature.properties")
@ConfigurationProperties(prefix = "signature")
public class SignatureProperties {

  private String signatureAlgorithm;
  private String digestAlgorithm;
  private String trustStorePath;
  private String trustStoreType;
  private String signerAlias;
  private String tokenId;
  private String keyId;
  private String trustStorePassword;

  public String getSignatureAlgorithm() {
    return signatureAlgorithm;
  }

  public void setSignatureAlgorithm(String signatureAlgorithm) {
    this.signatureAlgorithm = signatureAlgorithm;
  }

  public String getDigestAlgorithm() {
    return digestAlgorithm;
  }

  public void setDigestAlgorithm(final String digestAlgorithm) {
    this.digestAlgorithm = digestAlgorithm;
  }

  public String getTrustStorePath() {
    return trustStorePath;
  }

  public void setTrustStorePath(final String trustStorePath) {
    this.trustStorePath = trustStorePath;
  }

  public String getTrustStoreType() {
    return trustStoreType;
  }

  public void setTrustStoreType(final String trustStoreType) {
    this.trustStoreType = trustStoreType;
  }

  public String getSignerAlias() {
    return signerAlias;
  }

  public void setSignerAlias(String signerAlias) {
    this.signerAlias = signerAlias;
  }

  public String getTokenId() {
    return tokenId;
  }

  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }

  public String getKeyId() {
    return keyId;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

  public char[] getTrustStorePassword() {
    return trustStorePassword.toCharArray();
  }

  public void setTrustStorePassword(final String password) {
    this.trustStorePassword = password;
  }
}
