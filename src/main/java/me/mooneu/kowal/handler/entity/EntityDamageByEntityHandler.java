package me.mooneu.kowal.handler.entity;

import me.mooneu.kowal.CombatMain;
import me.mooneu.kowal.data.Combat.Combat;
import me.mooneu.kowal.data.Combat.CombatManager;
import me.mooneu.kowal.util.enums.TimeUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityHandler implements Listener {

    private final CombatMain plugin;
    private final CombatManager combatManager;
    private final long combatTimer;

    public EntityDamageByEntityHandler(CombatMain plugin) {
        this.plugin = plugin;
        this.combatManager = plugin.getCombatManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        FileConfiguration config = plugin.getConfig();
        this.combatTimer = TimeUtil.SECOND.getTime(config.getInt("combat.timer", 31));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Player)) return;
        Player damagedPlayer = (Player) e.getEntity();
        Player damagerPlayer = getDamager(e);
        if (damagerPlayer == null || damagedPlayer.equals(damagerPlayer)) return;

        Combat damagedFight = combatManager.getFight(damagedPlayer);
        Combat damagerFight = combatManager.getFight(damagerPlayer);
        if (damagedFight == null || damagerFight == null) return;

        long currentTime = System.currentTimeMillis();
        long attackTimeout = currentTime + combatTimer;

        damagedFight.setLastAttackTime(attackTimeout);
        damagerFight.setLastAttackTime(attackTimeout);
        damagedFight.setLastAttackPlayer(damagerPlayer);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onArrow(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                Combat fight = combatManager.getFight(shooter);
                if (fight.hasFight()) {
                    fight.setLastAttackTime(System.currentTimeMillis() + combatTimer);
                }
            }
        }
    }

    private Player getDamager(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (damager instanceof Player) return (Player) damager;
        if (damager instanceof Projectile) {
            Projectile projectile = (Projectile) damager;
            if (projectile.getShooter() instanceof Player) return (Player) projectile.getShooter();
        }
        return null;
    }

    @EventHandler
    public void offPearlDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager().getType() == EntityType.ENDER_PEARL) {
            event.setCancelled(true);
        }
    }
}