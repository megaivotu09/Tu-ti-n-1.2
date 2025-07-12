package vn.yourname.tutien;

import org.bukkit.plugin.java.JavaPlugin;
import vn.yourname.tutien.command.*;
import vn.yourname.tutien.listener.*;
import vn.yourname.tutien.manager.*;
import vn.yourname.tutien.task.*;

public class TuTienPlugin extends JavaPlugin {
    private static TuTienPlugin instance;
    
    private PlayerManager playerManager;
    private MeditationManager meditationManager;
    private SwordFlyManager swordFlyManager;
    private AttributeManager attributeManager;
    private BossBarManager bossBarManager;
    private ThienKiepManager thienKiepManager;
    private CombatManager combatManager;
    private InjuryManager injuryManager;
    private SoulManager soulManager;
    
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ConfigManager.loadConfig(this);
        
        // Initialize Managers in correct order
        this.playerManager = new PlayerManager();
        this.meditationManager = new MeditationManager();
        this.swordFlyManager = new SwordFlyManager();
        this.thienKiepManager = new ThienKiepManager();
        this.combatManager = new CombatManager();
        this.injuryManager = new InjuryManager();
        this.bossBarManager = new BossBarManager(playerManager);
        this.attributeManager = new AttributeManager();
        this.soulManager = new SoulManager(this, playerManager, attributeManager, bossBarManager);

        // Register Commands
        NgukiemCommand ngukiemCommand = new NgukiemCommand(playerManager, swordFlyManager);
        getCommand("tuvi").setExecutor(new TuViCommand(playerManager));
        getCommand("dotpha").setExecutor(new DotPhaCommand(this, playerManager, bossBarManager, attributeManager, thienKiepManager));
        getCommand("tuluyen").setExecutor(new TuLuyenCommand(meditationManager));
        getCommand("ngukiem").setExecutor(ngukiemCommand);
        getCommand("anki").setExecutor(new AnKiCommand(playerManager));
        getCommand("xoaan").setExecutor(new XoaAnCommand(playerManager));
        getCommand("dsanki").setExecutor(new DSAnKiCommand(playerManager));
        getCommand("setcanhgioi").setExecutor(new SetCanhGioiCommand(playerManager, bossBarManager, attributeManager));
        getCommand("setlinhcan").setExecutor(new SetLinhCanCommand(playerManager));
        getCommand("setlinhkhi").setExecutor(new SetLinhKhiCommand(playerManager, attributeManager, bossBarManager));
        getCommand("tuhondan").setExecutor(new TuHonDanCommand());
        getCommand("themenhphu").setExecutor(new TheMenhPhuCommand());
        getCommand("dichchuyenphu").setExecutor(new DichChuyenPhuCommand());
        getCommand("giautuvi").setExecutor(new GiauTuViCommand());

        // Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveListener(this, playerManager, soulManager, attributeManager, bossBarManager), this);
        getServer().getPluginManager().registerEvents(new CoreItemListener(this), this);
        getServer().getPluginManager().registerEvents(new SuppressionListener(playerManager, soulManager), this);
        getServer().getPluginManager().registerEvents(new SwordFlyListener(swordFlyManager, ngukiemCommand), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this, playerManager, attributeManager, bossBarManager, soulManager), this);
        getServer().getPluginManager().registerEvents(new InjuryListener(this, combatManager, injuryManager), this);
        getServer().getPluginManager().registerEvents(new AmuletListener(this), this);
        
        // Start Tasks
        new ConcealmentListener(this, playerManager, bossBarManager).runTaskTimer(this, 0L, 20L);
        new CultivationTask(playerManager, meditationManager, bossBarManager).runTaskTimer(this, 0L, 20L);
        new ParticleTask(meditationManager, playerManager).runTaskTimer(this, 0L, 4L);
        new SoulDrainTask(soulManager).runTaskTimer(this, 1200L, 1200L);
        new SwordFlyTask(playerManager, swordFlyManager, ngukiemCommand).runTaskTimer(this, 0L, 1L);
        new TrackingTask(playerManager).runTaskTimer(this, 0L, 20L);
        
        getLogger().info("TuTienPlugin da duoc bat!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("TuTienPlugin da duoc tat!");
    }
    
    public static TuTienPlugin getInstance() {
        return instance;
    }
}
