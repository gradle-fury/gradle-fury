#!/usr/bin/env bash

sed "s/NEXUS_USERNAME=/NEXUS_USERNAME=admin/g" gradle.properties >  gradle2.properties
rm gradle.properties
mv gradle2.properties gradle.properties

sed "s/NEXUS_PASSWORD\=/NEXUS_PASSWORD=admin123/g" gradle.properties >  gradle2.properties
rm gradle.properties
mv gradle2.properties gradle.properties

echo signing.passPhrase=abc >> gradle.properties
echo signing.useAgent=false >> gradle.properties
 #set the location of gpg in our gradle.properties file
echo GPG_PATH=$(which gpg) >> gradle.properties
echo GPG_PASSPHRASE=abc >> gradle.properties

sed "s/RELEASE_REPOSITORY_URL\=/RELEASE_REPOSITORY_URL=http:\/\/localhost:8081\/nexus\/content\/repositories\/releases\//g" gradle.properties >  gradle2.properties
rm gradle.properties
mv gradle2.properties gradle.properties

sed "s/SNAPSHOT_REPOSITORY_URL\=/SNAPSHOT_REPOSITORY_URL=http:\/\/localhost:8081\/nexus\/content\/repositories\/snapshots\//g" gradle.properties >  gradle2.properties
rm gradle.properties
mv gradle2.properties gradle.properties
