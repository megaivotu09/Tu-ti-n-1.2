package vn.yourname.tutien.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.yourname.tutien.util.ItemManager;

public class GiauTuViCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tutien.admin.giautuvi")) {
            sender.sendMessage(ChatColor.RED + "Bạn không có quyền.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Sử dụng: /giautuvi <tên>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Không tìm thấy người chơi.");
            return true;
        }
        target.getInventory().addItem(ItemManager.createAnTucQuyet());
        sender.sendMessage(ChatColor.GREEN + "Đã trao Ẩn Tức Quyết cho " + target.getName());
        return true;
    }
}
