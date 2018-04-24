#!/bin/bash

JAVA_PATH=../java
SOLIDITY_PATH=../solidity
CONTRACTS_PACKAGE=eu.futuretrust.gtsl.ethereum.contracts

# absolute path of the current script
DIR="$( cd "$( dirname "$0" )" && pwd )"
# absolute path of $JAVA_PATH
JAVA_PATH="$(cd "$DIR" && cd "$JAVA_PATH" && pwd)"
# absolute path of $SOLIDITY_PATH
SOLIDITY_PATH="$(cd "$DIR" && cd "$SOLIDITY_PATH"&& pwd)"

echo "--------------------------------- BEGIN --------------------------------"
echo ""
echo ""
echo ""

# Remove compiled Solidity sources
"${DIR}"/solidity/cleanAll.sh "${SOLIDITY_PATH}"

# Build all solidity sources in $SOLIDITY_PATH
"${DIR}"/solidity/buildAll.sh "${SOLIDITY_PATH}" "${JAVA_PATH}" "${CONTRACTS_PACKAGE}"


echo ""
echo ""
echo ""
echo "---------------------------------- END ---------------------------------"
