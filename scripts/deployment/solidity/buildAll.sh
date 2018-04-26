#!/bin/bash

echo ""
echo "========================================================================="
echo "======================= Building smart-contracts ========================"
echo "========================================================================="
echo ""


if [ $# -eq 3 ]; then
  WORKSPACE=$1
  JAVA_PATH=$2
  CONTRACTS_PACKAGE=$3

  # absolute path of the current script
  DIR="$( cd "$( dirname "$0" )" && pwd )"

  for sol_path in "${WORKSPACE}"/contracts/*.sol; do
    sol=${sol_path##*/}
echo "sol: ${sol}"
    "${DIR}"/solc.sh "${WORKSPACE}" "${sol}"

    contract=${sol_path%.sol}
    contract=${contract##*/}
echo "contract: ${contract}" 
    "${DIR}"/generate.sh "${WORKSPACE}" "${JAVA_PATH}" "${CONTRACTS_PACKAGE}" "${contract}"
  done

  # Replace . with / to build the contracts path
  CONTRACTS_PATH="$( echo "${CONTRACTS_PACKAGE}" | sed 's/\./\//g' )"
  CONTRACTS_PATH="${JAVA_PATH}"/"${CONTRACTS_PATH}"

  # Fix the errors of additional parameter in constructor
  for java_file in "${CONTRACTS_PATH}"/*.java; do
    #sed -i -e 's/super(BINARY, /super(/g' "${java_file}"
    #sed -i -e 's/@Override//g' "${java_file}"
    sed -i -e 's+package+/*\n * Copyright (c) 2017 European Commission.\n *\n * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European\n * Commission - subsequent versions of the EUPL (the \"Licence\").\n * You may not use this work except in compliance with the Licence.\n * You may obtain a copy of the Licence at:\n *\n * https://joinup.ec.europa.eu/software/page/eupl5\n *\n * Unless required by applicable law or agreed to in writing, software distributed under the Licence\n * is distributed on an \"AS IS\" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express\n * or implied.\n * See the Licence for the specific language governing permissions and limitations under the\n * Licence.\n */\n\npackage+g' "${java_file}"
  done

  echo ""
  echo "Files generated to ${CONTRACTS_PATH}"
  ls -l "${CONTRACTS_PATH}"
  echo ""

else
    echo "usage: "$0" <workspace> <java_path> <java_package>"
    echo ""
    echo "Please provide the path of the workspace."
    echo "workspace structure"
    echo "- contract : folder containing solidity sources"
    echo "- outputs : targeted folder"
    echo "Please provide the path of java sources."
    echo "Please provide the path of java package where .java files will be generated."
fi
