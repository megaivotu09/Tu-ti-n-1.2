package vn.yourname.tutien.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.ConfigManager;
import vn.yourname.tutien.manager.PlayerManager;
import vn.yourname.tutien.manager.SoulManager;

public class SoulDrainTask extends BukkitRunnable {

    private final SoulManager soulManager;
    private final PlayerManager playerManager;

    public SoulDrainTask(SoulManager sm, PlayerManager pm) {
        this.soulManager = sm;
        this.playerManager = pm;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (soulManager.isRemnantSoul(player)) {
                PlayerData data = playerManager.getPlayerData(player);
                if (data == null) continue;

                double newLinhKhi = data.getTotalLinhKhi() - ConfigManager.SOUL_DRAIN_AMOUNT;
                player.sendActionBar(ChatColor.DARK_GRAY + "Linh hồn đang tan rã... Linh khí hao mòn...");
                
                if (newLinhKhi <= 0) {
                    soulManager.shatterSoul(player);
                } else {
                    data.setTotalLinhKhi(newLinhKhi);
                    // Bossbar sẽ được cập nhật bởi CultivationTask
                }
            }
        }
    }
}
