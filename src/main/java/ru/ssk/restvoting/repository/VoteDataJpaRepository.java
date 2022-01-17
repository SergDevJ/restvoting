package ru.ssk.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ssk.restvoting.model.User;
import ru.ssk.restvoting.model.Vote;
import ru.ssk.restvoting.to.ProfileVotingHistoryTo;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


public interface VoteDataJpaRepository extends JpaRepository<Vote, Integer> {
    @Query(value = "select restaurants.name as restaurantName, votes.vote_date as voteDate " +
            "from votes inner join restaurants on votes.restaurant_id = restaurants.id " +
            "where votes.user_id = :user_id and vote_date between :start_date and :end_date " +
            "order by votes.vote_date desc", nativeQuery = true)
    List<ProfileVotingHistoryTo> getVotingHistory(@Param("user_id") int userId,
                                                  @Param("start_date") Date startDate,
                                                  @Param("end_date") Date endDate);

    @Query("select v from Vote v where v.user = :user and v.date = :date")
    Optional<Vote> findByUserAndDate(@Param("user") User user, @Param("date") Date date);
}
