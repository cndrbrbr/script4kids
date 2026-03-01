package com.jsmn.plugin.commands;

import com.jsmn.plugin.api.DroneAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class RainbowCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        int y = player.getLocation().getBlockY();
        int min = player.getWorld().getMinHeight();
        int max = player.getWorld().getMaxHeight() - 1;
        if (y < min || y > max) {
            player.sendMessage("§cYou are outside the build area (Y=" + y + "). Move between Y=" + min + " and Y=" + max + ".");
            return true;
        }

        int radius = args.length > 0 ? parseIntOrDefault(args[0], 18) : 18;
        try {
            new DroneAPI(player).rainbow(radius);
        } catch (Exception e) {
            sender.sendMessage("§cError: " + e.getMessage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }

    private int parseIntOrDefault(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
