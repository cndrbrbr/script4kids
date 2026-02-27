package com.jsmn.plugin.commands;

import com.jsmn.plugin.api.DroneAPI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BoxCommand implements CommandExecutor, TabCompleter {

    private static final List<String> BLOCK_NAMES = Arrays.stream(Material.values())
            .filter(m -> m.isBlock() && m != Material.AIR && m != Material.CAVE_AIR && m != Material.VOID_AIR)
            .map(Material::name)
            .sorted()
            .toList();

    private final boolean hollow;

    public BoxCommand(boolean hollow) {
        this.hollow = hollow;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("§eUsage: /" + label + " <material> [width] [height] [depth]");
            return true;
        }

        String material = args[0];
        int width  = args.length > 1 ? parseIntOrDefault(args[1], 1) : 1;
        int height = args.length > 2 ? parseIntOrDefault(args[2], 1) : 1;
        int depth  = args.length > 3 ? parseIntOrDefault(args[3], 1) : 1;

        DroneAPI drone = new DroneAPI(player);
        if (hollow) {
            drone.box0(material, width, height, depth);
        } else {
            drone.box(material, width, height, depth);
        }
        return true;
    }

    private int parseIntOrDefault(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String prefix = args[0].toUpperCase();
            return BLOCK_NAMES.stream()
                    .filter(name -> name.startsWith(prefix))
                    .toList();
        }
        return List.of();
    }
}
