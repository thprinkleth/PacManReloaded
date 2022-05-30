package de.minecraft.plugin.spigot.cmds;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSetSpawn implements CommandExecutor {

    private PacMan instance = PacMan.getInstance();

    /**
     * Runs when someone executes a command (Syntax: "/<command> <args...>")
     */
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {

        // Looks if the command is the correct one
        if (!cmd.getName().equalsIgnoreCase("setSpawn")) {
            return true;
        }

        // Looks if the executor is a player (the console can run commands as well)
        if (!(cs instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage((String) instance.getMessageFile().getValue("Commands.NoPlayer"));
            return true;
        }

        Player player = (Player) cs;

        // Looks if the player has the permission to run the command
        if (!player.hasPermission("commands.setup.setspawn")) {
            player.sendMessage((String) instance.getMessageFile().getValue("Commands.NoPerm"));
            return true;
        }

        // Looks if the command which starts with "/setspawn" has any arguments
        if (args.length != 0) {
            player.sendMessage((String) instance.getMessageFile().getValue("Commands.SetSpawn.Syntax"));
            return true;
        }

        // Saves the location where the player is currently at in the file
        instance.getLocationFile().setLocation("Spawn.SpawnPoint.AllPlayers", player.getLocation());
        // Sends a message to the player who executed the command
        player.sendMessage((String) instance.getMessageFile().getValue("Commands.SetSpawn.Success", player));

        return false;
    }
}
