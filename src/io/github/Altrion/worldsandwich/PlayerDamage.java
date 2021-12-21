package io.github.Altrion.worldsandwich;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.event.EventPriority.LOW;

public class PlayerDamage implements Listener {
    public PlayerDamage() {
        players= new ArrayList<>();
    }
    public List<UUID> players;
    @EventHandler(priority = LOW)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(event.getEntity().getType()!= EntityType.PLAYER) return;
        if(!players.contains(event.getEntity().getUniqueId())) return;
        players.remove(event.getEntity().getUniqueId());
        if(event.getCause()==EntityDamageEvent.DamageCause.FALL) {
            event.setDamage(0.0);
            event.setCancelled(true);
        }
    }
}
