#!/bin/bash

docker rmi -f $(docker images | grep ipfs | awk '{print $3}')
docker rmi -f $(docker images | grep docker_eth | awk '{print $3}')
docker rmi -f $(docker images | grep ethereum | awk '{print $3}')
docker rmi -f $(docker images | grep mongo | awk '{print $3}')