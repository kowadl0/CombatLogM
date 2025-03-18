package me.mooneu.kowal.handler;

import me.mooneu.kowal.CombatMain;
import me.mooneu.kowal.data.Combat.Combat;
import me.mooneu.kowal.data.Combat.CombatManager;
import me.mooneu.kowal.util.DeathUtil;
import me.mooneu.kowal.util.HelpUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDeathHandler implements Listener {

    private final CombatMain plugin;
    private final CombatManager combatManager;

    private final Map<UUID, Map<UUID, Long>> lastKillTime = new HashMap<>();
    private final long killCooldown;
    private final String killCooldownMessage;

    public PlayerDeathHandler(CombatMain plugin) {
        this.plugin = plugin;
        this.combatManager = plugin.getCombatManager();
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.killCooldown = plugin.getConfig().getLong("combat.killcooldown", 3600000L);
        this.killCooldownMessage = HelpUtil.hexColor(plugin.getConfig().getString("combat.messages.killcooldown", "&4You have already killed this player recently! Stats will not be updated."));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player victim = event.getEntity();
        Combat playerCombat = combatManager.getFight(victim);
        Player killer = victim.getKiller();

        if (killer == null && playerCombat != null && playerCombat.wasFight()) {
            killer = playerCombat.getLastAttackPlayer();
        }

        if (killer != null) {
            Combat killerFight = plugin.getCombatManager().getFight(killer);

            if (killerFight == null || !killerFight.hasFight()) return;

            if (victim.equals(killer)) return;

            long currentTime = System.currentTimeMillis();
            lastKillTime.putIfAbsent(killer.getUniqueId(), new HashMap<>());
            Map<UUID, Long> killerVictims = lastKillTime.get(killer.getUniqueId());

            if (killerVictims.containsKey(victim.getUniqueId())) {
                long lastKill = killerVictims.get(victim.getUniqueId());
                if (currentTime - lastKill < killCooldown) {
                    HelpUtil.sendMessage(killer, killCooldownMessage);
                    Bukkit.broadcastMessage(DeathUtil.deathsBroadcastMessage(victim, killer));
                    return;
                }
            }

            killerVictims.put(victim.getUniqueId(), currentTime);
            Bukkit.broadcastMessage(DeathUtil.deathsBroadcastMessage(victim, killer));
        } else {
            Bukkit.broadcastMessage(HelpUtil.fixColor(plugin.getConfig().getString("combat.messages.nokiller", "{victim} has fallen down").replace("{victim}", victim.getName())));
        }

        DeathUtil.remove(playerCombat);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Combat fight = combatManager.getFight(player);
        DeathUtil.remove(fight);
    }
}