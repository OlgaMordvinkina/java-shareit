package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingShortDtoTest {
    @Autowired
    private JacksonTester<BookingShortDto> json;
    private final LocalDateTime now = LocalDateTime.parse(LocalDate.now() + "T09:55:05.000001");

    @Test
    void serializeTest() throws Exception {
        BookingShortDto bookingDto = new BookingShortDto(1L, 2L, now.minusDays(5), now.minusDays(2));
        JsonContent<BookingShortDto> result = json.write(bookingDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.bookerId");
        assertThat(result).hasJsonPath("$.startTime");
        assertThat(result).hasJsonPath("$.endTime");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.bookerId").isEqualTo(2);
        assertThat(result).hasJsonPathValue("$.startTime");
        assertThat(result).hasJsonPathValue("$.endTime");
    }
}
