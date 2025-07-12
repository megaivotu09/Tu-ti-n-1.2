package vn.yourname.tutien.manager;

import org.bukkit.entity.Player;
import vn.yourname.tutien.data.PlayerData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    public void loadPlayerData(Player player) {
        playerDataMap.put(player.getUniqueId(), new PlayerData());
    }
    public void unloadPlayerData(Player player) {
        playerDataMap.remove(player.getUniqueId());
    }
    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }
}
