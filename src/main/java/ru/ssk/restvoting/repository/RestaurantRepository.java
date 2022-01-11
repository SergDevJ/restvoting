package ru.ssk.restvoting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.to.RestaurantVoteTo;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class RestaurantRepository {
    private final RestaurantDataJpaRepository crudRepository;
    private final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");
    private final Sort SORT_EMAIL = Sort.by(Sort.Direction.ASC, "email");


    public RestaurantRepository(@Autowired RestaurantDataJpaRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Transactional
    @Modifying
    @CacheEvict(value = "restaurants", allEntries = true)
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Transactional
    @Modifying
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant save(Restaurant restaurant) {
        return crudRepository.save(restaurant);
    }

    public Restaurant get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    public Restaurant getReference(Integer id) {
        return crudRepository.getById(id);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return crudRepository.findAll(SORT_EMAIL);
    }

    public List<RestaurantVoteTo> getRestaurantsWithUserVote(int userId, java.sql.Date voteDate) {
        return crudRepository.getRestaurantsWithUserVote(userId, voteDate);
    }

}
