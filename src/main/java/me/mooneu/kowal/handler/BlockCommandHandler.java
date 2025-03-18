package me.mooneu.kowal.handler;

import me.mooneu.kowal.CombatMain;
import me.mooneu.kowal.data.Combat.Combat;
import me.mooneu.kowal.util.HelpUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.List;

public class BlockCommandHandler implements Listener {

    private final CombatMain plugin;
    private final String allowedCmdMessage;
    private final List<String> allowedCommands;

    public BlockCommandHandler(CombatMain plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);

        FileConfiguration config = plugin.getConfig();
        this.allowedCmdMessage = config.getString("combat.allowed-cmds.message", "This command has been blocked in combat");
        this.allowedCommands = config.getStringList("combat.allowed-cmds.allow");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        Combat combat = plugin.getCombatManager().getFight(p);

        if (combat.hasFight() && !p.hasPermission("combatlogm.admin")) {
            final String message = e.getMessage().split(" ")[0].toLowerCase();

            if (allowedCommands.contains(message)) {
                return;
            }

            e.setCancelled(true);
            HelpUtil.sendMessage(p, HelpUtil.hexColor(allowedCmdMessage.replace("%command%", message)));
            p.closeInventory();
        }
    }
}