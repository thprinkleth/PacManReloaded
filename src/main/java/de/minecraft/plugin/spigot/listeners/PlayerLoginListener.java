package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.gamestate.GameState;
import de.minecraft.plugin.spigot.gamestate.LobbyState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    // Runs upon the player login in into the server
    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event){
        GameState current = PacMan.getInstance().getGameStateManager().getCurrent();
        if(!(current instanceof LobbyState)) {
            // Disallows the player to login into the server when the game is running or just ended
            event.disallow(null, "");
        }
    }
}
