package com.jsmn.plugin.commands;

import com.jsmn.plugin.ScriptManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ListScriptsCommand implements CommandExecutor {

    private final ScriptManager scriptManager;

    public ListScriptsCommand(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            // Show the player's own scripts
            List<String> personal = scriptManager.listScripts(p.getName());
            if (personal.isEmpty()) {
                sender.sendMessage("§eYou have no scripts yet. Use /savescript to create one.");
            } else {
                sender.sendMessage("§aYour scripts:");
                personal.forEach(s -> sender.sendMessage("§7 - " + s));
            }
            // Also show shared scripts if any
            List<String> shared = scriptManager.listScripts();
            if (!shared.isEmpty()) {
                sender.sendMessage("§aShared scripts:");
                shared.stream()
                        .map(name -> name.endsWith(".js") ? name.substring(0, name.length() - 3) : name)
                        .forEach(s -> sender.sendMessage("§7 - " + s));
            }
        } else {
            // Console: show all scripts
            List<String> shared = scriptManager.listScripts();
            if (shared.isEmpty()) {
                sender.sendMessage("No shared scripts found.");
            } else {
                sender.sendMessage("Shared scripts:");
                shared.forEach(s -> sender.sendMessage(" - " + s));
            }
            scriptManager.listPlayerFolders().forEach(folder -> {
                List<String> scripts = scriptManager.listScripts(folder);
                if (!scripts.isEmpty()) {
                    sender.sendMessage(folder + ":");
                    scripts.forEach(s -> sender.sendMessage(" - " + s));
                }
            });
        }
        return true;
    }
}
