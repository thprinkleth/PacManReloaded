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
        if(!rolePlayer.equalsIgnoreCase("Ghost")) {
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
        Location fromBlockLoc = locFrom;
        fromBlockLoc.setY(fromBlockLoc.getBlockY() + 10);

        // Setzt den Block auf Luft (Entfernt den alten Block, welcher da war
        Bukkit.getScheduler().runTaskLater(INSTANCE, new BlockSetter(fromBlockLoc, Material.AIR), 3);

        // Speichert die Position des neuen Blockes, an der der Spieler jetzt steht, nur um 10 Blöcke nach oben verschoben
        int x = locTo.getBlockX();
        int y = locTo.getBlockY() + 10;
        int z = locTo.getBlockZ();
        Location toBlockLoc = new Location(locFrom.getWorld(), x, y, z);

        // Setzt den Block auf roten Beton
        Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(toBlockLoc, 251, (byte) 14));
    }
}
