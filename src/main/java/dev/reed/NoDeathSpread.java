package dev.reed;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public final class NoDeathSpread extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Location pos = event.getEntity().getLocation();
        List<ItemStack> drops = new ArrayList<>(event.getDrops());
        event.getDrops().clear();

        // another highest handler can still cancel the death after ours
        getServer().getScheduler().runTask(this, () -> {
            if (event.isCancelled()) return;
            for (ItemStack stack : drops) {
                if (stack == null || stack.isEmpty()) continue;
                pos.getWorld().dropItem(pos, stack, item -> {
                    item.setVelocity(new Vector());
                    item.setPickupDelay(40);
                });
            }
        });
    }
}
