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

public class CmdSetPowerup implements CommandExecutor {

    private PacMan instance = PacMan.getInstance();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {

        if (!cmd.getName().equalsIgnoreCase("setPowerup")) {
            return true;
        }

        if (!(cs instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(instance.getMessageFile().getValue("Commands.NoPlayer").toString());
            return true     ;
        }

        Player player = (Player) cs;

        if (!player.hasPermission("commands.setup.setpowerup")) {
            player.sendMessage(instance.getMessageFile().getValue("Commands.NoPerm", player).toString());
            return true;
        }

        if (args.length != 0) {
            player.sendMessage(instance.getMessageFile().getValue("Commands.SetPowerUp.Syntax", player).toString());
            return true;
        }

        String powerUpItemName = instance.getMessageFile().getValue("Items.PowerUp.Name", player).toString();
        String powerUpItemLore = instance.getMessageFile().getValue("Items.PowerUp.Lore", player).toString();

        ItemStack powerUpItem = new ItemBuilder(Material.FEATHER).setName(powerUpItemName).addLoreLine(powerUpItemLore).toItemStack();

        player.setItemInHand(powerUpItem);
        player.sendMessage(instance.getMessageFile().getValue("Commands.SetPowerUp.ItemGiven", player).toString());

        return false;
    }
}
