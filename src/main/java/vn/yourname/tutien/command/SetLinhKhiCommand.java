package vn.yourname.tutien.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.AttributeManager;
import vn.yourname.tutien.manager.BossBarManager;
import vn.yourname.tutien.manager.PlayerManager;
import java.text.NumberFormat;
import java.util.Locale;

public class SetLinhKhiCommand implements CommandExecutor {
    private final PlayerManager playerManager;
    private final AttributeManager attributeManager;
    private final BossBarManager bossBarManager;

    public SetLinhKhiCommand(PlayerManager playerManager, AttributeManager attributeManager, BossBarManager bossBarManager) {
        this.playerManager = playerManager;
        this.attributeManager = attributeManager;
        this.bossBarManager = bossBarManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tutien.admin.setlinhkhi")) return true;
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Sử dụng: /setlinhkhi <tên> <số lượng>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) return true;
        double linhKhiAmount;
        try {
            linhKhiAmount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Số lượng không hợp lệ.");
            return true;
        }
        PlayerData data = playerManager.getPlayerData(target);
        data.setTotalLinhKhi(linhKhiAmount);
        attributeManager.updateAttributes(target, data);
        bossBarManager.updateBossBar(target, data);
        String formattedLinhKhi = NumberFormat.getInstance(Locale.US).format(linhKhiAmount);
        sender.sendMessage(ChatColor.GREEN + "Đã đặt tổng linh khí của " + target.getName() + " thành " + ChatColor.AQUA + formattedLinhKhi);
        return true;
    }
}
