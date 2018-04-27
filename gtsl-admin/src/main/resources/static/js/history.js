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

let history = {};

history.generateHtml = function (label, fieldName, objName, objId, index) {
  return '<div data-index="' + index + '" class="data-' + fieldName + '">'
      + '<div class="panel panel-default">'
      + ''
      + '<div id="heading-' + fieldName + '-' + index + '">'
      + '<h6 class="title">'
      + '<a class="panel-heading a-link-custom collapsed" '
      + 'data-toggle="collapse" '
      + 'href="#collapse-' + fieldName + '-' + index + '" '
      + 'data-parent="#parent-' + fieldName + '"> '
      + '<span>' + label + '</span>'
      + '<i class="fa fa-angle-down fa-lg hack-icon"></i>'
      + '</a>'
      + '</h6>'
      + '</div>'
      + ''
      + '<div id="collapse-' + fieldName + '-' + index + '" '
      + 'class="panel-collapse collapse" '
      + 'aria-labelledby="heading-' + fieldName + '-' + index + '">'
      + '<div class="panel-body">'
      + ''
      + '<label>Service type identifier</label>'
      + '<input class="form-control" id="' + objId + index
      + '.serviceTypeIdentifier.value" name="' + objName + '[' + index
      + '].serviceTypeIdentifier.value" value=""/>'
      + '<hr/>'
      + ''
      + '<label>Service status</label>'
      + '<input class="form-control" id="' + objId + index
      + '.serviceStatus.value" name="' + objName + '[' + index
      + '].serviceStatus.value" value=""/>'
      + '<hr/>'
      + ''
      + '<label>Starting date & time (dd/MM/yyyy HH:mm)</label>'
      + '<input class="form-control" id="' + objId + index
      + '.statusStartingTime" name="' + objName + '[' + index
      + '].statusStartingTime" value="" type="text" placeholder="dd/MM/yyyy HH:mm"/>'
      + '<hr/>'
      + ''
      + '<h4>Service name</h4>'
      + '<button type="button" class="btn btn-primary btn-xs" name="add-'
      + fieldName + '-' + index + '-serviceName">'
      + '<span class="glyphicon glyphicon-plus"></span>'
      + '<span>&ensp;' + 'Service name' + '</span>'
      + '</button>'
      + '<hr/>'
      + ''
      + '<label>Digital Identity</label>'
      + '<br/>'
      + '<button type="button" class="btn btn-primary btn-xs" name="add-'
      + fieldName + '-' + index + '-digitalId">'
      + '<span class="glyphicon glyphicon-plus"></span>'
      + '<span>&ensp;' + 'Digital Identity' + '</span>'
      + '</button>'
      + '<hr/>'
      + ''
      + ''
      + '<div id="heading-' + fieldName + '-' + index + '-extensions">'
      + '<h5 class="title">'
      + '<a class="panel-heading a-link-custom collapsed" data-toggle="collapse" '
      + 'data-parent="#parent-' + fieldName + '-' + index + '-extensions" '
      + 'href="#collapse-' + fieldName + '-' + index + '-extensions">'
      + '<span>Service extensions</span>'
      + '<i class="fa fa-angle-down fa-lg hack-icon"></i>'
      + '</a>'
      + '</h5>'
      + '</div>'
      + ''
      + '<div id="collapse-' + fieldName + '-' + index + '-extensions" '
      + 'class="panel-collapse collapse" '
      + 'aria-labelledby="heading-' + fieldName + '-' + index + '-extensions">'
      + '<div class="panel-body">'
      + '</div>'
      + ''
      + '<button type="button" class="btn btn-primary btn-xs" name="add-'
      + fieldName + '-' + index + '-extensions">'
      + '<span class="glyphicon glyphicon-plus"></span>'
      + '<span>&ensp;' + 'Service extension' + '</span>'
      + '</button>'
      + ''
      + '</div>'
      + ''
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
      + '</div>'
      + ''
      + '</div>'
      + '</div>';
};

history.add = function (label, fieldName, objName, objId) {

  //saveAttributes(fieldName);

  let buttonAdd = $('button[name=add-' + fieldName + ']');
  buttonAdd.off('click');
  buttonAdd.click(function (e) {
    e.preventDefault();
    e.stopImmediatePropagation();

    let me = $(this),
        obj = $('.data-' + fieldName),
        index = obj.length,
        html = history.generateHtml(label, fieldName, objName, objId, index);

    me.before(html);
    history.add(label, fieldName, objName, objId);
    history.addEvent(fieldName, objName, objId);
  });

  let buttonRemove = $('button[name=remove-' + fieldName + ']');
  buttonRemove.off('click');
  buttonRemove.click(function (e) {
    e.preventDefault();
    e.stopImmediatePropagation();

    let me = $(this),
        parent = me.closest('.data-' + fieldName),
        index = parent.data('index');

    history.reorderIndexes(fieldName, objName, objId, index);
    parent.remove();
    history.addEvent(fieldName, objName, objId);
  });

  history.addEvent(fieldName, objName, objId);
};

history.reorderIndexes = function (fieldName, objName, objId, index) {

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

history.addEvent = function (fieldName, objName, objId) {
  let obj = $('.data-' + fieldName);
  for (let i = 0; i < obj.length; i++) {
    multiLang.add('Service name', fieldName + '-' + i + '-serviceName',
        objName + '[' + i + '].serviceName.values',
        objId + i + '.serviceName.values');
    digitalId.add('Digital identity', fieldName + '-' + i + '-digitalId',
        objName + '[' + i + '].serviceDigitalIdentity.values',
        objId + i + '.serviceDigitalIdentity.values');
    serviceExtensions.add('Service extension', fieldName + '-' + i + '-extensions',
        objName + '[' + i + '].serviceInformationExtensions.values',
        objId + i + '.serviceInformationExtensions.values');
  }
};
