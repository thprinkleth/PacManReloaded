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

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand().getType() == Material.FEATHER && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(instance.getMessageFile().getValue("Items.PowerUp.Name").toString())) {
                player.openInventory(instance.getSetupInventory());
                player.updateInventory();
            }
        }
    }
}
