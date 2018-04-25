package eu.futuretrust.gtsl.web.nexu;

public class TLSignatureInformation {

  private int tlId;
  private byte[] signatureValue;
  private GetCertificateResponse certificateResponse;

  public int getTlId() {
    return tlId;
  }

  public void setTlId(int tlId) {
    this.tlId = tlId;
  }

  public byte[] getSignatureValue() {
    return signatureValue;
  }

  public void setSignatureValue(byte[] signatureValue) {
    this.signatureValue = signatureValue;
  }

  public GetCertificateResponse getCertificateResponse() {
    return certificateResponse;
  }

  public void setCertificateResponse(GetCertificateResponse certificateResponse) {
    this.certificateResponse = certificateResponse;
  }

}
