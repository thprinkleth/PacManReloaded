package de.minecraft.plugin.spigot.cmds;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.gamestate.GameState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdStart implements CommandExecutor {

    PacMan instance = PacMan.getInstance();
    int cooldownScheduler = 0;

    // Is executed once a person sends a message which starts with "/"
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {

        // Looks if the command executor writes "/start"
        if (!cmd.getName().equalsIgnoreCase("start")) {
            return true;
        }

        // Looks if the command executor is a Player
        if (!(cs instanceof Player)) {
            // Sends the console a message that the command executor has to be a player
            Bukkit.getConsoleSender().sendMessage(instance.getMessageFile().getValue("Commands.NoPlayer").toString());
            return true;
        }

        // Defines a new object of the Player as the command executor (tested it before so it won't throw a exception)
        Player player = ((Player) cs);

        // Looks if the player is allowed to perform the command
        if (player.hasPermission("commands.start")) {
            // Sends the player a message if he's not allowed to send the command
            player.sendMessage(instance.getMessageFile().getValue("Commands.NoPerm", player).toString());
            return true;
        }

        // Looks if the syntax of the command is really "/setup" and not "/setup <arguments>"
        if (args.length != 0) {
            // Sends the player a message if he wrote the wrong syntax
            player.sendMessage(instance.getMessageFile().getValue("Commands.Start.Syntax", player).toString());
            return true;
        }

        // Creates a repeating task, in which the method "run()" will be executed every 20 gameticks, or once every second
        cooldownScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {

            int countdown = 60 * 2;
            int pastCountdown = 0;

            @Override
            public void run() {
                // Will be true every minute, at 30 seconds left, 20 seconds left, 15 seocnds left, 10 seconds left, 5 seconds left, 3 seconds left, 3 seconds left, 2 seconds left and 1 second left
                if (pastCountdown % 60 == 0 || pastCountdown == countdown - 30 || pastCountdown == countdown - 20 || pastCountdown == countdown - 15 || pastCountdown == countdown - 10 ||
                        pastCountdown == countdown - 5 || pastCountdown == countdown - 4 || pastCountdown == countdown - 3 || pastCountdown == countdown - 2 || pastCountdown == countdown - 1) {

                    // Shows if there are enough players on the server to start the game
                    if (Bukkit.getServer().getOnlinePlayers().size() < (int) instance.getMessageFile().getValue("Game.PlayersNeededToStart")) {
                        // Sends the player a message if there aren't enough players online
                        player.sendMessage(instance.getMessageFile().getValue("Lobby.Countdown.NotEnoughPlayers", player).toString());
                        // Cancels the task so no computer resources wasted
                        Bukkit.getScheduler().cancelTask(cooldownScheduler);
                        return;
                    }

                    // Sends every player on the server a message which contains the remaining time until the server starts
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(instance.getMessageFile().getValue("Lobby.Countdown.Counting", player, countdown - pastCountdown).toString());
                    }

                    pastCountdown++;

                // Will be executed once the cooldown runs out -> the game starts
                } else if (pastCountdown == countdown) {
                    // Cancels the task so no computer resources are wasted
                    Bukkit.getScheduler().cancelTask(cooldownScheduler);
                    // Changes the current Gamestate to Ingame
                    instance.getGameStateManager().setCurrent(GameState.INGAME_STATE);
                }
            }
        }, 0, 20);

        return false;
    }
}
