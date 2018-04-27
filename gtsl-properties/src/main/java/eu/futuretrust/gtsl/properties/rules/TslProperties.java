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

package eu.futuretrust.gtsl.properties.rules;

import javax.validation.constraints.NotEmpty;

public class TslProperties {

  @NotEmpty
  private String documentVersion;
  @NotEmpty
  private String tag;
  @NotEmpty
  private String xmlNamespace;
  @NotEmpty
  private String tdpContainer;

  @Override
  public String toString() {
    return "TslProperties{" +
        "documentVersion='" + documentVersion + '\'' +
        ", tag='" + tag + '\'' +
        ", xmlNamespace='" + xmlNamespace + '\'' +
        ", tdpContainer='" + tdpContainer + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TslProperties that = (TslProperties) o;

    if (documentVersion != null ? !documentVersion.equals(that.documentVersion)
        : that.documentVersion != null) {
      return false;
    }
    if (tag != null ? !tag.equals(that.tag) : that.tag != null) {
      return false;
    }
    if (xmlNamespace != null ? !xmlNamespace.equals(that.xmlNamespace)
        : that.xmlNamespace != null) {
      return false;
    }
    return tdpContainer != null ? tdpContainer.equals(that.tdpContainer)
        : that.tdpContainer == null;
  }

  @Override
  public int hashCode() {
    int result = documentVersion != null ? documentVersion.hashCode() : 0;
    result = 31 * result + (tag != null ? tag.hashCode() : 0);
    result = 31 * result + (xmlNamespace != null ? xmlNamespace.hashCode() : 0);
    result = 31 * result + (tdpContainer != null ? tdpContainer.hashCode() : 0);
    return result;
  }

  public String getDocumentVersion() {
    return documentVersion;
  }

  public void setDocumentVersion(String documentVersion) {
    this.documentVersion = documentVersion;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getXmlNamespace() {
    return xmlNamespace;
  }

  public void setXmlNamespace(String xmlNamespace) {
    this.xmlNamespace = xmlNamespace;
  }

  public String getTdpContainer() {
    return tdpContainer;
  }

  public void setTdpContainer(String tdpContainer) {
    this.tdpContainer = tdpContainer;
  }
}
