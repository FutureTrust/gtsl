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

package eu.futuretrust.gtsl.business.services.validator.rules.signature;

/**
 * This enum stores the information contained in Table 9 of ETSI TS 119 312, v1.2.1, i.e. the acceptable key length
 * parameters for cryptographic keys used in digital signatures schemes.
 * The first parameter consists in the minimal key length acceptable for a given algorithm until end of the year 2020,
 * the second parameter consists in the minimal key length acceptable for a given algorithm until end of the year 2025.
 */
public enum CryptographicSuite {

    SHA256withRSA(1900, -1),
    SHA512withRSA(1900,-1),
    RSA_SSA_PSS_SHA256_MGF1(1900, 3000),
    RSA_SSA_PSS_SHA512_MGF1(1900, 3000),
    RSA_SSA_PSS_SHA384_MGF1(1900, 3000),
    SHA256withDSA(2048, 3072),
    SHA512withDSA(2048, 3072),
    SHA256withECDSA(256, 256),
    SHA512withECDSA(256, 256),
    SHA256withECSDSA(256, 256),
    SHA512withECSDSA(256, 256),
    SHA384withECDSA(256, 256),
    SHA384withECSDSA(256, 256);

    private final int keyLengthThreeYears;
    private final int keyLengthSixYears;

    CryptographicSuite(final int keyLengthThreeYears,
                       final int keyLengthSixYears) {

        this.keyLengthThreeYears = keyLengthThreeYears;
        this.keyLengthSixYears = keyLengthSixYears;
    }

    public int getKeyLengthThreeYears() {
        return keyLengthThreeYears;
    }

    public int getKeyLengthSixYears() {
        return keyLengthSixYears;
    }
}
