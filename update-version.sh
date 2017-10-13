#!/bin/bash

param=$1


function help
{
     echo -------------------------- ERROR ------------------------------------
     echo Hey buddy... please call me with the new version number!
     echo "Like this:"
     echo "  update-version.sh 1.1.1.1-SNAPSHOT"
     echo "But if you like to revert badly assigned version number, dont' worry."
     echo "Then please use this command:"
     echo "  update-version.sh revert"

}

function update
{
    echo OK, so let"'"s update this project to version $param
    mvn versions:set -DnewVersion=$param
}

function revert
{
    echo OK, so let"'" revert the version to the previous number whatever it was
    mvn versions:revert
}

case "$param" in
    "revert" ) revert;;
    [0-9]*  ) update;;
    "" ) help
esac

