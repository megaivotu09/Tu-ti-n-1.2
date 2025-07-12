package vn.yourname.tutien.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import vn.yourname.tutien.data.LinhCan;
import vn.yourname.tutien.data.PlayerData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SoulManager {
    private final Set<UUID> remnantSouls = new HashSet<>();
    private final JavaPlugin plugin;
    private final PlayerManager playerManager;
    private final BossBarManager bossBarManager;
    private AttributeManager attributeManager;

    public SoulManager(JavaPlugin plugin, PlayerManager pm, BossBarManager bbm) {
        this.plugin = plugin;
        this.playerManager = pm;
        this.bossBarManager = bbm;
    }

    public void setAttributeManager(AttributeManager attributeManager) {
        this.attributeManager = attributeManager;
    }

    public boolean isRemnantSoul(Player player) { return remnantSouls.contains(player.getUniqueId()); }
    public void setRemnantSoul(Player player) { remnantSouls.add(player.getUniqueId()); }
    public void removeRemnantSoul(Player player) { remnantSouls.remove(player.getUniqueId()); }

    public void shatterSoul(Player player) {
        PlayerData data = playerManager.getPlayerData(player);
        if (data == null) return;
        
        this.removeRemnantSoul(player);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
        
        data.setTotalLinhKhi(0);
        LinhCan newRandomLinhCan = LinhCan.getRandom();
        data.setLinhCan(newRandomLinhCan);
        
        attributeManager.updateAttributes(player, data);
        bossBarManager.updateBossBar(player, data);
        
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.5f);
        player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation().add(0, 1, 0), 100, 0.5, 1, 0.5, 0.1);
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendTitle(ChatColor.BLACK + "" + ChatColor.BOLD + "LINH HỒN TAN BIẾN", ChatColor.DARK_GRAY + "Ngươi đã hoàn toàn trọng tu từ đầu...", 10, 70, 20);
            player.sendMessage(ChatColor.DARK_RED + "Linh hồn của ngươi đã tan biến hoàn toàn!");
            player.sendMessage(ChatColor.YELLOW + "Linh căn mới của ngươi là: " + newRandomLinhCan.getColor() + "" + ChatColor.BOLD + newRandomLinhCan.getDisplayName());
        }, 20L);
    }
}
