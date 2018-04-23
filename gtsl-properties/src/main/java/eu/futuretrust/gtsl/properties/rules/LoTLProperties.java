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

public class LoTLProperties {

  @NotEmpty
  private String xml;
  @NotEmpty
  private String pdf;
  @NotEmpty
  private String sha2;

  @Override
  public String toString() {
    return "LoTLProperties{" +
        "xml='" + xml + '\'' +
        ", pdf='" + pdf + '\'' +
        ", sha2='" + sha2 + '\'' +
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

    LoTLProperties that = (LoTLProperties) o;

    if (xml != null ? !xml.equals(that.xml) : that.xml != null) {
      return false;
    }
    if (pdf != null ? !pdf.equals(that.pdf) : that.pdf != null) {
      return false;
    }
    return sha2 != null ? sha2.equals(that.sha2) : that.sha2 == null;
  }

  @Override
  public int hashCode() {
    int result = xml != null ? xml.hashCode() : 0;
    result = 31 * result + (pdf != null ? pdf.hashCode() : 0);
    result = 31 * result + (sha2 != null ? sha2.hashCode() : 0);
    return result;
  }

  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }

  public String getPdf() {
    return pdf;
  }

  public void setPdf(String pdf) {
    this.pdf = pdf;
  }

  public String getSha2() {
    return sha2;
  }

  public void setSha2(String sha2) {
    this.sha2 = sha2;
  }

}
