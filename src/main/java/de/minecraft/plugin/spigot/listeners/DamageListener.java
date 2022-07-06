package de.minecraft.plugin.spigot.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    // Will be executed upon an entity taking damage on the server
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        // Ignores the event if the damaged entity isn't a  player
        if (!(event.getEntity() instanceof Player))
            return;

        Player damager = ((Player) event.getDamager());
        Player player = ((Player) event.getEntity());

        /**
         * TODO:
         * - Wenn Geist den Pacman hittet stirbt PacMan
         * - Wenn Pacman Geist hittet stirbt PacMan
         * - PowerUps beachten!
         * - Gamestate wird auf End gesetzt
         */
    }
}
