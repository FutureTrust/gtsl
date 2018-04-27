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

import eu.futuretrust.gtsl.jaxb.xmldsig.SignatureTypeJAXB;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class SignatureType {

  private KeyInfoType keyInfo;
  private String id;
  private SignatureValueType signatureValue;
  private SignedInfoType signedInfo;
  protected List<ObjectType> object;

  public SignatureType() {
  }

  public SignatureType(final SignatureTypeJAXB signatureTypeJAXB) {

    this.setId(signatureTypeJAXB.getId());
    this.setKeyInfo(new KeyInfoType(signatureTypeJAXB.getKeyInfo()));
    this.setSignatureValue(new SignatureValueType(signatureTypeJAXB.getSignatureValue()));
    this.setSignedInfo(new SignedInfoType(signatureTypeJAXB.getSignedInfo()));

    if (!signatureTypeJAXB.getObject().isEmpty()) {
      object = signatureTypeJAXB.getObject().stream()
          .map(ObjectType::new)
          .collect(Collectors.toList());
    }
  }

  public List<ObjectType> getObject() {
    if (object == null) {
      object = new ArrayList<>();
    }
    return object;
  }

  public void setObject(List<ObjectType> object) {
    this.object = object;
  }

  public KeyInfoType getKeyInfo() {
    return keyInfo;
  }

  public void setKeyInfo(KeyInfoType keyInfo) {
    this.keyInfo = keyInfo;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SignatureValueType getSignatureValue() {
    return signatureValue;
  }

  public void setSignatureValue(SignatureValueType signatureValue) {
    this.signatureValue = signatureValue;
  }

  public SignedInfoType getSignedInfo() {
    return signedInfo;
  }

  public void setSignedInfo(SignedInfoType signedInfo) {
    this.signedInfo = signedInfo;
  }

  public SignatureTypeJAXB asJAXB() {
    SignatureTypeJAXB signatureTypeJAXB = new SignatureTypeJAXB();
    if (this.id != null) {
      signatureTypeJAXB.setId(id);
    }
    if (this.keyInfo != null) {
      signatureTypeJAXB.setKeyInfo(keyInfo.asJAXB());
    }
    if (this.signatureValue != null) {
      signatureTypeJAXB.setSignatureValue(signatureValue.asJAXB());
    }
    if (this.signedInfo != null) {
      signatureTypeJAXB.setSignedInfo(signedInfo.asJAXB());
    }
    if (this.object != null && CollectionUtils.isNotEmpty(object)) {
      signatureTypeJAXB.getObject().addAll(
          object.stream()
              .map(ObjectType::asJAXB)
              .collect(Collectors.toList()));
    }

    return signatureTypeJAXB;
  }
}
