package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird ausgeführt, wenn der Spieler eine Maustaste klickt, während ein Inventar offen ist
    @EventHandler
    public void onClick(InventoryClickEvent event) {

        // Speichert den Spieler, welcher, während ein Inventar offen ist, klickt
        Player player = ((Player) event.getWhoClicked());

        // Bricht das Event ab, sodass der Spieler keine Items verschieben kann
        event.setCancelled(true);

        // Try-catch-Block, da eine NullPointerException geworfen wird, wenn der Spieler auf eine leere Fläche drückt, solange er ein Inventar offen hat
        try {

            // Überprüft, ob das geöffnete Inventar das Setup-Inventar ist
            if (event.getClickedInventory().getName().equalsIgnoreCase(INSTANCE.getSetupInventory().getName())) {

                // Speichert ab, welche Position im Inventar angeklickt wurde
                int slot = event.getRawSlot();

                // Speichert die Menge an Coin-Positionen ab
                int amountCoinLocations = INSTANCE.getConfigFile().getIntValue("Game.Amount.Locations.Coins");
                // Speichert die Menge an PowerUp-Positionen ab
                int amountPowerupLocations = INSTANCE.getConfigFile().getIntValue("Game.Amount.Locations.PowerUps");

                // Speichert die Position zwischen, bei welcher die jeweiligen Items sein würden
                final int lobbyLocationSlot = 2;
                final int ghostLocationSlot = 4;
                final int pacmanLocationSlot = 6;
                final int powerupLocationSlot = 8 + 4;
                final int coinLocationSlot = 8 + 6;

                // Speichert die Position des angeklickten Blocks
                Location location = InteractListener.getClickedBlockLocation();

                // Überprüft, welche Position angeklickt wurde
                switch (slot) {

                    case lobbyLocationSlot:

                        // Speichert die Position als PreGame- bzw. PostGame-Position
                        INSTANCE.getLocationFile().setLocation("Game.Location.Lobby", location);

                        // Schickt dem Spieler eine Nachricht, dass die Position erfolgreich gesetzt wurde
                        player.sendMessage(INSTANCE.getMessageFile().getValue("Setup.Spawn.Set.Lobby.Success", player));

                        // Schließt das Inventar, welches der Spieler offen hat
                        player.closeInventory();

                        break;

                    case ghostLocationSlot:

                        // Speichert die Position, an der die Geister erscheinen sollen
                        INSTANCE.getLocationFile().setLocation("Game.Location.Ghost", location);

                        // Schickt dem Spieler eine Nachricht, dass die Position erfolgreich gesetzt wurde
                        player.sendMessage(INSTANCE.getMessageFile().getValue("Setup.Spawn.Set.Ghosts.Success", player));

                        // Schließt das Inventar, welches der Spieler offen hat
                        player.closeInventory();

                        break;

                    case pacmanLocationSlot:

                        // Speichert die Position, an der PacMan erscheinen sollen
                        INSTANCE.getLocationFile().setLocation("Game.Location.PacMan", location);

                        // Schickt dem Spieler eine Nachricht, dass die Position erfolgreich gesetzt wurde
                        player.sendMessage(INSTANCE.getMessageFile().getValue("Setup.Spawn.Set.PacMan.Success", player));

                        // Schließt das Inventar, welches der Spieler offen hat
                        player.closeInventory();

                        break;

                    case coinLocationSlot:

                        for (int i = 0; i < amountCoinLocations; i++) {

                            if (location.getBlockX() == INSTANCE.getLocationFile().getLocation("Game.Location.Coin." + i).getBlockX()
                                    && location.getBlockY() == INSTANCE.getLocationFile().getLocation("Game.Location.Coin." + i).getBlockY()
                                    && location.getBlockZ() == INSTANCE.getLocationFile().getLocation("Game.Location.Coin." + i).getBlockZ()) {

                                // Schickt dem Spieler eine Nachricht, dass die Position nicht gesetzt wurde
                                player.sendMessage(INSTANCE.getMessageFile().getValue("Setup.Spawn.Set.Coin.NotSuccess", player));

                                return;

                            }
                        }

                        // Speichert die Position, an der Coin erscheinen sollen
                        INSTANCE.getLocationFile().setLocation("Game.Location.Coin." + amountCoinLocations, location);

                        // Aktualisiert die Menge aller existierenden Coins in der Config-Datei
                        INSTANCE.getConfigFile().setValue("Game.Amount.Locations.Coins", amountCoinLocations + 1);

                        // Schickt dem Spieler eine Nachricht, dass die Position erfolgreich gesetzt wurde
                        player.sendMessage(INSTANCE.getMessageFile().getValue("Setup.Spawn.Set.Coin.Success", player, amountCoinLocations + 1));

                        // Schließt das Inventar, welches der Spieler offen hat
                        player.closeInventory();

                        break;

                    case powerupLocationSlot:

                        for (int i = 0; i < amountPowerupLocations; i++) {

                            if (location.getBlockX() == INSTANCE.getLocationFile().getLocation("Game.Location.PowerUp." + i).getBlockX()
                                    && location.getBlockY() == INSTANCE.getLocationFile().getLocation("Game.Location.PowerUp." + i).getBlockY()
                                    && location.getBlockZ() == INSTANCE.getLocationFile().getLocation("Game.Location.PowerUp." + i).getBlockZ()) {

                                // Schickt dem Spieler eine Nachricht, dass die Position nicht gesetzt wurde
                                player.sendMessage(INSTANCE.getMessageFile().getValue("Setup.Spawn.Set.PowerUp.NotSuccess", player));

                                return;
                            }
                        }

                        // Speichert die Position, an der das PowerUp erscheinen sollen
                        INSTANCE.getLocationFile().setLocation("Game.Location.PowerUp." + amountPowerupLocations, location);

                        // Aktualisiert die Menge aller existierenden PowerUps in der Config-Datei
                        INSTANCE.getConfigFile().setValue("Game.Amount.Locations.PowerUps", amountPowerupLocations + 1);

                        // Schickt dem Spieler eine Nachricht, dass die Position erfolgreich gesetzt wurde
                        player.sendMessage(INSTANCE.getMessageFile().getValue("Setup.Spawn.Set.PowerUp.Success", player, amountPowerupLocations + 1));

                        // Schließt das Inventar, welches der Spieler offen hat
                        player.closeInventory();

                        break;
                }
            }
        } catch (NullPointerException ex) {
        }
    }
}