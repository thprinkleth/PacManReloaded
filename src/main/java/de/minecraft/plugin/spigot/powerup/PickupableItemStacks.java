package de.minecraft.plugin.spigot.powerup;

import de.minecraft.plugin.spigot.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PickupableItemStacks {

    public ItemStack pointItemStack() {
        ItemStack itemStack = new ItemBuilder(Material.GOLD_NUGGET).toItemStack();
        return itemStack;
    }

    public ItemStack powerUpItemStack() {
        ItemStack itemStack = new ItemBuilder(Material.IRON_NUGGET).toItemStack();
        return itemStack;
    }
}
