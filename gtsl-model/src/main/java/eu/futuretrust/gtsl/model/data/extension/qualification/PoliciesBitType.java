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

package eu.futuretrust.gtsl.model.data.extension.qualification;

import eu.futuretrust.gtsl.jaxb.xades.DocumentationReferencesTypeJAXB;
import eu.futuretrust.gtsl.jaxb.xades.IdentifierTypeJAXB;
import eu.futuretrust.gtsl.jaxb.xades.ObjectIdentifierTypeJAXB;
import eu.futuretrust.gtsl.jaxb.xades.QualifierTypeJAXB;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class PoliciesBitType {

  private String description;
  private String identifierValue;
  private String identifierType;
  private List<String> documentationReferences;

  public PoliciesBitType() {
  }

  public PoliciesBitType(
      ObjectIdentifierTypeJAXB objectIdentifierJAXB) {
    if (objectIdentifierJAXB.getDescription() != null) {
      this.description = objectIdentifierJAXB.getDescription().trim();
    }
    if (objectIdentifierJAXB.getIdentifier() != null) {
      if (objectIdentifierJAXB.getIdentifier().getValue() != null) {
        this.identifierValue = objectIdentifierJAXB.getIdentifier().getValue().trim();
      }
      if (objectIdentifierJAXB.getIdentifier().getQualifier() != null
          && objectIdentifierJAXB.getIdentifier().getQualifier().name() != null
          && objectIdentifierJAXB.getIdentifier().getQualifier().value() != null) {
        this.identifierType = objectIdentifierJAXB.getIdentifier().getQualifier().value().trim();
      }
    }
    if (objectIdentifierJAXB.getDocumentationReferences() != null && CollectionUtils
        .isNotEmpty(
            objectIdentifierJAXB.getDocumentationReferences().getDocumentationReference())) {
      this.documentationReferences = objectIdentifierJAXB.getDocumentationReferences()
          .getDocumentationReference();
    }
  }

  // from TL-Manager
  public ObjectIdentifierTypeJAXB asJAXB() {
    ObjectIdentifierTypeJAXB objectIdJAXB = new ObjectIdentifierTypeJAXB();

    if (description != null) {
      objectIdJAXB.setDescription(this.getDescription());
    }

    IdentifierTypeJAXB identifierJAXB = new IdentifierTypeJAXB();
    identifierJAXB.setValue(this.identifierValue);
    if (QualifierTypeJAXB.OID_AS_URI.value().equalsIgnoreCase(this.identifierType)) {
      identifierJAXB.setQualifier(QualifierTypeJAXB.OID_AS_URI);
    } else if (QualifierTypeJAXB.OID_AS_URN.value().equalsIgnoreCase(this.identifierType)) {
      identifierJAXB.setQualifier(QualifierTypeJAXB.OID_AS_URN);
    }
    objectIdJAXB.setIdentifier(identifierJAXB);

    if (this.documentationReferences != null) {
      DocumentationReferencesTypeJAXB doc = new DocumentationReferencesTypeJAXB();
      for (String str : documentationReferences) {
        doc.getDocumentationReference().add(str);
      }
      objectIdJAXB.setDocumentationReferences(doc);
    }

    return objectIdJAXB;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getIdentifierValue() {
    return identifierValue;
  }

  public void setIdentifierValue(String identifierValue) {
    this.identifierValue = identifierValue;
  }

  public String getIdentifierType() {
    return identifierType;
  }

  public void setIdentifierType(String identifierType) {
    this.identifierType = identifierType;
  }

  public List<String> getDocumentationReferences() {
    return documentationReferences;
  }

  public void setDocumentationReferences(List<String> documentationReferences) {
    this.documentationReferences = documentationReferences;
  }

}
