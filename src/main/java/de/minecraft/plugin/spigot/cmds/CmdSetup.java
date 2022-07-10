package de.minecraft.plugin.spigot.cmds;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdSetup implements CommandExecutor {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird ausgeführt, wenn eine Person eine Nachricht schickt, welche mit "/" beginnt
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {

        // Überprüft, ob der Sender der Nachricht "/setup ..." geschrieben hat
        if (!cmd.getName().equalsIgnoreCase("setup")) {
            return true;
        }

        // Überprüft, ob der Sender ein Spieler ist (die Konsole kann auch Kommandos schicken)
        if (!(cs instanceof Player)) {

            // Sendet der Konsole eine Nachricht, dass nur ein Spieler das Kommando ausführen kann
            Bukkit.getConsoleSender().sendMessage(INSTANCE.getMessageFile().getValue("Commands.NoPlayer"));
            return true;
        }

        Player player = (Player) cs;

        // Überprüft, ob der Spieler die nötigen Rechte hat, um den Befehl auszuführen
        if (!player.hasPermission("commands.setup")) {

            // Sendet dem Spieler eine Nachricht, dass er nicht genug Rechte hat
            player.sendMessage(INSTANCE.getMessageFile().getValue("Commands.NoPerm", player));
            return true;
        }

        // Überprüft, ob die Syntax des Commands Argumente enthält (z.B. /setup xyz)
        if (args.length != 0) {

            // Sendet dem Spieler eine Nachricht, dass er die falsche Syntax benutzt hat
            player.sendMessage(INSTANCE.getMessageFile().getValue("Commands.Setup.Syntax", player));
            return true;
        }

        // Speichert Name und Lore des Items ab, welches dem Spieler gleich in die Hand gegeben wird
        String setupItemName = INSTANCE.getConfigFile().getValue("Items.Setup.Name", player);
        String setupItemLore = INSTANCE.getConfigFile().getValue("Items.Setup.Lore", player);

        // Erzeugt ein neues Item
        ItemStack setupItem = new ItemBuilder(Material.FEATHER).setName(setupItemName).addLoreLine(setupItemLore).toItemStack();

        // Erzeugt ein neues Item
        ItemStack autoCoinSetup = new ItemBuilder(Material.STICK)
                .setName(INSTANCE.getConfigFile().getValue("Items.SetupCoin.Name"))
                .addLoreLine(INSTANCE.getConfigFile().getValue("Items.SetupCoin.Lore")).toItemStack();

        // Setzt dem Spieler das Item in die Hand
        player.getInventory().setItem(0, setupItem);
        player.getInventory().setItem(1, autoCoinSetup);

        return false;
    }
}
