package me.mooneu.kowal.handler;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.mooneu.kowal.CombatMain;
import me.mooneu.kowal.data.Combat.Combat;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import java.util.List;

public class CombatLineHandler implements Listener {

    private final CombatMain plugin;
    private final List<String> regions;
    private final String regionsmessage;

    public CombatLineHandler(CombatMain plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();

        this.regions = config.getStringList("line.regions");
        this.regionsmessage = config.getString("line.message-line", "&cYou can't join this region in combat");

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("combatlogm.admin")) {
            return;
        }

        Combat combat = plugin.getCombatManager().getFight(player);
        if (combat == null || !combat.hasFight()) {
            return;
        }

        Location loc = player.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(player.getWorld()));
        if (regionManager == null) return;

        for (String regionName : regions) {
            ProtectedRegion region = regionManager.getRegion(regionName);
            if (region == null) continue;
            if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                player.sendMessage(regionsmessage.replace("&", "§"));
                knockBackPlayer(player);
                event.setCancelled(true);
                return;
            }
        }
    }
    private void knockBackPlayer(Player player) {
        Location loc = player.getLocation();
        Vector knockback = loc.getDirection().multiply(-1).setY(0.5);
        player.setVelocity(knockback);
    }
}