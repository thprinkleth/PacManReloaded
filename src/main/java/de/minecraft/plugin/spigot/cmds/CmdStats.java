package de.minecraft.plugin.spigot.cmds;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdStats implements CommandExecutor {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird ausgeführt, wenn eine Person eine Nachricht schickt, welche mit "/" beginnt
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        // Überprüft, ob der Sender der Nachricht "/stats ..." geschrieben hat
        if (!cmd.getName().equalsIgnoreCase("stats")) {
            return true;
        }

        // Überprüft, ob der Sender ein Player ist (die Konsole kann auch Kommandos schicken)
        if (!(cs instanceof Player)) {

            // Sendet der Konsole eine Nachricht, dass nur ein Spieler das Kommando ausführen kann
            Bukkit.getConsoleSender().sendMessage(INSTANCE.getMessageFile().getValue("Commands.NoPlayer"));
            return true;
        }

        Player player = (Player) cs;

        // Überprüft, ob die Syntax des Commands mehr als ein Argument hat (z.B. /stats xyz 123)
        if (args.length > 1) {

            // Sendet dem Spieler eine Nachricht, dass er die falsche Syntax benutzt hat
            player.sendMessage(INSTANCE.getMessageFile().getValue("Commands.Stats.Syntax", player));
            return true;
        }

        switch (args.length) {
            case 0:
                /**
                 * TODO:
                 * - Opens Inventory
                 * - Head of the player as item
                 * - Itemname: -> Messagefile
                 * - Lore: max score, Wins (Ghost + Pacman), Loses (Ghost + Pacman)
                 */
                break;
            case 1:
                /**
                 * TODO:
                 * - Opens Inventory
                 * - Head of the player as item
                 * - Itemname: Playername
                 * - Lore: max score, Wins (Ghost + Pacman), Loses (Ghost + Pacman)
                 */
                break;
        }
        return false;
    }
}
