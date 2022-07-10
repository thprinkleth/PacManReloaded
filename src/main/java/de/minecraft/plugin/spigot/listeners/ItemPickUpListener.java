package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.gamestate.GameState;
import de.minecraft.plugin.spigot.gamestate.IngameState;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemPickUpListener implements Listener {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird ausgeführt, wenn ein Spieler ein Item aufhebt
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {

        // Speichert den Spieler, welcher das Item aufheben würde
        Player player = event.getPlayer();

        // Bricht das Event ab, sodass man keine Items aufheben kann
        event.setCancelled(true);

        // Überprüft, ob es keine Spieler mit Rollen gibt
        if (INSTANCE.getRoleHandler().getPlayerRoles().isEmpty()) {
            return;
        }

        // Überprüft, ob der Spieler ein Geist ist
        if (INSTANCE.getRoleHandler().getPlayerRoles().get(player).equalsIgnoreCase("Ghost")) {
            return;
        }

        if (!(INSTANCE.getGameStateManager().getCurrent() instanceof IngameState)) {
            return;
        }

        // Speichert das Item, welches aufgehoben werden würde
        Item item = event.getItem();

        // Überprüft, ob das Item ein Coin ist
        if (item.getItemStack().getType() == INSTANCE.getPickupableItemStacks().coinItemStack().getType()) {

            // Fügt dem Spieler, abhängig, ob das "Doppelt Coin"-PowerUp aktiviert ist, ein oder zwei Einheiten zu seiner Punktzahl hinzu
            INSTANCE.getScoreHandler().addScore(INSTANCE.getPowerUpHandler().getPowerUpList()[4]);

            // Überprüft, die derzeitige Punktzahl durch die Coin-Anzahl auf der Welt teilbar ist -> alle Coins eingesammelt
            if (INSTANCE.getScoreHandler().getScore() % INSTANCE.getConfigFile().getIntValue("Game.Amount.Locations.Coins") == 0) {

                // Überprüft, ob das derzeitige Level das maximale Level ist
                if (INSTANCE.getPowerUpHandler().getLevel() == 3) {

                    // Setzt den Spielstatus auf PostGame
                    INSTANCE.getGameStateManager().setCurrent(GameState.POSTGAME_STATE);

                    // Geht jeden Spieler, der als Spieler eingetragen ist, durch
                    for (Player current : INSTANCE.getPlayerList()) {

                        // Überprüft, ob der Spieler PacMan ist
                        if (INSTANCE.getRoleHandler().getPlayerRoles().get(current).equalsIgnoreCase("PacMan")) {

                            // Rechnet ihm ein verlorenes Spiel an
                            INSTANCE.getMySQL().addWinsPacMan(current.getUniqueId().toString());

                        } else {

                            // Rechnet den Geistern ein gewonnenes Spiel an
                            INSTANCE.getMySQL().addLosesGhost(current.getUniqueId().toString());
                        }
                    }

                    return;
                }

                // Initialisiert das nächste Level
                INSTANCE.getPowerUpHandler().addLevel();
                INSTANCE.getGameStateManager().setCurrent(GameState.INGAME_STATE);
            }

            // Geht jeden Spieler, welcher als Spieler eingetragen ist, durch
            for (Player current : INSTANCE.getPlayerList()) {

                // Setzt das Level von dem Spieler gleich mit dem Level des Spieles
                current.setLevel(INSTANCE.getPowerUpHandler().getLevel());

                // Speichert den Fortschritt in Prozent in einer Variable
                float progress = ((float) INSTANCE.getScoreHandler().getScore() / (float) INSTANCE.getConfigFile().getIntValue("Game.Amount.Locations.Coins"));

                // Setzt die XP-Leiste des Spielers entsprechend dem Fortschritt in dem jeweiligen Level
                current.setExp(progress);
            }

            // Entfernt die Blöcke, sodass der Spieler auf der Karte sieht, dass der Coin bereits eingesammelt wurde
            INSTANCE.getCoinDotHandler().deleteCoinDot(item.getLocation());

        } else if (item.getItemStack().getType() == INSTANCE.getPickupableItemStacks().invincibilityPowerUpItemStack().getType()) {

            // Aktiviert das eingesammelte PowerUp
            activatePowerUp(0, false);

            // Entfernt die Blöcke, sodass der Spieler auf der Karte sieht, dass das PowerUp bereits eingesammelt wurde
            INSTANCE.getPowerUpDotHandler().deleteDotOnMap(item.getLocation());

        } else if (item.getItemStack().getType() == INSTANCE.getPickupableItemStacks().eatingGhostPowerUpItemStack().getType()) {

            // Aktiviert das eingesammelte PowerUp
            activatePowerUp(1, false);

            // Entfernt die Blöcke, sodass der Spieler auf der Karte sieht, dass das PowerUp bereits eingesammelt wurde
            INSTANCE.getPowerUpDotHandler().deleteDotOnMap(item.getLocation());

        } else if (item.getItemStack().getType() == INSTANCE.getPickupableItemStacks().speedPowerUpItemStack().getType()) {

            // Aktiviert das eingesammelte PowerUp
            activatePowerUp(2, false);

            // Setzt den Wert der Differenz des Effektes für PacMan gegenüber der Geister
            int amplifier = 2;

            // Überprüft, ob der Spieler schon den Effekt der Schnelligkeit hat
            if (player.hasPotionEffect(PotionEffectType.SPEED)) {

                // Addiert zu der jetzigen Stärke des Effektes die Differenz, welche oben festgelegt wurde
                amplifier += player.getPotionEffect(PotionEffectType.SPEED).getAmplifier();
            }

            // Gibt dem Geist die Effekte, dass er sich für eine gewisse Zeit, welche abhängig vom derzeitigen Level ist, schneller bewegen kann
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, INSTANCE.getPowerUpHandler().getDuration(false), amplifier));

            // Entfernt die Blöcke, sodass der Spieler auf der Karte sieht, dass das PowerUp bereits eingesammelt wurde
            INSTANCE.getPowerUpDotHandler().deleteDotOnMap(item.getLocation());

        } else if (item.getItemStack().getType() == INSTANCE.getPickupableItemStacks().freezeGhostPowerUpItemStack().getType()) {

            // Aktiviert das eingesammelte PowerUp
            activatePowerUp(3, false);

            // Geht jeden Spieler, welcher als Spieler eingetragen ist, durch
            for (Player current : INSTANCE.getPlayerList()) {

                // Überprüft, ob der Spieler ein Geist ist
                if (INSTANCE.getRoleHandler().getPlayerRoles().get(current).equalsIgnoreCase("Ghost")) {

                    // Gibt dem Geist die Effekte, dass er sich für eine gewisse Zeit, welche abhängig vom derzeitigen Level ist, nicht bewegen kann und nichts sieht
                    current.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, INSTANCE.getPowerUpHandler().getDuration(false), 200));
                    current.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, INSTANCE.getPowerUpHandler().getDuration(false), 200));
                }
            }

            // Entfernt die Blöcke, sodass der Spieler auf der Karte sieht, dass das PowerUp bereits eingesammelt wurde
            INSTANCE.getPowerUpDotHandler().deleteDotOnMap(item.getLocation());

        } else if (item.getItemStack().getType() == INSTANCE.getPickupableItemStacks().doubleScorePowerUpItemStack().getType()) {

            // Aktiviert das eingesammelte PowerUp
            activatePowerUp(4, true);

            // Entfernt die Blöcke, sodass der Spieler auf der Karte sieht, dass das PowerUp bereits eingesammelt wurde
            INSTANCE.getPowerUpDotHandler().deleteDotOnMap(item.getLocation());

        } else if (item.getItemStack().getType() == INSTANCE.getPickupableItemStacks().extraLifePowerUpItemStack().getType()) {

            // Überprüft, ob der Spieler noch ein Leben dazubekommnen kann
            if (player.getHealth() < player.getMaxHealth()) {

                // Gibt dem Spieler ein zusätzliches Leben
                player.setHealth(player.getHealth() + 2);
            }

            // Entfernt die Blöcke, sodass der Spieler auf der Karte sieht, dass das PowerUp bereits eingesammelt wurde
            INSTANCE.getPowerUpDotHandler().deleteDotOnMap(item.getLocation());
        }

        // Spielt ein Ton bei dem Spieler ab, dass er ein Item aufgehoben hat
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);

        // Löscht das Item aus der Welt
        event.getItem().remove();
    }

    private void activatePowerUp(int id, boolean doubleScore) {

        // Aktiviert das PowerUp
        INSTANCE.getPowerUpHandler().activatePowerUp(id);

        // Erzeugt einen Auftrag, welcher, abhängig von dem derzeitigen Level, eine gewisse Zeit später ausgeführt wird
        Bukkit.getScheduler().runTaskLaterAsynchronously(INSTANCE, () -> {

            // Deaktiviert das PowerUp
            INSTANCE.getPowerUpHandler().deactivatePowerUp(id);

        }, INSTANCE.getPowerUpHandler().getDuration(doubleScore));
    }
}
