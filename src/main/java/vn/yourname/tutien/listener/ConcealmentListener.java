package vn.yourname.tutien.listener;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import vn.yourname.tutien.TuTienPlugin;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.BossBarManager;
import vn.yourname.tutien.manager.PlayerManager;

public class ConcealmentListener extends BukkitRunnable {
    private final PlayerManager playerManager;
    private final BossBarManager bossBarManager;
    private final NamespacedKey itemKey;

    public ConcealmentListener(TuTienPlugin plugin, PlayerManager pm, BossBarManager bbm) {
        this.playerManager = pm;
        this.bossBarManager = bbm;
        this.itemKey = new NamespacedKey(plugin, "item_id");
    }

    @Override
    public void run() {
        for (Player player : TuTienPlugin.getInstance().getServer().getOnlinePlayers()) {
            PlayerData data = playerManager.getPlayerData(player);
            if (data == null) continue;
            ItemStack offHandItem = player.getInventory().getItemInOffHand();
            boolean shouldBeHidden = false;
            if (offHandItem != null && offHandItem.hasItemMeta()) {
                String itemId = offHandItem.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
                if (itemId != null && itemId.equals("an_tuc_quyet")) {
                    shouldBeHidden = true;
                }
            }
            if (data.isTuViHidden() != shouldBeHidden) {
                data.setTuViHidden(shouldBeHidden);
                bossBarManager.updateBossBar(player, data);
            }
        }
    }
}
