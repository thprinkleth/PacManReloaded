package de.minecraft.plugin.spigot.powerup;

import de.minecraft.plugin.spigot.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PickupableItemStacks {

    public ItemStack pointItemStack() {
        ItemStack itemStack = new ItemBuilder(Material.GOLD_NUGGET).toItemStack();
        return itemStack;
    }

    public ItemStack speedPowerUpItemStack() {
        ItemStack itemStack = new ItemBuilder(Material.DIAMOND_HOE).toItemStack();
        return itemStack;
    }

    public ItemStack eatingGhostPowerUpItemStack() {
        ItemStack itemStack = new ItemBuilder(Material.DIAMOND_AXE).toItemStack();
        return itemStack;
    }

    public ItemStack invincibilityPowerUpItemStack() {
        ItemStack itemStack = new ItemBuilder(Material.DIAMOND_SWORD).toItemStack();
        return itemStack;
    }

    public ItemStack freezeGhostPowerUpItemStack() {
        ItemStack itemStack = new ItemBuilder(Material.DIAMOND_SPADE).toItemStack();
        return itemStack;
    }

    public ItemStack doublePointsPowerUpItemStack() {
        ItemStack itemStack = new ItemBuilder(Material.DIAMOND_PICKAXE).toItemStack();
        return itemStack;
    }

    public ItemStack extraLifePowerUpItemStack() {
        ItemStack itemStack = new ItemBuilder(Material.APPLE).toItemStack();
        return itemStack;
    }
}
