package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingFullDto createBooking(BookingDto bookingDto, Long bookerId) throws NotValidDataException {
        User user = userExist(bookerId);
        Booking booking = BookingMapper.toBooking(bookingDto, itemExist(bookingDto.getItemId()), user);
        if (!booking.getItem().getAvailable()) {
            throw new NotValidDataException("Вещь с ID: " + booking.getId() + " недоступна");
        }
        if (Objects.equals(booking.getItem().getOwner().getId(), bookerId)) {
            throw new AccessException("Вы не можете арендовать свою вещь");
        }
        return BookingMapper.toBookingFullDto(repository.save(booking));
    }

    @Transactional
    @Override
    public BookingFullDto setStatusBooking(Long bookingId, Long userId, Boolean approved) throws NotValidDataException {
        Booking booking = bookingExist(bookingId);
        User user = userExist(userId);
        if (!Objects.equals(user.getId(), booking.getItem().getOwner().getId())) {
            throw new AccessException("Вы не являeтесь владельцем вещи c ID: " + bookingId);
        }
        if (Objects.equals(booking.getStatus(), Status.APPROVED)) {
            throw new NotValidDataException("Нельзя изменить статус после одобрения");
        }
        status:
        if (Objects.equals(booking.getStatus(), Status.WAITING)) {
            if (!approved && Objects.equals(userId, booking.getBooker().getId())) {
                booking.setStatus(Status.CANCELED);
                break status;
            }
            booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        }
        return BookingMapper.toBookingFullDto(repository.save(booking));
    }

    @Transactional(readOnly = true)
    @Override
    public BookingFullDto getBooking(Long bookingId, Long userId) {
        Booking booking = bookingExist(bookingId);
        User user = userExist(userId);
        if (!Objects.equals(user.getId(), booking.getItem().getOwner().getId())
                && !Objects.equals(user.getId(), booking.getBooker().getId())) {
            throw new AccessException("У вас нет доступа для просмотра бронирования с ID: " + bookingId);
        }
        return BookingMapper.toBookingFullDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingFullDto> getBookings(String state, Long bookerId) throws NotValidDataException {
        userExist(bookerId);
        LocalDateTime now = LocalDateTime.now();
        Sort sortDescByStart = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings;
        switch (state) {
            case ("ALL"):
                bookings = repository.findBookingsByBookerId(bookerId, sortDescByStart);
                break;
            case ("CURRENT"):
                bookings = repository.findBookingsByBookerIdAndStartIsBeforeAndEndIsAfter(bookerId, now, now, sortDescByStart);
                break;
            case ("PAST"):
                bookings = repository.findBookingsByBookerIdAndEndIsBefore(bookerId, now, sortDescByStart);
                break;
            case ("FUTURE"):
                bookings = repository.findBookingsByBookerIdAndStartIsAfter(bookerId, now, sortDescByStart);
                break;
            case ("WAITING"):
                bookings = repository.findBookingsByBookerIdAndStatus(bookerId, Status.WAITING, sortDescByStart);
                break;
            case ("REJECTED"):
                bookings = repository.findBookingsByBookerIdAndStatus(bookerId, Status.REJECTED, sortDescByStart);
                break;
            default:
                throw new NotValidDataException("Unknown state: " + state);
        }
        return bookings.stream()
                .map(BookingMapper::toBookingFullDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingFullDto> getBookingsOwner(String state, Long ownerId) throws NotValidDataException {
        userExist(ownerId);
        LocalDateTime now = LocalDateTime.now();
        Sort sortDescByStart = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings;
        switch (state) {
            case ("ALL"):
                bookings = repository.findAllByItem_OwnerId(ownerId, sortDescByStart);
                break;
            case ("CURRENT"):
                bookings = repository.findAllByItem_OwnerIdAndStartIsBeforeAndEndIsAfter(ownerId, now, now, sortDescByStart);
                break;
            case ("PAST"):
                bookings = repository.findAllByItem_OwnerIdAndEndIsBefore(ownerId, now, sortDescByStart);
                break;
            case ("FUTURE"):
                bookings = repository.findBookingsByItem_OwnerIdAndStartIsAfter(ownerId, now, sortDescByStart);
                break;
            case ("WAITING"):
                bookings = repository.findAllByItem_OwnerIdAndStatus(ownerId, Status.WAITING, sortDescByStart);
                break;
            case ("REJECTED"):
                bookings = repository.findAllByItem_OwnerIdAndStatus(ownerId, Status.REJECTED, sortDescByStart);
                break;
            default:
                throw new NotValidDataException("Unknown state: " + state);
        }
        return bookings.stream()
                .map(BookingMapper::toBookingFullDto)
                .collect(Collectors.toList());
    }

    private Item itemExist(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundItemException(id));
    }

    private User userExist(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundUserException(id));
    }

    private Booking bookingExist(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundBookingException(id));
    }

}
