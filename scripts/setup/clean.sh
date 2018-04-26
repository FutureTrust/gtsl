#!/bin/bash

# Docker Scripts
DOCKER_SCRIPTS_PATH=./docker_scripts

# absolute path of the current script
DIR="$( cd "$( dirname "$0" )" && pwd )"

# absolute path of DOCKER_SCRIPTS_PATH
DOCKER_SCRIPTS_PATH="$(cd "$DIR" && cd "$DOCKER_SCRIPTS_PATH" && pwd)"

# Remove all containers and images from docker
"${DOCKER_SCRIPTS_PATH}"/removeAll.sh

# Remove the volume created for the database
docker volume rm mongodata