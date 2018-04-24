#!/bin/bash

# Project
PROJECT_PATH=../../../

# Docker
DOCKER_PATH=../docker

# Resources
OUTPUTS_PATH=../../../outputs

# Templates
TEMPLATES_PATH=../templates

# Keystore
KEYSTORE_FOLDER=keystore

# Configuration file
CFG_IPFS=ipfs.properties
CFG_ETHEREUM=ethereum.properties
CFG_MONGO=mongo.properties
CFG_MAIL=mail.properties

# Containers
ETHEREUM_CONTAINER=ethereum-node
IPFS_CONTAINER=ipfs-node

# absolute path of the current script
DIR="$( cd "$( dirname "$0" )" && pwd )"
# absolute path of $PROJECT_PATH
PROJECT_PATH="$(cd "$DIR" && cd "$PROJECT_PATH" && pwd)"
# absolute path of $DOCKER_PATH
DOCKER_PATH="$(cd "$DIR" && cd "$DOCKER_PATH" && pwd)"
# absolute path of $RESOURCES__PATH
OUTPUTS_PATH="$(cd "$DIR" && cd "$OUTPUTS_PATH" && pwd)"
# absolute path of $TEMPLATES_PATH
TEMPLATES_PATH="$(cd "$DIR" && cd "$TEMPLATES_PATH" && pwd)"

# absolute path of $KEYSTORE_PATH
KEYSTORE_PATH="${OUTPUTS_PATH}/${KEYSTORE_FOLDER}"

# absolute path of templates
TEMPLATE_IPFS="${TEMPLATES_PATH}/${CFG_IPFS}"
TEMPLATE_ETHEREUM="${TEMPLATES_PATH}/${CFG_ETHEREUM}"
TEMPLATE_MONGO="${TEMPLATES_PATH}/${CFG_MONGO}"
TEMPLATE_MAIL="${TEMPLATES_PATH}/${CFG_MAIL}"

# absolute path of configuration files
CFG_IPFS="${OUTPUTS_PATH}/${CFG_IPFS}"
CFG_ETHEREUM="${OUTPUTS_PATH}/${CFG_ETHEREUM}"
CFG_MONGO="${OUTPUTS_PATH}/${CFG_MONGO}"
CFG_MAIL="${OUTPUTS_PATH}/${CFG_MAIL}"

