package me.mooneu.kowal.util.enums;

import java.text.SimpleDateFormat;

public enum TimeUtil {

    TICK(50, 50),
    MILLISECOND(1, 1),
    SECOND(1000, 1000),
    MINUTE(60000, 60),
    HOUR(3600000, 60),
    DAY(86400000, 24),
    WEEK(604800000, 7);

    public static final int MPT = 50;
    private final int time;
    private final int timeMultiplier;
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public static final String SECOND_LABEL = "s";
    public static final String MINUTE_LABEL = "m";
    public static final String HOUR_LABEL = "h";

    TimeUtil(final int time, final int timeMultiplier) {
        this.time = time;
        this.timeMultiplier = timeMultiplier;
    }

    public int getTick() {
        return this.time / MPT;
    }

    public int getTime(final int multiplier) {
        return this.time * multiplier;
    }
}