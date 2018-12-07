package hu.ferencbalogh.shopservice.converter;

import hu.ferencbalogh.shopservice.exception.DateTimeFormatException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class StringToZonedDateTimeConverter implements Converter<String, ZonedDateTime> {

    @Value("${api.datetime.format}")
    private String dateTimePattern;

    @Value("${api.datetime.timezone}")
    private String defaultZoneId;

    private ZoneId defaultTimeZone;

    private DateTimeFormatter formatter;

    @PostConstruct
    void initialize() {
        formatter = DateTimeFormatter.ofPattern(dateTimePattern).withZone(defaultTimeZone);
        defaultTimeZone = ZoneId.of(defaultZoneId);
    }

    @Override
    public ZonedDateTime convert(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        ZonedDateTime result = parseDateTimeWithZone(value);
        if (result == null) {
            result = parseDateTimeWithoutZone(value);
        }
        if (result == null) {
            result = parseDateOnly(value);
        }
        if (result == null) {
            throw new DateTimeFormatException("Could not parse date: " + value);
        }

        return result;
    }

    private ZonedDateTime parseDateTimeWithZone(String value) {
        try {
            return ZonedDateTime.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private ZonedDateTime parseDateTimeWithoutZone(String value) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(value, formatter);
            return ZonedDateTime.of(localDateTime, defaultTimeZone);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private ZonedDateTime parseDateOnly(String value) {
        try {
            return ZonedDateTime.of(LocalDate.parse(value), LocalTime.MIDNIGHT, defaultTimeZone);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}