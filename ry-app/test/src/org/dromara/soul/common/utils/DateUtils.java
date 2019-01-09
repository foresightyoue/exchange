package org.dromara.soul.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    private static final String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * parseLocalDateTime.
     * out put format:yyyy-MM-dd HH:mm:ss
     *
     * @param str date String
     * @return yyyy-MM-dd HH:mm:ss
     * @see LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(final String str) {
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }

    public static Date getDateYYYY() {
        LocalDateTime localDateTime = parseLocalDateTime(getCurrentDateTime());
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }


    /**
     * acquireMinutesBetween.
     *
     * @param start this is start date.
     * @param end   this is start date.
     * @return The number of days between start and end, if end is after start, returns a positive number, otherwise returns a negative number
     */
    public static long acquireMinutesBetween(final LocalDateTime start, final LocalDateTime end) {
        return start.until(end, ChronoUnit.MINUTES);
    }
}
