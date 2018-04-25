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

package eu.futuretrust.gtsl.model.data.abstracts;


import eu.futuretrust.gtsl.model.constraints.NoNullElements;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

public abstract class ListModel<T> {

  @NoNullElements
  @Valid // Validate all the elements in the list
  protected List<T> values;

  public ListModel() {values = new ArrayList<>();}

  public ListModel(List<T> values) {
    this.values = values;
  }

  public void setValues(List<T> values) {
    this.values = values;
  }

  public List<T> getValues() {
    return values;
  }
}
