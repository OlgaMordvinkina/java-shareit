package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findBookingsByBookerId(Long bookerId, Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start,
                                                                      LocalDateTime end, Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findBookingsByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Pageable pageable);


    Page<Booking> findBookingsByBookerIdAndStatus(Long bookerId, Status status, Pageable pageable);

    Page<Booking> findAllByItem_OwnerId(Long ownerId, Pageable pageable);

    Page<Booking> findAllByItem_OwnerIdAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime start,
                                                                     LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItem_OwnerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findBookingsByItem_OwnerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItem_OwnerIdAndStatus(Long bookerId, Status status, Pageable pageable);

    Booking findFirstByItem_IdAndStartBefore(Long itemId, LocalDateTime start, Sort sort);

    Booking findFirstByItem_IdAndStartAfter(Long itemId, LocalDateTime now, Sort sort);

    List<Booking> findBookingByItem_IdAndBooker_Id(Long itemId, Long bookerId);
}
