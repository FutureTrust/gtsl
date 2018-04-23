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

let policyOrLegalNotice = {};

policyOrLegalNotice.currentValue = "legalNotice";

policyOrLegalNotice.generateHtml = function (label, fieldName, objName, objId) {
  return '<div class="form-group">'
      + '<div data-index="0" class="data-' + fieldName + '">'
      + '<div class="row">'
      + ''
      + '<span class="col-xs-3 col-sm-2 col-md-2 col-lg-2">'
      + '<select class="form-control" id="' + objId + '0.lang.value" '
      + 'name="' + objName + '[0].lang.value">'
      + getLanguagesAsHtml(country.lang)
      + '</select>'
      + '</span>'
      + ''
      + '<span class="col-xs-6 col-sm-8 col-md-8 col-lg-8">'
      + '<input class="form-control"  id="' + objId + '0.value" '
      + 'name="' + objName + '[0].value" value="">'
      + '</span>'
      + ''
      + '<span class="col-xs-3 col-sm-2 col-md-2 col-lg-2">'
      + '<button type="button" class="btn btn-default btn-xs" '
      + 'name="remove-' + fieldName + '">'
      + '<span class="glyphicon glyphicon-minus"></span>'
      + '<span>&ensp;' + label + '</span>'
      + '</button>'
      + '</span>'
      + ''
      + '</div>'
      + '</div>'
      + ''
      + '<button type="button" class="btn btn-primary btn-xs"'
      + 'name="add-' + fieldName + '">'
      + '<span class="glyphicon glyphicon-plus"></span>'
      + '<span>&ensp;' + label + '</span>'
      + '</button>'
      + ''
      + '</div>'
      + '<hr/>';
};

policyOrLegalNotice.add = function (fieldName, parentObjName, parentObjId) {

  let options = policyOrLegalNotice.createOptions(parentObjName, parentObjId);

  let select = $('select[id="select-' + fieldName + '"]');
  let target = $('div[id="target-' + fieldName + '"]');

  policyOrLegalNotice.currentValue = select.val();

  $.each(options, function (index, option) {
    policyOrLegalNotice.addEvent(option);
  });

  select.change(function (e) {
    e.preventDefault();
    e.stopImmediatePropagation();

    let val = $(this).val();
    // save current content
    options[policyOrLegalNotice.currentValue].htmlText = target.html();
    // add new content
    target.html(policyOrLegalNotice.getHtml(options[val]));
    // add new event
    policyOrLegalNotice.addEvent(options[val]);
    // set new current value
    policyOrLegalNotice.currentValue = val;
  });
};

policyOrLegalNotice.getHtml = function (option) {
  if (option.htmlText) {
    return option.htmlText;
  } else {
    return policyOrLegalNotice.generateHtml(option.label, option.fieldName,
        option.objName, option.objId);
  }
};

policyOrLegalNotice.addEvent = function (option) {
  option.addFunction(option.label, option.fieldName, option.objName, option.objId);
};

policyOrLegalNotice.createOptions = function (parentObjName, parentObjId) {
  return {
    legalNotice: createOption('Legal notice', 'legalNotice', parentObjName + '.legalNotice.values', parentObjId + '.legalNotice.values', multiLang.add),
    policy: createOption('Policy', 'policy', parentObjName + '.policy.values', parentObjId + '.policy.values', multiLang.add)
  }
};