package ru.ssk.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.to.RestaurantVoteTo;

import java.util.List;

public interface RestaurantDataJpaRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("delete from Restaurant r where r.id = :id")
    int delete(@Param("id") int id);

    @Query("select r.id as id, r.name as name, r.address as address, " +
            "r.email as email, v.id as voteId, " +
            " CASE when (v.id is null) then false else true END as voted" +
            " from Restaurant r left outer join r.votes v " +
            "on v.user.id = :user_id and v.date = :vote_date")
    List<RestaurantVoteTo> getRestaurantsWithUserVote(@Param("user_id") int userId,
                                                      @Param("vote_date") java.sql.Date voteDate);
}
