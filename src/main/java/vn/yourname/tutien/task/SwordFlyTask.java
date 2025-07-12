package vn.yourname.tutien.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import vn.yourname.tutien.command.NgukiemCommand;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.ConfigManager;
import vn.yourname.tutien.manager.PlayerManager;
import vn.yourname.tutien.manager.SwordFlyManager;

import java.util.Map;
import java.util.UUID;

public class SwordFlyTask extends BukkitRunnable {
    private final PlayerManager playerManager;
    private final SwordFlyManager swordFlyManager;
    private final NgukiemCommand ngukiemCommand;
    private int tickCounter = 0;

    public SwordFlyTask(PlayerManager pm, SwordFlyManager sfm, NgukiemCommand nc) {
        this.playerManager = pm;
        this.swordFlyManager = sfm;
        this.ngukiemCommand = nc;
    }

    @Override
    public void run() {
        tickCounter++;
        for (Map.Entry<UUID, ArmorStand> entry : swordFlyManager.getFlyingPlayers().entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            ArmorStand sword = entry.getValue();

            if (player == null || !player.isOnline() || sword == null || sword.isDead()) {
                swordFlyManager.stopFlying(player);
                continue;
            }

            if (!player.isFlying() && !player.isOnGround()) {
                 ngukiemCommand.stopSwordFly(player);
                 continue;
            }

            Location targetLocation = player.getLocation().subtract(0, 1.4, 0);
            targetLocation.setYaw(player.getLocation().getYaw());
            sword.teleport(targetLocation);

            if (tickCounter % 20 == 0) {
                PlayerData data = playerManager.getPlayerData(player);
                double cost = ConfigManager.NGU_KIEM_COST;
                if (data.getTotalLinhKhi() < cost) {
                    player.sendMessage(ChatColor.RED + "Linh khí đã cạn, không thể tiếp tục ngự kiếm!");
                    ngukiemCommand.stopSwordFly(player);
                } else {
                    data.addLinhKhi(-cost);
                    player.sendActionBar(ChatColor.AQUA + "Đang ngự kiếm... Linh khí tiêu hao: " + (int)cost + "/s");
                }
            }
        }
    }
}
