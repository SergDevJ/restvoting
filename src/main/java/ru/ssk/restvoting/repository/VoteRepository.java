package ru.ssk.restvoting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.ssk.restvoting.model.User;
import ru.ssk.restvoting.model.Vote;
import ru.ssk.restvoting.to.ProfileVotingHistoryTo;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class VoteRepository {
    private final VoteDataJpaRepository crudRepository;

    @Autowired
    public VoteRepository(VoteDataJpaRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Vote save(Vote vote) {
        return crudRepository.save(vote);
    }

    public Optional<Vote> findByUserAndDate(User user, Date date) {
        return crudRepository.findByUserAndDate(user, date);
    }

    public List<ProfileVotingHistoryTo> getVotingHistory(int userId, Date startDate, Date endDate) {
        return crudRepository.getVotingHistory(userId, startDate, endDate);
    }
}
