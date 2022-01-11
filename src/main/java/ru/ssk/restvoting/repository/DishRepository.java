package ru.ssk.restvoting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.ssk.restvoting.model.Dish;

import java.util.List;

@Repository
public class DishRepository {
    private final DishDataJpaRepository crudRepository;
    private final Sort SORT_NAME = Sort.by("name");

    @Autowired
    public DishRepository(DishDataJpaRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    public Dish save(Dish dish){
        return crudRepository.save(dish);
    }

    public Dish get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    public Dish getReference(Integer id) {
        return crudRepository.getById(id);
    }

    public List<Dish> getAll() {
        return crudRepository.findAll(SORT_NAME);
    }

}
