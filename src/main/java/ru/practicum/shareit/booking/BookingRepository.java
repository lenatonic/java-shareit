package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query
    List<Booking> findByItemOwnerOrderByStartDesc(Long owner);

    @Transactional
    @Modifying
    @Query(value = "update Booking b set b.status = :status where b.id = :id")
    void approvedBooking(Status status, Long id);

    @Query
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @Query
    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status state);

    @Query
    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end);

    @Query
    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime time);

    @Query
    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime time);

    List<Booking> findByItemOwnerAndStatusOrderByStartDesc(Long idOwner, Status state);

    List<Booking> findByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long idOwner, LocalDateTime start,//!!!!!
                                                                               LocalDateTime end);

    List<Booking> findByItemOwnerAndEndIsBeforeOrderByStartDesc(Long idOwner, LocalDateTime time);

    List<Booking> findByItemOwnerAndStartIsAfterOrderByStartDesc(Long idOwner, LocalDateTime time);

    Booking findTop1BookingByItemIdAndBookerIdAndEndIsBeforeAndStatusOrderByStartDesc(
            Long itemId, Long userId, LocalDateTime time, Status status);

    @Query
    Optional<Booking> findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(Long itemId, Status status, LocalDateTime time);

    @Query
    Optional<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(Long itemId, Status status, LocalDateTime time);
}