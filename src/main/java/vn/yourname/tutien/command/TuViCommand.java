package vn.yourname.tutien.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.yourname.tutien.data.CanhGioi;
import vn.yourname.tutien.data.LinhCan;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.PlayerManager;

import java.text.NumberFormat;
import java.util.Locale;

public class TuViCommand implements CommandExecutor {
    private final PlayerManager playerManager;
    public TuViCommand(PlayerManager playerManager) { this.playerManager = playerManager; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player viewer = (Player) sender;
        if (args.length == 0 || (args.length > 0 && Bukkit.getPlayer(args[0]) == viewer)) {
            displayOwnInfo(viewer);
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                viewer.sendMessage(ChatColor.RED + "Không tìm thấy người chơi.");
                return true;
            }
            displayTargetInfo(viewer, target);
        }
        return true;
    }

    private void displayOwnInfo(Player player) {
        PlayerData data = playerManager.getPlayerData(player);
        if (data == null) return;
        player.sendMessage(ChatColor.GOLD + "--- Thông Tin Tu Luyện Của Bạn ---");
        displayFullInfo(player, data);
        player.sendMessage(ChatColor.GOLD + "---------------------------------");
    }

    private void displayTargetInfo(Player viewer, Player target) {
        PlayerData viewerData = playerManager.getPlayerData(viewer);
        PlayerData targetData = playerManager.getPlayerData(target);
        if (viewerData == null || targetData == null) return;

        CanhGioi viewerRealm = viewerData.getCanhGioi();
        CanhGioi targetRealm = targetData.getCanhGioi();
        boolean canSeeThroughAnything = viewerRealm.ordinal() >= CanhGioi.HOA_THAN.ordinal();

        if (targetData.isTuViHidden() && !canSeeThroughAnything) {
            displayConcealedInfo(viewer, target);
            return;
        }
        if (!canSeeThroughAnything && viewerRealm.ordinal() <= targetRealm.ordinal()) {
            displayConcealedInfo(viewer, target);
            return;
        }

        viewer.sendMessage(ChatColor.GOLD + "--- Thông Tin Tu Luyện của " + target.getName() + " ---");
        displayFullInfo(viewer, targetData);
        viewer.sendMessage(ChatColor.GOLD + "---------------------------------");
    }

    private void displayFullInfo(CommandSender sender, PlayerData data) {
        CanhGioi.RealmData realmData = CanhGioi.getRealmDataFor(data.getTotalLinhKhi());
        String canhGioiDisplay = realmData.canhGioi.getColor() + realmData.canhGioi.getDisplayName();
        if (realmData.canhGioi != CanhGioi.PHAM_NHAN) {
            canhGioiDisplay += " - Tầng " + realmData.tieuCanhGioi;
        }
        sender.sendMessage(ChatColor.YELLOW + "Cảnh giới: " + canhGioiDisplay);

        double linhKhiForLevel = data.getTotalLinhKhi() - realmData.linhKhiBatDau;
        double costOfLevel = realmData.linhKhiKetThuc - realmData.linhKhiBatDau;
        sender.sendMessage(ChatColor.YELLOW + "Linh Khí: " + ChatColor.AQUA + String.format("%,.0f / %,.0f", linhKhiForLevel, costOfLevel));

        LinhCan linhCan = data.getLinhCan();
        if (linhCan != null) {
            sender.sendMessage(ChatColor.YELLOW + "Linh Căn: " + linhCan.getColor() + linhCan.getDisplayName());
        }
    }

    private void displayConcealedInfo(Player viewer, Player target) {
        viewer.sendMessage(ChatColor.GOLD + "--- Thông Tin Tu Luyện của " + target.getName() + " ---");
        viewer.sendMessage(ChatColor.YELLOW + "Cảnh giới: " + ChatColor.DARK_GREEN + "Không thể nhìn thấu");
        viewer.sendMessage(ChatColor.GOLD + "---------------------------------");
        viewer.playSound(viewer.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.5f, 1.2f);
    }
}
