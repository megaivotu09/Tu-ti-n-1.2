package vn.yourname.tutien.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.PlayerManager;

public class DSAnKiCommand implements CommandExecutor {
    private final PlayerManager playerManager;
    public DSAnKiCommand(PlayerManager playerManager) { this.playerManager = playerManager; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        PlayerData data = playerManager.getPlayerData(player);
        if (data.getMarksPlaced().isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "Bạn chưa đặt ấn ký lên ai.");
        } else {
            player.sendMessage(ChatColor.GOLD + "--- Danh sách Ấn Ký đã đặt ---");
            for (String targetName : data.getMarksPlaced().values()) {
                player.sendMessage(ChatColor.YELLOW + "- " + targetName);
            }
        }
        return true;
    }
}
