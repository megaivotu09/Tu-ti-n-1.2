package vn.yourname.tutien.task;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.MeditationManager;
import vn.yourname.tutien.manager.PlayerManager;

import java.util.UUID;

public class ParticleTask extends BukkitRunnable {
    private final MeditationManager meditationManager;
    private final PlayerManager playerManager;
    private double angle = 0;

    public ParticleTask(MeditationManager meditationManager, PlayerManager playerManager) {
        this.meditationManager = meditationManager;
        this.playerManager = playerManager;
    }

    @Override
    public void run() {
        angle += Math.PI / 16; // Tốc độ xoay của hiệu ứng
        for (UUID playerUUID : meditationManager.getMeditatingPlayers().keySet()) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null || !player.isOnline()) continue;

            PlayerData data = playerManager.getPlayerData(player);
            if (data == null) continue;

            Particle particleType = data.getCanhGioi().getMeditationParticle();
            Location loc = player.getLocation();
            
            // Tạo 2 vòng xoáy ngược chiều nhau
            double radius = 0.8;
            double yOffset = 0.5;
            for (int i = 0; i < 2; i++) {
                double currentAngle = (i == 0) ? angle : -angle;
                double x = radius * Math.cos(currentAngle);
                double z = radius * Math.sin(currentAngle);
                player.getWorld().spawnParticle(particleType, loc.clone().add(x, yOffset, z), 1, 0, 0, 0, 0);
                yOffset += 0.7; // Tăng độ cao cho vòng xoáy thứ 2
            }
        }
    }
}
