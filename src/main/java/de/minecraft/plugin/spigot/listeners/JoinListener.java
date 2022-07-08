package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    PacMan instance = PacMan.getInstance();

    // Runs upon a player joining the server
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        // Adds the player to the playerlist, so the game-role will be assigned more easily
        instance.getPlayerList().add(player);

        // Sets the gamemode of the player to adventure: can't break/place blocks
        player.setGameMode(GameMode.ADVENTURE);

        // Sends every person on the server a specific message when the player joins
        event.setJoinMessage((String) instance.getMessageFile().getValue("World.Join", player));
        try {
            // Teleports the player directly after joining the server
            player.teleport(instance.getLocationFile().getSpawn("Game.Location.Lobby"));
        } catch (NullPointerException ex) {
        }
    }
}
