package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    PacMan instance = PacMan.getInstance();

    // Will be executed upon a player right- or left-clicking
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        // Checks if the action is purely a right-click on the mouse on a block
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Checks if the current item in the hand of the player is the setup-item
            if (player.getItemInHand().getType() == Material.FEATHER && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(instance.getMessageFile().getValue("Items.Setup.Name").toString())) {
                // Opens the setup inventory for the player who right-clicked
                player.openInventory(instance.getSetupInventory());
                player.updateInventory();
            } else if (player.getItemInHand().getType() == Material.STICK && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(instance.getMessageFile().getValue("Items.SetupPoint.Name").toString())){
                // Gets the location of the block the player is looking at
                Location location = player.getTargetBlock(null, 5).getLocation();
                int amountPointLocations = (int) instance.getMessageFile().getValue("Game.Amount.Locations.Points");
                for (int i = 0; i <= amountPointLocations; i++){
                    if (location == instance.getLocationFile().getLocation("Game.Location.Point." + i)) {
                        // Sends the player a message that the spawn has been successfully set
                        player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.Point.NotSuccess", player).toString());
                        return;
                    }
                }

                // Saves the location
                instance.getLocationFile().setLocation("Game.Location.Point." + amountPointLocations, location);

                int x = location.getBlockX();
                int y = location.getBlockY() + 9;
                int z = location.getBlockZ();

                Location blockLocation = new Location(location.getWorld(), x, y, z);
                location.getWorld().getBlockAt(blockLocation).setData((byte) 4);
                location.getWorld().getBlockAt(blockLocation).setType(Material.CONCRETE);
                // Updates the amount of point-locations so there can exist multiple without overwriting the last location
                instance.getMessageFile().setValue("Game.Amount.Locations.Points", amountPointLocations + 1);
                // Sends the player a message that the spawn has been successfully set
                player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.Point.Success", player).toString());
            }
        }
    }
}
