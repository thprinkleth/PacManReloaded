package de.minecraft.plugin.spigot.score;

import de.minecraft.plugin.spigot.PacMan;

import java.util.ArrayList;

public class ScoreHandler {

    private int score;
    private int pointsInWorld;
    private PacMan instance = PacMan.getInstance();

    public ScoreHandler() {
        score = 0;
        pointsInWorld = (int) instance.getMessageFile().getValue("Game.Amount.Locations.Points");
    }

    public int getScore() {
        return score;
    }

    public void addScore(boolean doublePowerUp) {
        score += (doublePowerUp) ? 0 : 1;
    }

    public int getPointsInWorld() {
        return pointsInWorld;
    }

    public void removeOnePointInWorld() {
        pointsInWorld--;
    }

    public void resetPointsInWorld() {
        pointsInWorld = (int) instance.getMessageFile().getValue("Game.Amount.Locations.Points");
    }
}
