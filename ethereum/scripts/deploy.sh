#!/bin/bash

if [ $# -eq 2 ]; then

JAVA_PATH=../java
SOLIDITY_PATH=../solidity
OUTPUTS_PATH=../../../outputs
CONTRACTS_PACKAGE=contracts

# absolute path of the current script
DIR="$( cd "$( dirname "$0" )" && pwd )"
# absolute path of $JAVA_PATH
JAVA_PATH="$(cd "$DIR" && cd "$JAVA_PATH" && pwd)"
# absolute path of $SOLIDITY_PATH
SOLIDITY_PATH="$(cd "$DIR" && cd "$SOLIDITY_PATH" && pwd)"
# absolute path of $OUTPUTS_PATH
OUTPUTS_PATH="$(cd "$DIR" && cd "$OUTPUTS_PATH" && pwd)"


echo "--------------------------------- BEGIN --------------------------------"
echo ""
echo ""
echo ""
echo "========================================================================="
echo "======================= Deploying smart-contracts ======================="
echo "========================================================================="
echo ""

# Compilation
cd "${DIR}"
cd ../../..
mvn clean
mvn compile
mvn package

# Execute
java -jar target/gtsl-ethereum.jar $1 $2

echo ""
echo ""
echo "contract.properties:"

cat "${OUTPUTS_PATH}"/contract.properties

echo ""
echo ""

echo "WARN: Please copy/paste the configuration file contract.properties from the outputs folder into the GTSL project as described in the documentation"

echo ""
echo ""
echo ""
echo "---------------------------------- END ---------------------------------"

else
    echo "usage: "$0" <quorum> <tlIdentifier>"
fi