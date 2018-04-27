#!/bin/bash

# Root directory
ROOT_DIR=../..

# absolute path of the current script
DIR="$( cd "$( dirname "$0" )" && pwd )"

# absolute path of $ROOT_DIR
ROOT_DIR="$(cd "$DIR" && cd "$ROOT_DIR" && pwd)"

java -jar "$ROOT_DIR"/nexu/nexu.jar