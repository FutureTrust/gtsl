#!/bin/bash

# absolute path of the current script
DIR="$( cd "$( dirname "$0" )" && pwd )"

"${DIR}"/removeAllContainers.sh
"${DIR}"/removeAllImages.sh