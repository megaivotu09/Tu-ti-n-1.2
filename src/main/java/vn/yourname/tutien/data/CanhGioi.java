package vn.yourname.tutien.data;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;

import java.util.LinkedHashMap;
import java.util.Map;

public enum CanhGioi {
    PHAM_NHAN("Phàm Nhân", 1_000, 0, 0, 0, 0, 0, 0.0, 0.0),
    LUYEN_KHI("Luyện Khí", 1_000, 2, 0.5, 0, 0, 0, 0.002, 0.005),
    TRUC_CO("Trúc Cơ", 100_000, 6, 1, 1, 0, 0, 0.005, 0.01),
    KIM_DAN("Kim Đan", 1_000_000, 10, 2, 2, 8, 3.0, 0.008, 0.015),
    NGUYEN_ANH("Nguyên Anh", 5_000_000, 14, 4, 3, 10, 3.5, 0.012, 0.02),
    HOA_THAN("Hóa Thần", 10_000_000, 20, 6, 5, 12, 4.0, 0.016, 0.025),
    DONG_HU("Động Hư", 20_000_000, 30, 8, 7, 15, 4.5, 0.020, 0.03),
    HOP_THE("Hợp Thể", 30_000_000, 40, 12, 10, 18, 5.0, 0.025, 0.035),
    DAI_THUA("Đại Thừa", 50_000_000, 60, 16, 15, 20, 6.0, 0.030, 0.04),
    DO_KIEP("Độ Kiếp", 80_000_000, 100, 25, 20, 0, 0, 0.040, 0.05);

    private static final Map<CanhGioi, Map<Integer, Double>> THRESHOLDS = new LinkedHashMap<>();

    static {
        double cumulativeLinhKhi = 0;
        for (CanhGioi realm : values()) {
            Map<Integer, Double> tierThresholds = new LinkedHashMap<>();
            if (realm == PHAM_NHAN) {
                cumulativeLinhKhi += realm.baseLinhKhi;
                tierThresholds.put(1, cumulativeLinhKhi);
            } else {
                for (int tier = 1; tier <= 9; tier++) {
                    double costForThisTier = realm.baseLinhKhi * (1 + (tier - 1) * 0.5);
                    cumulativeLinhKhi += costForThisTier;
                    tierThresholds.put(tier, cumulativeLinhKhi);
                }
            }
            THRESHOLDS.put(realm, tierThresholds);
        }
    }

    private final String displayName;
    private final double baseLinhKhi;
    private final double baseHealthBonus;
    private final double baseDamageBonus;
    private final double baseDefenseBonus;
    private final int tribulationDuration;
    private final double tribulationDamage;
    private final double baseSpeedBonus;
    private final double baseJumpBonus;

    CanhGioi(String displayName, double baseLinhKhi, double baseHealthBonus, double baseDamageBonus, double baseDefenseBonus, int tribulationDuration, double tribulationDamage, double baseSpeedBonus, double baseJumpBonus) {
        this.displayName = displayName;
        this.baseLinhKhi = baseLinhKhi;
        this.baseHealthBonus = baseHealthBonus;
        this.baseDamageBonus = baseDamageBonus;
        this.baseDefenseBonus = baseDefenseBonus;
        this.tribulationDuration = tribulationDuration;
        this.tribulationDamage = tribulationDamage;
        this.baseSpeedBonus = baseSpeedBonus;
        this.baseJumpBonus = baseJumpBonus;
    }

    public static RealmData getRealmDataFor(double totalLinhKhi) {
        double previousThreshold = 0;
        for (CanhGioi realm : values()) {
            for (int tier = 1; tier <= (realm == PHAM_NHAN ? 1 : 9); tier++) {
                double currentThreshold = THRESHOLDS.get(realm).get(tier);
                if (totalLinhKhi < currentThreshold) {
                    return new RealmData(realm, tier, previousThreshold, currentThreshold);
                }
                previousThreshold = currentThreshold;
            }
        }
        CanhGioi lastRealm = DO_KIEP;
        return new RealmData(lastRealm, 9, previousThreshold, Double.MAX_VALUE);
    }
    
