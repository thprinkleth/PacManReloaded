package de.minecraft.plugin.spigot.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class CancelledListeners implements Listener {

    // Wird beim Zerstören eines Blockes ausgeführt
    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        // Überprüft ob der Spieler nicht Operator-Rechte hat
        if (!event.getPlayer().isOp())

            // Bricht die Effekte des Events ab
            event.setCancelled(true);
    }

    // Wird beim Platzieren eines Blockes ausgeführt
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        // Überprüft ob der Spieler nicht Operator-Rechte hat
        if (!event.getPlayer().isOp())

            // Bricht die Effekte des Events ab
            event.setCancelled(true);
    }

    // Wird bei dem Fallenlassen eines Items ausgeführt
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        // Überprüft ob der Spieler nicht Operator-Rechte hat
        if (!event.getPlayer().isOp())

            // Bricht die Effekte des Events ab
            event.setCancelled(true);
    }

    // Wird ausgeführt, wenn mehrere Items auf dem Boden zu einem verschmelzen
    @EventHandler
    public void onItemMerge(ItemMergeEvent event) {

        // Bricht die Effekte des Events ab
        event.setCancelled(true);
    }

    // Wird ausgeführt, wenn ein Schleim sich spaltet
    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent event) {

        // Bricht die Effekte des Events ab
        event.setCancelled(true);
    }

    // Wird ausgeführt, wenn ein Gefährt beschädigt wird
    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {

        // Bricht die Effekte des Events ab
        event.setCancelled(true);
    }

    // Wird ausgeführt, wenn sich das Wetter ändert
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {

        // Bricht die Effekte des Events ab
        event.setCancelled(true);
    }

    // Wird ausgeführt, wenn der Spieler Hunger bekommt
    @EventHandler
    public void onFoodLeveLChange(FoodLevelChangeEvent event) {

        // Setzt die Hungersleiste und die Sättigungsleiste auf das Maximum
        ((Player) event.getEntity()).setSaturation(20);
        ((Player) event.getEntity()).setFoodLevel(20);

        // Bricht die Effekte des Events ab
        event.setCancelled(true);
    }

    // Wird ausgeführt, wenn ein Objekt schaden bekommt
    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        // Bricht die Effekte des Events ab
        event.setCancelled(true);
    }
}

