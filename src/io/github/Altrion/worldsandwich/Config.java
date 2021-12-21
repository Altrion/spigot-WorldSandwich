package io.github.Altrion.worldsandwich;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class Config {

    private static final int configVersion=1;
    public static boolean enablePlayerDisableDamage;

    public static HashMap<UUID, WorldLink> handleConfig(WorldSandwich main) {
        main.saveDefaultConfig();
        File f =new File(main.getDataFolder(),File.separator+"config.yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(f);
        System.out.println(f.exists());
        HashMap<UUID, WorldLink> returnValue = new HashMap<>();
        ConfigurationSection worlds = main.getConfig().getConfigurationSection("Worlds");
        int cfgVersion=0;
        if(configuration.contains("configVersion"))
            cfgVersion=configuration.getInt("configVersion");
        if(configVersion!=cfgVersion)
            Bukkit.getConsoleSender().sendMessage((ChatColor.YELLOW+"[WorldSandwich]: loaded wrong version of config (%s), please remove old configuration and let plugin create new one.. (current version: %d)" + ChatColor.RED+" [WRONG CONFIG VERSION MIGHT CREATE ERROR]")
                .replace("%s", cfgVersion+"").replace("%d",configVersion+""));


        assert worlds != null;
        for (String world : worlds.getKeys(false)) {
            World w = Bukkit.getWorld(world);
            if (w == null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[WorldSandwich]: Can't find world \"%worldname%\".".replace("%worldname%", world));
                continue;
            }
            ConfigurationSection _world = worlds.getConfigurationSection(world);

            WorldLink worldLink = new WorldLink();
            if(_world.contains("TopExit"))
                worldLink._topExit = _world.getInt("TopExit");
            if(_world.contains("BottomExit"))
                worldLink._bottomExit = _world.getInt("BottomExit");



            if (_world.contains("WorldAbove")) {

                if(_world.isString("WorldAbove")) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[WorldSandwich]: WorldAbove cannot be text, Please let plugin generate new config.yml and edit it");
                    continue;
                }
                ConfigurationSection worldAbove = _world.getConfigurationSection("WorldAbove");
                World WA = Bukkit.getWorld(worldAbove.getString("WorldName")+"");
                if (WA == null)
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[WorldSandwich]: Can't find world above for \"%worldname%\".".replace("%worldname%", world));
                else {
                    worldLink._topWorld = WA.getUID();
                    if(worldAbove.contains("SafeTeleport")) {
                        ConfigurationSection worldAboveSafe = worldAbove.getConfigurationSection("SafeTeleport");
                        worldLink.TOP_groundTeleport=worldAboveSafe.getBoolean("GroundTeleport");
                        worldLink.TOP_notOnFlight=worldAboveSafe.getBoolean("NotOnFlight");
                        worldLink.TOP_disableFallDamage=worldAboveSafe.getBoolean("DisableFallDamage");
                        if(worldAboveSafe.contains("LavaBelowFireRes")) {
                            worldLink.TOP_FireRes=worldAboveSafe.getInt("LavaBelowFireRes");
                        }

                        // check disable fall dmg
                        if(worldLink.TOP_disableFallDamage) {
                            enablePlayerDisableDamage=true;
                        }
                    }
                    if (worldAbove.contains("TeleportPosition"))
                        worldLink.worldTop_padding = worldAbove.getInt("TeleportPosition");
                }
            }
            if (_world.contains("WorldBelow")) {
                if(_world.isString("WorldBelow")) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[WorldSandwich]: WorldBelow cannot be text, Please let plugin generate new config.yml and edit it");
                    continue;
                }
                ConfigurationSection worldBelow = _world.getConfigurationSection("WorldBelow");
                World WB = Bukkit.getWorld(worldBelow.getString("WorldName")+"");
                if (WB == null)
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[WorldSandwich]: Can't find world below for \"%worldname%\".".replace("%worldname%", world));
                else {
                    worldLink._bottomWorld = WB.getUID();
                    if(worldBelow.contains("SafeTeleport")) {
                        ConfigurationSection worldBelowSafe = worldBelow.getConfigurationSection("SafeTeleport");
                        worldLink.BOT_groundTeleport=worldBelowSafe.getBoolean("GroundTeleport");
                        worldLink.BOT_notOnFlight=worldBelowSafe.getBoolean("NotOnFlight");
                        worldLink.BOT_disableFallDamage=worldBelowSafe.getBoolean("DisableFallDamage");
                        if(worldBelowSafe.contains("LavaBelowFireRes")) {
                            worldLink.BOT_FireRes=worldBelowSafe.getInt("LavaBelowFireRes");
                        }

                        // check disable fall dmg
                        if(worldLink.BOT_disableFallDamage) {
                            enablePlayerDisableDamage=true;
                        }
                    }
                    if (worldBelow.contains("TeleportPosition"))
                        worldLink.worldBottom_padding = worldBelow.getInt("TeleportPosition");
                }
            }
            if(worldLink._topWorld==null&&worldLink._bottomWorld==null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[WorldSandwich]: Found world: \"%worldname%\", but couldn't find any worlds to connect it to.".replace("%worldname%", world));
                continue;
            }
            returnValue.put(w.getUID(),worldLink);
        }
        if(returnValue.size()==0) {
            StringBuilder out = new StringBuilder(ChatColor.RED + "[WorldSandwich] ERROR: No worlds are loaded. Disabling plugin... \n");
            out.append("Available worlds: ");
            int a=Bukkit.getWorlds().size();
            for(World gotworld : Bukkit.getWorlds()) {
                a--;
                out.append(gotworld.getName());
                if(a!=0) out.append(", "); else out.append(".");
            }
            Bukkit.getConsoleSender().sendMessage(out.toString());
            Bukkit.getServer().getPluginManager().disablePlugin(main);
            return null;
        }
        return returnValue;
    }
}
