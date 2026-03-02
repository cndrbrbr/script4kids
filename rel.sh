#!/bin/bash
git tag -a v$1 -m "Release v$1"
git push origin --tags
cp /home/mint/spigot/plugins/jsmn-1.0-SNAPSHOT.jar /home/mint/thegitjsmn/script4kids/jsmn.jar
# gh release upload v$1 /home/mint/thegitjsmn/script4kids/jsmn.jar
gh release create v$1 /home/mint/thegitjsmn/script4kids/jsmn.jar   --title "v$1"   --notes "v$1 $2" 


