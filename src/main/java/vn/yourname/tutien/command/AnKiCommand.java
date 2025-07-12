package vn.yourname.tutien.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.PlayerManager;

public class AnKiCommand implements CommandExecutor {
    private final PlayerManager playerManager;
    private static final double MAX_DISTANCE = 10.0;

    public AnKiCommand(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player marker = (Player) sender;

        if (args.length != 1) {
            marker.sendMessage(ChatColor.RED + "Sử dụng sai: /anki <tên người chơi>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            marker.sendMessage(ChatColor.RED + "Không tìm thấy người chơi hoặc người chơi không online.");
            return true;
        }

        if (marker.equals(target)) {
            marker.sendMessage(ChatColor.RED + "Bạn không thể tự đặt ấn ký lên chính mình.");
            return true;
        }
        
        if (!marker.getWorld().equals(target.getWorld()) || marker.getLocation().distance(target.getLocation()) > MAX_DISTANCE) {
            marker.sendMessage(ChatColor.RED + "Bạn phải ở trong phạm vi " + (int)MAX_DISTANCE + " block so với mục tiêu.");
            return true;
        }

        PlayerData markerData = playerManager.getPlayerData(marker);
        PlayerData targetData = playerManager.getPlayerData(target);

        if (markerData.getCanhGioi().ordinal() <= targetData.getCanhGioi().ordinal()) {
            marker.sendMessage(ChatColor.RED + "Bạn chỉ có thể đặt ấn ký lên người có cảnh giới thấp hơn.");
            return true;
        }

        if (markerData.getMarksPlaced().size() >= markerData.getMaxMarks()) {
            marker.sendMessage(ChatColor.RED + "Bạn đã đạt đến giới hạn số lượng ấn ký có thể đặt.");
            return true;
        }

        if (targetData.getMarkedBy() != null) {
            marker.sendMessage(ChatColor.RED + "Người này đã bị một cường giả khác lưu lại ấn ký.");
            return true;
        }

        markerData.getMarksPlaced().put(target.getUniqueId(), target.getName());
        targetData.setMarkedBy(marker.getUniqueId());

        marker.sendMessage(ChatColor.GREEN + "Bạn đã lưu lại một đạo thần niệm lên người " + target.getName() + ".");
        target.sendMessage(ChatColor.DARK_RED + "Một cường giả đã lưu lại ấn ký trên người bạn!");
        target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_STARE, 1.0f, 0.8f);

        return true;
    }
}
