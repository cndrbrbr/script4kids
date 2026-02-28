package com.jsmn.plugin.commands;

import com.jsmn.plugin.ScriptManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class RunScriptCommand implements CommandExecutor, TabCompleter {

    private final ScriptManager scriptManager;

    public RunScriptCommand(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§eUsage: /runscript <filename>");
            return true;
        }

        scriptManager.runScript(args[0], sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            return scriptManager.listScripts().stream()
                    .map(name -> name.endsWith(".js") ? name.substring(0, name.length() - 3) : name)
                    .filter(name -> name.toLowerCase().startsWith(prefix))
                    .toList();
        }
        return List.of();
    }
}
