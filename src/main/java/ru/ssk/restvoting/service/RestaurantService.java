package ru.ssk.restvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.repository.RestaurantRepository;
import ru.ssk.restvoting.to.RestaurantVoteTo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static ru.ssk.restvoting.util.ValidationUtil.checkNew;
import static ru.ssk.restvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {
    private final RestaurantRepository repository;

    @Autowired
    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not be null");
        Assert.notNull(restaurant.getId(), "Restaurant id must not be null");
        repository.save(restaurant);
    }

    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not be null");
        checkNew(restaurant);
        return repository.save(restaurant);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<RestaurantVoteTo> getRestaurantsWithUserVote(int userId) {
        return getRestaurantsWithUserVote(userId, java.sql.Date.valueOf(LocalDate.now()));
    }

    public List<RestaurantVoteTo> getRestaurantsWithUserVote(int userId, Date voteDate) {
        return repository.getRestaurantsWithUserVote(userId, voteDate);
    }
}
