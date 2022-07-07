package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.gamestate.IngameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageListener implements Listener {

    private PacMan instance = PacMan.getInstance();

    // Will be executed upon an entity taking damage on the server
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        // Ignores the event if the damaged entity isn't a  player
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!(instance.getGameStateManager().getCurrent() instanceof IngameState)) {
            return;
        }

        Player damager = ((Player) event.getDamager());
        Player player = ((Player) event.getEntity());

        event.setCancelled(true);

        String roleDamager = instance.getRoleHandler().getPlayerRoles().get(damager);
        String rolePlayer = instance.getRoleHandler().getPlayerRoles().get(player);

        if (roleDamager.equalsIgnoreCase("Ghost")) {

            if (rolePlayer.equalsIgnoreCase("Ghost")) {
                return;
            }

            if (instance.getPowerUpHandler().isInvinciblePowerUp()) {
                return;
            }

            instance.getMySQL().addPacmanEaten(damager.getUniqueId().toString());

            if (player.getHealth() == 2) {
                instance.getGameStateManager().setCurrent(2);
            } else {
                player.setHealth(player.getHealth() - 2);
                for (Player current : Bukkit.getOnlinePlayers()) {
                    current.teleport(instance.getLocationFile().getLocation("Game.Location." + instance.getRoleHandler().getPlayerRoles().get(current)));
                    current.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 200));
                }
            }
        } else if (roleDamager.equalsIgnoreCase("PacMan")) {

            if (!instance.getPowerUpHandler().isGhostEatingPowerUp()) {
                return;
            }

            player.teleport(instance.getLocationFile().getLocation("Game.Location." + instance.getRoleHandler().getPlayerRoles().get(player)));
        }
    }
}
