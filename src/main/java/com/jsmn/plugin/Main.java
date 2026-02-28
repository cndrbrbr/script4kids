package com.jsmn.plugin;

import com.jsmn.plugin.commands.BoxCommand;
import com.jsmn.plugin.commands.CastleCommand;
import com.jsmn.plugin.commands.ListScriptsCommand;
import com.jsmn.plugin.commands.MazeCommand;
import com.jsmn.plugin.commands.RainbowCommand;
import com.jsmn.plugin.commands.RunScriptCommand;
import com.jsmn.plugin.commands.MyScriptsCommand;
import com.jsmn.plugin.commands.SaveScriptCommand;
import com.jsmn.plugin.commands.SphereCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private ScriptManager scriptManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getDataFolder().mkdirs();

        scriptManager = new ScriptManager(this);

        RunScriptCommand runScriptCmd = new RunScriptCommand(scriptManager);
        getCommand("runscript").setExecutor(runScriptCmd);
        getCommand("runscript").setTabCompleter(runScriptCmd);
        getCommand("listscripts").setExecutor(new ListScriptsCommand(scriptManager));
        getCommand("myscripts").setExecutor(new MyScriptsCommand(scriptManager));
        BoxCommand boxCmd  = new BoxCommand(false);
        BoxCommand box0Cmd = new BoxCommand(true);
        getCommand("box").setExecutor(boxCmd);
        getCommand("box").setTabCompleter(boxCmd);
        getCommand("box0").setExecutor(box0Cmd);
        getCommand("box0").setTabCompleter(box0Cmd);
        MazeCommand mazeCmd = new MazeCommand();
        getCommand("maze").setExecutor(mazeCmd);
        getCommand("maze").setTabCompleter(mazeCmd);
        SphereCommand sphereCmd  = new SphereCommand(false);
        SphereCommand sphere0Cmd = new SphereCommand(true);
        getCommand("sphere").setExecutor(sphereCmd);
        getCommand("sphere").setTabCompleter(sphereCmd);
        getCommand("sphere0").setExecutor(sphere0Cmd);
        getCommand("sphere0").setTabCompleter(sphere0Cmd);
        RainbowCommand rainbowCmd = new RainbowCommand();
        getCommand("rainbow").setExecutor(rainbowCmd);
        getCommand("rainbow").setTabCompleter(rainbowCmd);
        CastleCommand castleCmd = new CastleCommand();
        getCommand("castle").setExecutor(castleCmd);
        getCommand("castle").setTabCompleter(castleCmd);
        getCommand("savescript").setExecutor(new SaveScriptCommand(scriptManager));

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
