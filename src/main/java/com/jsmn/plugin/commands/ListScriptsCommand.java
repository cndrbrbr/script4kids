package com.jsmn.plugin.commands;

import com.jsmn.plugin.ScriptManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListScriptsCommand implements CommandExecutor {

    private final ScriptManager scriptManager;

    public ListScriptsCommand(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> scripts = scriptManager.listScripts();
        if (scripts.isEmpty()) {
            sender.sendMessage("§eNo scripts found in the scripts folder.");
        } else {
            sender.sendMessage("§aAvailable scripts:");
            scripts.forEach(s -> sender.sendMessage("§7 - " + s));
        }
        return true;
    }
}
