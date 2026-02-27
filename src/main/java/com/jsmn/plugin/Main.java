package com.jsmn.plugin;

import com.jsmn.plugin.commands.ListScriptsCommand;
import com.jsmn.plugin.commands.RunScriptCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private ScriptManager scriptManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getDataFolder().mkdirs();

        scriptManager = new ScriptManager(this);

        getCommand("runscript").setExecutor(new RunScriptCommand(scriptManager));
        getCommand("listscripts").setExecutor(new ListScriptsCommand(scriptManager));

        getLogger().info("jsmn enabled.");
    }

    @Override
    public void onDisable() {
        if (scriptManager != null) {
            scriptManager.shutdown();
        }
        getLogger().info("jsmn disabled.");
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }
}
