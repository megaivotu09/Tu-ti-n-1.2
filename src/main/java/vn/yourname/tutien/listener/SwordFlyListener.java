package vn.yourname.tutien.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import vn.yourname.tutien.command.NgukiemCommand;
import vn.yourname.tutien.manager.SwordFlyManager;

public class SwordFlyListener implements Listener {
    private final SwordFlyManager swordFlyManager;
    private final NgukiemCommand ngukiemCommand;

    public SwordFlyListener(SwordFlyManager sfm, NgukiemCommand nc) {
        this.swordFlyManager = sfm;
        this.ngukiemCommand = nc;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (swordFlyManager.isFlying(player)) {
                player.sendMessage(ChatColor.RED + "Bị tấn công, ngự kiếm bị gián đoạn!");
                ngukiemCommand.stopSwordFly(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (swordFlyManager.isFlying(event.getPlayer())) {
            ngukiemCommand.stopSwordFly(event.getPlayer());
        }
    }
}
