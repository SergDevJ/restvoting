package ru.ssk.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ssk.restvoting.model.MenuItem;
import ru.ssk.restvoting.to.MenuItemDisplay;
import ru.ssk.restvoting.to.MenuItemTo;

import java.util.List;

public interface MenuDataJpaRepository extends JpaRepository<MenuItem, Integer> {
    @Modifying
    @Transactional
    @Query("delete from MenuItem as m where m.id = :id")
    int delete(@Param("id") int id);

    @Query(value = "select menu.id, dishes.id as dishId, dishes.name, dishes.weight, " +
            "CAST((menu.price) AS DECIMAL(10, 2)) / 100.0 as price " +
            "from menu inner join dishes on menu.dish_id = dishes.id " +
            "and menu.restaurant_id =:restaurantId and menu.menu_date = :date " +
            "order by dishes.name", nativeQuery = true)
    List<MenuItemDisplay> getMenuForDisplay(@Param("restaurantId") int restaurantId,
                                            @Param("date") java.sql.Date date);

    @Query(value = "select menu.id, menu.restaurant_id as restaurantId, menu.menu_date as date, " +
            "menu.dish_id as dishId, " +
            "CAST((menu.price) AS DECIMAL(10, 2)) / 100.0 as price " +
            "from menu where menu.id =:id", nativeQuery = true)
    MenuItemTo getTo(@Param("id") int id);
}
