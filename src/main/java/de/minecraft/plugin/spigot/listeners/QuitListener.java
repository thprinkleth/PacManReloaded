package de.minecraft.plugin.spigot.listeners;


import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.gamestate.GameState;
import de.minecraft.plugin.spigot.gamestate.IngameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird ausgeführt, wenn der Spieler den Server verlässt
    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        // Speichert den Spieler, welcher den Server verlassen hat
        Player player = event.getPlayer();

        // Sends every person on the server a specific message when the player leaves
        event.setQuitMessage(INSTANCE.getMessageFile().getValue("World.Quit", player, INSTANCE.getPlayerList().size() - 1));

        // Überprüft, ob der derzeitge Spielstatus Ingame ist
        if (INSTANCE.getGameStateManager().getCurrent() instanceof IngameState) {

            // Setzt den neuen Spielstatus auf PostGame -> Spiel beendet
            INSTANCE.getGameStateManager().setCurrent(GameState.POSTGAME_STATE);

            // TODO: MySQL lose and everyone else win...

            INSTANCE.getMySQL().addLosesGhost(player);
            INSTANCE.getMySQL().addLosesPacMan(player);

            for (Player current : Bukkit.getOnlinePlayers()) {
                if (current != player) {
                    INSTANCE.getMySQL().addWinsGhost(current);
                    INSTANCE.getMySQL().addWinsPacMan(current);
                }
            }
            return;
        }

        // Entfernt den Spieler von der Spielerliste
        INSTANCE.removeFromPlayerList(player);
    }
}
