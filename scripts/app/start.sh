#!/bin/bash
set -e

usage()
{
    echo "usage: $0 <admin|web>"
}

if [ $# -ne 1 ]; then
  usage
  exit 1
fi

# Root directory
ROOT_DIR=../..

if [ "$1" = "admin" ]
then
  # Admin module
  APP="$ROOT_DIR"/gtsl-admin
elif [ "$1" = "web" ]
then
  # Web module
  APP="$ROOT_DIR"/gtsl-web
else
  usage
  exit 1
fi

# absolute path of the current script
DIR="$( cd "$( dirname "$0" )" && pwd )"

# absolute path of $APP
APP="$(cd "$DIR" && cd "$APP" && pwd)"

echo "--------------------------------- BEGIN --------------------------------"
echo ""
echo ""

echo ""
echo "========================================================================"
echo "============================= Running app =============================="
echo "========================================================================"
echo ""

JAR="$( find "${APP}"/target -name "*.jar" | head -n 1 )"
RUN_CMD="java -jar ${JAR}"
echo ${RUN_CMD}
${RUN_CMD}

echo ""
echo ""
echo ""
echo "--------------------------------- END --------------------------------"