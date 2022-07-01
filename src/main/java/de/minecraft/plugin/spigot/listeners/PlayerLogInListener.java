package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.gamestate.GameState;
import de.minecraft.plugin.spigot.gamestate.IngameState;
import de.minecraft.plugin.spigot.gamestate.LobbyState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLogInListener implements Listener {

    @EventHandler
    public void onPlayerLogInEvent(PlayerLoginEvent event){
        GameState current = PacMan.getInstance().getGameStateManager().getCurrent();
        if(!(current instanceof LobbyState)) {
            event.disallow(null, "");
        }
    }
}
