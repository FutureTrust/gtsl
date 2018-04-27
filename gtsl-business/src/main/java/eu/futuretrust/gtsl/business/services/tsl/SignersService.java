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

package eu.futuretrust.gtsl.business.services.tsl;

import eu.europa.esig.dss.x509.CertificateToken;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;

public interface SignersService {

  /**
   * Retrieves the signers authorised to sign the European List of the Lists. The certificates
   * for these signers are stored in the application's resources, in a PKCS12 trustore.
   * @return a List of {@link CertificateToken}, each of which belonging to an authorised signer.
   * @throws IOException
   */
  public List<CertificateToken> getLotlPotentialSigners() throws IOException;

  /**
   * Retrieves the signers authorised to sign a Trust List identified by its country code. The
   * certificates for these signers are retrieved from the List of the Lists.
   * @param countryCode
   * @return a List of {@link CertificateToken}, each of which belonging to an authorised signer.
   */
  public List<CertificateToken> getTLPotentialSigners(final String countryCode) throws IOException, CertificateException;

}
