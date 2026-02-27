# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Spigot Minecraft plugin that allows pupils to write and run JavaScript files inside a Minecraft server. The Java plugin loads and executes `.js` scripts using GraalVM Polyglot, exposing a sandboxed Minecraft API to the scripts.

## Stack

- **Java** (Spigot plugin) — plugin lifecycle, command handling, exposing Minecraft API to scripts
- **GraalVM Polyglot** — executes pupils' JavaScript files inside the JVM
- **JavaScript** — pupil-authored scripts that interact with the Minecraft world
- **Maven** — build system

## Build & Run

```bash
# Build the plugin jar
mvn package

# The output jar will be in target/
# Copy to your Spigot server's plugins/ folder to deploy
cp target/*.jar /path/to/spigot/plugins/
```

## Key Maven Dependencies

- `org.spigotmc:spigot-api` — Spigot plugin API
- `org.graalvm.sdk:graal-sdk` — GraalVM Polyglot engine
- `org.graalvm.js:js` — GraalVM JavaScript runtime

GraalVM JDK must be used to build and run the server for Polyglot to work correctly.

## Architecture

```
src/main/java/
  plugin/
    Main.java          # Plugin entry point (onEnable/onDisable)
    ScriptManager.java # Loads and executes .js files via GraalVM Polyglot
    commands/          # In-game commands (e.g., /runscript <file>)
    api/               # Java objects exposed to JavaScript (player, world, etc.)

scripts/               # Example/default .js files for pupils
```

### How it works

1. `Main.java` initialises a GraalVM `Context` (or per-script contexts for isolation).
2. `ScriptManager` reads `.js` files from a configurable directory (e.g., `plugins/jsmn/scripts/`).
3. Java API objects (wrapped Spigot types) are injected into the GraalVM context as top-level bindings so pupils can call them from JS.
4. Pupils' scripts are executed in a sandboxed context with access only to the exposed API, not arbitrary Java classes.

### JavaScript API surface (to be defined)

Expose a minimal, pupil-friendly API in JS, for example:

```js
player.sendMessage("Hello!");
world.spawnEntity(player.location, "CREEPER");
```

Keep the Java-side API classes in `api/` thin — they should delegate directly to Spigot calls.

## Development Notes

- Use `mvn package -DskipTests` for fast iteration builds.
- Reload the plugin on a local Spigot server with `/reload confirm` or a plugin manager like PlugMan.
- GraalVM Polyglot contexts should be closed after each script execution to free resources.
- Restrict pupil scripts using GraalVM's sandbox options (`Context.newBuilder().allowAllAccess(false)`).
