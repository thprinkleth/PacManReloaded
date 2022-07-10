package de.minecraft.plugin.spigot.minimap;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.util.BlockSetter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class CoinDotHandler {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Erstellt alle Coin-Blöcke auf der Welt
    public void createCoinDots() {

        // Löscht jeden Block für Coins, der gerade existiert
        deleteCoinDots();

        for (int i = 0; i < INSTANCE.getConfigFile().getIntValue("Game.Amount.Locations.Coins"); i++) {

            // Speichert die Position des jeweiligen Coins, als Spawn
            Location location = INSTANCE.getLocationFile().getSpawn("Game.Location.Coin." + i);

            // Setzt 9 Blöcke über der Position gelben Beton
            Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 9, location.getBlockZ()), 251, (byte) 4));
            // Setzt 1 Block unter der Position schwarzes Glas
            Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()), 95, (byte) 15));
            // Setzt 2 Blöcke unter der Position einen goldenen Block
            Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() -2, location.getBlockZ()), Material.GOLD_BLOCK));
        }
    }

    // Löscht einen Coin-Block auf der Welt
    public void deleteCoinDot(Location location) {

        // Setzt 9 Blöcke über der Position Luft (entfernt den Block)
        Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 9, location.getBlockZ()), Material.AIR));
        // Setzt 1 Block unter der Position schwarzes Glas
        Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()), 95, (byte) 15));
        // Setzt 2 Blöcke unter der Position schwarzen Beton
        Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 2, location.getBlockZ()), 251, (byte) 15));
    }

    // Löscht alle Coin-Blöcke auf der Welt
    public void deleteCoinDots() {

        // Geht jede Position für Coins durch
        for (int i = 0; i < INSTANCE.getConfigFile().getIntValue("Game.Amount.Locations.Coins"); i++) {

            // Löscht den Block für den Coin
            deleteCoinDot(INSTANCE.getLocationFile().getSpawn("Game.Location.Coin." + i));
        }
    }
}
