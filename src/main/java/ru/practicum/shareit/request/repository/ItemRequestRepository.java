package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query
    List<ItemRequest> findByRequestorIdOrderByCreatedDesc(Long idRequestor);

    @Query
    List<ItemRequest> findAllByRequestorIdNot(Long requestorId, Pageable pageable);
}