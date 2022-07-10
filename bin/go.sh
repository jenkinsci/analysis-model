#!/bin/bash

set -e

mvn clean install || { echo "Build failed"; exit 1; }

version=$( mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout )

echo Copying module analysis-model version ${version} to analysis-model-api-plugin Jenkins plugin

cd ../analysis-model-api-plugin || { echo "Wrong directory"; exit 1; }

exec ./go.sh $version
