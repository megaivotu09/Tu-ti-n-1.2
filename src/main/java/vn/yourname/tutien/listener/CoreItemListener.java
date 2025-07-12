package vn.yourname.tutien.listener;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import vn.yourname.tutien.TuTienPlugin;
import vn.yourname.tutien.util.ItemManager;

public class CoreItemListener implements Listener {
    private final NamespacedKey coreItemKey;

    public CoreItemListener(TuTienPlugin plugin) {
        this.coreItemKey = new NamespacedKey(plugin, "core_item");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) return;
        String coreItemId = item.getItemMeta().getPersistentDataContainer().get(coreItemKey, PersistentDataType.STRING);
        if (coreItemId == null) return;
        event.setCancelled(true);

        switch (coreItemId) {
            case "tu_luyen_item":
                player.performCommand("tuluyen");
                break;
            case "dot_pha_item":
                player.performCommand("dotpha");
                break;
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (isCoreItem(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Không thể vứt bỏ Vật Phẩm Linh Hồn!");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (isCoreItem(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(this::isCoreItem);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getInventory().getItem(7) == null) {
                    player.getInventory().setItem(7, ItemManager.createTuLuyenItem());
                }
                if (player.getInventory().getItem(8) == null) {
                    player.getInventory().setItem(8, ItemManager.createDotPhaItem());
                }
            }
        }.runTaskLater(TuTienPlugin.getInstance(), 1L);
    }
    
    private boolean isCoreItem(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(coreItemKey, PersistentDataType.STRING);
    }
}
