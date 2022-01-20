package ru.ssk.restvoting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
import ru.ssk.restvoting.model.MenuItem;
import ru.ssk.restvoting.to.MenuItemDisplay;

import java.util.List;

@Repository
public class MenuRepository {
    private final MenuDataJpaRepository crudRepository;

    @Autowired
    public MenuRepository(MenuDataJpaRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public MenuItem get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "votingMenu", allEntries = true)
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @CacheEvict(value = "votingMenu", allEntries = true)
    public MenuItem save(MenuItem menuItem) {
        return crudRepository.save(menuItem);
    }

    public List<MenuItemDisplay> getMenuForDisplay(int restaurantId, java.sql.Date date) {
        return crudRepository.getMenuForDisplay(restaurantId, date);
    }
}
