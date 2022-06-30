package de.minecraft.plugin.spigot.role;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class RoleHandler {

    private HashMap<Player, Role> playerRoles;

    public RoleHandler() {
        this.playerRoles = new HashMap<>();
    }

    public HashMap<Player, Role> getPlayerRoles() {
        return playerRoles;
    }
}
