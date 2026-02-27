package com.jsmn.plugin.api;

import org.bukkit.entity.Player;
import org.graalvm.polyglot.HostAccess;

/**
 * Pupil-facing JavaScript API for interacting with the player.
 * Methods on this class are callable directly from JS scripts.
 */
public class PlayerAPI {

    private final Player player;

    public PlayerAPI(Player player) {
        this.player = player;
    }

    @HostAccess.Export
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @HostAccess.Export
    public String getName() {
        return player.getName();
    }

    @HostAccess.Export
    public double getX() {
        return player.getLocation().getX();
    }

    @HostAccess.Export
    public double getY() {
        return player.getLocation().getY();
    }

    @HostAccess.Export
    public double getZ() {
        return player.getLocation().getZ();
    }

    @HostAccess.Export
    public int getHealth() {
        return (int) player.getHealth();
    }
}
