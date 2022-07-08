package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    PacMan instance = PacMan.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Player player = ((Player) event.getWhoClicked());

        // Cancels the event so the player can't drag items in the inventory
        event.setCancelled(true);

        // Try-catch-block because there will be NullPointerExceptions thrown when the player has an inventory opened but doesn't click in it
        try {
            // Checks if the inventory, which was clicked by the player, is the setup inventory
            if (event.getClickedInventory() == instance.getSetupInventory()) {

                // Saves the clicked slot
                int slot = event.getSlot();

                int amountPointLocations = (int) instance.getMessageFile().getValue("Game.Amount.Locations.Points");
                int amountPowerupLocations = (int) instance.getMessageFile().getValue("Game.Amount.Locations.PowerUps");

                final int lobbyLocationSlot = 2;
                final int ghostLocationSlot = 4;
                final int pacmanLocationSlot = 6;
                final int pointLocationSlot = 8 + 4;
                final int powerupLocationSlot = 8 + 6;

                Location location;

                switch (slot) {
                    case lobbyLocationSlot:
                        // Gets the location of the block the player is looking at
                        location = player.getTargetBlock(null, 5).getLocation();
                        // Saves the location as a spawn
                        instance.getLocationFile().setLocation("Game.Location.Lobby", location);
                        // Sends the player a message that the spawn has been successfully set
                        player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.Lobby.Success", player).toString());
                        break;
                    case ghostLocationSlot:
                        // Gets the location of the block the player is looking at
                        location = player.getTargetBlock(null, 5).getLocation();
                        // Saves the location as a spawn
                        instance.getLocationFile().setLocation("Game.Location.Ghost", location);
                        // Sends the player a message that the spawn has been successfully set
                        player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.Ghosts.Success", player).toString());
                        break;
                    case pacmanLocationSlot:
                        // Gets the location of the block the player is looking at
                        location = player.getTargetBlock(null, 5).getLocation();
                        if (location == instance.getLocationFile().getLocation("Game.Location.PacMan"))
                        // Saves the location as a spawn
                        instance.getLocationFile().setLocation("Game.Location.PacMan", location);
                        // Sends the player a message that the spawn has been successfully set
                        player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.PacMan.Success", player).toString());
                        break;
                    case pointLocationSlot:
                        // Gets the location of the block the player is looking at
                        location = player.getTargetBlock(null, 5).getLocation();
                        for (int i = 0; i <= amountPointLocations; i++){
                            if (location == instance.getLocationFile().getLocation("Game.Location.Point." + i)) {
                                // Sends the player a message that the spawn has been successfully set
                                player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.Point.NotSuccess", player).toString());
                                return;
                            }
                        }
                        // Saves the location as a spawn
                        instance.getLocationFile().setLocation("Game.Location.Point." + amountPointLocations, location);
                        // Updates the amount of point-locations so there can exist multiple without overwriting the last location
                        instance.getMessageFile().setValue("Game.Amount.Locations.Points", amountPointLocations + 1);
                        // Sends the player a message that the spawn has been successfully set
                        player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.Point.Success", player).toString());
                        break;
                    case powerupLocationSlot:
                        // Gets the location of the block the player is looking at
                        location = player.getTargetBlock(null, 5).getLocation();
                        for (int i = 0; i <= amountPowerupLocations; i++){
                            if (location == instance.getLocationFile().getLocation("Game.Location.PowerUp." + i)) {
                                // Sends the player a message that the spawn has been successfully set
                                player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.PowerUp.NotSuccess", player).toString());
                                return;
                            }
                        }
                        // Saves the location as a spawn
                        instance.getLocationFile().setLocation("Game.Location.PowerUp." + amountPointLocations, location);
                        // Updates the amount of powerup-locations so there can exist multiple without overwriting the last location
                        instance.getMessageFile().setValue("Game.Amount.Locations.PowerUps", amountPowerupLocations + 1);
                        // Sends the player a message that the spawn has been successfully set
                        player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.PowerUp.Success", player).toString());
                        break;
                }
            }
        } catch (NullPointerException ex) {
        }
    }
}