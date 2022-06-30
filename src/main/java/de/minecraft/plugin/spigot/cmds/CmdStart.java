package de.minecraft.plugin.spigot.cmds;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdStart implements CommandExecutor {

    PacMan instance = PacMan.getInstance();
    int cooldownScheduler = 0;

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {

        if (!cmd.getName().equalsIgnoreCase("start")) {
            return true;
        }

        if (!(cs instanceof Player)) {
            // TODO: Ausgabe
            return true;
        }

        Player player = ((Player) cs);

        if (player.hasPermission("")) { //TODO: Permission
            // TODO: Ausgabe
            return true;
        }

        if (args.length != 0) {
            //TODO: Ausgabe
            return true;
        }

        int onlinePlayersAmount = Bukkit.getServer().getOnlinePlayers().size();

        if (onlinePlayersAmount < 5) {
            //TODO: Ausgabe
            return true;
        }

        cooldownScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {

            int countdown = 60 * 5;
            int pastCountdown = 0;

            @Override
            public void run() {
                if (pastCountdown % 60 == 0 || pastCountdown == countdown - 30 || pastCountdown == countdown - 20 || pastCountdown == countdown - 15 || pastCountdown == countdown - 10 ||
                        pastCountdown == countdown - 5 || pastCountdown == countdown - 4 || pastCountdown == countdown - 3 || pastCountdown == countdown - 2 || pastCountdown == countdown - 1) {

                    // Sends every person on the server the message at every minute, 30 seconds, 20 seconds, 15 seconds, 10 seconds, 5 seconds, 4 seconds, 3 seconds, 2 seconds and 1 seconds
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(instance.getMessageFile().getValue("Lobby.Cooldown.Counting", player, pastCountdown / 60).toString());
                    }
                    pastCountdown++;
                } else if (pastCountdown == countdown) {
                    Bukkit.getScheduler().cancelTask(cooldownScheduler);
                    // TODO: Start game (GameState change Lobby -> Ingame)


                }
            }
        }, 0, 20);

        return false;
    }
}
