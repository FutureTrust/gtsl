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

$(function () {
  $('[data-toggle="tooltip"]').tooltip()
});

let saveAttributes = function (fieldName) {
  $('.data-' + fieldName + ' select').change(function () {
    $(this).find("option").removeAttr("selected");
    $(this).find("option:selected").attr("selected", "selected");
  });

  $('.data-' + fieldName + ' input').change(function () {
    $(this).attr("value", $(this).val());
  });
};

let getLanguagesAsHtml = function (languages) {
  let text = '';
  for (let i = 0; i < languages.length; i++) {
    if (languages[i] === "en") {
      text += "<option value=\"" + languages[i] + "\" selected=\"selected\">"
          + languages[i]
          + "</option>";
    } else {
      text += "<option value=\"" + languages[i] + "\">" + languages[i]
          + "</option>";
    }
  }
  return text;
};

let getMimeTypesAsHtml = function (mimeTypes) {
  let text = '';
  for (let i = 0; i < mimeTypes.length; i++) {
    text += "<option value=\"" + mimeTypes[i] + "\">" + mimeTypes[i]
        + "</option>";
  }
  return text;
};

let createOption = function (labelValue, fieldNameValue, objNameValue,
    objIdValue, addFunctionValue) {
  return {
    label: labelValue,
    fieldName: fieldNameValue,
    objName: objNameValue,
    objId: objIdValue,
    addFunction: addFunctionValue
  };
};