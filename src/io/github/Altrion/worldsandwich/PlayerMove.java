package io.github.Altrion.worldsandwich;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerMove implements Listener {
    private final WorldSandwich plugin;
    public PlayerMove(WorldSandwich main) {
        plugin=main;
    }
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if(!plugin.worlds.containsKey(event.getTo().getWorld().getUID())) return;
        Location to=event.getTo();
        Player pl = event.getPlayer();
        WorldLink worldLink=plugin.worlds.get(event.getTo().getWorld().getUID());
        if(worldLink==null) return;

        if(worldLink._topWorld!=null)
            if(to.getY()>=worldLink._topExit) {
                to.setWorld(Bukkit.getWorld(worldLink._topWorld));
                // y level handler
                if(worldLink.TOP_groundTeleport&&!(pl.isFlying()&&worldLink.TOP_notOnFlight)) {
                    to.setY(to.getWorld().getHighestBlockAt(to.getBlockX(), to.getBlockZ()).getY() + 1);
                    pl.setFallDistance(0.0f);
                } else {
                    to.setY(worldLink.worldTop_padding);
                }
                // no fall dmg handler
                Material blockUnder= to.add(0,-2,0).getBlock().getType();
                to.add(0,2,0);
                if(!pl.getAllowFlight()&&worldLink.TOP_disableFallDamage) {
                    if(blockUnder != Material.WATER) {
                        plugin.playerDamage.players.add(pl.getUniqueId());
                        pl.setFallDistance(4.0f);
                    }
                }
                // fire res handler...
                if(worldLink.TOP_FireRes!=0&&blockUnder==Material.LAVA) {
                    pl.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,worldLink.TOP_FireRes*20,1));
                }
                pl.teleport(to);
                return;
            }

        if(worldLink._bottomWorld!=null)
            if(to.getY()<= worldLink._bottomExit) {
                to.setWorld(Bukkit.getWorld(worldLink._bottomWorld));
                // y level handler..
                if(worldLink.BOT_groundTeleport&&!(pl.isFlying()&&worldLink.BOT_notOnFlight)) {
                    to.setY(to.getWorld().getHighestBlockAt(to.getBlockX(), to.getBlockZ()).getY() + 1);
                    pl.setFallDistance(0.0f);
                } else {
                    to.setY(worldLink.worldBottom_padding);
                }
                Material blockUnder= to.add(0,-2,0).getBlock().getType();
                to.add(0,2,0);
                // no fall dmg handler
                 if(!pl.getAllowFlight()&&worldLink.BOT_disableFallDamage) {
                    if(blockUnder != Material.WATER) {
                        plugin.playerDamage.players.add(pl.getUniqueId());
                        pl.setFallDistance(4.0f);
                    }
                }
                // fire res handler..
                if(worldLink.BOT_FireRes!=0&&blockUnder==Material.LAVA) {
                    pl.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,worldLink.BOT_FireRes*20,1));
                }

                pl.teleport(to);
            }
    }
}
