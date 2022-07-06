package de.minecraft.plugin.spigot.cmds;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdRanking implements CommandExecutor {

    private PacMan instance = PacMan.getInstance();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        // Looks if the command executor writes "/ranking"
        if (!cmd.getName().equalsIgnoreCase("rankings")) {
            return true;
        }

        // Looks if the command executor is a Player
        if (cs instanceof Player) {
            // Sends the console a message that the command executor has to be a player
            Bukkit.getConsoleSender().sendMessage(instance.getMessageFile().getValue("Commands.NoPlayer").toString());
            return true;
        }

        // Defines a new object of the Player as the command executor (tested it before so it won't throw a exception)
        Player player = (Player) cs;

        // Looks if the syntax of the command is really "/setup" and not "/setup <arguments>"
        if (args.length != 0) {
            // Sends the player a message if he wrote the wrong syntax
            player.sendMessage(instance.getMessageFile().getValue("Commands.Ranking.Syntax", player).toString());
            return true;
        }

        /**
         * TODO:
         * - Opens Inventory
         * - Top 3 Players
         *      - Heads of the player as item
         *      - Itemname: Playername
         *      - Lore: max score, Wins (Ghost + Pacman), Loses (Ghost + Pacman)
         */

        return false;
    }
}
