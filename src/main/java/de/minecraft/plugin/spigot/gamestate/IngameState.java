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
        instance.getRoleHandler().getPlayerRoles().put(instance.getPlayerList().get(pacManNumberPlayer), "PacMan");
        for(Player player:Bukkit.getOnlinePlayers()){
            if(!instance.getRoleHandler().getPlayerRoles().containsKey(player)){
                instance.getRoleHandler().getPlayerRoles().put(player, "Ghost");
            }
            player.teleport(instance.getLocationFile().getLocation("Game.Location." + instance.getRoleHandler().getPlayerRoles().get(player)));
        }


    }

    @Override
    public void stop() {

    }
}
