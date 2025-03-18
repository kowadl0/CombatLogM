package me.mooneu.kowal.data;

import me.mooneu.kowal.CombatMain;
import me.mooneu.kowal.data.Combat.Combat;
import me.mooneu.kowal.data.Combat.CombatManager;
import me.mooneu.kowal.util.DeathUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RegisterData implements Listener {

    private final CombatMain plugin;
    private final CombatManager combatManager;

    public RegisterData(CombatMain plugin, CombatManager combatManager) {
        this.plugin = plugin;
        this.combatManager = combatManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Combat combat = plugin.getCombatManager().getFight(player);

        if (combat == null) {
            plugin.getCombatManager().createFight(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Combat combat = plugin.getCombatManager().getFight(player);

        if (combat.hasFight()) {
            player.setHealth(0.0);
            DeathUtil.remove(combat);
        }
        combatManager.removeFight(player);
    }
}