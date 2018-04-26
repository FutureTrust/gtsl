#!/bin/bash

# Root directory
ROOT_DIR=../..

# Solidity workspace
SOLIDITY_PATH="$ROOT_DIR"/solidity

# absolute path of the current script
DIR="$( cd "$( dirname "$0" )" && pwd )"
# absolute path of $SOLIDITY_PATH
SOLIDITY_PATH="$(cd "$DIR" && cd "$SOLIDITY_PATH"&& pwd)"

# Remove compiled Solidity sources
"${DIR}"/solidity/cleanAll.sh "${SOLIDITY_PATH}"

# Remove all containers and images from docker
"${DIR}"/docker/removeAll.sh