    public static double getTotalLinhKhiFor(CanhGioi targetRealm, int targetTier) {
        double totalLinhKhi = 0;
        for (CanhGioi realm : values()) {
            for (int tier = 1; tier <= (realm == PHAM_NHAN ? 1 : 9); tier++) {
                if (realm == targetRealm && tier == targetTier) {
                    return totalLinhKhi;
                }
                totalLinhKhi = THRESHOLDS.get(realm).get(tier);
            }
        }
        return 0;
    }

    public static class RealmData {
        public final CanhGioi canhGioi;
        public final int tieuCanhGioi;
        public final double linhKhiBatDau;
        public final double linhKhiKetThuc;
        public RealmData(CanhGioi c, int t, double start, double end) {
            this.canhGioi = c;
            this.tieuCanhGioi = t;
            this.linhKhiBatDau = start;
            this.linhKhiKetThuc = end;
        }
    }
    
    // Getters
    public String getDisplayName() { return displayName; }
    public int getTribulationDuration() { return tribulationDuration; }
    public double getTribulationDamage() { return tribulationDamage; }
    public double getHealthBonus(int tier) { if (this == PHAM_NHAN) return 0; return this.baseHealthBonus + (tier - 1) * 2; }
    public double getDamageBonus(int tier) { if (this == PHAM_NHAN) return 0; return this.baseDamageBonus + Math.floor((tier - 1) / 3.0) * 0.5; }
    public double getDefenseBonus(int tier) { if (this == PHAM_NHAN) return 0; return this.baseDefenseBonus + Math.floor((tier - 1) / 2.0); }
    public double getSpeedBonus(int tier) { if (this == PHAM_NHAN) return 0; return this.baseSpeedBonus + (tier - 1) * 0.001; }
    public double getJumpBonus(int tier) { if (this == PHAM_NHAN) return 0; return this.baseJumpBonus + Math.floor((tier - 1) / 2.0) * 0.002; }

    public ChatColor getColor() {
        switch (this) {
            case PHAM_NHAN: return ChatColor.GRAY;
            case LUYEN_KHI: return ChatColor.WHITE;
            case TRUC_CO: return ChatColor.AQUA;
            case KIM_DAN: return ChatColor.GOLD;
            case NGUYEN_ANH: return ChatColor.LIGHT_PURPLE;
            case HOA_THAN: return ChatColor.DARK_PURPLE;
            case DONG_HU: return ChatColor.BLUE;
            case HOP_THE: return ChatColor.RED;
            case DAI_THUA: return ChatColor.DARK_RED;
            case DO_KIEP: return ChatColor.YELLOW;
            default: return ChatColor.WHITE;
        }
    }
    
    public BarColor getBarColor() {
        switch (this) {
            case PHAM_NHAN: case LUYEN_KHI: return BarColor.WHITE;
            case TRUC_CO: return BarColor.BLUE;
            case KIM_DAN: return BarColor.YELLOW;
            case NGUYEN_ANH: case HOA_THAN: return BarColor.PINK;
            case DONG_HU: case HOP_THE: return BarColor.PURPLE;
            case DAI_THUA: return BarColor.RED;
            case DO_KIEP: return BarColor.GREEN;
            default: return BarColor.WHITE;
        }
    }

    public Particle getMeditationParticle() {
        switch (this) {
            case PHAM_NHAN: case LUYEN_KHI: return Particle.WHITE_ASH;
            case TRUC_CO: case KIM_DAN: return Particle.ENCHANT;
            case NGUYEN_ANH: case HOA_THAN: return Particle.SOUL_FIRE_FLAME;
            case DONG_HU: case HOP_THE: return Particle.PORTAL;
            case DAI_THUA: case DO_KIEP: return Particle.DRAGON_BREATH;
            default: return Particle.WHITE_ASH;
        }
    }
}
