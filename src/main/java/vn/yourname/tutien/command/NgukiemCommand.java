package vn.yourname.tutien.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import vn.yourname.tutien.data.CanhGioi;
import vn.yourname.tutien.data.PlayerData;
import vn.yourname.tutien.manager.PlayerManager;
import vn.yourname.tutien.manager.SwordFlyManager;

public class NgukiemCommand implements CommandExecutor {
    private final PlayerManager playerManager;
    private final SwordFlyManager swordFlyManager;

    public NgukiemCommand(PlayerManager playerManager, SwordFlyManager swordFlyManager) {
        this.playerManager = playerManager;
        this.swordFlyManager = swordFlyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (swordFlyManager.isFlying(player)) {
            stopSwordFly(player);
        } else {
            startSwordFly(player);
        }
        return true;
    }

    private void startSwordFly(Player player) {
        PlayerData data = playerManager.getPlayerData(player);
        CanhGioi realm = data.getCanhGioi();
        int tier = data.getTieuCanhGioi();
        boolean canFly = (realm.ordinal() > CanhGioi.LUYEN_KHI.ordinal()) || (realm == CanhGioi.LUYEN_KHI && tier >= 5);
        if (!canFly) {
            player.sendMessage(ChatColor.RED + "Cảnh giới chưa đủ để Ngự Kiếm Phi Hành!");
            return;
        }
        if (!player.isOnGround()) {
            player.sendMessage(ChatColor.RED + "Phải đứng trên mặt đất để ngự kiếm.");
            return;
        }
        player.sendMessage(ChatColor.AQUA + "Ngự Kiếm Khởi!");
        player.setAllowFlight(true);
        player.setFlying(true);
        Location swordLocation = player.getLocation().subtract(0, 1.4, 0);
        ArmorStand sword = (ArmorStand) player.getWorld().spawnEntity(swordLocation, EntityType.ARMOR_STAND);
        sword.setVisible(false);
        sword.setGravity(false);
        sword.setSmall(true);
        sword.getEquipment().setHelmet(new ItemStack(Material.IRON_SWORD));
        swordFlyManager.startFlying(player, sword);
    }
    
    public void stopSwordFly(Player player) {
        if (!swordFlyManager.isFlying(player)) return;
        player.sendMessage(ChatColor.YELLOW + "Bạn đã hạ kiếm.");
        player.setFlying(false);
        if (player.getGameMode() != org.bukkit.GameMode.CREATIVE) {
            player.setAllowFlight(false);
        }
        swordFlyManager.stopFlying(player);
    }
}
