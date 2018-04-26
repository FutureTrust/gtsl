#!/bin/bash

# Docker Scripts
DOCKER_SCRIPTS_PATH=./docker_scripts

# Root directory
ROOT_DIR=../..

# Docker
DOCKER_PATH="$ROOT_DIR"/docker

# Properties
PROPERTIES_PATH="$ROOT_DIR"/properties

# Templates
TEMPLATES_PATH="$ROOT_DIR"/templates

# Keystore
KEYSTORE_PATH="$ROOT_DIR"/keystore

# Resources
ADMIN_RESOURCES="$ROOT_DIR"/gtsl-admin/src/main/resources
WEB_RESOURCES="$ROOT_DIR"/gtsl-web/src/main/resources
CONTRACTS_RESOURCES="$ROOT_DIR"/gtsl-contracts/src/main/resources

# Configuration file
CFG_CONTRACT=contract.properties
CFG_ETHEREUM=ethereum.properties
CFG_IPFS=ipfs.properties
CFG_LOTL=lotl.properties
CFG_MAIL=mail.properties
CFG_MONGO=mongo.properties
CFG_SIGNATURE=signature.properties
CFG_VAT=vat.properties

# Containers
ETHEREUM_CONTAINER=ethereum-node
IPFS_CONTAINER=ipfs-node
MONGO_CONTAINER=mongo-node



# absolute path of the current script
DIR="$( cd "$( dirname "$0" )" && pwd )"

# absolute path of $DOCKER_PATH
DOCKER_PATH="$(cd "$DIR" && cd "$DOCKER_PATH" && pwd)"

# absolute path of $DOCKER_SCRIPTS_PATH
DOCKER_SCRIPTS_PATH="$(cd "$DIR" && cd "$DOCKER_SCRIPTS_PATH" && pwd)"

# absolute path of $PROPERTIES_PATH
PROPERTIES_PATH="$(cd "$DIR" && cd "$PROPERTIES_PATH" && pwd)"

# absolute path of $TEMPLATES_PATH
TEMPLATES_PATH="$(cd "$DIR" && cd "$TEMPLATES_PATH" && pwd)"

# absolute path of $KEYSTORE_PATH
KEYSTORE_PATH="$(cd "$DIR" && cd "$KEYSTORE_PATH" && pwd)"

# absolute path of $ADMIN_RESOURCES
ADMIN_RESOURCES="$(cd "$DIR" && cd "$ADMIN_RESOURCES" && pwd)"

# absolute path of $WEB_RESOURCES
WEB_RESOURCES="$(cd "$DIR" && cd "$WEB_RESOURCES" && pwd)"

# absolute path of templates
TEMPLATE_CONTRACT="${TEMPLATES_PATH}/${CFG_CONTRACT}"
TEMPLATE_ETHEREUM="${TEMPLATES_PATH}/${CFG_ETHEREUM}"
TEMPLATE_IPFS="${TEMPLATES_PATH}/${CFG_IPFS}"
TEMPLATE_LOTL="${TEMPLATES_PATH}/${CFG_LOTL}"
TEMPLATE_MAIL="${TEMPLATES_PATH}/${CFG_MAIL}"
TEMPLATE_MONGO="${TEMPLATES_PATH}/${CFG_MONGO}"
TEMPLATE_SIGNATURE="${TEMPLATES_PATH}/${CFG_SIGNATURE}"
TEMPLATE_VAT="${TEMPLATES_PATH}/${CFG_VAT}"



