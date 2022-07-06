package de.minecraft.plugin.spigot.gamestate;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class IngameState extends GameState{

    private PacMan instance = PacMan.getInstance();

    private boolean speedPowerUp;
    private boolean ghostFreezePowerUp;
    private boolean ghostEatingPowerUp;
    private boolean invinciblePowerUp;
    private boolean doublePointsPowerUp;

    private int score;

    @Override
    public void start() {
        int playerAmount = Bukkit.getOnlinePlayers().size();
        speedPowerUp = false;
        ghostFreezePowerUp = false;
        ghostEatingPowerUp = false;
        invinciblePowerUp = false;
        doublePointsPowerUp = false;
        score = 0;
        Random random = new Random();
        int pacManNumberPlayer = random.nextInt(playerAmount);
        // Selects a random person of the people online and gives him the PacMan-Role
        instance.getRoleHandler().getPlayerRoles().put(instance.getPlayerList().get(pacManNumberPlayer), "PacMan");
        instance.getPlayerList().get(pacManNumberPlayer).setHealthScale(6);
        instance.getPlayerList().get(pacManNumberPlayer).setHealth(2);
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Gives the rest of the players online the Ghost role
            if(!instance.getRoleHandler().getPlayerRoles().containsKey(player)){
                instance.getRoleHandler().getPlayerRoles().put(player, "Ghost");
                player.setHealthScale(20);
                player.setHealth(20);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250));
            // Teleports every player to their specific spawn location in the arena (ghosts to the ghost-spawn, PacMan to the PacMan-spawn
            player.teleport(instance.getLocationFile().getLocation("Game.Location." + instance.getRoleHandler().getPlayerRoles().get(player)));
        }
    }

    @Override
    public void stop() {

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

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }
}
