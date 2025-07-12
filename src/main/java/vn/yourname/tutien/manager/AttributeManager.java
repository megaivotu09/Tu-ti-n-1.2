package vn.yourname.tutien.manager;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import vn.yourname.tutien.data.CanhGioi;
import vn.yourname.tutien.data.PlayerData;

public class AttributeManager {
    private final SoulManager soulManager;

    public AttributeManager(SoulManager soulManager) {
        this.soulManager = soulManager;
    }

    public void updateAttributes(Player player, PlayerData data) {
        if (data == null) return;
        CanhGioi realm = data.getCanhGioi();
        if (realm == null) return;
        int tier = data.getTieuCanhGioi();
        
        double penaltyMultiplier = soulManager.isRemnantSoul(player) ? ConfigManager.SOUL_POWER_MULTIPLIER : 1.0;
        
        updateAttribute(player, Attribute.GENERIC_MAX_HEALTH, 20.0 + realm.getHealthBonus(tier), penaltyMultiplier);
        updateAttribute(player, Attribute.GENERIC_ATTACK_DAMAGE, 1.0 + realm.getDamageBonus(tier), penaltyMultiplier);
        updateAttribute(player, Attribute.GENERIC_ARMOR, realm.getDefenseBonus(tier), penaltyMultiplier);
        updateAttribute(player, Attribute.GENERIC_MOVEMENT_SPEED, 0.1 + realm.getSpeedBonus(tier), 1.0);
        updateAttribute(player, Attribute.GENERIC_JUMP_STRENGTH, 0.42 + realm.getJumpBonus(tier), 1.0);
    }
    
    private void updateAttribute(Player player, Attribute attribute, double baseValue, double multiplier) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(baseValue * multiplier);
        }
    }
}
