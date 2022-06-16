package de.minecraft.plugin.spigot.listeners;


import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    private PacMan instance = PacMan.getInstance();
    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        Player player = event.getPlayer();
        event.setQuitMessage((String)instance.getMessageFile().getValue("World.Quit", player));
    }
}
