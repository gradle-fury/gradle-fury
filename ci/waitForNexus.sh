#!/usr/bin/env bash

until $(curl --output /dev/null --silent --head --fail http://localhost:8081/nexus); do
    printf '.'
    sleep 5
done