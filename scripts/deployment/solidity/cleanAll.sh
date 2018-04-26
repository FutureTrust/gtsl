#!/bin/bash

if [ $# -eq 1 ]; then
  WORKSPACE=$1

  # Remove compiled Solidity sources
  rm -rf "${WORKSPACE}/outputs"

else
    echo "usage: "$0" <workspace>"
    echo ""
    echo "Please provide the path of the workspace."
    echo "workspace structure"
    echo "- contract : folder containing solidity sources"
    echo "- outputs : targeted folder"
    echo "Please provide the path of java sources."
    echo "Please provide the path of java package where .java files will be generated."
fi