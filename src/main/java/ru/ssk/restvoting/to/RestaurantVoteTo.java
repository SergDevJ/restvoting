package ru.ssk.restvoting.to;

public interface RestaurantVoteTo {
    Integer getId();
    String getName();
    String getAddress();
    String getEmail();
    Integer getVoteId();
    Boolean getVoted();
}
