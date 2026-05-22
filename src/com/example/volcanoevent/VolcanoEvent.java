package com.example.volcanoevent;

import org.bukkit.plugin.java.JavaPlugin;

public class VolcanoEvent extends JavaPlugin {
    private VolcanoManager volcanoManager;
    private VolcanoCommand command;

    @Override
    public void onEnable() {
        getLogger().info("Ивент 'Вулкан' активирован!");
        this.volcanoManager = new VolcanoManager(this);
        this.command = new VolcanoCommand(this, volcanoManager);
        getCommand("vulcan").setExecutor(command);
        getServer().getPluginManager().registerEvents(new VolcanoListener(this), this);
    }

    public VolcanoManager getVolcanoManager() {
        return volcanoManager;
    }
}
