package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {
    @MockBean
    private BookingClient client;
    private final ObjectMapper mapper;
    private final MockMvc mvc;
    private final String header = "X-Sharer-User-Id";
    private final LocalDateTime now = LocalDateTime.now().plusHours(2);
    private final BookItemRequestDto bookingOne = new BookItemRequestDto(1L, now, now.plusDays(2));

    @Test
    void createBookingStartInPastTest() throws Exception {
        bookingOne.setStart(now.minusDays(10));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingOne))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBookingEndInPastTest() throws Exception {
        bookingOne.setEnd(now.minusDays(10));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingOne))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
