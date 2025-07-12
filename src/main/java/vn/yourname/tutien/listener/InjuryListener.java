package vn.yourname.tutien.listener;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import vn.yourname.tutien.manager.CombatManager;
import vn.yourname.tutien.manager.ConfigManager;
import vn.yourname.tutien.manager.InjuryManager;

public class InjuryListener implements Listener {
    private final JavaPlugin plugin;
    private final CombatManager combatManager;
    private final InjuryManager injuryManager;

    public InjuryListener(JavaPlugin plugin, CombatManager combatManager, InjuryManager injuryManager) {
        this.plugin = plugin;
        this.combatManager = combatManager;
        this.injuryManager = injuryManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player attacker = (Player) event.getDamager();
        if (injuryManager.isInjured(attacker)) {
            attacker.sendMessage(ChatColor.RED + "Bạn bị trọng thương, không thể tấn công!");
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        if (event.getDamager() instanceof Player) {
            combatManager.tag((Player) event.getDamager());
        }
        combatManager.tag(victim);

        double maxHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double currentHealth = victim.getHealth() - event.getFinalDamage();
        if (currentHealth <= (maxHealth * ConfigManager.INJURY_THRESHOLD) && combatManager.isInCombat(victim) && !injuryManager.isInjured(victim)) {
            applyGrievousInjury(victim);
        }
    }

    private void applyGrievousInjury(Player player) {
        injuryManager.setInjured(player);
        player.sendTitle(ChatColor.DARK_RED + "TRỌNG THƯƠNG!", ChatColor.RED + "Bạn mất khả năng chiến đấu!", 10, 40, 10);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, ConfigManager.INJURY_DURATION * 20, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, ConfigManager.INJURY_DURATION * 20, 4));
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (injuryManager.isInjured(player)) {
                injuryManager.removeInjured(player);
                player.removePotionEffect(PotionEffectType.SLOWNESS);
                player.removePotionEffect(PotionEffectType.WEAKNESS);
                player.sendMessage(ChatColor.GREEN + "Bạn đã tạm thời hồi phục.");
            }
        }, ConfigManager.INJURY_DURATION * 20L);
    }
}
