package com.epam.esm.formatter;

import com.epam.esm.constants.ApplicationConstants;
import org.springframework.format.Formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocaleDateTimeFormatter implements Formatter<LocalDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ApplicationConstants.DATE_PATTERN);

    @Override
    public String print(LocalDateTime object, Locale locale) {
        return formatter.format(object);
    }

    @Override
    public LocalDateTime parse(String text, Locale locale) {
        return LocalDateTime.parse(text, formatter);
    }
}


