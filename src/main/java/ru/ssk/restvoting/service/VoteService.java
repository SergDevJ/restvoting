package ru.ssk.restvoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.model.User;
import ru.ssk.restvoting.model.Vote;
import ru.ssk.restvoting.repository.RestaurantRepository;
import ru.ssk.restvoting.repository.UserRepository;
import ru.ssk.restvoting.repository.VoteRepository;
import ru.ssk.restvoting.application.Settings;
import ru.ssk.restvoting.to.ProfileVotingHistoryTo;
import ru.ssk.restvoting.util.SecurityUtil;
import ru.ssk.restvoting.util.ValidationUtil;
import ru.ssk.restvoting.util.exception.IllegalRequestDataException;
import ru.ssk.restvoting.util.exception.TooLateVoteException;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

@Service("voteService")
public class VoteService {
    private final String TOO_LATE_VOTE_MSG_CODE = "exception.tooLateVote";
    private final String INVALID_VOTE_DATE_MSG_CODE = "exception.invalidVoteDate";

    private final long MAX_VOTE_TIME_OFFSET = 24 * 60 * 60 - 1;

    public static final String FILTER_DEFAULT_START_DATE = "1900-01-01";
    public static final String FILTER_DEFAULT_END_DATE = "2100-01-01";

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReloadableResourceBundleMessageSource messageSource;
    private final Settings systemSettings;


    @Autowired
    public VoteService(VoteRepository voteRepository, UserRepository userRepository,
                       RestaurantRepository restaurantRepository,
                       ReloadableResourceBundleMessageSource messageSource,
                       Settings systemSettings) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.messageSource = messageSource;
        this.systemSettings = systemSettings;
    }


    @Transactional
    public void castVote(int restaurantId, LocalDateTime voteDateTime) {
        //maximum time difference between the application server time
        //and voting time should be no more than 24 hours
        long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long voteTime = voteDateTime.toEpochSecond(ZoneOffset.UTC);
        if (Math.abs(currentTime - voteTime) > MAX_VOTE_TIME_OFFSET) {
            String msg = messageSource.getMessage(INVALID_VOTE_DATE_MSG_CODE, null, "Validation error",
                    LocaleContextHolder.getLocale());
            throw new IllegalRequestDataException(msg);
        }

        User user = ValidationUtil.checkNotFoundWithId(userRepository.getReference(SecurityUtil.getAuthUserId()), SecurityUtil.getAuthUserId());
        Restaurant restaurant = ValidationUtil.checkNotFoundWithId(restaurantRepository.getReference(restaurantId), restaurantId);
        Vote findVote = voteRepository.findByUserAndDate(user, Date.valueOf(voteDateTime.toLocalDate())).orElse(null);
        if (findVote != null) {
            LocalTime voteLastTime = systemSettings.getVoteLastTime();
            if (voteDateTime.toLocalTime().isAfter(voteLastTime)) {
                String msg = messageSource.getMessage(TOO_LATE_VOTE_MSG_CODE, new Object[] {voteLastTime.toString()}, "Voting is not possible after {0}",
                        LocaleContextHolder.getLocale());
                throw new TooLateVoteException(msg);
            }
            findVote.setRestaurant(restaurant);
            voteRepository.save(findVote);
        } else {
            Vote vote = new Vote(user, restaurant, Date.valueOf(voteDateTime.toLocalDate()));
            voteRepository.save(vote);
        }
    }

    public List<ProfileVotingHistoryTo> getProfileVotingHistory(Date startDate, Date endDate) {
        int userId = SecurityUtil.getAuthUserId();
        if (startDate == null) startDate = Date.valueOf(FILTER_DEFAULT_START_DATE);
        if (endDate == null) endDate = Date.valueOf(FILTER_DEFAULT_END_DATE);
        return voteRepository.getVotingHistory(userId, startDate, endDate);
    }

}
