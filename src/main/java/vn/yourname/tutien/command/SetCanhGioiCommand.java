package vn.yourname.tutien.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.yourname.tutien.data.CanhGioi;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.AttributeManager;
import vn.yourname.tutien.manager.BossBarManager;
import vn.yourname.tutien.manager.PlayerManager;

public class SetCanhGioiCommand implements CommandExecutor {
    private final PlayerManager playerManager;
    private final BossBarManager bossBarManager;
    private final AttributeManager attributeManager;

    public SetCanhGioiCommand(PlayerManager playerManager, BossBarManager bossBarManager, AttributeManager attributeManager) {
        this.playerManager = playerManager;
        this.bossBarManager = bossBarManager;
        this.attributeManager = attributeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tutien.admin.setcanhgioi")) return true;
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Sử dụng: /setcanhgioi <tên> <cảnh giới> <tầng>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Không tìm thấy người chơi.");
            return true;
        }
        CanhGioi targetRealm;
        try {
            targetRealm = CanhGioi.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Tên cảnh giới không hợp lệ.");
            return true;
        }
        int targetTier;
        try {
            targetTier = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Tầng phải là số.");
            return true;
        }

        PlayerData playerData = playerManager.getPlayerData(target);
        double totalLinhKhiNeeded = CanhGioi.getTotalLinhKhiFor(targetRealm, targetTier);
        playerData.setTotalLinhKhi(totalLinhKhiNeeded);
        
        attributeManager.updateAttributes(target, playerData);
        bossBarManager.updateBossBar(target, playerData);
        sender.sendMessage(ChatColor.GREEN + "Đã đặt cảnh giới của " + target.getName());
        return true;
    }
}
