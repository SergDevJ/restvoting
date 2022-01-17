package ru.ssk.restvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.ssk.restvoting.repository.DishRepository;
import ru.ssk.restvoting.model.Dish;

import java.util.List;

import static ru.ssk.restvoting.util.ValidationUtil.checkNew;
import static ru.ssk.restvoting.util.ValidationUtil.checkNotFoundWithId;


@Service
public class DishService {
    private final DishRepository repository;

    public DishService(@Autowired DishRepository repository) {
        this.repository = repository;
    }

    public List<Dish> getAll() {
        return repository.getAll();
    }

    public Dish get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public void update(Dish dish) {
        Assert.notNull(dish, "Dish must not be null");
        Assert.notNull(dish.getId(), "Dish id must not be null");
        checkNotFoundWithId(repository.save(dish), dish.getId());
    }

    public Dish create(Dish dish) {
        Assert.notNull(dish, "Dish must not be null");
        checkNew(dish);
        return repository.save(dish);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }
}
