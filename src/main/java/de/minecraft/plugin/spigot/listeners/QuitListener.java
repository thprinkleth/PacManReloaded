package de.minecraft.plugin.spigot.listeners;


import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    private PacMan instance = PacMan.getInstance();

    // Runs upon the player leaving the server
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        // Removes the player from the playerlist, so they won't be counted in into the choosing of the role after they quit
        instance.getPlayerList().remove(player);
        // Sends every person on the server a specific message when the player leaves
        event.setQuitMessage(instance.getMessageFile().getValue("World.Quit", player).toString());
    }
}
