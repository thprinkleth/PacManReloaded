package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.gamestate.GameState;
import de.minecraft.plugin.spigot.gamestate.IngameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageListener implements Listener {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird ausgeführt, wenn ein Objekt von einem anderen Objekt Schaden bekommt
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        // Überprüft, ob das Objekt kein Spieler ist
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        // Negiert den erlittenen Schaden
        event.setCancelled(true);

        // Überprüft, ob der derzeitige Spielstatus nicht Ingame ist
        if (!(INSTANCE.getGameStateManager().getCurrent() instanceof IngameState)) {
            return;
        }

        // Speichert den Spieler, der Schaden erleidet
        Player damager = ((Player) event.getDamager());
        // Speichert den Spieler, der Schaden zufügt
        Player player = ((Player) event.getEntity());

        // Speichert die Rolle des Spielers, der Schaden macht
        String roleDamager = INSTANCE.getRoleHandler().getPlayerRoles().get(damager);
        // Speichert die Rolle des Spielers, der Schaden erleidet
        String rolePlayer = INSTANCE.getRoleHandler().getPlayerRoles().get(player);

        // Überprüft, ob der Spieler, der Schaden zugefügt hat, ein Geist ist
        if (roleDamager.equalsIgnoreCase("Ghost")) {

            // Überprüft, ob der Spieler, der Schaden bekommen hat, ein Geist ist
            if (rolePlayer.equalsIgnoreCase("Ghost")) {
                return;
            }

            // Überprüft, ob das PowerUp mit der ID 0 (Unsterblichkeit) aktiviert ist
            if (INSTANCE.getPowerUpHandler().getPowerUpList()[0]) {
                return;
            }

            INSTANCE.getMySQL().addPacmanEaten(damager.getUniqueId().toString());

            // Überprüft, ob der Spieler (PacMan) nur noch ein Leben hat
            if (player.getHealth() <= 2) {

                // Setzt den Spielstatus auf PostGame
                INSTANCE.getGameStateManager().setCurrent(GameState.POSTGAME_STATE);

                // Geht jeden Spieler, der als Spieler eingetragen ist, durch
                for (Player current : INSTANCE.getPlayerList()) {

                    // Überprüft, ob der Spieler PacMan ist
                    if (INSTANCE.getRoleHandler().getPlayerRoles().get(current).equalsIgnoreCase("PacMan")) {

                        // Rechnet ihm ein verlorenes Spiel an
                        INSTANCE.getMySQL().addLosesPacMan(current.getUniqueId().toString());

                    } else {

                        // Rechnet den Geistern ein gewonnenes Spiel an
                        INSTANCE.getMySQL().addWinsGhost(current.getUniqueId().toString());
                    }
                }

            } else {

                // Nimmt ein Leben von dem Spieler (PacMan)
                player.setHealth(player.getHealth() - 2);

                // Geht jeden Spieler durch, der in der Spielerliste eingetragen ist
                for (Player current : INSTANCE.getPlayerList()) {

                    // Teleportiert jeden Spieler zu seiner rechtmäßigen Position, an der er, rollenabhängig, erschienen soll
                    current.teleport(INSTANCE.getLocationFile().getSpawn("Game.Location." + INSTANCE.getRoleHandler().getPlayerRoles().get(current)));

                    // Gibt dem Spieler die Effekte, dass er für zwei Sekunden blind ist und sich nicht bwegen kann
                    current.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 200));
                    current.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2 * 20, 200));
                }
            }
        // Überprüft, ob der Spieler, der Schaden zugefügt hat, PacMan ist
        } else if (roleDamager.equalsIgnoreCase("PacMan")) {

            // Überprüft, ob das PowerUp mit der ID 1 (Geister essen) deaktiviert ist
            if (!INSTANCE.getPowerUpHandler().getPowerUpList()[1]) {
                return;
            }

            // Teleportiert den Spieler, der Schaden bekommen hat, an seine rechtmäßige Position
            player.teleport(INSTANCE.getLocationFile().getSpawn("Game.Location." + INSTANCE.getRoleHandler().getPlayerRoles().get(player)));

            // Gibt dem Spieler den Effekt, dass er sich für eine gewisse Zeit, welche Levelabhängig geregelt wird, nicht bewegen kann
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, INSTANCE.getPowerUpHandler().getDuration(false), 200));
        }
    }
}
