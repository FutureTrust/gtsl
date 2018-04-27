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

let providers = {};

providers.generateHtml = function (label, fieldName, objName, objId, index) {
  return '<div data-index="' + index + '" class="data-' + fieldName + '">'
      + '<div class="panel panel-default">'
      + ''
      + '<div id="heading-' + fieldName + '-' + index + '">'
      + '<h2 class="title">'
      + '<a class="panel-heading a-link-custom collapsed" '
      + 'data-toggle="collapse" '
      + 'href="#collapse-' + fieldName + '-' + index + '" '
      + 'data-parent="#parent-' + fieldName + '"> '
      + '<span>' + label + '</span>'
      + '<i class="fa fa-angle-down fa-lg hack-icon"></i>'
      + '</a>'
      + '</h2>'
      + '</div>'
      + ''
      + '<div id="collapse-' + fieldName + '-' + index + '" '
      + 'class="panel-collapse collapse" '
      + 'aria-labelledby="heading-' + fieldName + '-' + index + '">'
      + '<div class="panel-body">'
      + ''
      + '<div class="form-group accordion" '
      + 'id="parent-' + fieldName + '-' + index + '-information">'
      + '<div class="panel panel-default">'
      + ''
      + '<div id="heading-' + fieldName + '-' + index + '-information">'
      + '<h3 class="title">'
      + '<a class="panel-heading a-link-custom collapsed" data-toggle="collapse" '
      + 'data-parent="#parent-' + fieldName + '-' + index + '-information" '
      + 'href="#collapse-' + fieldName + '-' + index + '-information">'
      + '<span>TSP information</span>'
      + '<i class="fa fa-angle-down fa-lg hack-icon"></i>'
      + '</a>'
      + '</h3>'
      + '</div>'
      + ''
      + '<div id="collapse-' + fieldName + '-' + index + '-information" '
      + 'class="panel-collapse collapse" '
      + 'aria-labelledby="heading-' + fieldName + '-' + index + '-information">'
      + '<div class="panel-body">'
      + ''
      + '<h4>Name</h4>'
      + '<button type="button" class="btn btn-primary btn-xs" name="add-'
      + fieldName + '-' + index + '-name">'
      + '<span class="glyphicon glyphicon-plus"></span>'
      + '<span>&ensp;' + 'Name' + '</span>'
      + '</button>'
      + ''
      + '<hr/>'
      + ''
      + '<h4>Trade name</h4>'
      + '<button type="button" class="btn btn-primary btn-xs" name="add-'
      + fieldName + '-' + index + '-tradeName">'
      + '<span class="glyphicon glyphicon-plus"></span>'
      + '<span>&ensp;' + 'Trade name' + '</span>'
      + '</button>'
      + ''
      + '<hr/>'
      + ''
      + '<h4>Electronic address</h4>'
      + '<button type="button" class="btn btn-primary btn-xs" name="add-'
      + fieldName + '-' + index + '-electronicAddress">'
      + '<span class="glyphicon glyphicon-plus"></span>'
      + '<span>&ensp;' + 'Electronic address' + '</span>'
      + '</button>'
      + ''
      + '<hr/>'
      + ''
      + '<h4>Postal address</h4>'
      + '<button type="button" class="btn btn-primary btn-xs" name="add-'
      + fieldName + '-' + index + '-postalAddress">'
      + '<span class="glyphicon glyphicon-plus"></span>'
      + '<span>&ensp;' + 'Postal address' + '</span>'
      + '</button>'
      + ''
      + '<hr/>'
      + ''
      + '<h4>Information URI</h4>'
      + '<button type="button" class="btn btn-primary btn-xs" name="add-'
      + fieldName + '-' + index + '-informationUri">'
      + '<span class="glyphicon glyphicon-plus"></span>'
      + '<span>&ensp;' + 'Information URI' + '</span>'
      + '</button>'
      + ''
      + '<hr/>'
      + ''
      + '</div>'
      + '</div>'
      + ''
      + '<div id="heading-' + fieldName + '-' + index + '-services">'
      + '<h3 class="title">'
      + '<a class="panel-heading a-link-custom collapsed" data-toggle="collapse" '
      + 'data-parent="#parent-' + fieldName + '-' + index + '-services" '
      + 'href="#collapse-' + fieldName + '-' + index + '-services">'
      + '<span>TSP services</span>'
      + '<i class="fa fa-angle-down fa-lg hack-icon"></i>'
      + '</a>'
      + '</h3>'
      + '</div>'
      + ''
      + '<div id="collapse-' + fieldName + '-' + index + '-services" '
      + 'class="panel-collapse collapse" '
      + 'aria-labelledby="heading-' + fieldName + '-' + index + '-services">'
      + '<div class="panel-body">'
      + '</div>'
      + ''
      + '<button type="button" class="btn btn-primary btn-xs" name="add-'
      + fieldName + '-' + index + '-services">'
      + '<span class="glyphicon glyphicon-plus"></span>'
      + '<span>Service</span>'
      + '</button>'
      + ''
      + '</div>'
      + ''
      + '</div>'
      + '</div>'
      + ''
      + '<button type="button" class="btn btn-default btn-xs" name="remove-'
      + fieldName + '">'
      + '<span class="glyphicon glyphicon-minus"></span>'
      + '<span>&ensp;' + label + '</span>'
      + '</button>'
      + ''
      + '</div>'
      + '</div>'
      + ''
      + '</div>'
      + '</div>';
};

