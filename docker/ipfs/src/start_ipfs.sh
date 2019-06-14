#!/bin/sh
set -e
user=ipfs
repo="$IPFS_PATH"

export LIBP2P_FORCE_PNET=1

if [ `id -u` -eq 0 ]; then
  echo "Changing user to $user"
  # ensure folder is writable
  su-exec "$user" test -w "$repo" || chown -R -- "$user" "$repo"
  # restart script with new privileges
  exec su-exec "$user" "$0" "$@"
fi

# 2nd invocation with regular user
ipfs version

if [ -e "$repo/config" ]; then
  echo "Found IPFS fs-repo at $repo"
else
  case "$IPFS_PROFILE" in
    "") INIT_ARGS="" ;;
    *) INIT_ARGS="--profile=$IPFS_PROFILE" ;;
  esac
  ipfs init $INIT_ARGS
  ipfs config Addresses.API /ip4/0.0.0.0/tcp/5001
  ipfs config Addresses.Gateway /ip4/0.0.0.0/tcp/8080

  echo "copying swarm.key..."
  cp /opt/swarm.key ${IPFS_PATH}/swarm.key
  echo "swarm.key has been copied into $IPFS_PATH"
  echo "removing default peers..."
  ipfs bootstrap rm --all
  echo "adding peers from peers.cfg..."
  cat /opt/peers.cfg | ipfs bootstrap add
fi

exec ipfs "$@"