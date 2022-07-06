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

    private PacMan instance = PacMan.getInstance();

    // Is executed once a person sends a message which starts with "/"
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {

        // Looks if the command executor writes "/setup"
        if (!cmd.getName().equalsIgnoreCase("setup")) {
            return true;
        }

        // Looks if the command executor is a Player
        if (!(cs instanceof Player)) {
            // Sends the console a message that the command executor has to be a player
            Bukkit.getConsoleSender().sendMessage(instance.getMessageFile().getValue("Commands.NoPlayer").toString());
            return true     ;
        }

        // Defines a new object of the Player as the command executor (tested it before so it won't throw a exception)
        Player player = (Player) cs;

        // Looks if the player is allowed to perform the command
        if (!player.hasPermission("commands.setup")) {
            // Sends the player a message if he's not allowed to send the command
            player.sendMessage(instance.getMessageFile().getValue("Commands.NoPerm", player).toString());
            return true;
        }

        // Looks if the syntax of the command is really "/setup" and not "/setup <arguments>"
        if (args.length != 0) {
            // Sends the player a message if he wrote the wrong syntax
            player.sendMessage(instance.getMessageFile().getValue("Commands.Setup.Syntax", player).toString());
            return true;
        }

        // Defines variables with the name and lore of the item that will be put into the players hand once he performs the command
        String setupItemName = instance.getMessageFile().getValue("Items.Setup.Name", player).toString();
        String setupItemLore = instance.getMessageFile().getValue("Items.Setup.Lore", player).toString();

        // Creates the item
        ItemStack setupItem = new ItemBuilder(Material.FEATHER).setName(setupItemName).addLoreLine(setupItemLore).toItemStack();

        // Sets the player the item in the hand
        player.setItemInHand(setupItem);
        // Sends the player a message that the command was executed successfully
        player.sendMessage(instance.getMessageFile().getValue("Commands.Setup.ItemGiven", player).toString());

        return false;
    }
}
