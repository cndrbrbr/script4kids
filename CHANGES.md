# Changes

## 2026-04-12

### Updated: Spigot API version and Java target

**`pom.xml`**
- `spigot-api` updated from `1.20.4-R0.1-SNAPSHOT` → `1.21.11-R0.2-SNAPSHOT`,
  matching the exact Spigot server version in use on all workshop servers.
- `java.version` updated from `17` → `21`, matching the GraalVM JDK 21 runtime.

**`src/main/resources/plugin.yml`**
- `api-version` updated from `1.20` → `1.21.11`, which suppresses the Spigot
  enum-compatibility warning that appeared in the server log on every start.

---

## 2026-04-11

### Fixed: GraalVM shaded into plugin JAR

**`pom.xml`** — Switch GraalVM dependencies back to compile scope so the
maven-shade-plugin bundles them into the fat JAR.

An earlier change (2026-03-22) had set the GraalVM dependencies to `provided`
scope, expecting the GraalVM JDK to supply them at runtime. This only works
when the server JVM is a full GraalVM distribution with the SDK modules on the
classpath. On a standard JDK or a GraalVM JDK where those modules are not
exported to unnamed modules, the plugin failed to enable with
`NoClassDefFoundError: org/graalvm/polyglot/PolyglotException`, and the HTTP
upload server on port 25580 never started.

The fix is to shade the GraalVM polyglot API and JS engine into the plugin JAR
so it is self-contained and works regardless of the host JDK:
- `org.graalvm.polyglot:polyglot:24.1.2` — compile scope (shaded)
- `org.graalvm.polyglot:js-community:24.1.2` — compile scope / pom type (shaded
  transitively, brings in the JS language and Truffle runtime JARs)

---

## 2026-03-22

### Changed: Use GraalVM JDK at runtime instead of shading

**`pom.xml`** — Set GraalVM dependencies to `provided` scope to avoid
classloader conflicts between the shaded GraalVM classes and the JDK's own
GraalVM runtime. Intended for use with a GraalVM CE JDK as the server runtime.

*(This approach was later reversed on 2026-04-11; see above.)*

---

## 2026-03-01

### Added: Full plugin feature set

**Commands** — All drone building operations are now available as Minecraft chat
commands with tab completion:

| Command | Description |
|---|---|
| `/runscript <name>` | Run a JavaScript file from the player's scripts folder |
| `/savescript <name> <code…>` | Save a script directly from chat |
| `/listscripts` | List all scripts on the server |
| `/myscripts` | List only the current player's own scripts |
| `/box <material> [W H D]` | Place a solid box |
| `/box0 <material> [W H D]` | Place a hollow box |
| `/sphere <material> [radius]` | Build a solid sphere |
| `/sphere0 <material> [radius]` | Build a hollow sphere |
| `/maze <material> <cols> <rows> [height]` | Generate a random maze |
| `/rainbow [radius]` | Build a rainbow arch |
| `/castle <material> [side] [height]` | Build a castle with towers and battlements |

**Script upload** — HTTP upload server on port 25580 with a browser-based upload
page (`upload.html`). Players can upload `.js` files directly to their personal
script folder without needing Minecraft chat.

**Per-player script folders** — Scripts are stored per player so workshop
participants don't overwrite each other's work.

**GraalVM upgraded** — Polyglot / JS engine updated to `24.1.2` for Java 21
compatibility.

**Documentation** — `README.md` with installation guide, workshop setup
instructions, and JavaScript API reference; `HANDBUCH.txt` with a detailed
German-language user guide for workshop participants.

---

## 2026-02-27 — 2026-02-28

### Initial development

- Basic Spigot plugin scaffolding with `/runscript` and `/listscripts`
- `ScriptManager` for loading and executing JavaScript files via GraalVM Polyglot
- Iterative additions: `/myscripts`, HTTP script upload, drone API (`DroneAPI`),
  `PlayerAPI`, `ConsoleAPI`
- `BoxCommand`, `MazeCommand`, `SphereCommand`, `RainbowCommand`, `CastleCommand`
  added progressively
