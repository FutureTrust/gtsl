package eu.futuretrust.gtsl.persistence.entities;

import eu.futuretrust.gtsl.persistence.enums.SignatureStatus;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "signatures")
public class SignatureEntity {

  @Id
  private String id;

  private SignatureStatus indication;

  private String subIndication;

  private Date signingDate;

  private String signatureFormat;

  private String signedBy;

  private Date signedByNotBefore;

  private Date signedByNotAfter;

  private String digestAlgo;

  private String encryptionAlgo;

  private int keyLength;

  public SignatureEntity() {}

  public SignatureStatus getIndication() {
    return indication;
  }

  public void setIndication(SignatureStatus indication) {
    this.indication = indication;
  }

  public String getSubIndication() {
    return subIndication;
  }

  public void setSubIndication(String subIndication) {
    this.subIndication = subIndication;
  }

  public Date getSigningDate() {
    return signingDate;
  }

  public void setSigningDate(Date signingDate) {
    this.signingDate = signingDate;
  }

  public String getSignatureFormat() {
    return signatureFormat;
  }

  public void setSignatureFormat(String signatureFormat) {
    this.signatureFormat = signatureFormat;
  }

  public String getSignedBy() {
    return signedBy;
  }

  public void setSignedBy(String signedBy) {
    this.signedBy = signedBy;
  }

  public Date getSignedByNotBefore() {
    return signedByNotBefore;
  }

  public void setSignedByNotBefore(Date signedByNotBefore) {
    this.signedByNotBefore = signedByNotBefore;
  }

  public Date getSignedByNotAfter() {
    return signedByNotAfter;
  }

  public void setSignedByNotAfter(Date signedByNotAfter) {
    this.signedByNotAfter = signedByNotAfter;
  }

  public String getDigestAlgo() {
    return digestAlgo;
  }

  public void setDigestAlgo(String digestAlgo) {
    this.digestAlgo = digestAlgo;
  }

  public String getEncryptionAlgo() {
    return encryptionAlgo;
  }

  public void setEncryptionAlgo(String encryptionAlgo) {
    this.encryptionAlgo = encryptionAlgo;
  }

  public int getKeyLength() {
    return keyLength;
  }

  public void setKeyLength(int keyLength) {
    this.keyLength = keyLength;
  }
}
