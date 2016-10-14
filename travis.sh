#!/usr/bin/env bash


rm -rf ~/.m2/repository/com/chrisdoyle/

#build using gradle
./gradlew clean install -Pprofile=sources,javadoc
#verify publish
ci/verify.sh


  # build again with signatures, then verify the signatures and all the reporting stuff
./gradlew clean
./gradlew install -Pprofile=ci
rc=$?;
if [[ $rc != 0 ]]; then
    echo "FAIL install"
    exit $rc;
fi


ci/verify.sh
rc=$?;
if [[ $rc != 0 ]]; then
    echo "FAIL verify"
    exit $rc;
fi

ci/runApp.sh
rc=$?;
if [[ $rc != 0 ]]; then
    echo "FAIL runapp"
    exit $rc;
fi


# build with signatures and publish
./gradlew publishArtifacts -Pprofile=sources,javadoc
rc=$?;
if [[ $rc != 0 ]]; then
    echo "FAIL publish"
    exit $rc;
fi

# verify correctness
ci/verifySignatures.sh
rc=$?;
if [[ $rc != 0 ]]; then
    echo "FAIL verify sigs sig"
    exit $rc;
fi

#build the site
./gradlew siteWar
rc=$?;

if [[ $rc != 0 ]]; then
    echo "FAIL sitewar"
    exit $rc;
fi