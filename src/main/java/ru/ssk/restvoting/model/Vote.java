package ru.ssk.restvoting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "votes")
public class Vote extends AbstractBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private Restaurant restaurant;

    @Column(name = "vote_date")
    @NotNull
    private java.sql.Date date;

    public Vote(User user, Restaurant restaurant, Date date) {
        this.user = user;
        this.restaurant = restaurant;
        this.date = date;
    }

    public Vote() {
    }

    public User getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Date getDate() {
        return date;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return String.format("%s [%s, %s, %s]", super.toString(), user, date, restaurant);
    }

}
