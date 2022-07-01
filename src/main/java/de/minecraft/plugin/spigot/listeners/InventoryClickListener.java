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

        event.setCancelled(true);

        try {
            if (event.getClickedInventory() == instance.getSetupInventory()) {

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
                        location = player.getTargetBlock(null, 5).getLocation();
                        instance.getLocationFile().setSpawn("Game.Location.Lobby", location);
                        player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.Lobby.Success", player).toString());
                        break;
                    case ghostLocationSlot:
                        location = player.getTargetBlock(null, 5).getLocation();
                        instance.getLocationFile().setSpawn("Game.Location.Ghosts", location);
                        player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.Ghosts.Success", player).toString());
                        break;
                    case pacmanLocationSlot:
                        location = player.getTargetBlock(null, 5).getLocation();
                        instance.getLocationFile().setSpawn("Game.Location.PacMan", location);
                        player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.PacMan.Success", player).toString());
                        break;
                    case pointLocationSlot:
                        location = player.getTargetBlock(null, 5).getLocation();
                        instance.getLocationFile().setSpawn("Game.Location.Point." + amountPointLocations, location);
                        instance.getMessageFile().setValue("Game.Amount.Locations.Points", amountPointLocations + 1);
                        player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.Point.Success", player).toString());
                        break;
                    case powerupLocationSlot:
                        location = player.getTargetBlock(null, 5).getLocation();
                        instance.getLocationFile().setSpawn("Game.Location.PowerUp." + amountPointLocations, location);
                        instance.getMessageFile().setValue("Game.Amount.Locations.PowerUps", amountPowerupLocations + 1);
                        player.sendMessage(instance.getMessageFile().getValue("Setup.Spawn.Set.PowerUp.Success", player).toString());
                        break;
                }
            }
        } catch (NullPointerException ex) {
        }
    }
}