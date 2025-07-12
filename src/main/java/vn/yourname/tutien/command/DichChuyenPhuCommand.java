package vn.yourname.tutien.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.yourname.tutien.util.ItemManager;

public class DichChuyenPhuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tutien.admin.dichchuyenphu")) {
            sender.sendMessage(ChatColor.RED + "Bạn không có quyền sử dụng lệnh này.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Sử dụng: /dichchuyenphu <tên>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Không tìm thấy người chơi.");
            return true;
        }
        int amount = 1;
        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Số lượng không hợp lệ.");
                return true;
            }
        }
        target.getInventory().addItem(ItemManager.createDaiNaDiPhu(amount));
        sender.sendMessage(ChatColor.GREEN + "Đã trao " + amount + " Đại Na Di Phù cho " + target.getName());
        return true;
    }
}
