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

"use strict";

let list = {};

list.generateHtml = function (label, fieldName, objName, objId, index) {
  return '<div data-index="' + index + '" class="data-' + fieldName + '">'
      + '<div class="row">'
      + ''
      + '<span class="col-xs-9 col-sm-10 col-md-10 col-lg-10">'
      + '<input class="form-control"  id="' + objId + index
      + '.value" name="' + objName + '[' + index + '].value" value="">'
      + '</span>'
      + ''
      + '<span class="col-xs-3 col-sm-2 col-md-2 col-lg-2">'
      + '<button type="button" class="btn btn-default btn-xs" name="remove-'
      + fieldName + '" value="' + index + '">'
      + '<span class="glyphicon glyphicon-minus"></span>'
      + '<span>&ensp;' + label + '</span>'
      + '</button>'
      + '</span>'
      + ''
      + '</div>'
      + '</div>';
};

list.add = function (label, fieldName, objName, objId) {

  saveAttributes(fieldName);

  let buttonAdd = $('button[name=add-' + fieldName + ']');
  buttonAdd.off('click');
  buttonAdd.click(function (e) {
    e.preventDefault();
    e.stopImmediatePropagation();

    let me = $(this),
        index = $('.data-' + fieldName).length,
        html = list.generateHtml(label, fieldName, objName, objId, index);

    me.before(html);
    list.add(label, fieldName, objName, objId);
  });

  let buttonRemove = $('button[name=remove-' + fieldName + ']');
  buttonRemove.off('click');
  buttonRemove.click(function (e) {
    e.preventDefault();
    e.stopImmediatePropagation();

    let me = $(this),
        parent = me.closest('.data-' + fieldName),
        index = parent.data('index');

    list.reorderIndexes(fieldName, objName, objId, index);
    parent.remove();
  });
};

list.reorderIndexes = function (fieldName, objName, objId, index) {

  let obj = $('.data-' + fieldName);

  obj.find("input").each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          id = $(this).attr("id"),
          name = $(this).attr("name");

      if (id) {
        id = id.replace(objId + currentIndex, objId + newIndex);
        $(this).attr("id", id);
      }
      if (name) {
        name = name.replace(objName + '[' + currentIndex + ']',
            objName + '[' + newIndex + ']');
        $(this).attr("name", name);
      }
    }
  });

  obj.each(function () {
    let currentIndex = $(this).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1;
      $(this).data("index", newIndex);
      $(this).attr("data-index", newIndex);
    }
  });

};