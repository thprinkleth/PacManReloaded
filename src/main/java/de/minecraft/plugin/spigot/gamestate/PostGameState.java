package de.minecraft.plugin.spigot.gamestate;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.util.BlockSetter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PostGameState extends GameState {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird bei Wechsel des Gamestatuses auf PostGame ausgeführt
    @Override
    public void start() {

        /**
         * TODO:
         * - Send Title/Message/Hotbarmessage to every Player (Won, Loss, Score, ?Overall rank?)
         */

        // Geht jeden Spieler durch, der sich gerade auf dem Server befindet
        for (Player player : Bukkit.getOnlinePlayers()) {

            // Setzt das Inventar von dem Spieler zurück
            player.getInventory().clear();

            // Setzt die Leben des Spielers zurück
            player.setMaxHealth(20);
            player.setHealthScale(20);
            player.setHealth(20);

            // Teleportiert den Spieler an die Position, an der sie bei Betreten des Servers erschienen sind.
            player.teleport(INSTANCE.getLocationFile().getSpawn("Game.Location.Lobby"));

            // Geht jeden Effekt durch, den der Spieler hat
            for (PotionEffect potion : player.getActivePotionEffects()) {

                // Entfernt den Effekt von dem Spieler
                player.removePotionEffect(potion.getType());
            }

            // Gibt dem Spieler Effekte, dass er nicht springen kann, dass er für drei Sekunden nicht bewegen kann und nichts sieht
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 200));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 200));

            // Führt "stop()" nach 120 Sekunden aus
            Bukkit.getScheduler().runTaskLater(INSTANCE, new Runnable() {
                @Override
                public void run() {
                    stop();
                }
            }, 120 * 20);
        }
    }

    // Wird bei Wechsel des Gamestatuses auf einen anderen ausgeführt
    @Override
    public void stop() {

        // Schließt den Server
        Bukkit.getServer().shutdown();
    }
}
