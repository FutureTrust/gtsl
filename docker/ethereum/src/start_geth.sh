#!/bin/sh

GEN_ARGS=

echo "Running ethereum node with CHAIN_TYPE=$CHAIN_TYPE"

if [ "$CHAIN_TYPE" == "private" ]; then
  # empty datadir -> geth init
  DATA_DIR=${DATA_DIR:-"/root/.ethereum"}
  echo "DATA_DIR '$DATA_DIR' non existant or empty. Initializing DATA_DIR..."

  # check if keystore is empty
  KEYSTORE="$(ls -A ${DATA_DIR}/keystore)"
  COUNT_KEYSTORE="$(ls -A ${DATA_DIR}/keystore | wc -l  )"
  echo "You have ${COUNT_KEYSTORE} account(s)"
  if [ "$COUNT_KEYSTORE" -ge 2 ]; then
    echo "Keystore: "
    echo "${KEYSTORE}"
  else
    if [ "$ACCOUNT_NEW" == "true" ]; then
      echo "Generating new account(s)..."
      PASSWORD="/opt/password"
      if [ -s ${PASSWORD} ]; then
          geth --datadir "$DATA_DIR" --password "$PASSWORD" account new
          if [ "$COUNT_KEYSTORE" -eq 0 ]; then
            geth --datadir "$DATA_DIR" --password "$PASSWORD" account new
          fi
      else
          echo "Unable to create a new account: $PASSWORD file is missing or is empty."
          exit
      fi
    else
      echo "Unable to find an account, please add two existing account in data/keystore or enable to generate new account with ACCOUNT_NEW=true"
      exit
    fi
  fi

  # replace vars in genesis.json
  if [[ ! -z ${GEN_NONCE} ]]; then
      echo "Generating genesis.nonce from arguments..."
      sed "s/\${GEN_NONCE}/$GEN_NONCE/g" -i /opt/genesis.json
  fi

  if [[ ! -z ${NET_ID} ]]; then
      echo "Generating genesis.net_id from arguments..."
      sed "s/\${NET_ID}/$NET_ID/g" -i /opt/genesis.json
  fi

  echo "Accounts:"
  for f in "${DATA_DIR}"/keystore/* ; do
    ACCOUNT=$(echo ${f} | awk -F'--' 'NR==1{print $3}')
    echo "$ACCOUNT"
    GEN_ALLOC=${GEN_ALLOC}'"'${ACCOUNT}'": { "balance": "0x2000000000000000000000000000000000000000000000000000000000000000" }, '
  done
  GEN_ALLOC=$(echo ${GEN_ALLOC} | sed 's/.$//')

  echo "Generating genesis.alloc from accounts..."
  sed "s/\${GEN_ALLOC}/$GEN_ALLOC/g" -i /opt/genesis.json
  cat /opt/genesis.json

  geth --datadir "$DATA_DIR" init /opt/genesis.json

  GEN_ARGS="--datadir $DATA_DIR --nodiscover  --identity miner --fast --cache=1024 --verbosity=3 --maxpeers=0 --mine --minerthreads=4"
  [[ ! -z ${NET_ID} ]] && GEN_ARGS="$GEN_ARGS --networkid=$NET_ID"
fi

echo "Running geth with arguments $GEN_ARGS $@"
exec geth ${GEN_ARGS} "$@"

