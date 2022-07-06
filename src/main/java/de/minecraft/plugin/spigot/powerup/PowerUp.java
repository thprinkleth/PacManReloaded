package de.minecraft.plugin.spigot.powerup;

public class PowerUp {

    private String powerUpName;
    private double spawnPercentage;
    private double duration;
    private int amount;

    public PowerUp(String powerUpName, double spawnPercentage, double duration, int amount) {
        this.powerUpName = powerUpName;
        this.spawnPercentage = spawnPercentage;
        this.duration = duration;
        this.amount = amount;
    }

    public String getPowerUpName() {
        return powerUpName;
    }

    public void setPowerUpName(String powerUpName) {
        this.powerUpName = powerUpName;
    }

    public double getSpawnPercentage() {
        return spawnPercentage;
    }

    public void setSpawnPercentage(double spawnPercentage) {
        this.spawnPercentage = spawnPercentage;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
