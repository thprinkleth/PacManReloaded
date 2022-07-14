package de.minecraft.plugin.spigot.gamestate;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PreGameState extends GameState {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird bei Wechsel des Gamestatuses auf PreGame ausgeführt
    @Override
    public void start() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!INSTANCE.getPlayerList().contains(player)) {
                INSTANCE.getPlayerList().add(player);
            }

            player.teleport(INSTANCE.getLocationFile().getSpawn("Game.Location.Lobby"));
        }
    }

    // Wird bei Wechsel des Gamestatuses auf einen anderen ausgeführt
    @Override
    public void stop() {

    }
}
