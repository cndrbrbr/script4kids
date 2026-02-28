package com.jsmn.plugin.commands;

import com.jsmn.plugin.ScriptManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RunScriptCommand implements CommandExecutor, TabCompleter {

    private final ScriptManager scriptManager;

    public RunScriptCommand(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§eUsage: /runscript <filename>  or  /runscript <player>/<filename>");
            return true;
        }

        scriptManager.runScript(args[0], sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return List.of();

        String prefix = args[0].toLowerCase();
        List<String> completions = new ArrayList<>();

        // Player's own scripts (no prefix needed)
        if (sender instanceof Player p) {
            scriptManager.listScripts(p.getName()).stream()
                    .filter(name -> name.toLowerCase().startsWith(prefix))
                    .forEach(completions::add);
        }

        // Shared scripts
        scriptManager.listScripts().stream()
                .map(name -> name.endsWith(".js") ? name.substring(0, name.length() - 3) : name)
                .filter(name -> name.toLowerCase().startsWith(prefix))
                .filter(name -> !completions.contains(name))
                .forEach(completions::add);

        // Other players' folders as "folder/" prefix suggestions
        if (prefix.contains("/")) {
            String[] parts = prefix.split("/", 2);
            String folderPrefix = parts[0];
            String namePrefix = parts.length > 1 ? parts[1] : "";
            scriptManager.listPlayerFolders().stream()
                    .filter(folder -> folder.toLowerCase().startsWith(folderPrefix.toLowerCase()))
                    .forEach(folder -> {
                        scriptManager.listScripts(folder).stream()
                                .filter(name -> name.toLowerCase().startsWith(namePrefix.toLowerCase()))
                                .forEach(name -> completions.add(folder + "/" + name));
                    });
        } else {
            // Suggest folder names with trailing slash
            scriptManager.listPlayerFolders().stream()
                    .filter(folder -> {
                        if (sender instanceof Player p && folder.equals(p.getName())) return false;
                        return folder.toLowerCase().startsWith(prefix);
                    })
                    .map(folder -> folder + "/")
                    .forEach(completions::add);
        }

        return completions;
    }
}
