package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    private final PacMan INSTANCE = PacMan.getInstance();
    private static Location clickedBlockLocation;
    private Location firstLocationAutoCoin, secondLocationAutoCoin;

    // Wird ausgeführt, sobald der Spieler eine Maustaste drückt
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        // Speichert den Spieler, welcher die Interaktion durchführt
        Player player = event.getPlayer();

        // Try-catch block, da eine NullPointerException geworfen wird, wenn der Spieler nichts in seiner Hand hält
        try {

            // Überprüft, ob die Interaktion ein Rechtsklick auf ein Block ist
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                // Speichert die Position des angeklickten Blocks
                clickedBlockLocation = event.getClickedBlock().getLocation();

                // Überprüft, ob der Spieler das Setup-Item in der Hand hält
                if (event.getItem().getType() == Material.FEATHER && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(INSTANCE.getConfigFile().getValue("Items.Setup.Name"))) {

                    // Öffnet das Setup-Inventar für den Spieler
                    player.openInventory(INSTANCE.getSetupInventory());

                // Überprüft, ob der Spieler das Setup-Item für das automatische Hinzufügen für die Coin-Positionen in der Hand hält
                } else if (event.getItem().getType() == Material.STICK && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(INSTANCE.getConfigFile().getValue("Items.SetupCoin.Name"))) {

                    // Überprüft, ob die erste Position noch nicht gesetzt wurde
                    if (firstLocationAutoCoin == null) {

                        // Speichert die Position des angeklickten Blocks in der ersten Variable
                        firstLocationAutoCoin = clickedBlockLocation;

                        // Schickt dem Spieler eine Nachricht, dass die erste Position für das automatische Berechnen der Coin-Positionen gesetzt wurde
                        player.sendMessage(INSTANCE.getMessageFile().getValue("Setup.Spawn.Set.AutoCoin.FirstLocation", player));

                        return;
                    }

                    // Speichert die Position des angeklickten Blocks in der zweiten Variable
                    secondLocationAutoCoin = event.getClickedBlock().getLocation();

                    // Schickt dem Spieler eine Nachricht, dass die zweite Position für das automatische Berechnen der Coin-Positionen gesetzt wurde
                    player.sendMessage(INSTANCE.getMessageFile().getValue("Setup.Spawn.Set.AutoCoin.SecondLocation", player));

                    // Speichert die Anzahl an Coin-Positionen, die gesetzt werden
                    int amountLocationSet = 0;
                    // Speichert die derzeitige Anzahl an Coins, die schon gesetzt wurden
                    int amountCoinLocations = INSTANCE.getConfigFile().getIntValue("Game.Amount.Locations.Coins");


                    int xTop, xBottom, zTop, zBottom;

                    // Speichert die x- und z-Werte der Größe nach ab, um bei der Überprüfung der Blöcke einheitliche for-Schleifen zu machen
                    xBottom = Math.min(firstLocationAutoCoin.getBlockX(), secondLocationAutoCoin.getBlockX());
                    xTop = Math.max(firstLocationAutoCoin.getBlockX(), secondLocationAutoCoin.getBlockX());
                    zBottom = Math.min(firstLocationAutoCoin.getBlockZ(), secondLocationAutoCoin.getBlockZ());
                    zTop = Math.max(firstLocationAutoCoin.getBlockZ(), secondLocationAutoCoin.getBlockZ());

                    for (int x = xBottom; x < xTop; x++) {

                        for (int z = zBottom; z < zTop; z++) {

                            // Erzeugt eine Position mit den Koordinaten, in der die verschachtelte for-Schleife gerade ist
                            Location posFloor = new Location(player.getWorld(), x, firstLocationAutoCoin.getBlockY(), z);

                            // Überprüft, ob an der Position kein Glas-Block ist
                            if (Bukkit.getWorld(player.getWorld().getName()).getBlockAt(posFloor).getType() != Material.STAINED_GLASS) {
                                continue;
                            }

                            // Erzeugt eine Position mit den Koordinaten, in der die verschachtelte for-Schleife gerade ist, nur um eins nach unten verschoben
                            Location posUnderFloor = new Location(player.getWorld(), x, firstLocationAutoCoin.getBlockY() - 1, z);

                            // Überprüft, ob an der Position ein Glas-Block ist
                            if (Bukkit.getWorld(player.getWorld().getName()).getBlockAt(posUnderFloor).getType() != Material.GOLD_BLOCK) {
                                continue;
                            }

                            // Speichert die Position
                            INSTANCE.getLocationFile().setLocation("Game.Location.Coin." + amountCoinLocations, posFloor);

                            // Erhöht den Wert der Variable, welche die Menge der Coin-Positionen festlegt
                            amountCoinLocations++;

                            // Erhöht den Wert der Variable, um am Ende eine Nachricht auszugeben, welche die Menge der gesetzten Positionen enthält
                            amountLocationSet++;
                        }

                        // Setzt den Wert der existierenden Coins in der config-Datei
                        INSTANCE.getConfigFile().setValue("Game.Amount.Locations.Coins", amountCoinLocations);
                    }

                    // Sendet dem Spieler die Nachricht, dass eine bestimmte Anzahl an Positionen automatisch gesetzt wurden
                    player.sendMessage(INSTANCE.getMessageFile().getValue("Setup.Spawn.Set.AutoCoin.Success", player, amountLocationSet));
                    firstLocationAutoCoin = null;
                    secondLocationAutoCoin = null;
                }
            }
        } catch (NullPointerException ex) {
        }
    }

    public static Location getClickedBlockLocation() {
        return clickedBlockLocation;
    }
}
