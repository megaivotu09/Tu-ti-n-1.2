package vn.yourname.tutien.manager;

import org.bukkit.attribute.Attribute; import org.bukkit.attribute.AttributeInstance; import org.bukkit.entity.Player; import vn.yourname.tutien.data.CanhGioi; import vn.yourname.tutien.data.PlayerData;

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

    AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
    if (maxHealth != null) {
        maxHealth.setBaseValue((20.0 + realm.getHealthBonus(tier)) * penaltyMultiplier);
    }

    AttributeInstance attackDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
    if (attackDamage != null) {
        attackDamage.setBaseValue((1.0 + realm.getDamageBonus(tier)) * penaltyMultiplier);
    }

    AttributeInstance armor = player.getAttribute(Attribute.GENERIC_ARMOR);
    if (armor != null) {
        armor.setBaseValue(realm.getDefenseBonus(tier) * penaltyMultiplier);
    }

    AttributeInstance movementSpeed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
    if (movementSpeed != null) {
        movementSpeed.setBaseValue(0.1 + realm.getSpeedBonus(tier));
    }

    AttributeInstance jumpStrength = player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH);
    if (jumpStrength != null) {
        jumpStrength.setBaseValue(0.42 + realm.getJumpBonus(tier));
    }
}

}

