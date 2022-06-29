package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    PacMan instance = PacMan.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Player player = ((Player) event.getWhoClicked());

        event.setCancelled(true);

        try {
            if (event.getClickedInventory() == instance.getPowerupInventory()) {
                ItemStack clickedItem = event.getCurrentItem();
                int slot = event.getSlot();
                int amount = 0;

                switch (slot) {
                    case (8 + 3):
                        amount = (int) instance.getMessageFile().getValue("Game.PowerUp.Speed.Amount");
                        instance.getLocationFile().setLocation("Game.PowerUp.Speed.Location." + amount, player.getLocation());
                        player.closeInventory();
                        player.sendMessage(instance.getMessageFile().getValue("SetUp.PowerUp.Speed.SetLocation.Success", player, amount).toString());
                        instance.getMessageFile().setValue("Game.PowerUp.Speed.Amount", amount + 1);
                        break;
                    case (8 + 4):
                        amount = (int) instance.getMessageFile().getValue("Game.PowerUp.Invincibility.Amount");
                        instance.getLocationFile().setLocation("Game.PowerUp.Invincibility.Location." + amount, player.getLocation());
                        player.closeInventory();
                        player.sendMessage(instance.getMessageFile().getValue("SetUp.PowerUp.Invincibility.SetLocation.Success", player, amount).toString());
                        instance.getMessageFile().setValue("Game.PowerUp.Invincibility.Amount", amount + 1);
                        break;
                    case (8 + 5):
                        amount = (int) instance.getMessageFile().getValue("Game.PowerUp.GhostEating.Amount");
                        instance.getLocationFile().setLocation("Game.PowerUp.GhostEating.Location." + amount, player.getLocation());
                        player.closeInventory();
                        player.sendMessage(instance.getMessageFile().getValue("SetUp.PowerUp.GhostEating.SetLocation.Success", player, amount).toString());
                        instance.getMessageFile().setValue("Game.PowerUp.GhostEating.Amount", amount + 1);
                        break;
                    case (8 + 6):
                        amount = (int) instance.getMessageFile().getValue("Game.PowerUp.AddLife.Amount");
                        instance.getLocationFile().setLocation("Game.PowerUp.AddLife.Location." + amount, player.getLocation());
                        player.closeInventory();
                        player.sendMessage(instance.getMessageFile().getValue("SetUp.PowerUp.AddLife.SetLocation.Success", player, amount).toString());
                        instance.getMessageFile().setValue("Game.PowerUp.AddLife.Amount", amount + 1);
                        break;
                    case (8 + 7):
                        amount = (int) instance.getMessageFile().getValue("Game.PowerUp.DoublePoints.Amount");
                        instance.getLocationFile().setLocation("Game.PowerUp.DoublePoints.Location." + amount, player.getLocation());
                        player.closeInventory();
                        player.sendMessage(instance.getMessageFile().getValue("SetUp.PowerUp.DoublePoints.SetLocation.Success", player, amount).toString());
                        instance.getMessageFile().setValue("Game.PowerUp.DoublePoints.Amount", amount + 1);
                        break;
                    }

                }
            } catch(NullPointerException ex){
            }
        }
    }
