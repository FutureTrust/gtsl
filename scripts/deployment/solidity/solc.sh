#!/bin/bash

if [ $# -eq 2 ]; then
  WORKSPACE=$1
  SRC_FILE=$2

  echo ""
  echo "========================================================================="
  echo "Compiling contract ${SRC_FILE} from ${WORKSPACE}"

  docker run \
    -v "${WORKSPACE}"/contracts:/opt \
    -v "${WORKSPACE}"/outputs:/tmp/outputs \
    --name solc \
    ethereum/solc:0.4.21 \
    --bin --abi --optimize --overwrite -o /tmp/outputs \
    /opt/"${SRC_FILE}"

  docker rm -f solc

  echo "========================================================================="
  echo ""
else
    echo "usage: "$0" <workspace> <contract.sol>"
fi
