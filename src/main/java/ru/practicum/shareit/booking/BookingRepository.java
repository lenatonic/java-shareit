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
    List<Booking> findAllByItemOwnerOrderByStartDesc(Long Owner);

    @Transactional
    @Modifying
    @Query(value = "update Booking b set b.status = :status where b.id = :id")
    void approvedBooking(Status status, Long id);

    @Query
    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    @Query
    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status state);

    @Query
    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end);

    @Query
    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime time);

    @Query
    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime time);

    List<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(Long idOwner, Status state);

    List<Booking> findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long idOwner, LocalDateTime start,//!!!!!
                                                                                  LocalDateTime end);

    List<Booking> findAllByItemOwnerAndEndIsBeforeOrderByStartDesc(Long idOwner, LocalDateTime time);

    List<Booking> findAllByItemOwnerAndStartIsAfterOrderByStartDesc(Long idOwner, LocalDateTime time);

    Optional<Booking> findTop1BookingByItemIdAndEndIsBeforeAndStatusOrderByStartDesc(Long idItem,
                                                                                     LocalDateTime time, Status status);

    Optional<Booking> findTop1BookingByItemIdAndEndIsAfterAndStatusOrderByStartDesc(Long idItem,//!!!
                                                                                   LocalDateTime time, Status status);
    Optional<Booking> findTop1BookingByItemIdAndEndIsAfterAndStatusOrderByStartAsc(Long idItem,//!!!
                                                                                    LocalDateTime time, Status status);

    Booking findTop1BookingByItemIdAndBookerIdAndEndIsBeforeAndStatusOrderByStartDesc(
            Long itemId, Long userId, LocalDateTime time, Status status);
}
