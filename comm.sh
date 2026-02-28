#!/bin/bash
cp -R ../../jsmn/* .
git add .
git commit -m "c $*"
git push

# git tag -a v1.0.1 -m "Release v1.0.1"
