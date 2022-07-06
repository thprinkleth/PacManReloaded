package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemPickUpListener implements Listener {

    private PacMan instance = PacMan.getInstance();

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {

        Player player = event.getPlayer();

        event.setCancelled(true);

        if (instance.getRoleHandler().getPlayerRoles().get(player).equalsIgnoreCase("Ghost")) {
            return;
        }

        ItemStack item = event.getItem().getItemStack();

        if (item == instance.getPickupableItemStacks().pointItemStack()) {

        }

        event.getItem().remove();
    }
}
