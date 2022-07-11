package de.minecraft.plugin.spigot.powerup;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.util.EntitySpawner;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.HashMap;
import java.util.Random;

public class PowerUpHandler {

    /**
     * powerUpList[0] = invincibility
     * powerUpList[1] = eating ghosts
     * powerUpList[2] = speed
     * powerUpList[3] = freezing ghosts
     * powerUpList[4] = double coins
     * powerUpList[5] = extra life
     */
    private boolean[] powerUpList;

    private final PacMan INSTANCE = PacMan.getInstance();

    // Speichert die Art des PowerUps ab
    private final HashMap<Integer, String> powerUpHashMap;

    // Speichert das Level und die daraus abhängige Dauer der PowerUps ab
    int level, maxDuration, minDuration, maxDurationDoubleScore, minDurationDoubleScore;

    public PowerUpHandler() {
        powerUpList = new boolean[6];
        powerUpHashMap = new HashMap<>();
        level = 0;
        maxDuration = 7 * 20;
        minDuration = 7 * 10;
        maxDurationDoubleScore = maxDuration * 2;
        minDurationDoubleScore = minDuration * 2;
    }

    public boolean[] getPowerUpList() {
        return powerUpList;
    }

    // Deaktiviert alle PowerUps
    public void deactivatePowerUps() {
        for (int i = 0; i < powerUpList.length; i++) {
            powerUpList[i] = false;
        }
    }

    // Deaktiviert ein spezifisches PowerUp
    public void deactivatePowerUp(int id) {
        powerUpList[id] = false;
    }

    // Aktiviert ein neues Powerup -> alte werden überschrieben
    public void activatePowerUp(int id) {
        deactivatePowerUps();
        powerUpList[id] = true;
    }

    // Git die Dauer des PowerUps zurück (Doppelt Punkte hat eine extra Dauer)
    public int getDuration(boolean doubleScore) {
        if (doubleScore) {
             return maxDurationDoubleScore - (((maxDurationDoubleScore - minDurationDoubleScore) / 3) * level);
        } else {
            return maxDuration - (((maxDuration - minDuration) / 3) * level);
        }
    }

    // Gibt die maximale Anzahl an Leben von PacMan zurück
    public int getMaxLifes() {
        if (level == 0 || level == 1) {
            return 6;
        } else if (level == 2) {
            return 4;
        } else if (level == 3) {
            return 2;
        }
        return -1;
    }

    // Gibt das Level zurück
    public int getLevel() {
        return level;
    }

    // Erhöht das Level um eins
    public void addLevel() {
        level++;
    }

    // Erzeugt die Items für die PowerUps
    public void spawnPowerUps() {

        // Löscht jedes PowerUp-Item, das auf dem Boden liegt
        for (int i : powerUpHashMap.keySet()) {
            INSTANCE.getPowerUpDotHandler().deleteDotOnMap(INSTANCE.getLocationFile().getLocation("Game.Location.PowerUp." + i));
        }

        // Setzt die HashMap für die PowerUps zurück
        resetPowerUpHashMap();

        // Geht durch jede PowerUp-Position durcht
        for (int i = 0; i < INSTANCE.getConfigFile().getIntValue("Game.Amount.Locations.PowerUps"); i++) {

            // Erstellt ein zufälliges PowerUp an seiner Stelle
            int powerUp = new Random().nextInt(6);
            Bukkit.getScheduler().runTask(INSTANCE, new EntitySpawner(INSTANCE.getLocationFile().getSpawn("Game.Location.PowerUp." + i), INSTANCE.getPickupableItemStacks().getPowerUpItemStack(powerUp)));

            // Überprüft welches PowerUp gesetzt wurde
            switch (powerUp) {
                case 0:
                    powerUpHashMap.put(i, "Invincibility");
                    break;
                case 1:
                    powerUpHashMap.put(i, "GhostEating");
                    break;
                case 2:
                    powerUpHashMap.put(i, "Speed");
                    break;
                case 3:
                    powerUpHashMap.put(i, "GhostFreezing");
                    break;
                case 4:
                    powerUpHashMap.put(i, "DoubleCoins");
                    break;
                case 5:
                    powerUpHashMap.put(i, "ExtraLife");
                    break;
            }
        }

    }

    public HashMap<Integer, String> getPowerUpHashMap() {
        return powerUpHashMap;
    }

    public void resetPowerUpHashMap() {
        powerUpHashMap.clear();
    }
}
