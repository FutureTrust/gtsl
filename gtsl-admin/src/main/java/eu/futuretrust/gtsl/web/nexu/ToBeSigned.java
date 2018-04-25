package eu.futuretrust.gtsl.web.nexu;

import java.util.Base64;

public class ToBeSigned {

  private String bytes;

  public ToBeSigned(final eu.europa.esig.dss.ToBeSigned toBeSigned) {

    final byte[] tbs = toBeSigned.getBytes();
    bytes = Base64.getEncoder().encodeToString(tbs);
  }

  public String getBytes() {
    return bytes;
  }

  public void setBytes(String bytes) {
    this.bytes = bytes;
  }
}
