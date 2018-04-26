#!/bin/bash

docker rmi -f $(docker images | grep solc | awk '{print $3}')
docker rmi -f $(docker images | grep web3j | awk '{print $3}')