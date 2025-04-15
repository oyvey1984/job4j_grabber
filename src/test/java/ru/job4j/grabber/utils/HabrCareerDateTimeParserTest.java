package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class HabrCareerDateTimeParserTest {

    @Test
    void whenParseIsoOffsetDateTimeThenReturnLocalDateTime() {
        String input = "2024-04-13T14:21:00+03:00";
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        LocalDateTime result = parser.parse(input);
        LocalDateTime expected = LocalDateTime.of(2024, 4, 13, 14, 21);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void whenParseAnotherValidIsoOffsetDateTimeThenReturnCorrectLocalDateTime() {
        String input = "2023-12-01T08:00:00+00:00";
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        LocalDateTime result = parser.parse(input);
        LocalDateTime expected = LocalDateTime.of(2023, 12, 1, 8, 0);
        assertThat(result).isEqualTo(expected);
    }
}