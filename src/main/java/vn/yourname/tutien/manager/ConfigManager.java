package vn.yourname.tutien.manager;

import org.bukkit.configuration.file.FileConfiguration;
import vn.yourname.tutien.TuTienPlugin;

public class ConfigManager {
    public static double TU_LUYEN_BASE_GAIN;
    public static double NGU_KIEM_COST;
    public static double BREAKTHROUGH_FAIL_CHANCE;
    public static double BREAKTHROUGH_PENALTY;
    public static double INJURY_THRESHOLD;
    public static int INJURY_DURATION;
    public static double SUPPRESS_BONUS_PER_REALM;
    public static double SUPPRESS_REDUCTION_PER_REALM;
    public static double SOUL_DRAIN_AMOUNT;
    public static double SOUL_POWER_MULTIPLIER;
    public static double AMULET_THRESHOLD;
    public static int TELEPORT_MIN_DIST;
    public static int TELEPORT_MAX_DIST;

    public static void loadConfig(TuTienPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        TU_LUYEN_BASE_GAIN = config.getDouble("tu-luyen.linh-khi-moi-giay", 3.0);
        NGU_KIEM_COST = config.getDouble("tu-luyen.ngu-kiem-tieu-hao", 5.0);
        BREAKTHROUGH_FAIL_CHANCE = config.getDouble("dot-pha.ty-le-that-bai", 0.25);
        BREAKTHROUGH_PENALTY = config.getDouble("dot-pha.phan-tram-phat", 0.5);
        INJURY_THRESHOLD = config.getDouble("chien-dau.nguong-trong-thuong", 0.03);
        INJURY_DURATION = config.getInt("chien-dau.thoi-gian-trong-thuong", 20);
        SUPPRESS_BONUS_PER_REALM = config.getDouble("chien-dau.ap-che.bonus-moi-canh-gioi", 0.15);
        SUPPRESS_REDUCTION_PER_REALM = config.getDouble("chien-dau.ap-che.giam-tru-moi-canh-gioi", 0.30);
        SOUL_DRAIN_AMOUNT = config.getDouble("tan-hon.linh-khi-hao-mon", 2.0);
        SOUL_POWER_MULTIPLIER = 1.0 - config.getDouble("tan-hon.giam-suc-manh", 0.5);
        AMULET_THRESHOLD = config.getDouble("vat-pham.nguong-the-menh-phu", 0.15);
        TELEPORT_MIN_DIST = config.getInt("vat-pham.dich-chuyen-toi-thieu", 1000);
        TELEPORT_MAX_DIST = config.getInt("vat-pham.dich-chuyen-toi-da", 50000);
    }
}
