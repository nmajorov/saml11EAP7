#!/bin/env bash

#
# run client with wsdl located locally
# don't read it from web resource

# get script current directory
SCRIPT_DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
cd $SCRIPT_DIR


mvn clean package exec:java -DserviceUrl=file://$SCRIPT_DIR/src/main/resources/wsdls/Echo.wsdl

