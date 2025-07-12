package vn.yourname.tutien.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.yourname.tutien.data.LinhCan;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.PlayerManager;

public class SetLinhCanCommand implements CommandExecutor {
    private final PlayerManager playerManager;
    public SetLinhCanCommand(PlayerManager playerManager) { this.playerManager = playerManager; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tutien.admin.setlinhcan")) return true;
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Sử dụng: /setlinhcan <tên> <linh căn>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) return true;
        LinhCan targetLinhCan;
        try {
            targetLinhCan = LinhCan.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Tên linh căn không hợp lệ.");
            return true;
        }
        PlayerData data = playerManager.getPlayerData(target);
        data.setLinhCan(targetLinhCan);
        sender.sendMessage(ChatColor.GREEN + "Đã đặt linh căn của " + target.getName());
        return true;
    }
}
