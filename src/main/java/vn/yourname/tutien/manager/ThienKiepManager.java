package vn.yourname.tutien.manager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ThienKiepManager {
    private final Set<UUID> playersInTribulation = new HashSet<>();
    public boolean isInTribulation(UUID playerUUID) { return playersInTribulation.contains(playerUUID); }
    public void startTribulation(UUID playerUUID) { playersInTribulation.add(playerUUID); }
    public void endTribulation(UUID playerUUID) { playersInTribulation.remove(playerUUID); }
}
