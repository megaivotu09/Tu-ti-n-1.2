package vn.yourname.tutien.manager;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SwordFlyManager {
    private final Map<UUID, ArmorStand> flyingSwords = new ConcurrentHashMap<>();
    public boolean isFlying(Player player) { return flyingSwords.containsKey(player.getUniqueId()); }
    public void startFlying(Player player, ArmorStand sword) { flyingSwords.put(player.getUniqueId(), sword); }
    public void stopFlying(Player player) {
        ArmorStand sword = flyingSwords.remove(player.getUniqueId());
        if (sword != null) {
            sword.remove();
        }
    }
    public Map<UUID, ArmorStand> getFlyingPlayers() { return flyingSwords; }
}
