package vn.yourname.tutien.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import vn.yourname.tutien.TuTienPlugin;
import vn.yourname.tutien.manager.AttributeManager;
import vn.yourname.tutien.manager.ConfigManager;
import vn.yourname.tutien.manager.PlayerManager;
import vn.yourname.tutien.manager.SoulManager;

import java.util.Random;

public class AmuletListener implements Listener {
    private final TuTienPlugin plugin;
    private final NamespacedKey itemIdKey;
    private final SoulManager soulManager;
    private final PlayerManager playerManager;
    private final AttributeManager attributeManager;

    public AmuletListener(TuTienPlugin plugin, SoulManager sm, PlayerManager pm, AttributeManager am) {
        this.plugin = plugin;
        this.soulManager = sm;
        this.playerManager = pm;
        this.attributeManager = am;
        this.itemIdKey = new NamespacedKey(plugin, "item_id");
    }

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
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item != null && item.hasItemMeta()) {
                    String itemId = item.getItemMeta().getPersistentDataContainer().get(itemIdKey, PersistentDataType.STRING);
                    if (itemId != null && itemId.equals("the_menh_phu")) {
                        event.setCancelled(true);
                        item.setAmount(item.getAmount() - 1);
                        player.getInventory().setItem(i, item);
                        activateTheMenhPhu(player);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return;

        String itemId = item.getItemMeta().getPersistentDataContainer().get(itemIdKey, PersistentDataType.STRING);
        if (itemId == null) return;
        
        event.setCancelled(true);
        switch (itemId) {
            case "tu_hon_dan":
                if (soulManager.isRemnantSoul(player)) {
                    soulManager.removeRemnantSoul(player);
                    attributeManager.updateAttributes(player, playerManager.getPlayerData(player));
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "TỤ HỒN THÀNH CÔNG!");
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.sendMessage(ChatColor.RED + "Linh hồn của ngươi không bị tổn thương.");
                }
                break;
            case "dai_na_di_phu":
                activateDaiNaDiPhu(player, item);
                break;
        }
    }

    private void activateTheMenhPhu(Player player) {
        Location currentLocation = player.getLocation();
        Random random = new Random();
        int attempt = 0;
        Location safeLocation = null;

        while (attempt < 20 && safeLocation == null) {
            int x = currentLocation.getBlockX() + random.nextInt(200) - 100;
            int z = currentLocation.getBlockZ() + random.nextInt(200) - 100;
            Location randomLoc = currentLocation.getWorld().getHighestBlockAt(x, z).getLocation().add(0.5, 1, 0.5);
            if (isLocationSafe(randomLoc)) {
                safeLocation = randomLoc;
            }
            attempt++;
        }
        if (safeLocation == null) {
            safeLocation = player.getWorld().getSpawnLocation();
        }

        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation().add(0, 1, 0), 100);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        player.teleport(safeLocation);
        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute != null) {
             player.setHealth(Math.min(player.getHealth() + maxHealthAttribute.getValue() * 0.2, maxHealthAttribute.getValue()));
        }
        player.sendMessage(ChatColor.GOLD + "Thế Mệnh Phù đã kích hoạt, cứu ngươi một mạng!");
    }
    
    private boolean isLocationSafe(Location location) {
        Material feet = location.getBlock().getType();
        Material head = location.clone().add(0, 1, 0).getBlock().getType();
        return !feet.isSolid() && !head.isSolid();
    }
    
    private void activateDaiNaDiPhu(Player player, ItemStack amulet) {
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Đại Na Di Phù đang được kích hoạt...");
        amulet.setAmount(amulet.getAmount() - 1);
        player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5f, 1.0f);
        
        new BukkitRunnable() {
            int countdown = 2;
            @Override
            public void run() {
                if (countdown > 0) {
                    player.getWorld().spawnParticle(Particle.REVERSE_PORTAL, player.getLocation().add(0, 1, 0), 50);
                    countdown--;
                } else {
                    this.cancel();
                    Random random = new Random();
                    double distance = ConfigManager.TELEPORT_MIN_DIST + (ConfigManager.TELEPORT_MAX_DIST - ConfigManager.TELEPORT_MIN_DIST) * random.nextDouble();
                    double angle = random.nextDouble() * 2 * Math.PI;
                    Location loc = player.getLocation();
                    double newX = loc.getX() + distance * Math.cos(angle);
                    double newZ = loc.getZ() + distance * Math.sin(angle);
                    
                    Location newLocation = new Location(player.getWorld(), newX, 0, newZ);
                    player.getWorld().getChunkAtAsync(newLocation).thenAccept(chunk -> {
                         Location safeGroundLocation = player.getWorld().getHighestBlockAt((int)newX, (int)newZ).getLocation().add(0.5, 1.5, 0.5);
                         player.teleport(safeGroundLocation);
                         player.getWorld().spawnParticle(Particle.PORTAL, safeGroundLocation, 100);
                         player.sendMessage(ChatColor.GREEN + "Bạn đã được dịch chuyển đến một vùng đất xa lạ!");
                    });
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
