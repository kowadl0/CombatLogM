package me.mooneu.kowal;

import me.mooneu.kowal.data.Combat.CombatManager;
import me.mooneu.kowal.data.Combat.impl.CombatManagerImpl;
import me.mooneu.kowal.data.RegisterData;
import me.mooneu.kowal.handler.BlockCommandHandler;
import me.mooneu.kowal.handler.InteractionHandler;
import me.mooneu.kowal.handler.PlayerDeathHandler;
import me.mooneu.kowal.handler.entity.EntityDamageByEntityHandler;
import me.mooneu.kowal.runnable.ActionBarRunnable;
import me.mooneu.kowal.runnable.BossBarRunnable;
import me.mooneu.kowal.util.DeathUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatMain extends JavaPlugin {

    private static CombatMain instance;
    private CombatManager combatManager;

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