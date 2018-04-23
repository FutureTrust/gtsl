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

let digitalId = {};

digitalId.generateHtml = function (label, fieldName, objName, objId, index) {
  return '<div data-index="' + index + '" class="data-' + fieldName + '">'
      + '<div class="row">'
      + ''
      + '<div class="col-xs-4 col-sm-3 col-md-3 col-lg-3">'
      + '<select class="form-control" id="select-' + fieldName + '-' + index + '">'
      + '<option value="cert" selected="selected">X509Certificate</option>'
      + '<option value="sn">X509SubjectName</option>'
      + '<option value="ski">X509SKI</option>'
      + '</select>'
      + '</div>'
      + ''
      + '<div class="data-' + fieldName + '-' + index + ' '
      + 'target col-xs-5 col-sm-7 col-md-7 col-lg-7" '
      + 'id="target-' + fieldName + '-' + index + '" class="target">'
      + '<input class="form-control" '
      + 'id="' + objId + index + '.certificateList0.certEncoded" '
      + 'name="' + objName + '[' + index + '].certificateList[0].certEncoded" '
      + 'value="">'
      + '</div>'
      + ''
      + ''
      + '<div class="col-xs-3 col-sm-2 col-md-2 col-lg-2">'
      + '<button type="button" class="btn btn-default btn-xs" name="remove-'
      + fieldName + '">'
      + '<span class="glyphicon glyphicon-minus"></span>'
      + '<span>&ensp;' + label + '</span>'
      + '</button>'
      + '</div>'
      + ''
      + '</div>'
      + '</div>';
};

digitalId.add = function (label, fieldName, objName, objId) {

  let buttonAdd = $('button[name=add-' + fieldName + ']');
  buttonAdd.off('click');
  buttonAdd.click(function (e) {
    e.preventDefault();
    e.stopImmediatePropagation();

    let me = $(this),
        index = $('.data-' + fieldName).length,
        html = digitalId.generateHtml(label, fieldName, objName, objId, index);

    me.before(html);
    digitalId.add(label, fieldName, objName, objId);
    digitalIdValue.add(fieldName, objName, objId, index);
  });
  let buttonRemove = $('button[name=remove-' + fieldName + ']');
  buttonRemove.off('click');
  buttonRemove.click(function (e) {
    e.preventDefault();
    e.stopImmediatePropagation();

    let me = $(this),
        parent = me.closest('.data-' + fieldName),
        index = parent.data('index');

    digitalId.reorderIndexes(fieldName, objName, objId, index);
    parent.remove();
  });

  digitalId.addEvent(fieldName, objName, objId);
};

digitalId.reorderIndexes = function (fieldName, objName, objId, index) {

  let obj = $('.data-' + fieldName);

  obj.find('select[id*="' + objId + '"]').each(function () {
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

  obj.find('input[id*="' + objId + '"]').each(function () {
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

  obj.find('select[id*="select-' + fieldName + '-"]').each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          id = $(this).attr("id");

      if (id) {
        id = id.replace('select-' + fieldName + '-' + currentIndex,
            'select-' + fieldName + '-' + newIndex);
        $(this).attr("id", id);
      }
    }
  });

  obj.find('div[id*="target-' + fieldName + '-"]').each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          id = $(this).attr("id"),
          classAttr = $(this).attr("class");

      if (classAttr) {
        classAttr = classAttr.replace('data-' + fieldName + '-' + currentIndex,
            'data-' + fieldName + '-' + newIndex);
        $(this).attr("class", classAttr);
      }
      if (id) {
        id = id.replace('target-' + fieldName + '-' + currentIndex,
            'target-' + fieldName + '-' + newIndex);
        $(this).attr("id", id);
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

digitalId.addEvent = function (fieldName, objName, objId) {
  let obj = $('.data-' + fieldName);
  for (let i = 0; i < obj.length; i++) {
    digitalIdValue.add(fieldName, objName, objId, i);
  }
};