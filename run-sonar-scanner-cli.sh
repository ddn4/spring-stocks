#!/bin/bash

export SONARQUBE_URL=127.0.0.1:9000

docker run \
    --rm \
    -e SONAR_HOST_URL="http://${SONARQUBE_URL}" \
    -e SONAR_LOGIN="myAuthenticationToken" \
    -v "${PWD}:/usr/src" \
    sonarsource/sonar-scanner-cli

exit 0


# 51aa883995dd45b9602b2b366de4b5cbbf9e8dad