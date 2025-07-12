package vn.yourname.tutien.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import vn.yourname.tutien.TuTienPlugin;

import java.util.Arrays;

public class ItemManager {

    public static ItemStack createTuLuyenItem() {
        ItemStack item = new ItemStack(Material.SLIME_BALL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Tu Luyện");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Một vật phẩm dẫn dắt linh khí trời đất.",
                ChatColor.YELLOW + "Chuột phải để nhập định hoặc kết thúc tu luyện.",
                "",
                ChatColor.DARK_RED + "» Vật Phẩm Linh Hồn «"
        ));
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(new NamespacedKey(TuTienPlugin.getInstance(), "core_item"), PersistentDataType.STRING, "tu_luyen_item");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createDotPhaItem() {
        ItemStack item = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Đột Phá");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Hòn đá dẫn dắt linh khí để phá vỡ bình cảnh.",
                ChatColor.YELLOW + "Chuột phải để thử đột phá cảnh giới.",
                "",
                ChatColor.DARK_RED + "» Vật Phẩm Linh Hồn «"
        ));
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(new NamespacedKey(TuTienPlugin.getInstance(), "core_item"), PersistentDataType.STRING, "dot_pha_item");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createTuHonDan(int amount) {
        ItemStack item = new ItemStack(Material.ECHO_SHARD, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Tụ Hồn Đan");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Đan dược thần kỳ, ngưng tụ linh hồn",
                ChatColor.GRAY + "từ hư không để tái tạo lại thân thể.",
                "",
                ChatColor.YELLOW + "Chuột phải để sử dụng khi là Tàn Hồn."
        ));
        meta.getPersistentDataContainer().set(new NamespacedKey(TuTienPlugin.getInstance(), "item_id"), PersistentDataType.STRING, "tu_hon_dan");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createTheMenhPhu(int amount) {
        ItemStack item = new ItemStack(Material.PAPER, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Thế Mệnh Phù");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Một lá bùa chứa đựng không gian chi lực.",
                ChatColor.GRAY + "Sẽ tự động kích hoạt khi tính mạng gặp nguy.",
                "",
                ChatColor.DARK_GRAY + "Bị động: Kích hoạt khi máu dưới 15%."
        ));
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(new NamespacedKey(TuTienPlugin.getInstance(), "item_id"), PersistentDataType.STRING, "the_menh_phu");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createDaiNaDiPhu(int amount) {
        ItemStack item = new ItemStack(Material.ENDER_PEARL, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Đại Na Di Phù");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Một lá bùa cổ xưa chứa đựng sức mạnh",
                ChatColor.GRAY + "xé rách không gian.",
                "",
                ChatColor.YELLOW + "Chuột phải để sử dụng."
        ));
        meta.addEnchant(Enchantment.SOUL_SPEED, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(new NamespacedKey(TuTienPlugin.getInstance(), "item_id"), PersistentDataType.STRING, "dai_na_di_phu");
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createAnTucQuyet() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Ẩn Tức Quyết");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Một pháp quyết giúp thu liễm linh khí,",
                ChatColor.GRAY + "che giấu tu vi thật sự.",
                "",
                ChatColor.YELLOW + "Đặt vào ô tay phụ (F) để kích hoạt.",
                ChatColor.DARK_GRAY + "Cảnh giới từ Hóa Thần trở lên có thể nhìn thấu."
        ));
        meta.addEnchant(Enchantment.PROTECTION, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(new NamespacedKey(TuTienPlugin.getInstance(), "item_id"), PersistentDataType.STRING, "an_tuc_quyet");
        item.setItemMeta(meta);
        return item;
    }
}
