#!/bin/bash
cp -R ../../jsmn/* .
git add .
git commit -m "c $*"
git push
