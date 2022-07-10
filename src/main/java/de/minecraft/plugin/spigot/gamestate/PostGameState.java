package de.minecraft.plugin.spigot.gamestate;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.util.BlockSetter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PostGameState extends GameState {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird bei Wechsel des Gamestatuses auf PostGame ausgeführt
    @Override
    public void start() {

        /**
         * TODO:
         * - Send Title/Message/Hotbarmessage to every Player (Won, Loss, Score, ?Overall rank?)
         */

        INSTANCE.updateRankingInventory();

        // Geht jeden Spieler durch, der sich gerade auf dem Server befindet
        for (Player player : Bukkit.getOnlinePlayers()) {

            // Teleportiert den Spieler an die Position, an der sie bei Betreten des Servers erschienen sind.
            player.getInventory().clear();
            player.setMaxHealth(20);
            player.setHealthScale(20);
            player.setHealth(20);
            player.teleport(INSTANCE.getLocationFile().getSpawn("Game.Location.Lobby"));
        }
    }

    // Wird bei Wechsel des Gamestatuses auf einen anderen ausgeführt
    @Override
    public void stop() {

        // Setzt die Rollenverteilung der Spieler zurück
        INSTANCE.getRoleHandler().getPlayerRoles().clear();

        // TODO: only if necessary
        Bukkit.getServer().shutdown();
    }
}
