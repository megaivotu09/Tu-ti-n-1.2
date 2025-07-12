package vn.yourname.tutien.task;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import vn.yourname.tutien.data.CanhGioi;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.*;

public class ThienKiepTask extends BukkitRunnable {

    private final Player player;
    private final PlayerData playerData;
    private final ThienKiepManager thienKiepManager;
    private final BossBarManager bossBarManager;
    private final AttributeManager attributeManager;
    private int timeLeft;
    private final double damagePerStrike;
    private final Location startLocation;

    public ThienKiepTask(JavaPlugin plugin, Player player, int duration, double damage, PlayerManager pm, ThienKiepManager tkm, BossBarManager bbm, AttributeManager am) {
        this.player = player;
        this.playerData = pm.getPlayerData(player);
        this.thienKiepManager = tkm;
        this.bossBarManager = bbm;
        this.attributeManager = am;
        this.timeLeft = duration;
        this.damagePerStrike = damage;
        this.startLocation = player.getLocation();
    }

    @Override
    public void run() {
        if (player == null || !player.isOnline() || player.isDead()) {
            failTribulation("Ngươi đã ngã xuống trong Thiên Kiếp!");
            return;
        }
        if (timeLeft <= 0) {
            succeedTribulation();
            return;
        }
        player.getWorld().strikeLightningEffect(player.getLocation());
        player.damage(this.damagePerStrike);
        player.sendTitle(ChatColor.RED + "THIÊN KIẾP!", "Còn lại: " + timeLeft + " giây", 5, 20, 5);
        timeLeft--;
    }
    
    private void succeedTribulation() {
        this.cancel();
        thienKiepManager.endTribulation(player.getUniqueId());
        player.teleport(startLocation.getWorld().getHighestBlockAt(startLocation).getLocation().add(0, 1, 0));
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
        playerData.updateRealmData();
        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "VƯỢT KIẾP THÀNH CÔNG!");
        String canhGioiDisplay = playerData.getCanhGioi().getColor() + playerData.getCanhGioi().getDisplayName() + " - Tầng " + playerData.getTieuCanhGioi();
        player.sendMessage(ChatColor.YELLOW + "Cảnh giới của bạn đã tăng lên: " + canhGioiDisplay);
        attributeManager.updateAttributes(player, playerData);
        
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) {
            player.setHealth(maxHealth.getValue());
        }
        
        bossBarManager.updateBossBar(player, playerData);
    }
    
    private void failTribulation(String message) {
        this.cancel();
        thienKiepManager.endTribulation(player.getUniqueId());
        if (player.isOnline() && !player.isDead()) {
             player.teleport(startLocation.getWorld().getHighestBlockAt(startLocation).getLocation().add(0, 1, 0));
            if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }
        player.sendMessage(ChatColor.DARK_RED + "ĐỘT PHÁ THẤT BẠI!");
        player.sendMessage(ChatColor.RED + message);
        
        CanhGioi.RealmData realmData = CanhGioi.getRealmDataFor(playerData.getTotalLinhKhi());
        double penalty = (realmData.linhKhiKetThuc - realmData.linhKhiBatDau) * 0.20;
        playerData.setTotalLinhKhi(Math.max(realmData.linhKhiBatDau, playerData.getTotalLinhKhi() - penalty));
        bossBarManager.updateBossBar(player, playerData);
    }
}
