package de.minecraft.plugin.spigot.gamestate;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.util.BlockSetter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class IngameState extends GameState {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird bei Wechsel des Gamestatuses auf Ingame ausgeführt
    @Override
    public void start() {

        // Geht jedes Objekt in einer Kugel, ausgehend von der Position, an der Geister erscheinen werden, durch
        for (Entity entity : Bukkit.getWorld("world").getNearbyEntities(INSTANCE.getLocationFile().getLocation("Game.Location.Ghost"), 50, 50, 50)) {

            // Überprüft, ob das Objekt kein Spieler ist
            if (!(entity instanceof Player)) {

                // Löscht das Objekt
                entity.remove();
            } else {
                ((Player) entity).setGameMode(GameMode.ADVENTURE);
                if (!INSTANCE.getPlayerList().contains(((Player) entity))) {
                    INSTANCE.getPlayerList().add((Player) entity);
                }
            }
        }

        // Erzeugt die Items, welche PacMan aufheben kann, und die damit verbundene Logik, dass man diese auf der Karte sehen kann
        INSTANCE.getScoreHandler().spawnCoins();
        INSTANCE.getPowerUpHandler().spawnPowerUps();
        INSTANCE.getCoinDotHandler().createCoinDots();
        INSTANCE.getPowerUpDotHandler().createPowerUpDots();


        // Überprüft, ob das Spiel bei Level 0 ist
        if (INSTANCE.getPowerUpHandler().getLevel() == 0) {

            // Speichert die Menge an Spielern
            int playerAmount = INSTANCE.getPlayerList().size();

            // Wählt einen zufälligen Spieler als PacMan aus
            Random random = new Random();
            int pacManNumberPlayer = random.nextInt(playerAmount);
            Player pacMan = INSTANCE.getPlayerList().get(pacManNumberPlayer);
            INSTANCE.getRoleHandler().getPlayerRoles().put(pacMan, "PacMan");

            // Setzt die maximale Anzahl an Leben, die PacMan haben könnte
            pacMan.setHealthScale(INSTANCE.getPowerUpHandler().getMaxLifes());
            pacMan.setMaxHealth(INSTANCE.getPowerUpHandler().getMaxLifes());

            // Setzt die anfängliche Anzahl an Leben, die PacMan hat
            pacMan.setHealth(2);

            MapView map = INSTANCE.getServer().createMap(pacMan.getWorld());
            map.setCenterX(INSTANCE.getLocationFile().getLocation("Game.Location.Ghost").getBlockX());
            map.setCenterZ(INSTANCE.getLocationFile().getLocation("Game.Location.Ghost").getBlockZ());
            map.setScale(MapView.Scale.CLOSE);
            // Erzeugt die Karte und setzt sie PacMan in die linke Hand
            pacMan.getInventory().setItemInOffHand(new ItemStack(Material.MAP));
            pacMan.sendMap(map);

            // Erzeugt einen sich wiederholenden Auftrag, welcher jede viertel Sekunde aufgerufen wird
            Bukkit.getScheduler().scheduleAsyncRepeatingTask(INSTANCE, new Runnable() {
                @Override
                public void run() {

                    // Geht jeden Spieler, der als Spieler eingetragen ist, durch
                    for (Player player : INSTANCE.getPlayerList()) {

                        // Überprüft, ob der Spieler PacMan ist
                        if (player == pacMan) {

                            // Schickt dem Spieler die Nachricht an die Actionbar (Schriftzug über Toolbar), was für einen Score er hat
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(INSTANCE.getMessageFile().getValue("Game.Score.PacMan", player, INSTANCE.getScoreHandler().getScore())));

                        } else {

                            // Schickt dem Spieler die Nachricht an die Actionbar (Schriftzug über Toolbar), was für einen Score PacMan schon erreicht hat
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(INSTANCE.getMessageFile().getValue("Game.Score.Ghost", player, INSTANCE.getScoreHandler().getScore())));
                        }
                    }
                }
            }, 0, 5);

            // Geht jeden Spieler, der als Spieler eingetragen ist, durch
            for (Player player : INSTANCE.getPlayerList()) {

                // Überprüft, ob der Spieler nicht schon PacMan ist
                if (!INSTANCE.getRoleHandler().getPlayerRoles().containsKey(player)) {

                    // Gibt dem Spieler die Rolle als Geist
                    INSTANCE.getRoleHandler().getPlayerRoles().put(player, "Ghost");
                }

                // Erzeugt einen synchronen Auftrag
                Bukkit.getScheduler().runTask(INSTANCE, () -> {

                    // Gibt dem Spieler die Effekte, dass er nicht springen kann und in Dunkelheit was sieht
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 251));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));

                    // Teleportiert jeden Spieler zu seiner rechtmäßigen Position, an der er, rollenabhängig, erschienen soll
                    player.teleport(INSTANCE.getLocationFile().getSpawn("Game.Location." + INSTANCE.getRoleHandler().getPlayerRoles().get(player)));
                });

                INSTANCE.getMySQL().addGame(player.getUniqueId().toString());
            }
        } else {

            // Geht jeden Spieler, der als Spieler eingetragen ist, durch
            for (Player player : INSTANCE.getPlayerList()) {

                // Spielt für den Spieler den Ton, dass er ein Level aufgestiegen ist
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

                // Erzeugt einen Titel und einen Untertitel, der groß, mittig auf dem Bildschirm angezeigt wird
                String title = INSTANCE.getMessageFile().getValue("Game.NextLevel." + INSTANCE.getRoleHandler().getPlayerRoles().get(player) + ".Title", player, INSTANCE.getPowerUpHandler().getLevel() + 1);
                String subTitle = INSTANCE.getMessageFile().getValue("Game.NextLevel." + INSTANCE.getRoleHandler().getPlayerRoles().get(player) + ".SubTitle", player, INSTANCE.getPowerUpHandler().getLevel() + 1);
                player.sendTitle(title, subTitle);

                // Geht jeden Effekt durch, den der Spieler hat
                for (PotionEffect potion : player.getActivePotionEffects()) {

                    // Entfernt den Effekt von dem Spieler
                    player.removePotionEffect(potion.getType());
                }

                // Gibt dem Spieler Effekte, dass er nicht springen kann, in der Dunkelheit sieht, Schnelligkeit in Abhängigkeit des derzeitigen Levels hat, und für drei Sekunden nicht bewegen kann und nichts sieht
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 200));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 200));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 251));
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, INSTANCE.getPowerUpHandler().getLevel()));

                // Überprüft, ob der Spieler die Rolle des PacMans spielt
                if (INSTANCE.getRoleHandler().getPlayerRoles().get(player).equalsIgnoreCase("PacMan")) {

                    // Setzt die maximale Anzahl an Leben, die PacMan haben könnte
                    player.setHealthScale(INSTANCE.getPowerUpHandler().getMaxLifes());
                    player.setMaxHealth(INSTANCE.getPowerUpHandler().getMaxLifes());
                }

                // Erzeugt einen Auftrag, welcher eine drei-viertel Sekunde später ausgeführt wird
                Bukkit.getScheduler().runTaskLater(INSTANCE, new BukkitRunnable() {
                    @Override
                    public void run() {

                        // Teleportiert jeden Spieler zu seiner rechtmäßigen Position, an der er, rollenabhängig, erschienen soll
                        player.teleport(INSTANCE.getLocationFile().getSpawn("Game.Location." + INSTANCE.getRoleHandler().getPlayerRoles().get(player)));
                    }
                }, 15);
            }
        }
    }

    // Wird bei Wechsel des Gamestatuses auf einen anderen ausgeführt
    @Override
    public void stop() {

        int y = INSTANCE.getLocationFile().getLocation("Game.Location.Ghost").getBlockY() + 11;

        for (int x = INSTANCE.getLocationFile().getLocation("Game.Location.Ghost").getBlockX() - 50; x < INSTANCE.getLocationFile().getLocation("Game.Location.Ghost").getBlockX() + 50; x++) {
            for (int z = INSTANCE.getLocationFile().getLocation("Game.Location.Ghost").getBlockZ() - 50; z < INSTANCE.getLocationFile().getLocation("Game.Location.Ghost").getBlockZ() + 50; z++) {

                Location loc = new Location(INSTANCE.getLocationFile().getLocation("Game.Location.Ghost").getWorld(), x, y, z);
                Bukkit.getScheduler().runTask(INSTANCE, new BlockSetter(loc, Material.AIR));
            }
        }

    }
}
