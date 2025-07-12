package vn.yourname.tutien.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import vn.yourname.tutien.data.LinhCan;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.AttributeManager;
import vn.yourname.tutien.manager.BossBarManager;
import vn.yourname.tutien.manager.PlayerManager;
import vn.yourname.tutien.manager.SoulManager;
import vn.yourname.tutien.util.ItemManager;

public class PlayerJoinLeaveListener implements Listener {
    private final JavaPlugin plugin;
    private final PlayerManager playerManager;
    private final SoulManager soulManager;
    private final AttributeManager attributeManager;
    private final BossBarManager bossBarManager;

    public PlayerJoinLeaveListener(JavaPlugin plugin, PlayerManager pm, SoulManager sm, AttributeManager am, BossBarManager bbm) {
        this.plugin = plugin;
        this.playerManager = pm;
        this.soulManager = sm;
        this.attributeManager = am;
        this.bossBarManager = bbm;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerManager.loadPlayerData(player);
        PlayerData data = playerManager.getPlayerData(player);

        if (!player.hasPlayedBefore()) {
            player.getInventory().setItem(7, ItemManager.createTuLuyenItem());
            player.getInventory().setItem(8, ItemManager.createDotPhaItem());
            if (data.getLinhCan() == null) {
                LinhCan randomLinhCan = LinhCan.getRandom();
                data.setLinhCan(randomLinhCan);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.sendMessage(ChatColor.GOLD + "================= VẬN MỆNH ĐÃ ĐỊNH =================");
                    player.sendMessage(ChatColor.YELLOW + "Linh căn của ngươi là: " + randomLinhCan.getColor() + "" + ChatColor.BOLD + randomLinhCan.getDisplayName());
                }, 60L);
            }
        }
        if (soulManager.isRemnantSoul(player)) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
        attributeManager.updateAttributes(player, data);
        bossBarManager.createBossBar(player);
        bossBarManager.updateBossBar(player, data);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.isFlying() && player.getGameMode() != GameMode.CREATIVE) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
        bossBarManager.removeBossBar(player);
        playerManager.unloadPlayerData(player);
    }
}
