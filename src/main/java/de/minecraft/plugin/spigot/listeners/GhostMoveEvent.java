package de.minecraft.plugin.spigot.listeners;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.gamestate.IngameState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

public class GhostMoveEvent {

    private PacMan instance = PacMan.getInstance();

    @EventHandler
    public void onMovement(PlayerMoveEvent event){

        Player player = event.getPlayer();
        String rolePlayer = instance.getRoleHandler().getPlayerRoles().get(player);

        if(!(instance.getGameStateManager().getCurrent() instanceof IngameState)){
            return;
        }

        if(rolePlayer != "Ghost"){
            return;
        }

        double xFrom = event.getFrom().getBlockX();
        double yFrom = event.getFrom().getBlockY();
        double zFrom = event.getFrom().getBlockZ();
        yFrom += 10;

        Location locationFromBlock = new Location(player.getLocation().getWorld(), xFrom, yFrom, zFrom);
        player.getLocation().getWorld().getBlockAt(locationFromBlock).setType(Material.AIR);

        double xTo = event.getTo().getBlockX();
        double zTo = event.getTo().getBlockZ();

        Location locationToBlock = new Location(player.getLocation().getWorld(), xTo, yFrom, zTo);
        player.getLocation().getWorld().getBlockAt(locationToBlock).setType(Material.CONCRETE);
        player.getLocation().getWorld().getBlockAt(locationToBlock).setData((byte) 14);
    }
}
