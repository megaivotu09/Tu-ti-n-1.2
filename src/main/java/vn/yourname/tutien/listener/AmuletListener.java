// ... Các import khác ...
import org.bukkit.attribute.Attribute;
// ...

public class AmuletListener implements Listener {
    // ...
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerTakeFatalDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute == null) return;
        
        double maxHealth = maxHealthAttribute.getValue();
        double currentHealth = player.getHealth();
        double damage = event.getFinalDamage();

        if ((currentHealth - damage) <= (maxHealth * ConfigManager.AMULET_THRESHOLD)) {
            // ...
        }
    }

    // ...
    private void activateTheMenhPhu(Player player) {
        // ...
        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute != null) {
             player.setHealth(Math.min(player.getHealth() + maxHealthAttribute.getValue() * 0.2, maxHealthAttribute.getValue()));
        }
        player.sendMessage(ChatColor.GOLD + "Thế Mệnh Phù đã kích hoạt, cứu ngươi một mạng!");
    }
    //...
}
