#!/bin/bash
set +x

if [ $# -eq 4 ]; then

  # Docker
  WEB3J_PATH=../docker/web3j

  # absolute path of the current script
  DIR="$( cd "$( dirname "$0" )" && pwd )"
  # absolute path of $WEB3J_PATH
  WEB3J_PATH="$(cd "$DIR" && cd "$WEB3J_PATH" && pwd)"

  WORKSPACE=$1
  JAVA_PATH=$2
  CONTRACTS_PACKAGE=$3
  CONTRACT=$4

  # Replace . with / to build the contracts path
  CONTRACTS_PATH="$( echo "${CONTRACTS_PACKAGE}" | sed 's/\./\//g' )"
  CONTRACTS_PATH="${JAVA_PATH}"/"${CONTRACTS_PATH}"

  echo ""
  echo "========================================================================="
  echo "Generating contract ${CONTRACT} from ${WORKSPACE}"

  bin="${CONTRACT}.bin"
  abi="${CONTRACT}.abi"

  echo ""
  echo "bin: ${bin}"
  echo "abi: ${abi}"
  echo ""

  echo ""
  echo "List compiled files:"
  ls -l "${WORKSPACE}"/outputs
  echo ""

  cd ${WEB3J_PATH}
  docker build -t web3j .
  docker run \
    -v "${WORKSPACE}"/outputs:/inputs \
    -v "${CONTRACTS_PATH}":/java/"$( echo "${CONTRACTS_PACKAGE}" | sed 's/\./\//g' )" \
    --name web3j \
    web3j \
    solidity generate \
    "/inputs/${bin}" "/inputs/${abi}" -o "/java" -p "${CONTRACTS_PACKAGE}"

  docker rm -f web3j

  echo "========================================================================="
  echo ""
else
    echo "usage: "$0" <workspace> <java_path> <java_package> <contract>, please provide the contract name"
fi
