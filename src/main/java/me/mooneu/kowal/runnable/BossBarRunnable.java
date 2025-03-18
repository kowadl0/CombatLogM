package me.mooneu.kowal.runnable;

import me.mooneu.kowal.CombatMain;
import me.mooneu.kowal.data.Combat.Combat;
import me.mooneu.kowal.util.DataUtil;
import me.mooneu.kowal.util.HelpUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;

public class BossBarRunnable extends BukkitRunnable {

    private final CombatMain plugin;
    private final Map<Player, BossBar> fightBars;
    private final String combatMessageBar;

    public BossBarRunnable(CombatMain plugin) {
        this.plugin = plugin;
        this.fightBars = new HashMap<>();

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

                String title = combatMessageBar.replace("%time%", formattedTime);

                BossBar fightBar = fightBars.get(player);

                if (fightBar == null) {
                    fightBar = Bukkit.createBossBar(title, BarColor.RED, BarStyle.SOLID);
                    fightBars.put(player, fightBar);
                    fightBar.addPlayer(player);
                }

                fightBar.setTitle(title);
            } else {
                BossBar fightBar = fightBars.remove(player);
                if (fightBar != null) {
                    fightBar.removeAll();
                }
            }
        });
    }
}