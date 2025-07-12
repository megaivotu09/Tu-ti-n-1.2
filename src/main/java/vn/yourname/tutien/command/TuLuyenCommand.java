package vn.yourname.tutien.command;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import vn.yourname.tutien.manager.MeditationManager;
import vn.yourname.tutien.manager.SoulManager;

public class TuLuyenCommand implements CommandExecutor {
    private final MeditationManager meditationManager;
    private final SoulManager soulManager;

    public TuLuyenCommand(MeditationManager meditationManager, SoulManager soulManager) {
        this.meditationManager = meditationManager;
        this.soulManager = soulManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (soulManager.isRemnantSoul(player)) {
            player.sendMessage(ChatColor.GRAY + "Trạng thái Tàn Hồn không thể tu luyện.");
            return true;
        }
        if (meditationManager.isMeditating(player)) {
            stopMeditation(player);
        } else {
            startMeditation(player);
        }
        return true;
    }

    private void startMeditation(Player player) {
        if (!player.isOnGround()) {
            player.sendMessage(ChatColor.RED + "Phải đứng trên mặt đất để tu luyện.");
            return;
        }
        player.sendMessage(ChatColor.AQUA + "Bạn đã bước vào trạng thái thiền định...");
        Location meditationLocation = player.getLocation().clone().add(0, 2, 0);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.teleport(meditationLocation);
        ArmorStand seat = (ArmorStand) player.getWorld().spawnEntity(meditationLocation.clone().subtract(0, 1.7, 0), EntityType.ARMOR_STAND);
        seat.setGravity(false);
        seat.setVisible(false);
        seat.setInvulnerable(true);
        seat.addPassenger(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, false, false));
        meditationManager.startMeditation(player, seat);
    }

    private void stopMeditation(Player player) {
        if (!meditationManager.isMeditating(player)) return;
        player.sendMessage(ChatColor.YELLOW + "Bạn đã kết thúc thiền định.");
        Location seatLocation = meditationManager.getSeat(player).getLocation();
        player.teleport(seatLocation.getWorld().getHighestBlockAt(seatLocation).getLocation().add(0, 1, 0));
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
        player.removePotionEffect(PotionEffectType.GLOWING);
        meditationManager.stopMeditation(player);
    }
}
