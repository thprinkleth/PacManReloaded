package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    PacMan instance = PacMan.getInstance();

    /**
     * Runs upon a player joining the server
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        // Sets the message which will be sent to the chat
        event.setJoinMessage((String) instance.getMessageFile().getValue("World.Join", player));

        // Teleports the player directly after joining the server
        try {
            player.teleport(instance.getLocationFile().getLocation("Game.Location.Lobby"));
        } catch (NullPointerException ex) {
        }
    }
}
