package me.mooneu.kowal.util;

import java.text.SimpleDateFormat;
import java.util.*;

public class DataUtil {

    private static final SimpleDateFormat timeFormat;
    private static final LinkedHashMap<Integer, String> values;
    private static final LinkedHashMap<Integer, String> values1;

    public static String secondsToString(final long l) {
        int seconds = (int) ((l - System.currentTimeMillis()) / 1000L);
        final StringBuilder sb = new StringBuilder();
        if (seconds < 1) {
            return "0s";
        }
        for (final Map.Entry<Integer, String> e : DataUtil.values.entrySet()) {
            final int iDiv = seconds / e.getKey();
            if (iDiv >= 1) {
                final int x = (int) Math.floor(iDiv);
                sb.append(x).append(e.getValue());
                seconds -= x * e.getKey();
            }
        }
        if (sb.toString().equals("")) {
            return "0s";
        }
        return sb.toString();
    }
    static {
        timeFormat = new SimpleDateFormat("HH:mm");
        (values = new LinkedHashMap<>(6)).put(31104000, "y, ");
        DataUtil.values.put(2592000, "m, ");
        DataUtil.values.put(86400, "d, ");
        DataUtil.values.put(3600, "h, ");
        DataUtil.values.put(60, "m, ");
        DataUtil.values.put(1, "s");
        (values1 = new LinkedHashMap<>(6)).put(60, "m");
        DataUtil.values1.put(1, "s");
    }
}