#!/bin/bash
cp -R ../../jsmn/* .
git add .
git commit -m "c $1"
git push
