package vn.yourname.tutien.manager;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MeditationManager {
    private final Map<UUID, ArmorStand> meditatingPlayers = new HashMap<>();
    public boolean isMeditating(Player player) { return meditatingPlayers.containsKey(player.getUniqueId()); }
    public void startMeditation(Player player, ArmorStand seat) { meditatingPlayers.put(player.getUniqueId(), seat); }
    public void stopMeditation(Player player) {
        ArmorStand seat = meditatingPlayers.remove(player.getUniqueId());
        if (seat != null) {
            seat.remove();
        }
    }
    public ArmorStand getSeat(Player player) { return meditatingPlayers.get(player.getUniqueId()); }
    public Map<UUID, ArmorStand> getMeditatingPlayers() { return meditatingPlayers; }
}
