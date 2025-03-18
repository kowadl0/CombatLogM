package me.mooneu.kowal.runnable;

import me.mooneu.kowal.CombatMain;
import me.mooneu.kowal.data.Combat.Combat;
import me.mooneu.kowal.util.DataUtil;
import me.mooneu.kowal.util.HelpUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarRunnable extends BukkitRunnable {

    private final CombatMain plugin;
    private final String combatMessageBar;

    public ActionBarRunnable(CombatMain plugin) {
        this.plugin = plugin;

        FileConfiguration config = plugin.getConfig();
        this.combatMessageBar = HelpUtil.hexColor(config.getString("combat.message-bar", "&cYou are in combat! Time left %time%"));

        this.runTaskTimer(plugin, 20L, 20L);
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!player.isOnline()) return;

            Combat fight = plugin.getCombatManager().getFight(player);
            if (fight != null && fight.hasFight()) {
                long lastAttackTime = fight.getLastAttackTime();
                String formattedTime = DataUtil.secondsToString(lastAttackTime).isEmpty() ? "0s" : DataUtil.secondsToString(lastAttackTime);

                String actionBarMessage = combatMessageBar.replace("%time%", formattedTime);

                player.sendActionBar(actionBarMessage);
            }
        });
    }
}