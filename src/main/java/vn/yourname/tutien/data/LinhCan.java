package vn.yourname.tutien.data;

import org.bukkit.ChatColor;

public enum LinhCan {
    PHE_LINH_CAN("Phế Linh Căn", 0.0, ChatColor.DARK_GRAY, 20.0),
    HA_LINH_CAN("Hạ Linh Căn", 1.0, ChatColor.WHITE, 35.0),
    TRUNG_LINH_CAN("Trung Linh Căn", 3.0, ChatColor.AQUA, 30.0),
    THUONG_LINH_CAN("Thượng Linh Căn", 5.0, ChatColor.GOLD, 10.0),
    THANH_LINH_CAN("Thánh Linh Căn", 10.0, ChatColor.LIGHT_PURPLE, 5.0);

    private final String displayName;
    private final double multiplier;
    private final ChatColor color;
    private final double weight;

    LinhCan(String displayName, double multiplier, ChatColor color, double weight) {
        this.displayName = displayName;
        this.multiplier = multiplier;
        this.color = color;
        this.weight = weight;
    }

    public String getDisplayName() { return displayName; }
    public double getMultiplier() { return multiplier; }
    public ChatColor getColor() { return color; }

    public static LinhCan getRandom() {
        double totalWeight = 0.0;
        for (LinhCan linhCan : values()) {
            totalWeight += linhCan.weight;
        }
        double random = Math.random() * totalWeight;
        for (LinhCan linhCan : values()) {
            random -= linhCan.weight;
            if (random <= 0.0) {
                return linhCan;
            }
        }
        return PHE_LINH_CAN;
    }
}
