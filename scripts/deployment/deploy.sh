#!/bin/bash
set -e

if [ $# -eq 2 ]; then



# Root directory
ROOT_DIR=../..

# Contracts module
CONTRACTS_PATH="$ROOT_DIR"/gtsl-contracts

# Outputs
OUTPUTS_PATH="$CONTRACTS_PATH"/outputs

# Properties
PROPERTIES_PATH="$ROOT_DIR"/properties

# Keystore
KEYSTORE_PATH="$ROOT_DIR"/keystore

# Resources
CONTRACTS_RESOURCES="$ROOT_DIR"/gtsl-contracts/src/main/resources
ADMIN_RESOURCES="$ROOT_DIR"/gtsl-admin/src/main/resources
WEB_RESOURCES="$ROOT_DIR"/gtsl-web/src/main/resources



# absolute path of the current script
DIR="$( cd "$( dirname "$0" )" && pwd )"

# absolute path of $CONTRACTS_PATH
CONTRACTS_PATH="$(cd "$DIR" && cd "$CONTRACTS_PATH" && pwd)"

# absolute path of $OUTPUTS_PATH
OUTPUTS_PATH="$(cd "$DIR" && cd "$OUTPUTS_PATH" && pwd)"

# absolute path of $PROPERTIES_PATH
PROPERTIES_PATH="$(cd "$DIR" && cd "$PROPERTIES_PATH" && pwd)"

# absolute path of $KEYSTORE_PATH
KEYSTORE_PATH="$(cd "$DIR" && cd "$KEYSTORE_PATH" && pwd)"

# absolute path of $CONTRACTS_RESOURCES
CONTRACTS_RESOURCES="$(cd "$DIR" && cd "$CONTRACTS_RESOURCES" && pwd)"

# absolute path of $ADMIN_RESOURCES
ADMIN_RESOURCES="$(cd "$DIR" && cd "$ADMIN_RESOURCES" && pwd)"

# absolute path of $WEB_RESOURCES
WEB_RESOURCES="$(cd "$DIR" && cd "$WEB_RESOURCES" && pwd)"



echo "--------------------------------- BEGIN --------------------------------"
echo ""
echo ""
echo ""
echo "========================================================================="
echo "======================= Deploying smart-contracts ======================="
echo "========================================================================="
echo ""

# Copy keystore into contracts module resources
rm -rf "${CONTRACTS_RESOURCES}"/keystore
cp -a "${KEYSTORE_PATH}" "${CONTRACTS_RESOURCES}"/keystore

# Copy ethereum properties into contracts module resources
rm -rf "${CONTRACTS_RESOURCES}"/ethereum.properties
cp "${PROPERTIES_PATH}"/ethereum.properties "${CONTRACTS_RESOURCES}"/ethereum.properties

# Clean outputs
mkdir -p "${OUTPUTS_PATH}"
rm -rf "${OUTPUTS_PATH}"/contract.properties

# Compilation
cd "${CONTRACTS_PATH}"
mvn clean package -Dmaven.test.skip=true

# Execute

JAR="$( find "${CONTRACTS_PATH}"/target -name "gtsl-*-jar-with-dependencies.jar" | head -n 1 )"
RUN_CMD="java -jar ${JAR} $1 $2"
echo ${RUN_CMD}
${RUN_CMD}

echo ""
echo ""
echo "contract.properties:"

cat "${OUTPUTS_PATH}"/contract.properties

cp "${OUTPUTS_PATH}"/contract.properties "${PROPERTIES_PATH}"/contract.properties
cp "${OUTPUTS_PATH}"/contract.properties "${ADMIN_RESOURCES}"/properties/contract.properties
cp "${OUTPUTS_PATH}"/contract.properties "${WEB_RESOURCES}"/properties/contract.properties


echo ""
echo ""
echo ""
echo "---------------------------------- END ---------------------------------"

else
    echo "usage: "$0" <quorum> <tlIdentifier>"
fi