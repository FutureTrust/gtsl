#!/bin/bash



# Root directory
ROOT_DIR=../..

# Root directory
ROOT_DIR=../..

# Solidity workspace
SOLIDITY_PATH="$ROOT_DIR"/solidity

# Contracts module
CONTRACTS_PATH="$ROOT_DIR"/gtsl-contracts

# Java sources path
JAVA_PATH="$CONTRACTS_PATH"/src/main/java

# Java package for the contracts
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
