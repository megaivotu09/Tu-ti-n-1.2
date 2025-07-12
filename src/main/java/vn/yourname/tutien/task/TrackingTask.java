package vn.yourname.tutien.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.PlayerManager;

import java.util.UUID;

public class TrackingTask extends BukkitRunnable {
    private final PlayerManager playerManager;

    public TrackingTask(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = playerManager.getPlayerData(player);
            if (data == null || data.getMarksPlaced().isEmpty()) continue;

            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            if (mainHandItem.getType() == Material.COMPASS) {
                UUID targetUUID = data.getMarksPlaced().keySet().iterator().next();
                Player target = Bukkit.getPlayer(targetUUID);
                if (target != null && target.isOnline() && player.getWorld().equals(target.getWorld())) {
                    player.setCompassTarget(target.getLocation());
                    int distance = (int) player.getLocation().distance(target.getLocation());
                    player.sendActionBar(ChatColor.AQUA + "Đang theo dõi " + target.getName() + " - Khoảng cách: " + distance + "m");
                } else {
                    player.sendActionBar(ChatColor.GRAY + "Không thể cảm ứng được vị trí của mục tiêu.");
                }
            }
        }
    }
}
