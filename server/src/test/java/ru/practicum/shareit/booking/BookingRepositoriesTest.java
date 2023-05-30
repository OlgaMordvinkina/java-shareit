package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingRepositoriesTest {
    @Mock
    private BookingRepository bookingRepository;
    private final Long id = 1L;
    private final LocalDateTime now = LocalDateTime.parse(LocalDate.now() + "T09:55:05.000001");
    private final BookingDto bookingDto = new BookingDto(1L, now.minusDays(5), now.minusDays(2));
    private final Booking booking = BookingMapper.toBooking(bookingDto, new Item(), new User());
    private final Page<Booking> bookings = new PageImpl<>(Collections.singletonList(booking));

    @BeforeEach
    void beforeEach() {
        booking.setId(id);
    }

    @Test
    void findBookingsByBookerId_BookingFound_thenReturnedPageBooking() {
        when(bookingRepository.findBookingsByBookerId(anyLong(), any(Pageable.class)))
                .thenReturn(bookings);

        assertEquals(bookings, bookingRepository.findBookingsByBookerId(id, Pageable.unpaged()));
    }

    @Test
    void findBookingsByBookerIdAndStartIsBeforeAndEndIsAfter_BookingFound_thenReturnedPageBooking() {
        when(bookingRepository.findBookingsByBookerIdAndStartIsBeforeAndEndIsAfter(
                anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(bookings);

        assertEquals(bookings, bookingRepository.findBookingsByBookerIdAndStartIsBeforeAndEndIsAfter(
                id,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Pageable.unpaged()
        ));
    }

    @Test
    void findBookingsByBookerIdAndEndIsBefore_BookingFound_thenReturnedPageBooking() {
        when(bookingRepository.findBookingsByBookerIdAndEndIsBefore(
                anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(bookings);

        assertEquals(bookings, bookingRepository.findBookingsByBookerIdAndEndIsBefore(
                id,
                LocalDateTime.now(),
                Pageable.unpaged()
        ));
    }

    @Test
    void findBookingsByBookerIdAndStartIsAfter_BookingFound_thenReturnedPageBooking() {
        when(bookingRepository.findBookingsByBookerIdAndStartIsAfter(
                anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(bookings);

        assertEquals(bookings, bookingRepository.findBookingsByBookerIdAndStartIsAfter(
                id,
                LocalDateTime.now(),
                Pageable.unpaged()
        ));
    }

    @Test
    void findBookingsByBookerIdAndStatus_BookingFound_thenReturnedPageBooking() {
        when(bookingRepository.findBookingsByBookerIdAndStatus(
                anyLong(),
                any(Status.class),
                any(Pageable.class)
        ))
                .thenReturn(bookings);

        assertEquals(bookings, bookingRepository.findBookingsByBookerIdAndStatus(
                id,
                Status.APPROVED,
                Pageable.unpaged()
        ));
    }

    @Test
    void findAllByItem_OwnerId_BookingFound_thenReturnedPageBooking() {
        when(bookingRepository.findAllByItem_OwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(bookings);

        assertEquals(bookings, bookingRepository.findAllByItem_OwnerId(id, Pageable.unpaged()));
    }

    @Test
    void findAllByItem_OwnerIdAndStartIsBeforeAndEndIsAfter_BookingFound_thenReturnedPageBooking() {
        when(bookingRepository.findAllByItem_OwnerIdAndStartIsBeforeAndEndIsAfter(
                anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(bookings);

        assertEquals(bookings, bookingRepository.findAllByItem_OwnerIdAndStartIsBeforeAndEndIsAfter(
                id,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Pageable.unpaged()
        ));
    }

    @Test
    void findAllByItem_OwnerIdAndEndIsBefore_BookingFound_thenReturnedPageBooking() {
        when(bookingRepository.findAllByItem_OwnerIdAndEndIsBefore(
                anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(bookings);

        assertEquals(bookings, bookingRepository.findAllByItem_OwnerIdAndEndIsBefore(
                id,
                LocalDateTime.now(),
                Pageable.unpaged()
        ));
    }

    @Test
    void findBookingsByItem_OwnerIdAndStartIsAfter_BookingFound_thenReturnedPageBooking() {
        when(bookingRepository.findBookingsByItem_OwnerIdAndStartIsAfter(
                anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(bookings);

        assertEquals(bookings, bookingRepository.findBookingsByItem_OwnerIdAndStartIsAfter(
                id,
                LocalDateTime.now(),
                Pageable.unpaged()
        ));
    }

    @Test
    void findAllByItem_OwnerIdAndStatus_BookingFound_thenReturnedPageBooking() {
        when(bookingRepository.findAllByItem_OwnerIdAndStatus(
                anyLong(),
                any(Status.class),
                any(Pageable.class)
        ))
                .thenReturn(bookings);

        assertEquals(bookings, bookingRepository.findAllByItem_OwnerIdAndStatus(
                id,
                Status.APPROVED,
                Pageable.unpaged()
        ));
    }

    @Test
    void findFirstByItem_IdAndStartBefore_BookingFound_thenReturnedBooking() {
        when(bookingRepository.findFirstByItem_IdAndStartBefore(
                anyLong(),
                any(LocalDateTime.class),
                any(Sort.class)
        ))
                .thenReturn(booking);

        assertEquals(booking, bookingRepository.findFirstByItem_IdAndStartBefore(
                id,
                LocalDateTime.now(),
                Sort.unsorted()
        ));
    }

    @Test
    void findFirstByItem_IdAndStartAfter_BookingFound_thenReturnedBooking() {
        when(bookingRepository.findFirstByItem_IdAndStartAfter(
                anyLong(),
                any(LocalDateTime.class),
                any(Sort.class)
        ))
                .thenReturn(booking);

        assertEquals(booking, bookingRepository.findFirstByItem_IdAndStartAfter(
                id,
                LocalDateTime.now(),
                Sort.unsorted()
        ));
    }

    @Test
    void findBookingByItem_IdAndBooker_Id_BookingFound_thenReturnedListBooking() {
        ArrayList<Booking> bookingsList = new ArrayList<>(Collections.singletonList(booking));
        when(bookingRepository.findBookingByItem_IdAndBooker_Id(anyLong(), anyLong()))
                .thenReturn(bookingsList);

        assertEquals(bookingsList, bookingRepository.findBookingByItem_IdAndBooker_Id(id, id));
    }
}