package vn.yourname.tutien.manager;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatManager {
    private static final long COMBAT_DURATION = 15 * 1000;
    private final Map<UUID, Long> combatTimers = new HashMap<>();

    public void tag(Player player) {
        combatTimers.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public boolean isInCombat(Player player) {
        if (!combatTimers.containsKey(player.getUniqueId())) return false;
        long lastCombatTime = combatTimers.get(player.getUniqueId());
        return (System.currentTimeMillis() - lastCombatTime) < COMBAT_DURATION;
    }
}
