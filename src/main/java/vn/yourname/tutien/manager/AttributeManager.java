package vn.yourname.tutien.manager; // Đảm bảo dòng này ở đầu tiên

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import vn.yourname.tutien.data.CanhGioi;
import vn.yourname.tutien.data.PlayerData;

// Đảm bảo dòng này tồn tại
public class AttributeManager { 

    private final SoulManager soulManager;

    public AttributeManager(SoulManager soulManager) {
        this.soulManager = soulManager;
    }

    public void updateAttributes(Player player, PlayerData data) {
        if (data == null) return;
        CanhGioi realm = data.getCanhGioi();
        if (realm == null) return; // Thêm kiểm tra null để an toàn
        int tier = data.getTieuCanhGioi();
        
        double penaltyMultiplier = soulManager.isRemnantSoul(player) ? ConfigManager.SOUL_POWER_MULTIPLIER : 1.0;

        // Cập nhật Máu Tối đa
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue((20.0 + realm.getHealthBonus(tier)) * penaltyMultiplier);
        }

        // Cập nhật Sát thương
        AttributeInstance attackDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.setBaseValue((1.0 + realm.getDamageBonus(tier)) * penaltyMultiplier);
        }

        // Cập nhật Phòng ngự
        AttributeInstance armor = player.getAttribute(Attribute.GENERIC_ARMOR);
        if (armor != null) {
            armor.setBaseValue(realm.getDefenseBonus(tier) * penaltyMultiplier);
        }

        // Cập nhật Tốc độ Di chuyển
        AttributeInstance movementSpeed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (movementSpeed != null) {
            movementSpeed.setBaseValue(0.1 + realm.getSpeedBonus(tier));
        }

        // Cập nhật Sức bật Nhảy
        AttributeInstance jumpStrength = player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH);
        if (jumpStrength != null) {
            jumpStrength.setBaseValue(0.42 + realm.getJumpBonus(tier));
        }
    }
} // Đảm bảo có dấu ngoặc đóng này
