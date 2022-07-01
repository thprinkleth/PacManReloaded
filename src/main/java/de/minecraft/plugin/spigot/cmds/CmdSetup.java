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

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {

        if (!cmd.getName().equalsIgnoreCase("setup")) {
            return true;
        }

        if (!(cs instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(instance.getMessageFile().getValue("Commands.NoPlayer").toString());
            return true     ;
        }

        Player player = (Player) cs;

        if (!player.hasPermission("commands.setup")) {
            player.sendMessage(instance.getMessageFile().getValue("Commands.NoPerm", player).toString());
            return true;
        }

        if (args.length != 0) {
            player.sendMessage(instance.getMessageFile().getValue("Commands.Setup.Syntax", player).toString());
            return true;
        }

        String setupItemName = instance.getMessageFile().getValue("Items.Setup.Name", player).toString();
        String setupItemLore = instance.getMessageFile().getValue("Items.Setup.Lore", player).toString();

        ItemStack setupItem = new ItemBuilder(Material.FEATHER).setName(setupItemName).addLoreLine(setupItemLore).toItemStack();

        player.setItemInHand(setupItem);
        player.sendMessage(instance.getMessageFile().getValue("Commands.Setup.ItemGiven", player).toString());

        return false;
    }
}
