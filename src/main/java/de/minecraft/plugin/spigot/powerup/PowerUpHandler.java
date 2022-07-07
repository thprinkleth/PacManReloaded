package de.minecraft.plugin.spigot.powerup;

public class PowerUpHandler {

    /**
     * powerUpList[0] = invincibility
     * powerUpList[1] = eating ghosts
     * powerUpList[2] = speed
     * powerUpList[3] = freezing ghosts
     * powerUpList[4] = double points
     * powerUpList[5] = extra life
     */
    private boolean[] powerUpList;

    int level, maxDuration, minDuration, maxDurationDoublePoints, minDurationDoublePoints;

    public PowerUpHandler() {
        powerUpList = new boolean[6];
        level = 0;
        maxDuration = 7 * 20;
        minDuration = 7 * 10;
        maxDurationDoublePoints *= maxDuration;
        minDurationDoublePoints *= minDuration;
    }

    public boolean[] getPowerUpList() {
        return powerUpList;
    }

    public void setPowerUpList(boolean[] powerUpList) {
        this.powerUpList = powerUpList;
    }

    public void deactivatePowerUps() {
        for (int i = 0; i < powerUpList.length; i++) {
            powerUpList[i] = false;
        }
    }

    public void deactivatePowerUp(int id) {
        powerUpList[id] = false;
    }

    public void activatePowerUp(int id) {
        deactivatePowerUps();
        powerUpList[id] = true;
    }

    public int getDuration(boolean doublePoints) {
        if (doublePoints) {
             return maxDurationDoublePoints - (((maxDurationDoublePoints - minDurationDoublePoints) / 3) * level);
        } else {
            return maxDuration - (((maxDuration - minDuration) / 3) * level);
        }
    }

    public int getMaxLifes() {
        if (level == 0 || level == 1) {
            return 6;
        } else if (level == 2) {
            return 4;
        } else if (level == 3) {
            return 2;
        }
        return 10;
    }
}
