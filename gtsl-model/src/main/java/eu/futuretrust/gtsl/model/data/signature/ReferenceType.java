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

import eu.futuretrust.gtsl.jaxb.xmldsig.ReferenceTypeJAXB;

public class ReferenceType {

  protected TransformsType transforms;
  protected DigestMethodType digestMethod;
  protected byte[] digestValue;
  protected String id;
  protected String uri;
  protected String type;


  public ReferenceType (final ReferenceTypeJAXB referenceTypeJAXB) {

    if (referenceTypeJAXB.getTransforms() != null) {
      this.transforms = new TransformsType(referenceTypeJAXB.getTransforms());
    }
    if (referenceTypeJAXB.getDigestMethod() != null) {
      this.digestMethod = new DigestMethodType(referenceTypeJAXB.getDigestMethod());
    }
    if (referenceTypeJAXB.getDigestValue() != null) {
      this.digestValue = referenceTypeJAXB.getDigestValue();
    }

    if (referenceTypeJAXB.getId() != null) {
      this.id = referenceTypeJAXB.getId();
    }
    if (referenceTypeJAXB.getURI() != null) {
      this.uri = referenceTypeJAXB.getURI();
    }
    if (referenceTypeJAXB.getType() != null) {
      this.type = referenceTypeJAXB.getType();
    }
  }

  public TransformsType getTransforms() {
    return transforms;
  }

  public void setTransforms(TransformsType transforms) {
    this.transforms = transforms;
  }

  public DigestMethodType getDigestMethod() {
    return digestMethod;
  }

  public void setDigestMethod(DigestMethodType digestMethod) {
    this.digestMethod = digestMethod;
  }

  public byte[] getDigestValue() {
    return digestValue;
  }

  public void setDigestValue(byte[] digestValue) {
    this.digestValue = digestValue;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ReferenceTypeJAXB asJAXB() {

    ReferenceTypeJAXB referenceTypeJAXB = new ReferenceTypeJAXB();
    if (digestMethod != null) {
      referenceTypeJAXB.setDigestMethod(digestMethod.asJAXB());
    }
    if (digestValue != null) {
      referenceTypeJAXB.setDigestValue(digestValue);
    }
    if (id != null) {
      referenceTypeJAXB.setId(id);
    }
    if (transforms != null) {
      referenceTypeJAXB.setTransforms(transforms.asJAXB());
    }
    if (uri != null) {
      referenceTypeJAXB.setURI(uri);
    }
    if (type != null) {
      referenceTypeJAXB.setType(type);
    }
    return referenceTypeJAXB;
  }
}
