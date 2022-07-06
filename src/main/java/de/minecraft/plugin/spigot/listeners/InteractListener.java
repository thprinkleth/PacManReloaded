package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    PacMan instance = PacMan.getInstance();

    // Will be executed upon a player right- or left-clicking
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        // Checks if the action is purely a right-click on the mouse on a block
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Checks if the current item in the hand of the player is the setup-item
            if (player.getItemInHand().getType() == Material.FEATHER && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(instance.getMessageFile().getValue("Items.PowerUp.Name").toString())) {
                // Opens the setup inventory for the player who right-clicked
                player.openInventory(instance.getSetupInventory());
                player.updateInventory();
            }
        }
    }
}
