package com.jsmn.plugin.api;

import org.bukkit.command.CommandSender;
import org.graalvm.polyglot.HostAccess;

/**
 * Minimal API exposed to JS when running from the server console (no real player).
 */
public class ConsoleAPI {

    private final CommandSender sender;

    public ConsoleAPI(CommandSender sender) {
        this.sender = sender;
    }

    @HostAccess.Export
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    @HostAccess.Export
    public String getName() {
        return sender.getName();
    }

    @HostAccess.Export
    public double getX() { return 0; }
    @HostAccess.Export
    public double getY() { return 0; }
    @HostAccess.Export
    public double getZ() { return 0; }
    @HostAccess.Export
    public int getHealth() { return 0; }
}
