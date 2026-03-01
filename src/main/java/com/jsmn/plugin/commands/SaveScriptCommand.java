package com.jsmn.plugin.commands;

import com.jsmn.plugin.ScriptManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.regex.Pattern;

public class SaveScriptCommand implements CommandExecutor {

    private static final Pattern VALID_NAME = Pattern.compile("^[a-zA-Z0-9_-]+$");

    private final ScriptManager scriptManager;

    public SaveScriptCommand(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§eUsage: /savescript <name> <content...>");
            return true;
        }

        String name = args[0];
        if (!VALID_NAME.matcher(name).matches()) {
            sender.sendMessage("§cInvalid script name. Use only letters, numbers, underscores and hyphens.");
            return true;
        }

        // Join all remaining args as the script content
        StringBuilder content = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i > 1) content.append(" ");
            content.append(args[i]);
        }

        try {
            scriptManager.saveScript(name, content.toString(), player.getName());
            sender.sendMessage("§aScript '" + name + "' saved.");
        } catch (IOException e) {
            sender.sendMessage("§cFailed to save script: " + e.getMessage());
        }

        return true;
    }
}
