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

let digitalIdValue = {};

digitalIdValue.currentValue = [];

digitalIdValue.generateHtml = function (objName, objId) {
  return '<input class="form-control" '
      + 'id="' + objId + '" '
      + 'name="' + objName + '" '
      + 'value="">';
};

digitalIdValue.add = function (parentFieldName, parentObjName, parentObjId, index) {
  let fieldName = parentFieldName + '-' + index;
  let options = digitalIdValue.createOptions(parentObjName, parentObjId, index);

  saveAttributes(fieldName);

  let select = $('select[id="select-' + fieldName + '"]');
  let target = $('div[id="target-' + fieldName + '"]');

  digitalIdValue.currentValue[index] = select.val();

  select.change(function (e) {
    e.preventDefault();
    e.stopImmediatePropagation();


    console.log(fieldName, ':', digitalIdValue.currentValue[index], '->', $(this).val());

    let val = $(this).val();
    // save current content
    options[digitalIdValue.currentValue[index]].htmlText = target.html();
    // add new content
    target.html(digitalIdValue.getHtml(options[val]));
    // add new event
    //digitalIdValue.addEvent(options[val]);
    // set new current value
    digitalIdValue.currentValue[index] = val;

    saveAttributes(fieldName);
  });

};

digitalIdValue.getHtml = function (option) {
  if (option.htmlText) {
    return option.htmlText;
  } else {
    return digitalIdValue.generateHtml(option.objName, option.objId);
  }
};

digitalIdValue.createOptions = function (objName, objId, index) {
  return {
    cert: createOption('X509Certificate', 'cert', objName + '[' + index + '].certificateList[0].certEncoded', objId + index + '.certificateList0.certEncoded'),
    sn: createOption('X509SubjectName', 'sn', objName + '[' + index + '].subjectName', objId + index + '.subjectName'),
    ski: createOption('X509SKI', 'ski', objName + '[' + index + '].x509ski', objId + index + '.x509ski')
  };
};
