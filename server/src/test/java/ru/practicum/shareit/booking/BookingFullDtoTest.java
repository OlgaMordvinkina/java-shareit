package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingFullDtoTest {
    @Autowired
    private JacksonTester<BookingFullDto> json;
    private final LocalDateTime now = LocalDateTime.parse(LocalDate.now() + "T09:55:05.000001");

    @Test
    void serializeTest() throws Exception {
        BookingFullDto bookingDto = new BookingFullDto(1L, now.minusDays(5), now.minusDays(2), new Item(), new User(), Status.WAITING);
        JsonContent<BookingFullDto> result = json.write(bookingDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).hasJsonPath("$.status");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);
        assertThat(result).hasJsonPathValue("$.start");
        assertThat(result).hasJsonPathValue("$.end");
        assertThat(result).hasJsonPathValue("$.item");
        assertThat(result).hasJsonPathValue("$.booker");
        assertThat(result).extractingJsonPathValue("$.status").isEqualTo(Status.WAITING.name());
    }
}
