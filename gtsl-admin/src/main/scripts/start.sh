#!/bin/bash

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
echo "============================= Running app =============================="
echo "========================================================================"
echo ""

JAR="$( find "${PROJECT_PATH}"/target -name "*.jar" | head -n 1 )"
RUN_CMD="java -jar ${JAR}"
echo ${RUN_CMD}
${RUN_CMD}

echo ""
echo ""
echo ""
echo "--------------------------------- END --------------------------------"