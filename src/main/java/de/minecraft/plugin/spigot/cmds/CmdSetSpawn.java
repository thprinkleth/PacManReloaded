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
            Bukkit.getConsoleSender().sendMessage(instance.getMessageFile().getValue("Commands.NoPlayer").toString());
            return true;
        }

        Player player = (Player) cs;

        // Looks if the player has the permission to run the command
        if (!player.hasPermission("commands.setup.setspawn")) {
            player.sendMessage(instance.getMessageFile().getValue("Commands.NoPerm", player).toString());
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(instance.getMessageFile().getValue("Commands.SetSpawn.Syntax", player).toString());
        }

        switch (args[0]) {
            case "lobby":
                // Saves the location where the player is currently at in the file
                instance.getLocationFile().setLocation("Spawn.SpawnPoint.AllPlayers", player.getLocation());
                // Sends a message to the player who executed the command
                player.sendMessage(instance.getMessageFile().getValue("Commands.SetSpawn.AllPlayers.Success", player).toString());
                break;
            case "ghosts":
                instance.getLocationFile().setLocation("Spawn.SpawnPoint.Ghosts", player.getLocation());
                player.sendMessage(instance.getMessageFile().getValue("Commands.SetSpawn.Ghosts.Success", player).toString());
                break;
            case "pacman":
                instance.getLocationFile().setLocation("Spawn.SpawnPoint.PacMan", player.getLocation());
                player.sendMessage(instance.getMessageFile().getValue("Commands.SetSpawn.PacMan.Success", player).toString());
                break;
        }

        return false;
    }
}
