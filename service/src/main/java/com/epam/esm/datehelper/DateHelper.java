package com.epam.esm.datehelper;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class provides a current time as String in ISO 8601 format.
 *
 * @since 1.0
 */
public class DateHelper {

    public static String getNowAsString() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
