#!/bin/bash

if [ $# -eq 2 ]; then

  # Project
  PROJECT_PATH=../../../

  # absolute path of the current script
  DIR="$( cd "$( dirname "$0" )" && pwd )"
  # absolute path of $PROJECT_PATH
  PROJECT_PATH="$(cd "$DIR" && cd "$PROJECT_PATH" && pwd)"

  echo "--------------------------------- BEGIN --------------------------------"
  echo ""
  echo ""

  echo ""
  echo "========================================================================"
  echo "============================= Building app ============================="
  echo "========================================================================"
  echo ""

  # Go to project root in order to build the project
  cd "${PROJECT_PATH}"

  mvn package -Dmaven.test.skip=true -Pdocker

  # Go back to script directory
  cd "${DIR}"

  echo ""
  echo "========================================================================"
  echo "============================= Running app =============================="
  echo "========================================================================"
  echo ""

  RUN_CMD="docker run --restart=always -d -p $2:8081 --name $1 gtsl/gtsl-admin"
  echo ${RUN_CMD}
  ${RUN_CMD}

  echo ""
  echo ""
  echo ""
  echo "--------------------------------- END --------------------------------"

else
  echo "usage: "$0" <container_name> <host_port>"
fi