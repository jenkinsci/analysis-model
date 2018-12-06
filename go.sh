#!/bin/bash

mvn clean install || { echo "Build failed"; exit 1; }
