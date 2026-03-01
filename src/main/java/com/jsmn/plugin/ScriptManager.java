package com.jsmn.plugin;

import com.jsmn.plugin.api.ConsoleAPI;
import com.jsmn.plugin.api.DroneAPI;
import com.jsmn.plugin.api.PlayerAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptManager {

    private final Main plugin;
    private final File scriptsDir;
    private Engine engine;

    public ScriptManager(Main plugin) {
        this.plugin = plugin;
        this.scriptsDir = new File(plugin.getDataFolder(), "scripts");
        this.scriptsDir.mkdirs();
        try {
            this.engine = Engine.create();
        } catch (Exception | Error e) {
            plugin.getLogger().severe("Failed to initialize GraalVM engine: " + e.getMessage());
            plugin.getLogger().severe("Restart the server (do not use /reload) to fix this.");
            this.engine = null;
        }
    }

    /** Returns (and creates) the personal scripts folder for a player. */
    public File getPlayerScriptsDir(String playerName) {
        File dir = new File(scriptsDir, playerName);
        dir.mkdirs();
        return dir;
    }

    /** Saves script content to the player's personal folder. */
    public void saveScript(String name, String content, String playerName) throws IOException {
        File dir = getPlayerScriptsDir(playerName);
        File file = new File(dir, name + ".js");
        Files.writeString(file.toPath(), content);
    }

    /** Lists scripts in the player's personal folder (names without .js). */
    public List<String> listScripts(String playerName) {
        File dir = new File(scriptsDir, playerName);
        if (!dir.exists()) return List.of();
        File[] files = dir.listFiles((d, n) -> n.endsWith(".js"));
        if (files == null) return List.of();
        return Arrays.stream(files)
                .map(f -> f.getName().substring(0, f.getName().length() - 3))
                .sorted()
                .collect(Collectors.toList());
    }

    /** Lists scripts in the shared folder (names without .js). */
    public List<String> listScripts() {
        File[] files = scriptsDir.listFiles((dir, name) -> name.endsWith(".js"));
        if (files == null) return List.of();
        return Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    /** Returns all player folder names (subdirectories of scriptsDir). */
    public List<String> listPlayerFolders() {
        File[] dirs = scriptsDir.listFiles(File::isDirectory);
        if (dirs == null) return List.of();
        return Arrays.stream(dirs).map(File::getName).sorted().collect(Collectors.toList());
    }

    public void runScript(String path, CommandSender sender) {
        if (engine == null) {
            sender.sendMessage("§cScript engine failed to initialise. The server needs a full restart.");
            return;
        }

        File scriptFile = resolveScript(path, sender);
        if (scriptFile == null) {
            sender.sendMessage("§cScript not found: " + path);
            return;
        }

        try (Context context = Context.newBuilder("js")
                .engine(engine)
                .allowHostAccess(HostAccess.EXPLICIT)
                .allowAllAccess(false)
                .build()) {

            Value bindings = context.getBindings("js");
            Object api = (sender instanceof Player p) ? new PlayerAPI(p) : new ConsoleAPI(sender);
            bindings.putMember("player", api);

            if (sender instanceof Player p) {
                DroneAPI drone = new DroneAPI(p);
                bindings.putMember("drone", drone);
                context.eval("js", "function box(mat,w,h,d){ return drone.box(mat,w||1,h||1,d||1); }");
                context.eval("js", "function box0(mat,w,h,d){ return drone.box0(mat,w||1,h||1,d||1); }");
                context.eval("js", "function turn(n){ return drone.turn(n===undefined?1:n); }");
                context.eval("js", "function maze(mat,cols,rows,h){ return h!==undefined?drone.maze(mat,cols,rows,h):drone.maze(mat,cols,rows); }");
                context.eval("js", "function castle(mat,side,height){ return (side!==undefined&&height!==undefined)?drone.castle(mat,side,height):drone.castle(mat); }");
                context.eval("js", "function rainbow(r){ return r!==undefined?drone.rainbow(r):drone.rainbow(); }");
                context.eval("js", "function sphere(mat,r){ return r!==undefined?drone.sphere(mat,r):drone.sphere(mat); }");
                context.eval("js", "function sphere0(mat,r){ return r!==undefined?drone.sphere0(mat,r):drone.sphere0(mat); }");
            }

            String code = Files.readString(scriptFile.toPath());
            Source source = Source.newBuilder("js", code, scriptFile.getName()).build();
            context.eval(source);

        } catch (PolyglotException e) {
            sender.sendMessage("§cScript error: " + e.getMessage());
            plugin.getLogger().warning("Script error in " + path + ": " + e.getMessage());
        } catch (IOException e) {
            sender.sendMessage("§cFailed to read script.");
            plugin.getLogger().severe("Could not read script " + path + ": " + e.getMessage());
        }
    }

    /**
     * Resolves a script path to a File.
     *
     * - "folder/name"  → scripts/folder/name.js  (explicit folder)
     * - "name"         → scripts/<PlayerName>/name.js  (player's own folder, if player)
     *                  → scripts/name.js                (shared folder fallback)
     */
    private File resolveScript(String path, CommandSender sender) {
        String normalized = path.endsWith(".js") ? path.substring(0, path.length() - 3) : path;

        if (normalized.contains("/")) {
            // Explicit folder/name — look directly there
            File f = new File(scriptsDir, normalized + ".js");
            return f.exists() ? f : null;
        }

        // Check player's own folder first
        if (sender instanceof Player p) {
            File personal = new File(getPlayerScriptsDir(p.getName()), normalized + ".js");
            if (personal.exists()) return personal;
        }

        // Fall back to shared folder
        File shared = new File(scriptsDir, normalized + ".js");
        return shared.exists() ? shared : null;
    }

    public void shutdown() {
        if (engine != null) {
            engine.close();
        }
    }
}
