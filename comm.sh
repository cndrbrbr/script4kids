#!/bin/bash
cp -R ../../jsmn/* .
git add .
git commit -m "c $*"
git push

# git tag -a v1.0.1 -m "Release v1.0.1"
# git push origin --tags
# gh release upload v1.0.1 /home/mint/spigot/plugins/jsmn.jar
# gh release create v1.0.1 build/jsmn.jar   --title "v1.0.1"   --notes "Initial release" 
# cp /home/mint/spigot/plugins/jsmn-1.0-SNAPSHOT.jar /home/mint/thegitjsmn/script4kids/jsmn.jar

