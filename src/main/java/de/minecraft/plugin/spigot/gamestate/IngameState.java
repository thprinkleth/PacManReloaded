package de.minecraft.plugin.spigot.gamestate;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class IngameState extends GameState{

    private PacMan instance = PacMan.getInstance();

    @Override
    public void start() {
        if (instance.getPowerUpHandler().getLevel() == 0) {
            int playerAmount = Bukkit.getOnlinePlayers().size();
            Random random = new Random();
            int pacManNumberPlayer = random.nextInt(playerAmount);
            // Selects a random person of the people online and gives him the PacMan-Role
            instance.getRoleHandler().getPlayerRoles().put(instance.getPlayerList().get(pacManNumberPlayer), "PacMan");
            instance.getPlayerList().get(pacManNumberPlayer).setHealthScale(6);
            instance.getPlayerList().get(pacManNumberPlayer).setHealth(2);

            for (Player player : Bukkit.getOnlinePlayers()) {
                // Gives the rest of the players online the Ghost role
                if (!instance.getRoleHandler().getPlayerRoles().containsKey(player)) {
                    instance.getRoleHandler().getPlayerRoles().put(player, "Ghost");
                    player.setHealthScale(2);
                    player.setHealth(2);
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250));
                spawnPoints();
                spawnPowerUps();
                // Teleports every player to their specific spawn location in the arena (ghosts to the ghost-spawn, PacMan to the PacMan-spawn
                player.teleport(instance.getLocationFile().getSpawn("Game.Location." + instance.getRoleHandler().getPlayerRoles().get(player)));
                instance.getMySQL().addGame(player.getUniqueId().toString());
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                String title = instance.getMessageFile().getValue("Game.NextLevel." + instance.getRoleHandler().getPlayerRoles().get(player) + ".Title", player, instance.getPowerUpHandler().getLevel() + 1).toString();
                String subTitle = instance.getMessageFile().getValue("Game.NextLevel." + instance.getRoleHandler().getPlayerRoles().get(player) + ".SubTitle", player, instance.getPowerUpHandler().getLevel() + 1).toString();
                player.sendTitle(title, subTitle);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 200));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 200));
                if (instance.getRoleHandler().getPlayerRoles().get(player).equalsIgnoreCase("PacMan")) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, instance.getPowerUpHandler().getLevel()));
                }
            }
            spawnPowerUps();
            spawnPoints();
            instance.getScoreHandler().resetPointsInWorld();
        }
    }

    @Override
    public void stop() {

    }

    private void spawnPowerUps() {
        for (int i = 0; i < (int) instance.getMessageFile().getValue("Game.Amount.Locations.PowerUps"); i++) {
            int powerUp = new Random().nextInt(6);
            instance.getLocationFile().getLocation("Game.Location.Point." + i).getWorld().dropItem(instance.getLocationFile().getSpawn("Game.Location.PowerUp." + i), instance.getPickupableItemStacks().getPowerUpItemStack(powerUp));
        }
    }

    private void spawnPoints() {
        for (int i = 0; i < (int) instance.getMessageFile().getValue("Game.Amount.Locations.Points"); i++) {
            instance.getLocationFile().getLocation("Game.Location.Point." + i).getWorld().dropItem(instance.getLocationFile().getSpawn("Game.Location.Point." + i), instance.getPickupableItemStacks().pointItemStack());
        }
    }
}
