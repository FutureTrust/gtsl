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

import eu.futuretrust.gtsl.jaxb.xmldsig.KeyInfoTypeJAXB;
import java.util.ArrayList;
import java.util.List;

public class KeyInfoType {

  protected List<Object> content;
  protected String id;


  public KeyInfoType (final KeyInfoTypeJAXB keyInfoTypeJAXB) {

    this.setId(keyInfoTypeJAXB.getId());
    this.content = keyInfoTypeJAXB.getContent();
  }

  public KeyInfoTypeJAXB asJAXB() {

    KeyInfoTypeJAXB keyInfoTypeJAXB = new KeyInfoTypeJAXB();
    if (id != null) {
      keyInfoTypeJAXB.setId(id);
    }
    if (content != null) {
      keyInfoTypeJAXB.getContent().addAll(content);
    }

    return keyInfoTypeJAXB;
  }

  public List<Object> getContent() {
    if (content == null) {
      content = new ArrayList<>();
    }
    return content;
  }

  public void setContent(List<Object> content) {
    this.content = content;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
