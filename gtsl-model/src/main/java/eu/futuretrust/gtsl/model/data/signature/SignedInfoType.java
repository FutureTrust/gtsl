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

package eu.futuretrust.gtsl.model.data.signature;

import eu.futuretrust.gtsl.jaxb.xmldsig.SignedInfoTypeJAXB;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SignedInfoType {

  private CanonicalizationMethodType canonicalizationMethod;
  private SignatureMethodType signatureMethod;
  private List<ReferenceType> reference;
  private String id;

  public SignedInfoType (final SignedInfoTypeJAXB signedInfoTypeJAXB) {

    canonicalizationMethod = new CanonicalizationMethodType(signedInfoTypeJAXB
        .getCanonicalizationMethod());
    id = signedInfoTypeJAXB.getId();
    signatureMethod = new SignatureMethodType(signedInfoTypeJAXB.getSignatureMethod());
    if (signedInfoTypeJAXB.getReference().isEmpty()) {
      reference = Collections.emptyList();
    }
    else {
      reference = signedInfoTypeJAXB.getReference()
          .stream()
          .map(ReferenceType::new)
          .collect(Collectors.toList());
    }
  }

  public CanonicalizationMethodType getCanonicalizationMethod() {
    return canonicalizationMethod;
  }

  public void setCanonicalizationMethod(CanonicalizationMethodType canonicalizationMethod) {
    this.canonicalizationMethod = canonicalizationMethod;
  }

  public SignatureMethodType getSignatureMethod() {
    return signatureMethod;
  }

  public void setSignatureMethod(SignatureMethodType signatureMethod) {
    this.signatureMethod = signatureMethod;
  }

  public List<ReferenceType> getReference() {
    return reference;
  }

  public void setReference(List<ReferenceType> reference) {
    this.reference = reference;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SignedInfoTypeJAXB asJAXB() {

    SignedInfoTypeJAXB signedInfoTypeJAXB = new SignedInfoTypeJAXB();
    signedInfoTypeJAXB.setCanonicalizationMethod(canonicalizationMethod.asJAXB());
    signedInfoTypeJAXB.setId(id);
    signedInfoTypeJAXB.setSignatureMethod(signatureMethod.asJAXB());

    signedInfoTypeJAXB.getReference()
        .addAll(getReference().stream()
          .map(ref -> ref.asJAXB())
          .collect(Collectors.toList()));

    return signedInfoTypeJAXB;
  }
}
