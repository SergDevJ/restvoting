package ru.ssk.restvoting.system;

import java.time.LocalTime;

public class Settings {
    private LocalTime VOTE_LAST_TIME = LocalTime.of(11, 0);

    public LocalTime getVoteLastTime() {
        return VOTE_LAST_TIME;
    }

    public void setVoteLastTime(LocalTime voteLastTime) {
        VOTE_LAST_TIME = voteLastTime;
    }
}
