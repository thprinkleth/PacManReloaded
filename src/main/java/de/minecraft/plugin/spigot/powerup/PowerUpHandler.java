package de.minecraft.plugin.spigot.powerup;

public class PowerUpHandler {

    private boolean speedPowerUp;
    private boolean ghostFreezePowerUp;
    private boolean ghostEatingPowerUp;
    private boolean invinciblePowerUp;
    private boolean doublePointsPowerUp;

    public PowerUpHandler() {
        speedPowerUp = false;
        ghostFreezePowerUp = false;
        ghostEatingPowerUp = false;
        invinciblePowerUp = false;
        doublePointsPowerUp = false;
    }

    public boolean isSpeedPowerUp() {
        return speedPowerUp;
    }

    public void setSpeedPowerUp(boolean speedPowerUp) {
        this.speedPowerUp = speedPowerUp;
    }

    public boolean isGhostFreezePowerUp() {
        return ghostFreezePowerUp;
    }

    public void setGhostFreezePowerUp(boolean ghostFreezePowerUp) {
        this.ghostFreezePowerUp = ghostFreezePowerUp;
    }

    public boolean isGhostEatingPowerUp() {
        return ghostEatingPowerUp;
    }

    public void setGhostEatingPowerUp(boolean ghostEatingPowerUp) {
        this.ghostEatingPowerUp = ghostEatingPowerUp;
    }

    public boolean isInvinciblePowerUp() {
        return invinciblePowerUp;
    }

    public void setInvinciblePowerUp(boolean invinciblePowerUp) {
        this.invinciblePowerUp = invinciblePowerUp;
    }

    public boolean isDoublePointsPowerUp() {
        return doublePointsPowerUp;
    }

    public void setDoublePointsPowerUp(boolean doublePointsPowerUp) {
        this.doublePointsPowerUp = doublePointsPowerUp;
    }
}
