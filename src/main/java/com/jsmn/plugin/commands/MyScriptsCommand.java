package com.jsmn.plugin.commands;

import com.jsmn.plugin.ScriptManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MyScriptsCommand implements CommandExecutor {

    private final ScriptManager scriptManager;

    public MyScriptsCommand(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        List<String> scripts = scriptManager.listScripts(p.getName());
        if (scripts.isEmpty()) {
            sender.sendMessage("§eYou have no scripts yet. Use /savescript to create one.");
        } else {
            sender.sendMessage("§aYour scripts:");
            scripts.forEach(s -> sender.sendMessage("§7 - " + s));
        }
        return true;
    }
}
