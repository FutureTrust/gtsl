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

let criteria = {};

criteria.generateHtml = function (label, fieldName, objName, objId) {
  return '  <div>'
      + ''
      + '  <div>'
      + '  <label>Asserts</label>'
      + '  <input class="form-control" '
      + 'id="' + objId + '.asserts" '
      + 'name="' + objName + '.asserts" '
      + 'value="" '
      + 'type="text">'
      + '  <hr>'
      + '</div>'
      + ''
      + '  <div>'
      + '  <label>Description</label>'
      + '  <input class="form-control" '
      + 'id="' + objId + '.description" '
      + 'name="' + objName + '.description" '
      + 'value="" '
      + 'type="text">'
      + '  <hr>'
      + '</div>'
      + ''
      + '<!-- Key usage list -->'
      + '  <div id="heading-' + fieldName + '-keyUsage">'
      + '    <h5 class="title">'
      + '      <a class="panel-heading a-link-custom collapsed" '
      + 'data-toggle="collapse" '
      + 'href="#collapse-' + fieldName + '-keyUsage" '
      + 'data-parent="#parent-' + fieldName
      + '-keyUsage" aria-expanded="false">'
      + '        <span>Key usage list</span>'
      + '        <i class="fa fa-angle-down fa-lg hack-icon"></i>'
      + '      </a>'
      + '    </h5>'
      + '  </div>'
      + '  <div id="collapse-' + fieldName + '-keyUsage" '
      + 'class="panel-collapse collapse" '
      + 'aria-labelledby="heading-' + fieldName + '-keyUsage">'
      + '    <div class="panel-body">'
      + '      <div>'
      + '  <div class="form-group accordion" '
      + 'id="parent-' + fieldName + '-keyUsage">'
      + '    '
      + ''
      + '    <button type="button" class="btn btn-primary btn-xs" '
      + 'name="add-' + fieldName + '-keyUsage">'
      + '      <span class="glyphicon glyphicon-plus"></span>'
      + '      <span> Key usage</span>'
      + '    </button>'
      + '  </div>'
      + ''
      + '  <hr>'
      + '</div>'
      + '    </div>'
      + '  </div>'
      + ''
      + '<!-- Policies list -->'
      + '  <div id="heading-' + fieldName + '-policyList">'
      + '    <h5 class="title">'
      + '      <a class="panel-heading a-link-custom collapsed" '
      + 'data-toggle="collapse" '
      + 'href="#collapse-' + fieldName + '-policyList" '
      + 'data-parent="#parent-' + fieldName
      + '-policyList" aria-expanded="false">'
      + '        <span>Policies list</span>'
      + '        <i class="fa fa-angle-down fa-lg hack-icon"></i>'
      + '      </a>'
      + '    </h5>'
      + '  </div>'
      + '  <div id="collapse-' + fieldName + '-policyList" '
      + 'class="panel-collapse collapse" '
      + 'aria-labelledby="heading-' + fieldName + '-policyList">'
      + '    <div class="panel-body">'
      + '      <div>'
      + '  <div class="form-group accordion" '
      + 'id="parent-' + fieldName + '-policyList">'
      + '    '
      + ''
      + '    <button type="button" class="btn btn-primary btn-xs" '
      + 'name="add-' + fieldName + '-policyList">'
      + '      <span class="glyphicon glyphicon-plus"></span>'
      + '      <span> Policy</span>'
      + '    </button>'
      + '  </div>'
      + ''
      + '  <hr>'
      + '</div>'
      + '    </div>'
      + '  </div>'
      + ''
      + '<!-- Criteria list -->'
      + '  <div id="heading-' + fieldName + '-criteriaList">'
      + '    <h5 class="title">'
      + '      <a class="panel-heading a-link-custom collapsed" '
      + 'data-toggle="collapse" '
      + 'href="#collapse-' + fieldName + '-criteriaList" '
      + 'data-parent="#parent-' + fieldName
      + '-criteriaList" aria-expanded="false">'
      + '        <span>Criteria list</span>'
      + '        <i class="fa fa-angle-down fa-lg hack-icon"></i>'
      + '      </a>'
      + '    </h5>'
      + '  </div>'
      + '  <div id="collapse-' + fieldName + '-criteriaList" '
      + 'class="panel-collapse collapse" '
      + 'aria-labelledby="heading-' + fieldName + '-criteriaList">'
      + '    <div class="panel-body">'
      + '      <div>'
      + '  <div class="form-group accordion" '
      + 'id="parent-' + fieldName + '-criteriaList">'
      + '    '
      + ''
      + '    <button type="button" class="btn btn-primary btn-xs" '
      + 'name="add-' + fieldName + '-criteriaList">'
      + '      <span class="glyphicon glyphicon-plus"></span>'
      + '      <span> Criteria</span>'
      + '    </button>'
      + '  </div>'
      + ''
      + '  <hr>'
      + '</div>'
      + '    </div>'
      + '  </div>'
      + ''
      + '<!-- Other criteria -->'
      + '  <div id="heading-' + fieldName + '-otherCriteria">'
      + '    <h5 class="title">'
      + '      <a class="panel-heading a-link-custom collapsed" '
      + 'data-toggle="collapse" '
      + 'href="#collapse-' + fieldName + '-otherCriteria" '
      + 'data-parent="#parent-' + fieldName + '-otherCriteria">'
      + '        <span>Other criteria</span>'
      + '        <i class="fa fa-angle-down fa-lg hack-icon"></i>'
      + '      </a>'
      + '    </h5>'
      + '  </div>'
      + '  <div id="collapse-' + fieldName + '-otherCriteria" '
      + 'class="panel-collapse collapse" '
      + 'aria-labelledby="heading-' + fieldName + '-otherCriteria">'
      + '    <div class="panel-body">'
      + '      <div>'
      + ''
      + '<!-- Extended key usage list -->'
      + '  <div id="heading-' + fieldName
      + '-otherCriteria-extendedKeyUsageList">'
      + '    <h6 class="title">'
      + '      <a class="panel-heading a-link-custom collapsed" '
      + 'data-toggle="collapse" '
      + 'href="#collapse-' + fieldName + '-otherCriteria-extendedKeyUsageList" '
      + 'data-parent="#parent-' + fieldName
      + '-otherCriteria-extendedKeyUsageList">'
      + '        <span>Extended key usage list</span>'
      + '        <i class="fa fa-angle-down fa-lg hack-icon"></i>'
      + '      </a>'
      + '    </h6>'
      + '  </div>'
      + '  <div id="collapse-' + fieldName
      + '-otherCriteria-extendedKeyUsageList" '
      + 'class="panel-collapse collapse" '
      + 'aria-labelledby="heading-' + fieldName
      + '-otherCriteria-extendedKeyUsageList">'
      + '    <div class="panel-body">'
      + ''
      + '      <div>'
      + '  <div class="form-group accordion" '
      + 'id="parent-' + fieldName + '-otherCriteria-extendedKeyUsageList">'
      + '    '
      + ''
      + '    <button type="button" class="btn btn-primary btn-xs" '
      + 'name="add-' + fieldName + '-otherCriteria-extendedKeyUsageList">'
      + '      <span class="glyphicon glyphicon-plus"></span>'
      + '      <span> Extended key usage</span>'
      + '    </button>'
      + '  </div>'
      + ''
      + '  <hr>'
      + '</div>'
      + ''
      + '    </div>'
      + '  </div>'
      + ''
      + '<!-- Cert DNa list -->'
      + '  <div id="heading-' + fieldName + '-otherCriteria-certDnaList">'
      + '    <h6 class="title">'
      + '      <a class="panel-heading a-link-custom collapsed" '
      + 'data-toggle="collapse" '
      + 'href="#collapse-' + fieldName + '-otherCriteria-certDnaList" '
      + 'data-parent="#parent-' + fieldName + '-otherCriteria-certDnaList">'
      + '        <span>CertDNA list</span>'
      + '        <i class="fa fa-angle-down fa-lg hack-icon"></i>'
      + '      </a>'
      + '    </h6>'
      + '  </div>'
      + '  <div id="collapse-' + fieldName + '-otherCriteria-certDnaList" '
      + 'class="panel-collapse collapse" '
      + 'aria-labelledby="heading-' + fieldName + '-otherCriteria-certDnaList">'
      + '    <div class="panel-body">'
      + ''
      + '      <div>'
      + '  <div class="form-group accordion" '
      + 'id="parent-' + fieldName + '-otherCriteria-certDnaList">'
      + '    '
      + ''
      + '    <button type="button" class="btn btn-primary btn-xs" '
      + 'name="add-' + fieldName + '-otherCriteria-certDnaList">'
      + '      <span class="glyphicon glyphicon-plus"></span>'
      + '      <span> Cert Subject DN Attribute</span>'
      + '    </button>'
      + '  </div>'
      + ''
      + '  <hr>'
      + '</div>'
      + ''
      + '    </div>'
      + '  </div>'
      + ''
      + '</div>'
      + '    </div>'
      + '  </div>'
      + ''
      + '</div>';
};

criteria.add = function (label, fieldName, objName, objId) {
  criteria.addEvent(fieldName, objName, objId);
};

criteria.addEvent = function (fieldName, objName, objId) {
  keyUsage.add('Key usage', fieldName + '-keyUsage',
      objName + '.keyUsage',
      objId + '.keyUsage');
  policy.add('Policy', fieldName + '-policyList',
      objName + '.policyList',
      objId + '.policyList');
  policyBit.add('Extended key usage', fieldName
      + '-otherCriteria-extendedKeyUsageList',
      objName + '.otherList.extendedKeyUsageList',
      objId + '.otherList.extendedKeyUsageList');
  policyBit.add('Cert Subject DN Attribute', fieldName
      + '-otherCriteria-certDnaList',
      objName + '.otherList.certDnaList',
      objId + '.otherList.certDnaList');
  criteriaList.add('Criteria', fieldName + '-criteriaList',
      objName + '.criteriaList',
      objId + '.criteriaList');
};
