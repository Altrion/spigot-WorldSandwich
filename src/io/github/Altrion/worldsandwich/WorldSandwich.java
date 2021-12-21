package io.github.Altrion.worldsandwich;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class WorldSandwich extends JavaPlugin {

    public HashMap<UUID, WorldLink> worlds;
    public PlayerDamage playerDamage;
    @Override
    public void onEnable() {
        worlds = Config.handleConfig(this);
        if(worlds==null) return;
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(this),this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"[WorldSandwich]: Loaded " +worlds.size()+" worlds.");
        if(Config.enablePlayerDisableDamage) {
            playerDamage = new PlayerDamage();
            Bukkit.getPluginManager().registerEvents(playerDamage,this);
        }
    }
}
