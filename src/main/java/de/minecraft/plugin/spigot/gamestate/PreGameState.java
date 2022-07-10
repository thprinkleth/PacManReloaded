package de.minecraft.plugin.spigot.gamestate;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Location;
import org.bukkit.Material;

public class PreGameState extends GameState {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird bei Wechsel des Gamestatuses auf PreGame ausgeführt
    @Override
    public void start() {

    }

    // Wird bei Wechsel des Gamestatuses auf einen anderen ausgeführt
    @Override
    public void stop() {

    }
}
