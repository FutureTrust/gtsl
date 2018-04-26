#!/bin/bash

# Project
PROJECT_PATH=../

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

echo "mvn package..."
mvn clean package -Dmaven.test.skip=true

# Go back to script directory
cd "${DIR}"

echo ""
echo ""
echo ""
echo "--------------------------------- END --------------------------------"