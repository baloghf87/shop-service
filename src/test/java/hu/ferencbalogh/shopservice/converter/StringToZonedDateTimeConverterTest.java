package hu.ferencbalogh.shopservice.converter;

import hu.ferencbalogh.shopservice.exception.DateTimeFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.*;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(properties = {
        "datetime-pattern=yyyy-MM-dd'TTT'HH:mm:ss",
        "default-timezone=UTC"})
@ContextConfiguration(classes = {StringToZonedDateTimeConverter.class})
public class StringToZonedDateTimeConverterTest {

    @Autowired
    private StringToZonedDateTimeConverter converter;

    @Test
    public void convertDateTimeWithTimezone() {
        //given
        String input = "2018-12-05T13:15:30-00:00";
        ZonedDateTime expectedResult = ZonedDateTime.of(LocalDateTime.of(2018, 12, 5, 13, 15, 30), ZoneId.of("UTC"));

        //when
        ZonedDateTime result = converter.convert(input);

        //then
        assertTrue(expectedResult.isEqual(result));
    }

    @Test
    public void convertDateTimeWithTimezone2() {
        //given
        String input = "2018-12-05T13:15:30+01:00";
        ZonedDateTime expectedResult = ZonedDateTime.of(LocalDateTime.of(2018, 12, 5, 12, 15, 30), ZoneId.of("UTC"));

        //when
        ZonedDateTime result = converter.convert(input);

        //then
        assertTrue(expectedResult.isEqual(result));
    }

    @Test
    public void convertDateTimeWithoutTimezone() {
        //given
        String input = "2018-12-05TTT13:15:30";
        ZonedDateTime expectedResult = ZonedDateTime.of(LocalDateTime.of(2018, 12, 5, 13, 15, 30), ZoneId.of("UTC"));

        //when
        ZonedDateTime result = converter.convert(input);

        //then
        assertTrue(expectedResult.isEqual(result));
    }

    @Test
    public void convertDate() {
        //given
        String input = "2018-12-05";
        ZonedDateTime expectedResult = ZonedDateTime.of(LocalDate.of(2018, 12, 5), LocalTime.MIDNIGHT, ZoneId.of("UTC"));

        //when
        ZonedDateTime result = converter.convert(input);

        //then
        assertTrue(expectedResult.isEqual(result));
    }

    @Test(expected = DateTimeFormatException.class)
    public void failOnInvalidFormat() {
        //given
        String input = "this is not good";

        //when
        converter.convert(input);

        //then it should fail
    }
}