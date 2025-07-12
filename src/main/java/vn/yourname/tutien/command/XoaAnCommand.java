package vn.yourname.tutien.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.PlayerManager;
import java.util.UUID;

public class XoaAnCommand implements CommandExecutor {
    private final PlayerManager playerManager;
    public XoaAnCommand(PlayerManager playerManager) { this.playerManager = playerManager; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player marker = (Player) sender;
        if (args.length != 1) {
            marker.sendMessage(ChatColor.RED + "Sử dụng: /xoaan <tên>");
            return true;
        }
        String targetName = args[0];
        PlayerData markerData = playerManager.getPlayerData(marker);
        UUID targetUUID = null;
        for(Map.Entry<UUID, String> entry : markerData.getMarksPlaced().entrySet()){
            if(entry.getValue().equalsIgnoreCase(targetName)){
                targetUUID = entry.getKey();
                break;
            }
        }
        if (targetUUID == null) {
            marker.sendMessage(ChatColor.RED + "Bạn chưa đặt ấn ký lên người này.");
            return true;
        }
        markerData.getMarksPlaced().remove(targetUUID);
        Player target = Bukkit.getPlayer(targetUUID);
        if(target != null && target.isOnline()){
            playerManager.getPlayerData(target).setMarkedBy(null);
        }
        marker.sendMessage(ChatColor.GREEN + "Đã xóa ấn ký trên người " + targetName);
        return true;
    }
}
