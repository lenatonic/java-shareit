package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Transactional
    @Modifying
    @Query(value = "update Item it set it.name = :name, it.description = :description," +
            " it.available = :available where it.id = :id")
    void up(@Param("name") String name, @Param("description") String description,
            @Param("available") Boolean available, @Param("id") Long id);

    @Query
    List<Item> findItemsByOwner(Long id);

    @Query(value = "FROM Item it " +
            "WHERE LOWER (it.name) LIKE LOWER (concat('%',:text,'%')) " +
            "OR LOWER (it.description) LIKE LOWER (concat('%',:text,'%')) " +
            "AND it.available = TRUE")
    List<Item> findItemsByText(@Param("text") String text);

    List<Item> findAllByOwner(Long idOwner);
}