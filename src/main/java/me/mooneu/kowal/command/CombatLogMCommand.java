package me.mooneu.kowal.command;

import me.mooneu.kowal.CombatMain;
import me.mooneu.kowal.util.HelpUtil;
import me.mooneu.kowal.util.enums.TimeUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public class CombatLogMCommand implements CommandExecutor {

    private final CombatMain plugin;

    private int combatTimer;
    private String allowedCmdMessage;
    private List<String> allowedCommands;
    private List<String> blockedInteractions;
    private String interactionBlockedMessage;
    private long killCooldown;
    private String killCooldownMessage;
    private String combatMessageBar;
    private String broadcastMessage;
    private String titleText;
    private String titleSubtitle;
    private List<String> regions;
    private String regionsmessage;

    public CombatLogMCommand(CombatMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();

        if (!sender.hasPermission("combatlogm.admin.reload")) {
            String noPermissionMessage = config.getString("combat.messages.no-permission", "&cYou do not have permission to reload the config.");
            sender.sendMessage(HelpUtil.fixColor(noPermissionMessage));

            String pluginVersion = plugin.getDescription().getVersion();
            sender.sendMessage(HelpUtil.fixColor("&cCombatLogM - Combat System ver. " + pluginVersion));
            return true;
        }

        plugin.reloadConfig();

        config = CombatMain.getPlugin().getConfig();
        combatTimer = TimeUtil.SECOND.getTime(config.getInt("combat.timer", 31));
        allowedCmdMessage = config.getString("combat.allowed-cmds.message", "This command has been blocked in combat");
        allowedCommands = config.getStringList("combat.allowed-cmds.allow");
        blockedInteractions = config.getStringList("combat.interaction.type-block");
        interactionBlockedMessage = config.getString("combat.interaction.message", "&cThis interaction has been blocked in combat!");
        killCooldown = config.getLong("combat.killcooldown", 3600000L);
        killCooldownMessage = HelpUtil.hexColor(config.getString("combat.messages.killcooldown", "&4You have already killed this player recently! Stats will not be updated."));
        combatMessageBar = HelpUtil.hexColor(config.getString("combat.message-bar", "&cYou are in combat! Time left %time%"));
        broadcastMessage = config.getString("combat.messages.broadcast", "&cPlayer {killer} killed {victim} with hp {hp}");
        titleText = config.getString("combat.messages.title.text", "&cKILL!");
        titleSubtitle = config.getString("combat.messages.title.subtitletext", "&cYou killed a {opponent}");
        regions = config.getStringList("line.regions");
        regionsmessage = config.getString("line.message-line", "&cYou can't join this region in combat");

        String reloadSuccessMessage = config.getString("combat.messages.reload-success", "&aConfig reloaded successfully!");
        sender.sendMessage(HelpUtil.fixColor(reloadSuccessMessage));

        String pluginVersion = plugin.getDescription().getVersion();
        sender.sendMessage(HelpUtil.fixColor("&cCombatLogM - Combat System ver. " + pluginVersion));
        return true;
    }
}