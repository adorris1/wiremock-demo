#!/usr/bin/env bash
java -jar wiremock-1.54-standalone.jar \
    --proxy-all="http://welcome.web" \
    --record-mappings --verbose