# Define which IP will be used as endpoint
DOCKER_IP=127.0.0.1
if [ $# -eq 1 ]; then
  DOCKER_IP="$(docker-machine ip $1)"
fi

# Manage keystore
rm -rf "${DOCKER_PATH}"/ethereum/data/keystore
mkdir -p "${DOCKER_PATH}"/ethereum/data/keystore
cp -a "${KEYSTORE_PATH}" "${DOCKER_PATH}"/ethereum/data/keystore

# Manage password
rm -rf "${DOCKER_PATH}"/ethereum/src/password
read -s -p "Enter the password of your keystore: " user_password
echo $user_password > "${DOCKER_PATH}"/ethereum/src/password

echo ""
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
"${DOCKER_SCRIPTS_PATH}"/wait-for-container.sh "${IPFS_CONTAINER}" "Daemon is ready" &
# Wait for Ethereum is ready
"${DOCKER_SCRIPTS_PATH}"/wait-for-container.sh "${ETHEREUM_CONTAINER}" "Successfully sealed new block" &

# Wait for all containers are ready
wait



echo ""
echo "========================================================================"
echo "=========================== Configuring app ============================"
echo "========================================================================"
echo ""

# Copy the configuration files from template
cp "${TEMPLATE_CONTRACT}" "${PROPERTIES_PATH}" # copy the template in the properties folder (empty values because the blockchain has been cleaned)

cp "${TEMPLATE_ETHEREUM}" "${PROPERTIES_PATH}" # copy the template in the properties folder
cp "${TEMPLATE_IPFS}" "${PROPERTIES_PATH}" # copy the template in the properties folder
cp "${TEMPLATE_MONGO}" "${PROPERTIES_PATH}" # copy the template in the properties folder

cp -n "${TEMPLATE_LOTL}" "${PROPERTIES_PATH}" # copy the template in the properties folder if not exists
cp -n "${TEMPLATE_MAIL}" "${PROPERTIES_PATH}" # copy the template in the properties folder if not exists
cp -n "${TEMPLATE_SIGNATURE}" "${PROPERTIES_PATH}" # copy the template in the properties folder if not exists
cp -n "${TEMPLATE_VAT}" "${PROPERTIES_PATH}" # copy the template in the properties folder if not exists

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
KEYSTORE="keystore${KEYSTORE}"

# Prepare configuration values
IPFS_ENDPOINT=/ip4/"${DOCKER_IP}"/tcp/5001

ETH_ENDPOINT=http://"${DOCKER_IP}":8545/
ETH_PASSWORD="$( cat "${DOCKER_PATH}"/ethereum/src/password )"

MONGO_IP="${DOCKER_IP}"
MONGO_PORT=27017
MONGO_DB=gtsl

# absolute path of configuration files
CFG_IPFS="${PROPERTIES_PATH}/${CFG_IPFS}"
CFG_ETHEREUM="${PROPERTIES_PATH}/${CFG_ETHEREUM}"
CFG_MONGO="${PROPERTIES_PATH}/${CFG_MONGO}"

# Replace values in application configuration file
sed -i -e "s@{IPFS_ENDPOINT}@${IPFS_ENDPOINT}@g" "${CFG_IPFS}"

sed -i -e "s@{ETH_ENDPOINT}@${ETH_ENDPOINT}@g" "${CFG_ETHEREUM}"
sed -i -e "s@{ETH_PASSWORD}@${ETH_PASSWORD}@g" "${CFG_ETHEREUM}"
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

# Copy outputs into gtsl-admin resources
cp -a "${KEYSTORE_PATH}" "${ADMIN_RESOURCES}"
cp -a "${PROPERTIES_PATH}" "${ADMIN_RESOURCES}"

# Copy outputs into gtsl-web resources
cp -a "${KEYSTORE_PATH}" "${WEB_RESOURCES}"
cp -a "${PROPERTIES_PATH}" "${WEB_RESOURCES}"

# Copy outputs into gtsl-contracts resources
cp -a "${KEYSTORE_PATH}" "${CONTRACTS_RESOURCES}"
cp "${PROPERTIES_PATH}"/contract.properties "${CONTRACTS_RESOURCES}"/contract.properties

# Clean password
rm -rf "${DOCKER_PATH}"/ethereum/src/password

# Clean keystore
rm -rf "${DOCKER_PATH}"/ethereum/data/keystore

