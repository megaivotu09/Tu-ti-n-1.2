package vn.yourname.tutien.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import vn.yourname.tutien.data.CanhGioi;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.ConfigManager;
import vn.yourname.tutien.manager.PlayerManager;
import vn.yourname.tutien.manager.SoulManager;

public class SuppressionListener implements Listener {
    private final PlayerManager playerManager;
    private final SoulManager soulManager;

    public SuppressionListener(PlayerManager playerManager, SoulManager soulManager) {
        this.playerManager = playerManager;
        this.soulManager = soulManager;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        
        if (soulManager.isRemnantSoul(victim)) {
            if (!(event.getDamager() instanceof Player)) {
                event.setCancelled(true);
                return;
            }
            Player attacker = (Player) event.getDamager();
            PlayerData attackerData = playerManager.getPlayerData(attacker);
            if (attackerData != null && attackerData.getCanhGioi().ordinal() < CanhGioi.NGUYEN_ANH.ordinal()) {
                event.setCancelled(true);
                attacker.sendActionBar(ChatColor.GRAY + "Đòn tấn công đã xuyên qua Tàn Hồn!");
                return;
            }
        }

        if (!(event.getDamager() instanceof Player)) return;
        Player attacker = (Player) event.getDamager();
        PlayerData attackerData = playerManager.getPlayerData(attacker);
        PlayerData victimData = playerManager.getPlayerData(victim);
        if (attackerData == null || victimData == null) return;

        CanhGioi attackerRealm = attackerData.getCanhGioi();
        CanhGioi victimRealm = victimData.getCanhGioi();
        int realmDifference = attackerRealm.ordinal() - victimRealm.ordinal();

        if (realmDifference > 0) {
            double multiplier = 1.0 + (realmDifference * ConfigManager.SUPPRESS_BONUS_PER_REALM);
            event.setDamage(event.getDamage() * multiplier);
            attacker.sendActionBar(ChatColor.RED + "» Áp Đảo Cảnh Giới! «");
        } else if (realmDifference < 0) {
            double reduction = Math.min(0.95, Math.abs(realmDifference) * ConfigManager.SUPPRESS_REDUCTION_PER_REALM);
            event.setDamage(event.getDamage() * (1.0 - reduction));
            attacker.sendActionBar(ChatColor.GRAY + "» Cảnh giới quá thấp! «");
        } else {
            double attackerLinhKhi = attackerData.getTotalLinhKhi();
            double victimLinhKhi = victimData.getTotalLinhKhi();
            if (attackerLinhKhi > victimLinhKhi * 1.1) {
                double ratio = attackerLinhKhi / victimLinhKhi;
                double bonus = Math.min(0.10, (ratio - 1.0) * 0.1);
                event.setDamage(event.getDamage() * (1.0 + bonus));
                attacker.sendActionBar(ChatColor.AQUA + "» Linh khí hùng hậu hơn! «");
            }
        }
    }
}
