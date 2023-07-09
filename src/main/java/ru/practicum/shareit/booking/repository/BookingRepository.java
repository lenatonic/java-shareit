package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Modifying
    @Query(value = "update Booking b set b.status = :status where b.id = :id")
    void approvedBooking(Status status, Long id);

    @Query
    Page<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    @Query
    Page<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status state, Pageable pageable);

    @Query
    Page<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query
    Page<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime time, Pageable pageable);

    @Query
    Page<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime time, Pageable pageable);

    Booking findTop1BookingByItemIdAndBookerIdAndEndIsBeforeAndStatusOrderByStartDesc(
            Long itemId, Long userId, LocalDateTime time, Status status);

    @Query
    Optional<Booking> findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(Long itemId, Status status, LocalDateTime time);

    @Query
    Optional<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(Long itemId, Status status, LocalDateTime time);


    Page<Booking> findByItem_Owner_IdOrderByStartDesc(Long owner, Pageable pageable);

    Page<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(Long owner, Status status, Pageable pageable);

    Page<Booking> findAllByItem_Owner_IdAndStartIsAfter(Long owner, LocalDateTime time, Pageable pageable);//future

    Page<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(Long owner, LocalDateTime time1,
                                                                      LocalDateTime time, Pageable pageable);//current

    Page<Booking> findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(Long owner, LocalDateTime time, Pageable pageable);//past
}