package io.github.Altrion.worldsandwich;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    private WorldSandwich plugin;
    public PlayerMove(WorldSandwich main) {
        plugin=main;
    }
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if(!plugin.worlds.containsKey(event.getTo().getWorld().getUID())) return;
        Location to=event.getTo();
        WorldLink worldLink=plugin.worlds.get(event.getTo().getWorld().getUID());
        if(worldLink==null) return;
        if(worldLink._topWorld!=null)
            if(to.getY()>=worldLink._topExit) {
                event.getPlayer().teleport(new Location(Bukkit.getWorld(worldLink._topWorld),to.getX(),worldLink.worldTop_padding,to.getZ()));
                return;
            }
        if(worldLink._bottomWorld!=null)
            if(to.getY()<= worldLink._bottomExit) {
                event.getPlayer().teleport(new Location(Bukkit.getWorld(worldLink._bottomWorld),to.getX(),worldLink.worldBottom_padding,to.getZ()));
                return;
            }
    }
}
