package vn.yourname.tutien.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.BossBarManager;
import vn.yourname.tutien.manager.ConfigManager;
import vn.yourname.tutien.manager.MeditationManager;
import vn.yourname.tutien.manager.PlayerManager;

public class CultivationTask extends BukkitRunnable {

    private final PlayerManager playerManager;
    private final MeditationManager meditationManager;
    private final BossBarManager bossBarManager;

    public CultivationTask(PlayerManager pm, MeditationManager mm, BossBarManager bbm) {
        this.playerManager = pm;
        this.meditationManager = mm;
        this.bossBarManager = bbm;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = playerManager.getPlayerData(player);
            if (data == null) continue;

            if (meditationManager.isMeditating(player)) {
                double multiplier = 1.0;
                if (data.getLinhCan() != null) {
                    multiplier = data.getLinhCan().getMultiplier();
                }
                double linhKhiToAdd = ConfigManager.TU_LUYEN_BASE_GAIN * multiplier;
                data.addLinhKhi(linhKhiToAdd);
            }

            bossBarManager.updateBossBar(player, data);
        }
    }
}
