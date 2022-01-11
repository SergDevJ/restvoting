package ru.ssk.restvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.ssk.restvoting.model.MenuItem;
import ru.ssk.restvoting.repository.DishRepository;
import ru.ssk.restvoting.repository.MenuRepository;
import ru.ssk.restvoting.repository.RestaurantRepository;
import ru.ssk.restvoting.to.MenuItemDisplay;
import ru.ssk.restvoting.to.MenuItemTo;
import ru.ssk.restvoting.util.ValidationUtil;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Service("menuService")
public class MenuService {
    private final MenuRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @Autowired
    public MenuService(MenuRepository repository, RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }


    @Cacheable("votingMenu")
    public List<MenuItemDisplay> getMenuForVoting(int restaurantId, Date date) {
        return repository.getMenuForDisplay(restaurantId, date);
    }

    public List<MenuItemDisplay> getMenu(int restaurantId, Date date) {
        return repository.getMenuForDisplay(restaurantId, date);
    }

    public MenuItemTo getTo(int id) {
        return ValidationUtil.checkNotFoundWithId(repository.getTo(id), id);
    }

    public void update(MenuItemTo menuItemTo) {
        Assert.notNull(menuItemTo, "Menu item must not be null");
        Assert.notNull(menuItemTo.getId(), "Menu item id must not be null");
        MenuItem menuItem = createFromMenuItemTo(menuItemTo);
        repository.save(menuItem);
    }

    public MenuItem create(MenuItemTo menuItemTo) {
        Assert.notNull(menuItemTo, "Menu item must not be null");
        ValidationUtil.checkNew(menuItemTo);
        MenuItem menuItem = createFromMenuItemTo(menuItemTo);
        return repository.save(menuItem);
    }


    private MenuItem createFromMenuItemTo(MenuItemTo menuItemTo) {
        Objects.requireNonNull(menuItemTo);
        return new MenuItem(menuItemTo.getId(),
                restaurantRepository.getReference(menuItemTo.getRestaurantId()),
                menuItemTo.getDate(),
                dishRepository.getReference(menuItemTo.getDishId()),
                (int) (menuItemTo.getPrice() * 100));
    }

    public void delete(int id) {
        ValidationUtil.checkNotFoundWithId(repository.delete(id), id);
    }
}
