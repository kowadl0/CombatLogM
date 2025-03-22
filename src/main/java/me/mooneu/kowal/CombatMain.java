package me.mooneu.kowal;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.mooneu.kowal.command.CombatLogMCommand;
import me.mooneu.kowal.data.Combat.CombatManager;
import me.mooneu.kowal.data.Combat.impl.CombatManagerImpl;
import me.mooneu.kowal.data.RegisterData;
import me.mooneu.kowal.handler.BlockCommandHandler;
import me.mooneu.kowal.handler.CombatLineHandler;
import me.mooneu.kowal.handler.InteractionHandler;
import me.mooneu.kowal.handler.PlayerDeathHandler;
import me.mooneu.kowal.handler.entity.EntityDamageByEntityHandler;
import me.mooneu.kowal.runnable.ActionBarRunnable;
import me.mooneu.kowal.runnable.BossBarRunnable;
import me.mooneu.kowal.util.DeathUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatMain extends JavaPlugin {

    private static CombatMain instance;
    private CombatManager combatManager;
    private WorldGuardPlugin wgPlugin;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info(" ");
        getLogger().info("███    ███  ██████   ██████  ███    ██ ███████ ██    ██                                   ");
        getLogger().info("████  ████ ██    ██ ██    ██ ████   ██ ██      ██    ██                                   ");
        getLogger().info("██ ████ ██ ██    ██ ██    ██ ██ ██  ██ █████   ██    ██                                   ");
        getLogger().info("██  ██  ██ ██    ██ ██    ██ ██  ██ ██ ██      ██    ██                                   ");
        getLogger().info("██      ██  ██████   ██████  ██   ████ ███████  ██████                                   ");
        getLogger().info("                                                                                          ");
        getLogger().info("                                                                                          ");
        getLogger().info(" ██████  ██████  ███    ███ ██████   █████  ████████ ██       ██████   ██████  ███    ███ ");
        getLogger().info("██      ██    ██ ████  ████ ██   ██ ██   ██    ██    ██      ██    ██ ██       ████  ████ ");
        getLogger().info("██      ██    ██ ██ ████ ██ ██████  ███████    ██    ██      ██    ██ ██   ███ ██ ████ ██ ");
        getLogger().info("██      ██    ██ ██  ██  ██ ██   ██ ██   ██    ██    ██      ██    ██ ██    ██ ██  ██  ██ ");
        getLogger().info(" ██████  ██████  ██      ██ ██████  ██   ██    ██    ███████  ██████   ██████  ██      ██ ");
        getLogger().info(" ");

        int pluginId = 25146;
        new Metrics(this, pluginId);
        getLogger().info("bStats initialized! https://bstats.org/plugin/bukkit/CombatLogM/25146");

        saveDefaultConfig();

        DeathUtil.loadConfigValues(getConfig());

        this.getCommand("combatlogm").setExecutor(new CombatLogMCommand(this));
        getLogger().info("Loaded command: combatlogm");

        registerManagers();

        new PlayerDeathHandler(this);
        new EntityDamageByEntityHandler(this);
        new BlockCommandHandler(this);
        new RegisterData(this, combatManager);

        boolean interactionActive = getConfig().getBoolean("combat.interaction.active", true);

        if (interactionActive) {
            new InteractionHandler(this);
            getLogger().info("InteractionHandler has been loaded");
        } else {
            getLogger().info("InteractionHandler is disabled in config");
        }

        String combatType = getConfig().getString("combat.type", "BOSSBAR");

        if (combatType.equalsIgnoreCase("BOSSBAR")) {
            new BossBarRunnable(this);
            getLogger().info("message-bar = BOSSBAR");
        } else if (combatType.equalsIgnoreCase("ACTIONBAR")) {
            new ActionBarRunnable(this);
            getLogger().info("message-bar = ACTIONBAR");
        }

        boolean worldguardActive = getConfig().getBoolean("line.enabled", true);

        if (worldguardActive) {
            wgPlugin = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");

            if (wgPlugin == null) {
                getLogger().severe("WorldGuard not found!");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            new CombatLineHandler(this);
            getLogger().info("WorldGuardHook enabled");
        } else {
            getLogger().info("WorldGuardHook disabled");
        }
    }

    @Override
    public void onDisable() {
        combatManager.getFights().clear();
    }

    private void registerManagers() {
        this.combatManager = new CombatManagerImpl(this);
    }

    public static CombatMain getInstance() {
        return instance;
    }
    public static CombatMain getPlugin() {
        return CombatMain.instance;
    }
    public CombatManager getCombatManager() {
        return combatManager;
    }
}