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

let sign = function (draftId) {

  getNexuConfiguration(function(err, config) {
    if (err) showSignatureMessage("Could not retrieve Nexu configuration from server.");
    else {
      getCertificateChain(config.nexuScheme, config.nexuPort, function(err, certData) {
        if (err) showSignatureMessage("Could not retrieve certificate chain and signing"
            + " certificate from Nexu");
        else {
          getToBeSigned(draftId, certData.response, function (err, signInfo) {
            if (err) showSignatureMessage("Could not obtain value to sign from server.");
            else {
              nexuSign(signInfo, function (err, sigResult) {
                if (err) showSignatureMessage("An error occurred while attempting to sign"
                    + " with Nexu.");
                else {
                  uploadSignatureValue(sigResult.response, draftId, signInfo.signingDate, function(err) {
                    if (err) showSignatureMessage("An error occurred while uploading the"
                        + " signature value to the server. Please try again.");
                    else {
                      showSignatureMessage("The TSL was successfully signed. It will"
                          + " now be pushed to the gTSL.", "Success!");
                    }
                  });
                }
              });
            }
          });
        }
      });
    }
  });
};

let getNexuConfiguration = function (cb) {

  let url = window.location.origin + '/api/sign/config';

  $.ajax({
    url: url,
    type: 'GET',
    success: function (data) {
      cb(null, data);
    },
    error: function (err) {
      cb(err, null);
    }
  });
};

let getCertificateChain = function (nexuScheme, nexuPort, cb) {

  let url = nexuScheme + '://localhost:' + nexuPort + '/rest/certificates';

  $.ajax({
    url: url,
    type: 'GET',
    success: function (data) {
      cb(null, data);
    },
    error: function (error) {
      cb(error, null);
    }
  });
}

let getToBeSigned = function (draftId, certInfo, cb) {

  let url = window.location.origin + "/api/sign",
      sigData = new FormData();

  sigData.append('draftId', draftId);
  sigData.append('keyId', certInfo.keyId);
  sigData.append('tokenId', certInfo.tokenId.id);
  sigData.append('signingCert', certInfo.certificate);
  sigData.append('certChain', certInfo.certificateChain);

  $.ajax({
    type: 'POST',
    url: url,
    contentType: false,
    cache: false,
    data: sigData,
    processData: false,
    success: function (signInfo) {
      cb (null, signInfo);
    },
    error: function (err) {
      cb (err, null);
    }
  });
};

let nexuSign = function (signInfo, cb) {

    let url = signInfo.nexuScheme + '://localhost:' + signInfo.nexuPort + '/rest/sign';

    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(signInfo.signatureRequest),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            cb(null, data);
        },
        error: function (err) {
          cb(err, null);
        }
    });
};

let uploadSignatureValue = function (signatureInfo, draftId, signingDate, cb) {

  let url = window.location.origin + '/api/sign/' + draftId;

  signatureInfo.signingDate = signingDate;

  $.ajax({
    url: url,
    type: 'PUT',
    data: JSON.stringify(signatureInfo),
    contentType: 'application/json; charset=utf-8',
    success: function () {
      cb(null);
    },
    error: function (err) {
      cb(err);
    }
  });
};

let showSignatureMessage = function(msg, title) {
  title = title || "An error occurred.";
  $('#sign-error-title').text(title);
  $('#sign-error-message').text(msg);
  $('#modalSignMessage').modal('show');
};
