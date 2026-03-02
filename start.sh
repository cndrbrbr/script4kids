#!/bin/bash
cd /home/mint/spigot
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 java -Xmx1G \
  -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI \
  --add-opens=java.base/java.lang=ALL-UNNAMED \
  --add-opens=java.base/java.lang.invoke=ALL-UNNAMED \
  --add-opens=java.base/java.lang.ref=ALL-UNNAMED \
  --add-opens=java.base/java.nio=ALL-UNNAMED \
  --add-opens=java.base/java.util=ALL-UNNAMED \
  --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED \
  -Dpolyglot.engine.WarnInterpreterOnly=false \
  -jar spigot-1.21.11.jar nogui 2>&1 | tee server.log
