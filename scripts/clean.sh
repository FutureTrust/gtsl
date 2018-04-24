#!/bin/bash

# absolute path of the current script
DIR="$( cd "$( dirname "$0" )" && pwd )"

# Remove all containers and images from docker
"${DIR}"/docker/removeAll.sh

# Remove the volume created for the database
docker volume rm mongodata