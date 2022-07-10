package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.gamestate.GameState;
import de.minecraft.plugin.spigot.gamestate.PreGameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    // Wird ausgeführt, kurz bevor ein Spieler dem Server beitritt
    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event){

        // Speichert den derzeitigen Spielstatus
        GameState current = PacMan.getInstance().getGameStateManager().getCurrent();

        // Überprüft, ob der derzeitige Spielstatus nicht vor dem Spiel ist
        if(!(current instanceof PreGameState)) {

            // Verweigert der Person, der versucht auf den Server zu gehen, den Zutritt
            event.disallow(null, "");
        }
    }
}
