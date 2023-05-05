package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByBookerId(Long bookerId, Sort sort);

    List<Booking> findBookingsByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start,
                                                                      LocalDateTime end, Sort sort);

    List<Booking> findBookingsByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findBookingsByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);


    List<Booking> findBookingsByBookerIdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findAllByItem_OwnerId(Long ownerId, Sort sort);

    List<Booking> findAllByItem_OwnerIdAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime start,
                                                                     LocalDateTime end, Sort sort);

    List<Booking> findAllByItem_OwnerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findBookingsByItem_OwnerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByItem_OwnerIdAndStatus(Long bookerId, Status status, Sort sort);

    Booking findFirstByItem_IdAndStartBefore(Long itemId, LocalDateTime start, Sort sort);

    Booking findFirstByItem_IdAndStartAfter(Long itemId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItem_IdAndBooker_Id(Long itemId, Long bookerId);
}
