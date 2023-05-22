package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.exceptions.AccessException;
import ru.practicum.shareit.exceptions.NotFoundBookingException;
import ru.practicum.shareit.exceptions.NotValidDataException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    private BookingService service;
    private final Long id = 1L;
    private final LocalDateTime now = LocalDateTime.parse(LocalDate.now() + "T09:55:05.000001");
    private final BookingDto bookingDto = new BookingDto(1L, now.minusDays(5), now.minusDays(2));
    private final ItemDto itemDto = new ItemDto(1L, "name", "description", true, 1L);
    private final Item item = ItemMapper.toItem(itemDto);
    private final User user = new User(id, "name", "email@bk.ru");
    private final Booking booking = BookingMapper.toBooking(bookingDto, item, user);


    @BeforeEach
    void beforeEach() {
        booking.setId(id);
        item.setOwner(user);
        service = new BookingServiceImpl(itemRepository, userRepository, bookingRepository);
    }

    @Test
    void createBooking_BookingFound_thenReturnedBookingFullDto() throws NotValidDataException {
        item.getOwner().setId(2L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingFullDto expectedBooking = service.createBooking(bookingDto, id);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(bookingDto);
        bookingFullDto.setItem(item);

        assertEquals(bookingFullDto.getId(), expectedBooking.getId());
        assertEquals(bookingFullDto.getStart(), expectedBooking.getStart());
        assertEquals(bookingFullDto.getEnd(), expectedBooking.getEnd());
        verify(bookingRepository).save(any());
    }

    @Test
    void createBooking_itemNotAvailable_thenReturnedNotValidDataException() {
        item.setAvailable(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(NotValidDataException.class, () -> service.createBooking(bookingDto, id));
    }

    @Test
    void createBooking_bookingYourItems_thenReturnedNotValidDataException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(AccessException.class, () -> service.createBooking(bookingDto, id));
    }

    @Test
    void setStatusBooking_BookingFound_thenReturnedBookingFullDto() throws NotValidDataException {
        itemDto.setAvailable(false);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingFullDto expectedBooking = service.setStatusBooking(id, id, true);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, item, user);

        assertEquals(bookingFullDto, expectedBooking);
        verify(bookingRepository).save(any());
    }

    @Test
    void setStatusBooking_userNotOwnerItems_thenReturnedNotValidDataException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(AccessException.class, () -> service.setStatusBooking(id, id, false));
    }

    @Test
    void setStatusBooking_bookingYourItems_thenReturnedNotValidDataException() {
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(NotValidDataException.class, () -> service.setStatusBooking(id, id, false));
    }

    @Test
    void setStatusBooking_userNotOwnerItems_thenReturnedBookingFullDto() throws NotValidDataException {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingFullDto expectedBooking = service.setStatusBooking(id, id, false);
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(booking, item, user);


        assertEquals(bookingFullDto, expectedBooking);
        verify(bookingRepository).save(any());
    }

    @Test
    void getBooking_BookingFound_thenReturnedBookingFullDto() {
        BookingFullDto bookingFullDto = BookingMapper.toBookingFullDto(bookingDto);
        bookingFullDto.setItem(item);
        bookingFullDto.setBooker(user);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        BookingFullDto expectedBooking = service.getBooking(id, id);

        assertEquals(bookingFullDto, expectedBooking);
    }

    @Test
    void getBooking_BookingNotFound_thenReturnedNotFoundBookingException() {
        when(bookingRepository.findById(anyLong())).thenThrow(new NotFoundBookingException(id));

        assertThrows(NotFoundBookingException.class, () -> service.getBooking(id, id));
    }

    @Test
    void getBooking_notAccessToBooking_thenReturnedAccessException() {
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User(2L, "name", "description")));

        assertThrows(AccessException.class, () -> service.getBooking(id, id));
    }

    @Test
    void getBookings_stateAll_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findBookingsByBookerId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookings("ALL", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }

    @Test
    void getBookings_stateCurrent_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findBookingsByBookerIdAndStartIsBeforeAndEndIsAfter(
                anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookings("CURRENT", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }

    @Test
    void getBookings_statePast_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findBookingsByBookerIdAndEndIsBefore(
                anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookings("PAST", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }

    @Test
    void getBookings_stateFuture_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findBookingsByBookerIdAndStartIsAfter(
                anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookings("FUTURE", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }

    @Test
    void getBookings_stateWaiting_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findBookingsByBookerIdAndStatus(
                anyLong(),
                any(Status.class),
                any(Pageable.class)
        ))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookings("WAITING", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }

    @Test
    void getBookings_stateRejected_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findBookingsByBookerIdAndStatus(
                anyLong(),
                any(Status.class),
                any(Pageable.class)
        ))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookings("REJECTED", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }

    @Test
    void getBookings_notValidState_thenReturnedNotValidDataException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(NotValidDataException.class, () -> service.getBookings("Unknown", id, 1, 10));
    }

    @Test
    void getBookingsOwner_notValidState_thenReturnedNotValidDataException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        assertThrows(NotValidDataException.class, () -> service.getBookingsOwner("Unknown", id, 1, 10));
    }

    @Test
    void getBookingsOwner_stateAll_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findAllByItem_OwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookingsOwner("ALL", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }

    @Test
    void getBookingsOwner_stateCurrent_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findAllByItem_OwnerIdAndStartIsBeforeAndEndIsAfter(
                anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookingsOwner("CURRENT", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }

    @Test
    void getBookingsOwner_statePast_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findAllByItem_OwnerIdAndEndIsBefore(
                anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookingsOwner("PAST", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }

    @Test
    void getBookingsOwner_stateFuture_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findBookingsByItem_OwnerIdAndStartIsAfter(
                anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class)
        ))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookingsOwner("FUTURE", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }

    @Test
    void getBookingsOwner_stateWaiting_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findAllByItem_OwnerIdAndStatus(
                anyLong(),
                any(Status.class),
                any(Pageable.class)
        ))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookingsOwner("WAITING", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }

    @Test
    void getBookingsOwner_stateRejected_thenReturnedListBookingFullDto() throws ValidationException {
        when(bookingRepository.findAllByItem_OwnerIdAndStatus(
                anyLong(),
                any(Status.class),
                any(Pageable.class)
        ))
                .thenReturn(new PageImpl<>(Collections.singletonList(booking)));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingFullDto> expectedBooking = service.getBookingsOwner("REJECTED", id, 0, 10);

        assertNotNull(expectedBooking);
        assertFalse(expectedBooking.isEmpty());
    }
}