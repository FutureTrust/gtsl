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

package eu.futuretrust.gtsl.business.utils;

import eu.europa.esig.dss.x509.CertificateToken;
import eu.futuretrust.gtsl.jaxb.xmldsig.X509DataTypeJAXB;
import eu.futuretrust.gtsl.model.data.signature.KeyInfoType;
import eu.futuretrust.gtsl.model.data.signature.SignatureType;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.bouncycastle.asn1.eac.ECDSAPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

public class SignatureUtils
{

    private static final String X509_QNAME = "{http://www.w3.org/2000/09/xmldsig#}X509Certificate";

    /**
     * Retrieves the certificate from the signature
     * @param signature the signature model
     * @return a {@link CertificateToken}, extracted from the signature
     */
    public static List<CertificateToken> getCertificateTokensFromSignature(final SignatureType signature) {

        KeyInfoType keyInfo = signature.getKeyInfo();

        if (keyInfo != null && keyInfo.getContent().size() > 0) {

            for (final Object obj : keyInfo.getContent()) {
                try {
                    final JAXBElement jaxbElement = (JAXBElement) obj;

                    if (jaxbElement.getDeclaredType().equals(X509DataTypeJAXB.class)) {
                        return getCertificatesFromX509Data((X509DataTypeJAXB) jaxbElement.getValue())
                                .stream()
                                .map(o -> new CertificateToken(o))
                                .collect(Collectors.toList());
                    }
                } catch (ClassCastException | CertificateException e) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves the list of certificates that may be included in a signature's KeyInfo element.
     * @param signature the signature from which the certificates must be extracted
     * @return the list of {@link X509Certificate} extracted from the signature's KeyInfo element.
     */
    public static List<X509Certificate> getCertificatesFromSignature(final SignatureType signature) {

        KeyInfoType keyInfo = signature.getKeyInfo();

        if (keyInfo != null && keyInfo.getContent().size() > 0) {

          for (final Object obj : keyInfo.getContent()) {

            if (obj instanceof JAXBElement)
            {
              final JAXBElement jaxbElement = (JAXBElement) obj;

              if (jaxbElement.getDeclaredType().equals(X509DataTypeJAXB.class))
              {
                try
                {
                  return getCertificatesFromX509Data((X509DataTypeJAXB) jaxbElement.getValue());
                } catch (final CertificateException e)
                {
                  return new ArrayList<>();
                }
              }
            }
          }
        }
        return new ArrayList<>();
    }

    /**
     * Retrieves the only certificate that is supposed to be available in the signature's X509Data element.
     * @param x509Data the signature's X509Data element
     * @return the extracted certificate
     * @throws CertificateException if such an exception was encountered when building the certificate from the available
     * base 64 value
     * ETSI TS 119 612 standard specifies (section 5.7.1) that "[...] the ds:KeyInfo element [...] shall not contain any
     * other certificate forming any kind of associated chain."
     */
    public static List<X509Certificate> getCertificatesFromX509Data(final X509DataTypeJAXB x509Data)
            throws CertificateException
    {

        final List<X509Certificate> certificatesFromX509Data = new ArrayList<>();

        if (x509Data != null && x509Data.getX509IssuerSerialOrX509SKIOrX509SubjectName().size() > 0) {
            for (final Object obj : x509Data.getX509IssuerSerialOrX509SKIOrX509SubjectName()) {
                final JAXBElement jaxbElement = (JAXBElement) obj;

                if (jaxbElement.getName().equals(QName.valueOf(X509_QNAME))) {
                    final CertificateFactory factory = CertificateFactory.getInstance("X.509");
                    certificatesFromX509Data.add(
                            (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(
                                    (byte[]) jaxbElement.getValue())));
                }
            }
        }
        return certificatesFromX509Data;
    }

    /**
     * Implements the constraint specified in ETSI TS 119 612 V2.2.1:
     * "The TLSO certificate, to be used to validate its digital signature on the TL, shall be
     * protected with the digital signature by incorporating the TLSO certificate within the
     * ds:KeyInfo element [...]"
     * @param signature
     * @return
     */
    public static boolean isCertificateInKeyInfo(final SignatureType signature) { return false;}


    /**
     * Retrieves a consolidated list of all TSP services from a given TSL
     * @param tsl the TSL from which the list of TSP services must be retrieved
     * @return a list of {@link TSPServiceType}, extracted from the TSL
     */
    public static List<TSPServiceType> getTSPServiceTypesFromTSL(final TrustStatusListType tsl) {

        return tsl.getTrustServiceProviderList()
                .getValues()
                .stream()
                .flatMap(o -> o.getTspServices().getValues().stream())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the key length from a provided {@link PublicKey}
     * As the gTSL implements the requirements set forth by the ETSI TS 119 312 standard, the only public key formats
     * that are supported are: RSA, DSA, ECDSA
     * @param pk
     * @return an int corresponding to the key length
     */
    public static int getKeyLength(final PublicKey pk)
    {
        if (pk instanceof RSAPublicKey) {
            return getKeyLength((RSAPublicKey) pk);
        } else if (pk instanceof DSAPublicKey) {
            return getKeyLength((DSAPublicKey) pk);
        } else if (pk instanceof ECDSAPublicKey) {
            return getKeyLength((ECDSAPublicKey) pk);
        } else if (pk instanceof ECPublicKey) {
          return getKeyLength((ECPublicKey) pk);
        } else if (pk instanceof BCECPublicKey) {
            return getKeyLength((BCECPublicKey) pk);
        } else {
          return -1;
        }
    }

    public static int getKeyLength(final RSAPublicKey pk) {
        return pk.getModulus().bitLength();
    }

    public static int getKeyLength(final DSAPublicKey pk) {
        if (null != pk.getParams()) {
            return pk.getParams().getP().bitLength();
        } else {
            return pk.getY().bitLength();
        }
    }

    public static int getKeyLength(final ECDSAPublicKey pk) {
        return pk.getPrimeModulusP().bitLength();
    }

    public static int getKeyLength(final ECPublicKey pk) {
        if (pk.getParams() != null) {
            return pk.getParams().getOrder().bitLength();
        }
        return 0;
    }
}
