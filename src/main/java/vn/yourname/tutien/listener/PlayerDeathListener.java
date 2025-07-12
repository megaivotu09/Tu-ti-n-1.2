package vn.yourname.tutien.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import vn.yourname.tutien.data.CanhGioi;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.AttributeManager;
import vn.yourname.tutien.manager.BossBarManager;
import vn.yourname.tutien.manager.PlayerManager;
import vn.yourname.tutien.manager.SoulManager;

public class PlayerDeathListener implements Listener {
    private final JavaPlugin plugin;
    private final PlayerManager playerManager;
    private final BossBarManager bossBarManager;
    private final SoulManager soulManager;
    private final AttributeManager attributeManager;

    public PlayerDeathListener(JavaPlugin plugin, PlayerManager pm, BossBarManager bbm, SoulManager sm, AttributeManager am) {
        this.plugin = plugin;
        this.playerManager = pm;
        this.bossBarManager = bbm;
        this.soulManager = sm;
        this.attributeManager = am;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerData data = playerManager.getPlayerData(player);
        if (data == null) return;
        String killerName = (player.getKiller() != null) ? player.getKiller().getName() : "định mệnh";

        if (soulManager.isRemnantSoul(player)) {
            event.setDeathMessage(ChatColor.DARK_PURPLE + "✨ " + player.getName() + " đã bị " + killerName + " diệt hồn, tan biến vào hư không! ✨");
            soulManager.shatterSoul(player);
            return;
        }

        CanhGioi currentRealm = data.getCanhGioi();
        if (currentRealm.ordinal() >= CanhGioi.NGUYEN_ANH.ordinal()) {
            soulManager.setRemnantSoul(player);
            event.setDeathMessage(ChatColor.DARK_AQUA + "☯ " + player.getName() + " bị " + killerName + " đánh cho hồn phi phách tán! ☯");
        } else if (currentRealm.ordinal() >= CanhGioi.TRUC_CO.ordinal()) {
            soulManager.shatterSoul(player);
            event.setDeathMessage(ChatColor.DARK_RED + "☠ " + player.getName() + " đã bị " + killerName + " trảm sát, trọng tu từ đầu! ☠");
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (soulManager.isRemnantSoul(player)) {
            applyRemnantSoulEffects(player);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                attributeManager.updateAttributes(player, playerManager.getPlayerData(player));
                bossBarManager.updateBossBar(player, playerManager.getPlayerData(player));
            }
        }.runTaskLater(plugin, 1L);
    }
    
    private void applyRemnantSoulEffects(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
        player.sendTitle(ChatColor.GRAY + "TÀN HỒN", ChatColor.DARK_GRAY + "Tìm Tụ Hồn Đan để phục hồi", 10, 70, 20);
    }
}
