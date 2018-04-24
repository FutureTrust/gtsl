#!/bin/bash

if [ $# -eq 2 ]; then

  CONTAINER_ID=$1
  REGEX=$2

  # Wait for the container is running
  RUNNING=
  time=2
  count=0
  until [ "${RUNNING}" = true ]
  do
    # Search in logs the line that indicates that the DAG is generated
    RUNNING="$( docker inspect -f "{{.State.Running}}" "${CONTAINER_ID}" )"

    if [ "${RUNNING}" != true ]
    then
      echo "${CONTAINER_ID} is not running yet... [elapsed=$(($time*$count / 60))m$(($time*$count % 60))s]"
      sleep ${time}
      count=$((count+1))
    fi
  done
  echo "${CONTAINER_ID} is running"

  # Wait for the container is ready
  READY=
  time=10
  count=0
  until [ ! -z "${READY}" ]
  do
    # Search in logs a line to validate $REGEX
    READY="$( docker logs --tail 50 "${CONTAINER_ID}" 2>&1 | grep "${REGEX}" )"

    if [ -z "${READY}" ]
    then
      echo "${CONTAINER_ID} is not ready yet... [$(($time*$count)) seconds elapsed]"
      echo "$( docker logs --tail 1 "${CONTAINER_ID}" )"
      sleep ${time}
      count=$((count+1))
    fi
  done
  echo "${CONTAINER_ID} is ready"

else
    echo "usage: "$0" <name> <container> <regex>"
fi