package io.github.Altrion.worldsandwich;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class WorldSandwich extends JavaPlugin {

    public HashMap<UUID, WorldLink> worlds;

    @Override
    public void onEnable() {
        worlds = Config.handleConfig(this);
        if(worlds==null) return;
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(this),this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"[WorldSandwich]: Loaded " +worlds.size()+" worlds.");
    }

    public static class Config {
        public static HashMap<UUID, WorldLink> handleConfig(WorldSandwich main) {
            main.saveDefaultConfig();
            HashMap<UUID, WorldLink> returnValue = new HashMap<>();
            ConfigurationSection worlds = main.getConfig().getConfigurationSection("Worlds");
            for (String world : worlds.getKeys(false)) {
                World w = Bukkit.getWorld(world);
                if (w == null) {
                    main.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[WorldSandwich]: Can't find world \"%worldname%\".".replace("%worldname%", world));
                    continue;
                }
                ConfigurationSection _world = worlds.getConfigurationSection(world);
                assert _world != null;
                WorldLink worldLink = new WorldLink();
                if(_world.contains("TopExit"))
                    worldLink._topExit = _world.getInt("TopExit");
                else
                    worldLink._topExit =256;
                if(_world.contains("BottomExit"))
                    worldLink._bottomExit = _world.getInt("BottomExit");
                else
                    worldLink._bottomExit=0;

                if (_world.contains("WorldAbove")) {
                    World WA = Bukkit.getWorld(_world.getString("WorldAbove"));
                    if (WA == null)
                        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[WorldSandwich]: Can't find world above for \"%worldname%\".".replace("%worldname%", world));
                    else {
                        worldLink._topWorld = WA.getUID();
                        if (_world.contains("WorldAbove_Position"))
                            worldLink.worldTop_padding = _world.getInt("WorldAbove_Position");
                        else
                            worldLink.worldTop_padding = 0;
                    }
                }
                if (_world.contains("WorldBelow")) {
                    World WB = Bukkit.getWorld(_world.getString("WorldBelow"));
                    if (WB == null)
                        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[WorldSandwich]: Can't find world below for \"%worldname%\".".replace("%worldname%", world));
                    else {
                        worldLink._bottomWorld = WB.getUID();
                        if (_world.contains("WorldBelow_Position"))
                            worldLink.worldBottom_padding = _world.getInt("WorldBelow_Position");
                        else
                            worldLink.worldBottom_padding = 255;
                    }
                }

                if(worldLink._topWorld==null&&worldLink._bottomWorld==null) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[WorldSandwich]: Found world: \"%worldname%\", but couldn't find any worlds to connect it to.".replace("%worldname%", world));
                    continue;
                }
                returnValue.put(w.getUID(),worldLink);
            }
            if(returnValue.size()==0) {
                String out = ChatColor.RED+"[WorldSandwich] ERROR: No worlds are loaded. Disabling plugin... \n";
                out+="Available worlds: ";
                int a=Bukkit.getWorlds().size();
                for(World gotworld : Bukkit.getWorlds()) {
                    a--;
                    out+=gotworld.getName();
                    if(a!=0)out+=", "; else out+=".";
                }
                Bukkit.getConsoleSender().sendMessage(out);
                Bukkit.getServer().getPluginManager().disablePlugin(main);
                return null;
            }
            return returnValue;
        }
    }
}
