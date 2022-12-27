package com.disney.teams.log.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoTimeLog implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(AutoTimeLog.class);

    public static int DEFAULT_TIMEOUT_WARNING_MILLIS = 100;

    private final StackTraceElement stack;

    private final long start;

    private final int defaultTimeoutWarningMillis;

    public AutoTimeLog(int defaultTimeoutWarningMillis) {
        this(null, defaultTimeoutWarningMillis);
    }

    public AutoTimeLog(StackTraceElement stack) {
        this(stack, DEFAULT_TIMEOUT_WARNING_MILLIS);
    }

    public AutoTimeLog(StackTraceElement stack, int defaultTimeoutWarningMillis) {
        this.start = System.currentTimeMillis();
        this.stack = stack;
        this.defaultTimeoutWarningMillis = defaultTimeoutWarningMillis;
    }

    @Override
    public void close() {
        long escape = System.currentTimeMillis() - start;
        if (escape >= defaultTimeoutWarningMillis) {
            log.warn("Call method {} spent {}ms", stack, escape);
        }
    }

    /**
     * 使用方式
     * try(AutoTimeLog ignore = AutoTimeLog.warn()) {...}
     */
    public static AutoTimeLog warn() {
        StackTraceElement[] stacks = new Exception().getStackTrace();
        StackTraceElement stack = stacks[1];
        return new AutoTimeLog(stack);
    }

    /**
     * 使用方式
     * try(AutoTimeLog ignore = AutoTimeLog.warn(100)) {...}
     */
    public static AutoTimeLog warn(int defaultTimeoutWarningMillis) {
        StackTraceElement[] stacks = new Exception().getStackTrace();
        StackTraceElement stack = stacks[1];
        return new AutoTimeLog(stack, defaultTimeoutWarningMillis);
    }

    public AutoTimeLog warning() {
        return warn(defaultTimeoutWarningMillis);
    }
}
