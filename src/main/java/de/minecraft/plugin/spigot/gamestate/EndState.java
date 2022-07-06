package de.minecraft.plugin.spigot.gamestate;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EndState extends GameState {

    private PacMan instance = PacMan.getInstance();

    /**
     * Will be executed upon changing the Gamestate to the Endstate
     */
    @Override
    public void start() {
        /**
         * TODO:
         * - Send Title/Message/Hotbarmessage to every Player (Won, Loss, Score, ?Overall rank?)
         */
        // Teleports every person on the Server to the location set as the lobby
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Teleports every player to the lobby spawn for the ending period
            player.teleport(instance.getLocationFile().getLocation("Game.Location.Lobby"));
        }
        // Resets the Role-Hashmap to prepare for the next game
        instance.getRoleHandler().getPlayerRoles().clear();

        instance.updateRankingInventory();
    }

    /**
     * Will be executed upon changing the Gamestate to another state
     */
    @Override
    public void stop() {
        // TODO: only if necessary
        Bukkit.getServer().shutdown();
    }
}