providers.add = function (label, fieldName, objName, objId) {

  //saveAttributes(fieldName);

  let buttonAdd = $('button[name=add-' + fieldName + ']');
  buttonAdd.off('click');
  buttonAdd.click(function (e) {
    e.preventDefault();
    e.stopImmediatePropagation();

    let me = $(this),
        obj = $('.data-' + fieldName),
        index = obj.length,
        html = providers.generateHtml(label, fieldName, objName, objId, index);

    me.before(html);
    providers.add(label, fieldName, objName, objId);
    providers.addEvent(fieldName, objName, objId);
  });

  let buttonRemove = $('button[name=remove-' + fieldName + ']');
  buttonRemove.off('click');
  buttonRemove.click(function (e) {
    e.preventDefault();
    e.stopImmediatePropagation();

    let me = $(this),
        parent = me.closest('.data-' + fieldName),
        index = parent.data('index');

    providers.reorderIndexes(fieldName, objName, objId, index);
    parent.remove();
    providers.addEvent(fieldName, objName, objId);
  });

  providers.addEvent(fieldName, objName, objId);
};

providers.reorderIndexes = function (fieldName, objName, objId, index) {

  let obj = $('.data-' + fieldName);

  obj.find('div[id*="parent-' + fieldName + '"]').each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          id = $(this).attr("id");

      if (id) {
        id = id.replace('parent-' + fieldName + '-' + currentIndex,
            'parent-' + fieldName + '-' + newIndex);
        $(this).attr("id", id);
      }
    }
  });
  obj.find('a[data-parent*="#parent-' + fieldName + '"]').each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          dataParent = $(this).attr("data-parent");

      if (dataParent) {
        dataParent = dataParent.replace('parent-' + fieldName + '-' + currentIndex,
            'parent-' + fieldName + '-' + newIndex);
        $(this).attr("data-parent", dataParent);
      }
    }
  });
  obj.find('div[id*="parent-' + fieldName + '"]').each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          id = $(this).attr("id");

      if (id) {
        id = id.replace('parent-' + fieldName + '-' + currentIndex,
            'parent-' + fieldName + '-' + newIndex);
        $(this).attr("id", id);
      }
    }
  });
  obj.find('div[id*="heading-' + fieldName + '"]').each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          id = $(this).attr("id");

      if (id) {
        id = id.replace('heading-' + fieldName + '-' + currentIndex,
            'heading-' + fieldName + '-' + newIndex);
        $(this).attr("id", id);
      }
    }
  });
  obj.find('a[href*="#collapse-' + fieldName + '"]').each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          href = $(this).attr("href");

      if (href) {
        href = href.replace('#collapse-' + fieldName + '-' + currentIndex,
            '#collapse-' + fieldName + '-' + newIndex);
        $(this).attr("href", href);
      }
    }
  });
  obj.find('div[id*="collapse-' + fieldName + '"]').each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          id = $(this).attr("id"),
          label = $(this).attr("aria-labelledby");

      if (id) {
        id = id.replace('collapse-' + fieldName + '-' + currentIndex,
            'collapse-' + fieldName + '-' + newIndex);
        $(this).attr("id", id);
      }
      if (label) {
        label = label.replace('heading-' + fieldName + '-' + currentIndex,
            'heading-' + fieldName + '-' + newIndex);
        $(this).attr("aria-labelledby", label);
      }
    }
  });

  obj.find('div[class*="data-' + fieldName + '-"]').each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          classAttr = $(this).attr("class");

      if (classAttr) {
        classAttr = classAttr.replace('data-' + fieldName + '-' + currentIndex,
            'data-' + fieldName + '-' + newIndex);
        $(this).attr("class", classAttr);
      }
    }
  });

  obj.find('button[name*="-' + fieldName + '-"]').each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          name = $(this).attr("name");

      if (name) {
        name = name.replace('-' + fieldName + '-' + currentIndex,
            '-' + fieldName + '-' + newIndex);
        $(this).attr("name", name);
      }
    }
  });

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

  obj.find('input[name*="_' + objName + '"]').each(function () {
    let currentIndex = $(this).closest('.data-' + fieldName).data("index");
    if (currentIndex > index) {
      let newIndex = currentIndex - 1,
          name = $(this).attr("name");

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

providers.addEvent = function (fieldName, objName, objId) {
  let obj = $('.data-' + fieldName);
  for (let i = 0; i < obj.length; i++) {
    multiLang.add('Name', fieldName + '-' + i + '-name',
        objName + '[' + i + '].tspInformation.tspName.values',
        objId + i + '.tspInformation.tspName.values');

    multiLang.add('Trade name', fieldName + '-' + i + '-tradeName',
        objName + '[' + i + '].tspInformation.tspTradeName.values',
        objId + i + '.tspInformation.tspTradeName.values');

    multiLang.add('Electronic address', fieldName + '-' + i + '-electronicAddress',
        objName + '[' + i + '].tspInformation.tspAddress.electronicAddress.values',
        objId + i + '.tspInformation.tspAddress.electronicAddress.values');

    address.add('Postal address', fieldName + '-' + i + '-postalAddress',
        objName + '[' + i + '].tspInformation.tspAddress.postalAddresses.values',
        objId + i + '.tspInformation.tspAddress.postalAddresses.values');

    multiLang.add('Information URI', fieldName + '-' + i + '-informationUri',
        objName + '[' + i + '].tspInformation.tspInformationURI.values',
        objId + i + '.tspInformation.tspInformationURI.values');

    services.add('Service', fieldName + '-' + i + '-services',
        objName + '[' + i + '].tspServices.values',
        objId + i + '.tspServices.values');
  }
};
