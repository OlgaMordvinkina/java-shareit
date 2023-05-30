package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {
    @MockBean
    private BookingService service;
    private final ObjectMapper mapper;
    private final MockMvc mvc;
    private final String header = "X-Sharer-User-Id";
    private final LocalDateTime now = LocalDateTime.now().plusHours(2);
    private final BookingDto bookingOne = new BookingDto(1L, now, now.plusDays(2));
    private final BookingDto bookingTwo = new BookingDto(2L, now, now.plusDays(5));

    @Test
    void createBookingTest() throws Exception {
        when(service.createBooking(any(), anyLong())).thenReturn(BookingMapper.toBookingFullDto(bookingOne));

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(header, 1L)
                        .content(mapper.writeValueAsString(bookingOne)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value(Status.WAITING.name()));
    }

    @Test
    void setStatusBookingTest() throws Exception {
        BookingFullDto booking = BookingMapper.toBookingFullDto(bookingOne);
        booking.setStatus(Status.APPROVED);
        when(service.setStatusBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);

        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingOne))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value(Status.APPROVED.name()));
    }

    @Test
    void getBookingTest() throws Exception {
        when(service.getBooking(anyLong(), anyLong())).thenReturn(BookingMapper.toBookingFullDto(bookingOne));

        mvc.perform(get("/bookings/1")
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value(Status.WAITING.name()));
    }

    @Test
    void getBookingsOwnerTest() throws Exception {
        List<BookingDto> listDto = new ArrayList<>();
        listDto.add(bookingOne);
        listDto.add(bookingTwo);

        when(service.getBookings(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(
                listDto.stream()
                        .map(BookingMapper::toBookingFullDto)
                        .collect(Collectors.toList())
        );

        mvc.perform(get("/bookings")
                        .header(header, 1L)
                        .content(mapper.writeValueAsString(listDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void getBookingsTest() throws Exception {
        List<BookingDto> listDto = new ArrayList<>();
        listDto.add(bookingOne);
        listDto.add(bookingTwo);

        when(service.getBookingsOwner(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(
                listDto.stream()
                        .map(BookingMapper::toBookingFullDto)
                        .collect(Collectors.toList())
        );

        mvc.perform(get("/bookings/owner")
                        .content(mapper.writeValueAsString(listDto))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
