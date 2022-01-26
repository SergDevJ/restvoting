package ru.ssk.restvoting.to;

import ru.ssk.restvoting.model.AbstractEmailEntity;

public class RestaurantVoteTo extends AbstractEmailEntity {
    private String address;
    private Integer voteId;
    private Boolean voted;

    public RestaurantVoteTo(Integer id, String name, String email, String address, Integer voteId, Boolean voted) {
        super(id, name, email);
        this.address = address;
        this.voteId = voteId;
        this.voted = voted;
    }

    public Integer getVoteId() {
        return voteId;
    }

    public Boolean isVoted() {
        return voted;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
