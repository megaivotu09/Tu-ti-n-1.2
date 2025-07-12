package vn.yourname.tutien.manager;

import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InjuryManager {
    private final Set<UUID> injuredPlayers = new HashSet<>();
    public boolean isInjured(Player player) { return injuredPlayers.contains(player.getUniqueId()); }
    public void setInjured(Player player) { injuredPlayers.add(player.getUniqueId()); }
    public void removeInjured(Player player) { injuredPlayers.remove(player.getUniqueId()); }
}
