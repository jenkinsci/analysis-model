#!/bin/bash

mvn clean install || { echo "Build failed"; exit 1; }

cd ../analysis-model-api-plugin || { echo "Wrong directory"; exit 1; }

exec ./go.sh
