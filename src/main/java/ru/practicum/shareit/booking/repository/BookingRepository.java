package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select b from Booking b left join Item i on i.owner = b.item.owner where i.owner = :owner order by b.start desc")
    List<Booking>findByItemOwner(@Param("owner") Long owner);
//
//    @Query(value = "select b from Booking b where b.status = :status and b.item.owner.id = :owner order by b.start desc")
//    List<Booking> findByItemOwnerAndStatus(@Param("owner") Long idOwner, @Param("status") Status state);
//
//    @Query
//    List<Booking> findByItemOwnerOrderByStartDesc(Long owner);

    @Modifying
    @Query(value = "update Booking b set b.status = :status where b.id = :id")
    void approvedBooking(Status status, Long id);

    @Query
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @Query
    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status state);

//    @Query
//    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartAsc(
//            Long bookerId, LocalDateTime start, LocalDateTime end);

    @Query
    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end);

    @Query
    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime time);

    @Query
    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime time);

//    List<Booking> findByItemOwnerAndStatusOrderByStartDesc(Long idOwner, Status state);


    List<Booking> findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long owner, LocalDateTime start,//!!!!!
                                                                               LocalDateTime end);//current

    List<Booking> findAllByItemOwnerAndEndIsBeforeOrderByStartDesc(Long owner, LocalDateTime time);//past

    List<Booking> findAllByItemOwnerAndStartIsAfterOrderByStartDesc(Long owner, LocalDateTime time);//future

    Booking findTop1BookingByItemIdAndBookerIdAndEndIsBeforeAndStatusOrderByStartDesc(
            Long itemId, Long userId, LocalDateTime time, Status status);

    @Query
    Optional<Booking> findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(Long itemId, Status status, LocalDateTime time);

    @Query
    Optional<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(Long itemId, Status status, LocalDateTime time);
//////////////////////
    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long owner);
    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long owner, Status status);

    List<Booking>findByItem_Owner_IdOrderByStartDesc(Long owner);

    List<Booking>findByItem_Owner_IdAndStatusOrderByStartDesc(Long owner, Status status);

    List<Booking>findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(Long owner, LocalDateTime time);//future

    List<Booking>findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long owner,LocalDateTime time1,
                                                                                     LocalDateTime time);//current
    List<Booking>findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(Long owner, LocalDateTime time);//past
}