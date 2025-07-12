package vn.yourname.tutien.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.yourname.tutien.util.ItemManager;

public class TuHonDanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tutien.admin.tuhondan")) return true;
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Sử dụng: /tuhondan <tên>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) return true;
        int amount = 1;
        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return true;
            }
        }
        target.getInventory().addItem(ItemManager.createTuHonDan(amount));
        sender.sendMessage(ChatColor.GREEN + "Đã trao " + amount + " Tụ Hồn Đan cho " + target.getName());
        return true;
    }
}
