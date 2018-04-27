package eu.futuretrust.gtsl.admin.tsl;

import eu.futuretrust.gtsl.persistence.entities.SignatureEntity;
import eu.futuretrust.gtsl.persistence.enums.SignatureStatus;
import eu.futuretrust.gtsl.model.data.enums.TSLTag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TLSignatureDTO {

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

  public TLSignatureDTO() {}

  public TLSignatureDTO(final SignatureEntity signature) {

    this.setDigestAlgo(signature.getDigestAlgo());
    this.setEncryptionAlgo(signature.getEncryptionAlgo());
    this.setIndication(signature.getIndication());
    this.setKeyLength(signature.getKeyLength());
    this.setSignatureFormat(signature.getSignatureFormat());
    this.setSignedBy(signature.getSignedBy());
    this.setSignedByNotAfter(signature.getSignedByNotAfter());
    this.setSignedByNotBefore(signature.getSignedByNotBefore());
    this.setSigningDate(signature.getSigningDate());
    this.setSubIndication(signature.getSubIndication());
  }

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

  public List<TLDifferenceDTO> asPublishedDiff(TLSignatureDTO comparedSignature, String parent) {
    List<TLDifferenceDTO> differences = new ArrayList<TLDifferenceDTO>();
    if (comparedSignature.getIndication()==SignatureStatus.NOT_SIGNED) {
      differences.add(new TLDifferenceDTO(parent, "", this.getSignedBy() + " - " + this.getSigningDate().toString()));
    } else if (!this.equals(comparedSignature)) {
      // Digest Algo
      if (!this.getDigestAlgo().equals(comparedSignature.getDigestAlgo())) {
        differences.add(new TLDifferenceDTO(parent + "_" + TSLTag.DIGEST_ALGORITHM, comparedSignature.getDigestAlgo(), this.getDigestAlgo()));
      }
      // Encryption Algo
      if (!this.getEncryptionAlgo().equals(comparedSignature.getEncryptionAlgo())) {
        differences.add(
            new TLDifferenceDTO(parent + "_" + TSLTag.ENCRYPTION_ALGORITHM, comparedSignature.getEncryptionAlgo(), this.getEncryptionAlgo()));
      }
      // Key Length
      if (!this.getIndication().equals(comparedSignature.getIndication())) {
        differences.add(new TLDifferenceDTO(parent + "_" + TSLTag.INDICATION, comparedSignature.getIndication().toString(),
            this.getIndication().toString()));
      }
      // Signature Format
      if (!this.getSignatureFormat().equals(comparedSignature.getSignatureFormat())) {
        differences.add(
            new TLDifferenceDTO(parent + "_" + TSLTag.SIGNATURE_FORMAT, comparedSignature.getSignatureFormat(), this.getSignatureFormat()));
      }
      // Signature By
      if (!this.getSignedBy().equals(comparedSignature.getSignedBy())) {
        differences.add(new TLDifferenceDTO(parent + "_" + TSLTag.SIGNED_BY, comparedSignature.getSignedBy(), this.getSignedBy()));
      }
      // Signing Date
      final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      String currentDate = this.getSigningDate() != null ? df.format(getSigningDate()) : "";
      String comparedDate = comparedSignature.getSigningDate() != null ? df.format(comparedSignature
          .getSigningDate()) : "";
      if (!currentDate.equals(comparedDate)) {
        differences.add(new TLDifferenceDTO(parent + "_" + TSLTag.SIGNING_DATE, comparedDate, currentDate));
      }
    }
    return differences;
  }
}
