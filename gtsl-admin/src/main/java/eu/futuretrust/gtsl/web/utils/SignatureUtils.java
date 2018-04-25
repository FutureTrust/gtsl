package eu.futuretrust.gtsl.web.utils;

import eu.europa.esig.dss.x509.CertificateToken;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignatureUtils {

  private final static Logger LOGGER = LoggerFactory.getLogger(SignatureUtils.class);

  public static CertificateToken getCertificateTokenFromBase64String(final String base64Value) {

    CertificateFactory certFactory;
    try {
      certFactory = CertificateFactory.getInstance("X.509");

      final byte[] certBytes = Base64.getDecoder().decode(base64Value);
      final ByteArrayInputStream is = new ByteArrayInputStream(certBytes);
      return new CertificateToken ((X509Certificate) certFactory.generateCertificate(is));
    } catch (CertificateException e) {
      LOGGER.error("Error generating certificate");
      e.printStackTrace();
    }
    return null;
  }

  public static CertificateToken[] getCertificateChainFromBase64Array(final String[] base64Chain) {

    CertificateToken[] certificateChain = new CertificateToken[base64Chain.length];

    for (int i = 0; i < base64Chain.length; i++) {

      certificateChain[i] = getCertificateTokenFromBase64String(base64Chain[i]);
    }

    return certificateChain;
  }
}
