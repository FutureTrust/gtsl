package eu.futuretrust.gtsl.admin.nexu;

import eu.europa.esig.dss.SignatureAlgorithm;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.futuretrust.gtsl.admin.utils.SignatureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Date;

public class SignatureInformation {

  private final static Logger LOGGER = LoggerFactory.getLogger(SignatureInformation.class);

  private byte[] signatureValue;
  private SignatureAlgorithm signatureAlgorithm;
  private CertificateToken certificate;
  private CertificateToken[] certificateChain;
  private Date signingDate;

  public byte[] getSignatureValue() {
    return signatureValue;
  }

  public void setSignatureValue(final String base64Value) {
    this.signatureValue = Base64.getDecoder().decode(base64Value);
  }

  public SignatureAlgorithm getSignatureAlgorithm() {
    return signatureAlgorithm;
  }

  public void setSignatureAlgorithm(final SignatureAlgorithm signatureAlgorithm) {
    this.signatureAlgorithm = signatureAlgorithm;
  }

  public CertificateToken getCertificate() {
    return certificate;
  }

  public void setCertificate(final String base64Value) {
    this.certificate = SignatureUtils.getCertificateTokenFromBase64String(base64Value);

  }

  public CertificateToken[] getCertificateChain() {
    return certificateChain;
  }

  public void setCertificateChain(final String[] certificateChain) {
    this.certificateChain = new CertificateToken[certificateChain.length];

    for (int i=0; i < certificateChain.length; i++) {
      this.certificateChain[i] = SignatureUtils.getCertificateTokenFromBase64String
          (certificateChain[i]);
    }
  }

  public Date getSigningDate() {
    return signingDate;
  }

  public void setSigningDate(Date signingDate) {
    this.signingDate = signingDate;
  }
}
