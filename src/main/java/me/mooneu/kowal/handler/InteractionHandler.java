package me.mooneu.kowal.handler;

import me.mooneu.kowal.CombatMain;
import me.mooneu.kowal.data.Combat.Combat;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.List;

public class InteractionHandler implements Listener {

    private final CombatMain plugin;
    private final List<String> blockedInteractions;
    private final String interactionBlockedMessage;

    public InteractionHandler(CombatMain plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();

        this.blockedInteractions = config.getStringList("combat.interaction.type-block");
        this.interactionBlockedMessage = config.getString("combat.interaction.message", "&cThis interaction has been blocked in combat!");

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Combat combat = plugin.getCombatManager().getFight(player);

        if (combat.hasFight()) {
            if (event.getClickedBlock() != null) {
                Material blockType = event.getClickedBlock().getType();

                if (blockedInteractions.contains(blockType.name())) {
                    event.setCancelled(true);
                    player.sendMessage(interactionBlockedMessage.replace("&", "§"));
                }
            }
        }
    }
}
