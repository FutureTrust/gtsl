#!/bin/sh

ipfs init

echo "copying swarm.key..."
cp /opt/swarm.key ${IPFS_PATH}/swarm.key
echo "swarm.key has been copied into $IPFS_PATH"

echo ""

echo "removing default peers..."
ipfs bootstrap rm --all

echo ""

echo "adding peers from peers.cfg..."
cat /opt/peers.cfg | ipfs bootstrap add

echo ""

export LIBP2P_FORCE_PNET=1
exec ipfs daemon