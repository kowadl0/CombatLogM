package me.mooneu.kowal.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpUtil {


    public static String fixColor(String message) {
        if (message == null || message.isEmpty()) return "";

        return ChatColor.translateAlternateColorCodes('&', message.replace(">>", "»").replace("<<", "«"));
    }

    public static List<String> fixColor(final List<String> msg) {
        final List<String> s = new ArrayList<>();
        for (final String m : msg) {
            s.add(fixColor(m));
        }
        return s;
    }

    public static boolean sendMessage(CommandSender sender, String message) {
        sender.sendMessage(fixColor(message));
        return true;
    }
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String hexColor(String message) {
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, "" + net.md_5.bungee.api.ChatColor.of(color));
            matcher = pattern.matcher(message);
        }

        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }
    public static double round(double value, int decimals) {
        double p = Math.pow(10.0, decimals);
        return Math.round(value * p) / p;
    }
}