package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemPickUpListener implements Listener {

    private PacMan instance = PacMan.getInstance();

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {

        Player player = event.getPlayer();

        event.setCancelled(true);

        if (instance.getRoleHandler().getPlayerRoles().get(player).equalsIgnoreCase("Ghost")) {
            return;
        }

        final ItemStack item = event.getItem().getItemStack();

        if (item == instance.getPickupableItemStacks().pointItemStack()) {
            instance.getScoreHandler().addScore(instance.getPowerUpHandler().getPowerUpList()[4]);
            instance.getScoreHandler().removeOnePointInWorld();
            if (instance.getScoreHandler().getScore() == 0) {
                if (instance.getPowerUpHandler().getLevel() == 3) {
                    instance.getGameStateManager().setCurrent(2);
                    return;
                }
                instance.getPowerUpHandler().addLevel();
                instance.getGameStateManager().setCurrent(1);
            }
        } else if (item == instance.getPickupableItemStacks().invincibilityPowerUpItemStack()) {
            activatePowerUp(0, false);
        } else if (item == instance.getPickupableItemStacks().eatingGhostPowerUpItemStack()) {
            activatePowerUp(1, false);
        } else if (item == instance.getPickupableItemStacks().speedPowerUpItemStack()) {
            activatePowerUp(2, false);
            int amplifier = 2;
            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                amplifier = player.getPotionEffect(PotionEffectType.SPEED).getAmplifier() + 2;
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, instance.getPowerUpHandler().getDuration(false), amplifier));
        } else if (item == instance.getPickupableItemStacks().freezeGhostPowerUpItemStack()) {
            activatePowerUp(3, false);
            for (Player current : Bukkit.getOnlinePlayers()) {
                if (instance.getRoleHandler().getPlayerRoles().get(current).equalsIgnoreCase("Ghost")) {
                    current.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, instance.getPowerUpHandler().getDuration(false), 200));
                    current.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, instance.getPowerUpHandler().getDuration(false), 200));
                }
            }
        } else if (item == instance.getPickupableItemStacks().doublePointsPowerUpItemStack()) {
            activatePowerUp(4, true);
        } else if (item == instance.getPickupableItemStacks().extraLifePowerUpItemStack()) {
            player.setHealthScale(instance.getPowerUpHandler().getMaxLifes());
        }

        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);

        event.getItem().remove();
    }

    private void activatePowerUp(int id, boolean doublePoints) {
        instance.getPowerUpHandler().activatePowerUp(id);
        Bukkit.getScheduler().runTaskLaterAsynchronously(instance, () -> instance.getPowerUpHandler().deactivatePowerUp(id), instance.getPowerUpHandler().getDuration(doublePoints));
    }
}
