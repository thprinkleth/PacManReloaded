package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
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

        // Teleports the player directly after joining the server
        player.teleport(instance.getLocationFile().getLocation("Spawn.SpawnPoint.AllPlayers"));
        // Sets the message which will be sent to the chat
        event.setJoinMessage((String) instance.getMessageFile().getValue("World.Join", player));
    }
}
