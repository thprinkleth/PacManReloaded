package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;

public class JoinListener implements Listener {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird ausgeführt, wenn ein Spieler dem Server beitritt
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        // Speichert den Spieler, welcher dem Server beigetreten ist
        Player player = event.getPlayer();

        // Setzt die maximale Anzahl an Leben auf 10
        player.setMaxHealth(20);
        player.setHealthScale(20);

        // Setzt die derzeitige Anzahl an Leben auf 10
        player.setHealth(20);

        // Setzt das Essenslevel auf 10 (kein Hunger)
        player.setFoodLevel(20);

        player.getInventory().clear();

        player.setLevel(0);
        player.setExp(0);

        // Geht durch jeden Effekt durch, den der Spieler gerade hat
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {

            // Entfernt den Effekt von dem Spieler
            player.removePotionEffect(potionEffect.getType());
        }

        // Fügt den Spieler zu der Spielerliste hinzu
        INSTANCE.addToPlayerList(player);

        // Setzt den Spielmodus des Spielers auf Abenteuer (kann nicht mit Blöcken interagieren)
        player.setGameMode(GameMode.ADVENTURE);

        // Sendet jedem Spieler auf dem Server eine spezielle Nachricht
        event.setJoinMessage(INSTANCE.getMessageFile().getValue("World.Join", player));

        // Try-catch-Block, da eine NullPointerException geworfen wird, wenn der Spieler das erste Mal den Server betritt, nachdem das Plugin aktiviert wurde
        try {

            // Teleportiert den Spieler an die Position, an der er vor und nach dem Spiel erscheinen soll
            player.teleport(INSTANCE.getLocationFile().getSpawn("Game.Location.Lobby"));

        } catch (NullPointerException ex) {
        }
    }
}