# Define which IP will be used as endpoint
DOCKER_IP=127.0.0.1
if [ $# -eq 1 ]; then
  DOCKER_IP="$(docker-machine ip $1)"
fi

RUN_CMD=

echo "--------------------------------- BEGIN --------------------------------"
echo ""
echo ""
echo ""
echo "========================================================================"
echo "======================== Running docker-compose ========================"
echo "========================================================================"
echo ""

# Fix the issue of end of line dos/unix
sed -i -e 's/^M$//g' "${DOCKER_PATH}"/ethereum/src/start_geth.sh

# Go to docker folder in order to run docker-compose
cd "${DOCKER_PATH}"

# Workaround : create a persistent volume for the MongoDB database
# https://github.com/docker-library/mongo/issues/74#issuecomment-238918488
docker volume create --name=mongodata

# If there are existing containers for a service, and the service’s configuration or image was
# changed after the container’s creation, docker-compose up picks up the changes by stopping and
# recreating the containers (preserving mounted volumes).
docker-compose up -d --build

# Go back to script directory
cd "${DIR}"

# Wait for IPFS is ready
"${DIR}"/docker/wait-for-container.sh "${IPFS_CONTAINER}" "Daemon is ready" &
# Wait for Ethereum is ready
"${DIR}"/docker/wait-for-container.sh "${ETHEREUM_CONTAINER}" "Successfully sealed new block" &

# Wait for all containers are ready
wait

echo ""
echo "========================================================================"
echo "=========================== Configuring app ============================"
echo "========================================================================"
echo ""

# Copy the configuration files from template
cp "${TEMPLATE_IPFS}" "${OUTPUTS_PATH}" # copy the template in the resources folder
cp "${TEMPLATE_ETHEREUM}" "${OUTPUTS_PATH}" # copy the template in the resources folder
cp "${TEMPLATE_MONGO}" "${OUTPUTS_PATH}" # copy the template in the resources folder
cp "${TEMPLATE_MAIL}" "${OUTPUTS_PATH}" # copy the template in the resources folder

# Copy keystore to outputs
mkdir -p "${KEYSTORE_PATH}"
for i in $(find "${DOCKER_PATH}"/ethereum/data/keystore -name "UTC*"); do # Not recommended, will break on whitespace
    cp "${i}" "${KEYSTORE_PATH}"
done

# retrieve the "main" account
KEYSTORE="$( find "${DOCKER_PATH}"/ethereum/data/keystore -name "UTC*" | head -n 1 )"

# Remove absolute path
KEYSTORE="$( echo "${KEYSTORE}" | sed "s@${DOCKER_PATH}/ethereum/data/keystore@@g" )"

# Create keystore relative path from resources folder
KEYSTORE="${KEYSTORE_FOLDER}${KEYSTORE}"

# Prepare configuration values
IPFS_ENDPOINT=/ip4/"${DOCKER_IP}"/tcp/5001
ETH_ENDPOINT=http://"${DOCKER_IP}":8545/
PASSWORD="$( cat "${DOCKER_PATH}"/ethereum/src/password )"
MONGO_IP="${DOCKER_IP}"
MONGO_PORT=27017
MONGO_DB=gtsl

# Replace values in application configuration file
sed -i -e "s@{IPFS_ENDPOINT}@${IPFS_ENDPOINT}@g" "${CFG_IPFS}"
sed -i -e "s@{ETH_ENDPOINT}@${ETH_ENDPOINT}@g" "${CFG_ETHEREUM}"
sed -i -e "s@{PASSWORD}@${PASSWORD}@g" "${CFG_ETHEREUM}"
sed -i -e "s@{KEYSTORE}@${KEYSTORE}@g" "${CFG_ETHEREUM}"
sed -i -e "s@{MONGO_IP}@${MONGO_IP}@g" "${CFG_MONGO}"
sed -i -e "s@{MONGO_PORT}@${MONGO_PORT}@g" "${CFG_MONGO}"
sed -i -e "s@{MONGO_DB}@${MONGO_DB}@g" "${CFG_MONGO}"

echo "${CFG_IPFS}:"
echo ""
cat "${CFG_IPFS}"
echo ""
echo ""
echo ""
echo "${CFG_ETHEREUM}:"
echo ""
cat "${CFG_ETHEREUM}"
echo ""
echo ""
echo ""
echo "${CFG_MONGO}:"
echo ""
cat "${CFG_MONGO}"
echo ""
echo ""
echo ""
echo "${CFG_MAIL}:"
echo ""
cat "${CFG_MAIL}"
echo ""

#echo ""
#echo "========================================================================"
#echo "============================= Building app ============================="
#echo "========================================================================"
#echo ""

# Go to project root in order to build the project
#cd "${PROJECT_PATH}"

#mvn package

# Go back to script directory
#cd "${DIR}"

#echo ""
#echo "========================================================================"
#echo "============================= Running app =============================="
#echo "========================================================================"
#echo ""

#JAR="$( find "${PROJECT_PATH}"/target -name "*.jar" | head -n 1 )"
#RUN_CMD="java -jar ${JAR}"
#echo ${RUN_CMD}
#${RUN_CMD}

echo ""
echo "========================================================================"
echo "=========================== Copy/Paste files ==========================="
echo "========================================================================"
echo ""

echo "WARN: Please copy/paste the keystore and the configuration file from the outputs folder into the GTSL project as described in the documentation"

echo ""
echo ""
echo ""
echo "--------------------------------- END --------------------------------"


