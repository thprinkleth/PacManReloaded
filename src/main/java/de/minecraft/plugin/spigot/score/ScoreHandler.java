package de.minecraft.plugin.spigot.score;

public class ScoreHandler {

    private int score;

    public ScoreHandler() {
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public void addScore(boolean doublePowerUp) {
        score += (doublePowerUp) ? 0 : 1;
    }
}
