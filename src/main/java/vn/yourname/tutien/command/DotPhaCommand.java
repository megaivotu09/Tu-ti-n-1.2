package vn.yourname.tutien.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import vn.yourname.tutien.data.CanhGioi;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.*;
import vn.yourname.tutien.task.ThienKiepTask;

public class DotPhaCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final PlayerManager playerManager;
    private final BossBarManager bossBarManager;
    private final AttributeManager attributeManager;
    private final ThienKiepManager thienKiepManager;

    public DotPhaCommand(JavaPlugin plugin, PlayerManager pm, BossBarManager bbm, AttributeManager am, ThienKiepManager tkm) {
        this.plugin = plugin;
        this.playerManager = pm;
        this.bossBarManager = bbm;
        this.attributeManager = am;
        this.thienKiepManager = tkm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        
        if (thienKiepManager.isInTribulation(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Bạn đang trong quá trình độ kiếp!");
            return true;
        }

        PlayerData data = playerManager.getPlayerData(player);
        CanhGioi.RealmData currentRealmData = CanhGioi.getRealmDataFor(data.getTotalLinhKhi());

        if (data.getTotalLinhKhi() < currentRealmData.linhKhiKetThuc) {
            player.sendMessage(ChatColor.RED + "Linh khí chưa đủ để đột phá!");
            return true;
        }

        boolean isRiskyBreakthrough = currentRealmData.tieuCanhGioi == 9 && currentRealmData.canhGioi.ordinal() >= CanhGioi.KIM_DAN.ordinal() && currentRealmData.canhGioi != CanhGioi.DO_KIEP;

        if (isRiskyBreakthrough) {
            if (Math.random() < ConfigManager.BREAKTHROUGH_FAIL_CHANCE) {
                handleBreakthroughFailure(player, data);
            } else {
                startTribulation(player);
            }
        } else {
            performMinorBreakthrough(player, data.getCanhGioi() == CanhGioi.PHAM_NHAN);
        }
        return true;
    }

    private void handleBreakthroughFailure(Player player, PlayerData data) {
        player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "ĐỘT PHÁ ĐẠI CẢNH GIỚI THẤT BẠI!");
        CanhGioi.RealmData realmData = CanhGioi.getRealmDataFor(data.getTotalLinhKhi());
        double linhKhiInCurrentLevel = realmData.linhKhiKetThuc - realmData.linhKhiBatDau;
        double penalty = linhKhiInCurrentLevel * ConfigManager.BREAKTHROUGH_PENALTY;
        data.setTotalLinhKhi(data.getTotalLinhKhi() - penalty);
        player.sendMessage(ChatColor.RED + "Linh khí phản phệ, tu vi của ngươi bị hao tổn!");
        bossBarManager.updateBossBar(player, data);
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.7f, 1.0f);
        player.getWorld().spawnParticle(Particle.LARGE_SMOKE, player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.05);
    }

    private void performMinorBreakthrough(Player player, boolean isFirstBreakthrough) {
        PlayerData data = playerManager.getPlayerData(player);
        data.updateRealmData();
        if (isFirstBreakthrough) {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "THOÁT PHÀM THÀNH CÔNG!");
        } else {
            player.sendMessage(ChatColor.AQUA + "Đột phá thành công!");
        }
        String canhGioiDisplay = data.getCanhGioi().getColor() + data.getCanhGioi().getDisplayName() + " - Tầng " + data.getTieuCanhGioi();
        player.sendMessage(ChatColor.YELLOW + "Cảnh giới của bạn đã tăng lên: " + canhGioiDisplay);
        attributeManager.updateAttributes(player, data);
        bossBarManager.updateBossBar(player, data);
        player.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, player.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.1);
    }

    private void startTribulation(Player player) {
        player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "THIÊN KIẾP ĐANG KÉO ĐẾN!");
        Location skyLocation = player.getLocation().clone().add(0, 20, 0);
        player.teleport(skyLocation);
        player.setAllowFlight(true);
        player.setFlying(true);
        thienKiepManager.startTribulation(player.getUniqueId());
        CanhGioi currentRealm = playerManager.getPlayerData(player).getCanhGioi();
        int duration = currentRealm.getTribulationDuration();
        double damage = currentRealm.getTribulationDamage();
        new ThienKiepTask(plugin, player, duration, damage, playerManager, thienKiepManager, bossBarManager, attributeManager).runTaskTimer(plugin, 20L, 20L);
    }
}
