package de.minecraft.plugin.spigot.score;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.util.EntitySpawner;
import org.bukkit.Bukkit;

public class ScoreHandler {

    private int score;

    private final PacMan INSTANCE = PacMan.getInstance();

    public ScoreHandler() {
        score = 0;
    }

    // Gibt die derzeitige Punktzahl von PacMan wieder
    public int getScore() {
        return score;
    }

    public void setScore() {

    }

    // Fügt der Punktzahl, abhängig, ob das PowerUp für doppelte Punktzahl aktiviert ist, entweder 1 oder 2 Punkte hinzu
    public void addScore(boolean doublePowerUp) {
        score += (doublePowerUp) ? 2 : 1;
    }

    // Erzeugt die Items für die Coins
    public void spawnCoins() {
        for (int i = 0; i < INSTANCE.getConfigFile().getIntValue("Game.Amount.Locations.Coins"); i++) {
            Bukkit.getScheduler().runTask(INSTANCE, new EntitySpawner(INSTANCE.getLocationFile().getSpawn("Game.Location.Coin." + i), INSTANCE.getPickupableItemStacks().coinItemStack()));
        }
    }
}