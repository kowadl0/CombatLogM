package me.mooneu.kowal.util;

import me.mooneu.kowal.data.Combat.Combat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DeathUtil {

    private static String broadcastMessage;
    private static String titleText;
    private static String titleSubtitle;

    public static void loadConfigValues(FileConfiguration config) {
        broadcastMessage = config.getString("combat.messages.broadcast", "&cPlayer {killer} killed {victim} with hp {hp}");
        titleText = config.getString("combat.messages.title.text", "&cKILL!");
        titleSubtitle = config.getString("combat.messages.title.subtitletext", "&cYou killed a {oponnent}");
    }

    public static String deathsBroadcastMessage(Player victim, Player killer) {
        double hp = killer.getHealth();

        String finalTitle = titleText;
        String finalSubtitle = titleSubtitle.replace("{oponnent}", victim.getName());

        killer.sendTitle(
                HelpUtil.hexColor(finalTitle),
                HelpUtil.hexColor(finalSubtitle),
                50, 100, 50
        );

        String message = broadcastMessage
                .replace("{killer}", killer.getName())
                .replace("{victim}", victim.getName())
                .replace("{hp}", String.format("%.2f", hp));

        String formattedMessage = HelpUtil.hexColor(message);

        return HelpUtil.hexColor(formattedMessage);
    }

    public static void remove(Combat combat) {
        if (combat == null) return;
        combat.setLastAttackTime(0L);
        combat.setLastAttackPlayer(null);
    }
}