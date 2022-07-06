package de.minecraft.plugin.spigot.gamestate;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

public class IngameState extends GameState{

    private PacMan instance = PacMan.getInstance();

    @Override
    public void start() {
        int playerAmount = Bukkit.getOnlinePlayers().size();
        Random random = new Random();
        int pacManNumberPlayer = random.nextInt(playerAmount);
        // Selects a random person of the people online and gives him the PacMan-Role
        instance.getRoleHandler().getPlayerRoles().put(instance.getPlayerList().get(pacManNumberPlayer), "PacMan");
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Gives the rest of the players online the Ghost role
            if(!instance.getRoleHandler().getPlayerRoles().containsKey(player)){
                instance.getRoleHandler().getPlayerRoles().put(player, "Ghost");
            }
            // Teleports every player to their specific spawn location in the arena (ghosts to the ghost-spawn, PacMan to the PacMan-spawn
            player.teleport(instance.getLocationFile().getLocation("Game.Location." + instance.getRoleHandler().getPlayerRoles().get(player)));
        }


    }

    @Override
    public void stop() {

    }
}
