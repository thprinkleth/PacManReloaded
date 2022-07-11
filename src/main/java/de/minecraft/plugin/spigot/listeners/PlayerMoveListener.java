package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.gamestate.IngameState;
import de.minecraft.plugin.spigot.util.BlockSetter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird ausgeführt, sobald ein Spieler sich bewegt
    @EventHandler
    public void onMovement(PlayerMoveEvent event){

        // Speichert den Spieler, welcher sich bewegt
        Player player = event.getPlayer();

        // Überprüft, ob der derzeitige Spielstatus nicht Ingame ist
        if(!(INSTANCE.getGameStateManager().getCurrent() instanceof IngameState)){
            return;
        }

        // Speichert die Rolle des Spielers
        String rolePlayer = INSTANCE.getRoleHandler().getPlayerRoles().get(player);

        // Überprüft, ob der Spieler kein Geist ist
        if(rolePlayer == null || !rolePlayer.equalsIgnoreCase("Ghost")) {
            return;
        }

        // Speichert die Position, an der der Spieler vor der Bewegung war
        Location locFrom = event.getFrom();
        // Speichert die Position, an der der Spieler jetzt ist
        Location locTo = event.getTo();

        // Überprüft, ob es keine Differenz der Position auf horizontaler Ebene gibt
        if (locFrom.getBlockX() == locTo.getBlockX() && locFrom.getBlockZ() == locTo.getBlockZ()) {
            return;
        }

        // Speichert die Position des Blockes ab, von der bewegt hat, nur um 10 Blöcke nach oben verschoben
        Location fromBlockLoc1 = new Location(locFrom.getWorld(), locFrom.getBlockX(), locFrom.getBlockY() + 10, locFrom.getBlockZ());
        // Speichert die Position des Blockes ab, von der bewegt hat, nur um 10 Blöcke nach oben und um einen in x-Richtung verschoben
        Location fromBlockLoc2 = new Location(locFrom.getWorld(), locFrom.getBlockX() + 1, locFrom.getBlockY() + 10, locFrom.getBlockZ());
        // Speichert die Position des Blockes ab, von der bewegt hat, nur um 10 Blöcke nach oben, um einen in x-Richtung verschoben ist und einen in z-Richtung verschoben
        Location fromBlockLoc3 = new Location(locFrom.getWorld(), locFrom.getBlockX() + 1, locFrom.getBlockY() + 10, locFrom.getBlockZ() + 1);
        // Speichert die Position des Blockes ab, von der bewegt hat, nur um 10 Blöcke nach oben und um einen in z-Richtung verschoben
        Location fromBlockLoc4 = new Location(locFrom.getWorld(), locFrom.getBlockX(), locFrom.getBlockY() + 10, locFrom.getBlockZ() + 1);

        // Setzt den Block auf Luft (Entfernt den alten Block, welcher da war
        Bukkit.getScheduler().runTaskLater(INSTANCE, new BlockSetter(fromBlockLoc1, Material.AIR), 3);
        Bukkit.getScheduler().runTaskLater(INSTANCE, new BlockSetter(fromBlockLoc2, Material.AIR), 3);
        Bukkit.getScheduler().runTaskLater(INSTANCE, new BlockSetter(fromBlockLoc3, Material.AIR), 3);
        Bukkit.getScheduler().runTaskLater(INSTANCE, new BlockSetter(fromBlockLoc4, Material.AIR), 3);

        // Speichert die Position des neuen Blockes ab, an der der Spieler jetzt steht, nur um 10 Blöcke nach oben verschoben
        Location toBlockLoc1 = new Location(locTo.getWorld(), locTo.getBlockX(), locTo.getBlockY() + 10, locTo.getBlockZ());
        // Speichert die Position des neuen Blockes ab, an der der Spieler jetzt steht, nur um 10 Blöcke nach oben und um einen in x-Richtung verschoben
        Location toBlockLoc2 = new Location(locTo.getWorld(), locTo.getBlockX() + 1, locTo.getBlockY() + 10, locTo.getBlockZ());
        // Speichert die Position des neuen Blockes ab, an der der Spieler jetzt steht, nur um 10 Blöcke nach oben, um einen in x-Richtung und einen in z-Richtung verschoben
        Location toBlockLoc3 = new Location(locTo.getWorld(), locTo.getBlockX() + 1, locTo.getBlockY() + 10, locTo.getBlockZ() + 1);
        // Speichert die Position des neuen Blockes ab, an der der Spieler jetzt steht, nur um 10 Blöcke nach oben und um einen in z-Richtung verschoben
        Location toBlockLoc4 = new Location(locTo.getWorld(), locTo.getBlockX(), locTo.getBlockY() + 10, locTo.getBlockZ() + 1);

        // Setzt den Block auf roten Beton
        Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(toBlockLoc1, 251, (byte) 14));
        Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(toBlockLoc2, 251, (byte) 14));
        Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(toBlockLoc3, 251, (byte) 14));
        Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(toBlockLoc4, 251, (byte) 14));
    }
}
