# JSMN — JavaScript in Minecraft

A Spigot plugin that lets pupils write and run JavaScript files directly inside
a Minecraft server. Scripts can interact with the world, build structures,
generate mazes and castles, and much more — making Minecraft a live coding
environment for kids.

A custom GUI with an integrated code editor was built for the pupils so they
can write and run their scripts without leaving their workspace. This plugin was
created alongside it to handle script execution and maintenance on the server.

---

## Acknowledgement

JSMN is inspired by **ScriptCraft**, the pioneering plugin by
[Walter Higgins](https://github.com/walterhiggins/ScriptCraft) that made it
possible to teach children programming through Minecraft. Walter showed that
Minecraft is not just a game — it is one of the best tools ever made for
getting kids excited about writing code.

JSMN exists to keep that tradition alive.

> ScriptCraft repository: https://github.com/walterhiggins/ScriptCraft

Thank you, Walter.

---

## Stack

- **Java** — Spigot plugin, command handling, Minecraft API
- **GraalVM Polyglot** — executes pupil JavaScript inside the JVM
- **JavaScript** — pupil-authored scripts
- **Maven** — build system

## Installation

JSMN is a standard Spigot plugin. **No modifications to Spigot are needed.**

1. Copy `jsmn-1.0-SNAPSHOT.jar` into the server's `plugins/` folder.
2. Start the server with the following JVM flags (GraalVM needs access to JDK internals at runtime):

```bash
java -Xmx2G \
  -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI \
  --add-opens=java.base/java.lang=ALL-UNNAMED \
  --add-opens=java.base/java.lang.invoke=ALL-UNNAMED \
  --add-opens=java.base/java.lang.ref=ALL-UNNAMED \
  --add-opens=java.base/java.nio=ALL-UNNAMED \
  --add-opens=java.base/java.util=ALL-UNNAMED \
  --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED \
  -Dpolyglot.engine.WarnInterpreterOnly=false \
  -jar spigot.jar nogui
```

The plugin runs on standard **OpenJDK 21**. Scripts execute in GraalVM interpreter mode, which is fast enough for teaching purposes.

3. Put `.js` script files into `plugins/jsmn/scripts/`.

> **Note:** Do not use `/reload` or `/reload confirm`. GraalVM's native library cannot be reloaded in the same JVM process. Always do a full server restart after updating the plugin JAR.

## Build

```bash
mvn clean package -DskipTests
cp target/jsmn-1.0-SNAPSHOT.jar /path/to/spigot/plugins/
```

## Commands

Each player has a personal scripts folder at `plugins/jsmn/scripts/<PlayerName>/`.
`/runscript` checks the player's own folder first, then the shared folder.
Use `/runscript Steve/castle` to run another player's script.

| Command | Description |
|---|---|
| `/savescript <name> <content>` | Save a script to your personal folder |
| `/listscripts` | List your scripts, all other players' scripts, and shared scripts |
| `/runscript <file>` | Run a script (own folder first, then shared) |
| `/box <material> [w] [h] [d]` | Place a solid box |
| `/box0 <material> [w] [h] [d]` | Place a hollow box |
| `/sphere <material> [radius]` | Build a solid sphere |
| `/sphere0 <material> [radius]` | Build a hollow sphere |
| `/maze <material> <cols> <rows> [height]` | Generate a random maze |
| `/rainbow [radius]` | Build a rainbow arch |
| `/castle <material> [side] [height]` | Build a castle |

All commands have tab completion for material names and script filenames.

## Project Structure

```
src/main/java/com/jsmn/plugin/
  Main.java                  Plugin entry point
  ScriptManager.java         Loads and executes scripts via GraalVM
  commands/                  One class per command
  api/                       Java objects exposed to JavaScript
    PlayerAPI.java
    ConsoleAPI.java
    DroneAPI.java

src/main/resources/
  plugin.yml                 Command and permission definitions
  config.yml                 Plugin configuration

scripts/                     Example scripts for pupils
HANDBUCH.txt                 Full user guide with examples
```

## JavaScript API

Every script has access to:

- **`player`** — `getName()`, `sendMessage()`, `getX/Y/Z()`, `getHealth()`
- **`drone`** — builder object starting at the player's position:
  `box`, `box0`, `sphere`, `sphere0`, `maze`, `castle`, `rainbow`,
  `fwd`, `back`, `left`, `right`, `up`, `down`, `turn`, `chkpt`, `move`

See `HANDBUCH.txt` for the full reference and example scripts.
