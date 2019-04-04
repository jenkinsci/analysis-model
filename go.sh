#!/bin/bash

mvn clean install || { echo "Build failed"; exit 1; }

cd ../analysis-model-api-plugin

exec ./go.sh
