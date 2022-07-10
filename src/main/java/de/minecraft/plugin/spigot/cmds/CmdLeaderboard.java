package de.minecraft.plugin.spigot.cmds;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdLeaderboard implements CommandExecutor {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird ausgeführt, wenn eine Person eine Nachricht schickt, welche mit "/" beginnt
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        // Überprüft, ob der Sender der Nachricht "/leaderboard ..." geschrieben hat
        if (!cmd.getName().equalsIgnoreCase("leaderboard")) {
            return true;
        }

        // Überprüft, ob der Sender ein Spieler ist (die Konsole kann auch Kommandos schicken)
        if (!(cs instanceof Player)) {

            // Sendet der Konsole eine Nachricht, dass nur ein Spieler das Kommando ausführen kann
            Bukkit.getConsoleSender().sendMessage(INSTANCE.getMessageFile().getValue("Commands.NoPlayer"));
            return true;
        }

        Player player = (Player) cs;

        // Überprüft, ob die Syntax des Commands Argumente enthält (z.B. /leaderboard xyz)
        if (args.length != 0) {

            // Sendet dem Spieler eine Nachricht, dass er die falsche Syntax benutzt hat
            player.sendMessage(INSTANCE.getMessageFile().getValue("Commands.Leaderboard.Syntax", player));
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
