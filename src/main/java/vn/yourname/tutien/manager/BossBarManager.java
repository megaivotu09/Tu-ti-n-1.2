package vn.yourname.tutien.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import vn.yourname.tutien.data.CanhGioi;
import vn.yourname.tutien.data.PlayerData;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class BossBarManager {
    private final Map<UUID, BossBar> playerBossBars = new HashMap<>();
    private final PlayerManager playerManager;

    public BossBarManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void createBossBar(Player player) {
        if (playerBossBars.containsKey(player.getUniqueId())) return;
        BossBar bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_10);
        bossBar.addPlayer(player);
        playerBossBars.put(player.getUniqueId(), bossBar);
    }

    public void removeBossBar(Player player) {
        BossBar bossBar = playerBossBars.remove(player.getUniqueId());
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    public void updateBossBar(Player player, PlayerData data) {
        BossBar bossBar = playerBossBars.get(player.getUniqueId());
        if (bossBar == null) return;

        if (data.isTuViHidden()) {
            bossBar.setTitle(ChatColor.DARK_GREEN + "» Ẩn Tức Trạng Thái «");
            bossBar.setColor(BarColor.GREEN);
            bossBar.setProgress(1.0);
            return;
        }

        CanhGioi.RealmData realmData = CanhGioi.getRealmDataFor(data.getTotalLinhKhi());
        double linhKhiForLevel = data.getTotalLinhKhi() - realmData.linhKhiBatDau;
        double costOfLevel = realmData.linhKhiKetThuc - realmData.linhKhiBatDau;

        String title = realmData.canhGioi.getColor() + realmData.canhGioi.getDisplayName();
        if (realmData.canhGioi != CanhGioi.PHAM_NHAN) {
            title += " - Tầng " + realmData.tieuCanhGioi;
        }
        String formattedCurrent = NumberFormat.getInstance(Locale.US).format(linhKhiForLevel);
        String formattedMax = NumberFormat.getInstance(Locale.US).format(costOfLevel);
        title += " §f| §bLinh Khí: " + formattedCurrent + " / " + formattedMax;

        bossBar.setTitle(title);
        double progress = (costOfLevel > 0) ? (linhKhiForLevel / costOfLevel) : 0.0;
        bossBar.setProgress(Math.max(0.0, Math.min(1.0, progress)));
        bossBar.setColor(realmData.canhGioi.getBarColor());
    }
}
