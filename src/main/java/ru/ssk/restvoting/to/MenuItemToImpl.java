package ru.ssk.restvoting.to;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.sql.Date;

public class MenuItemToImpl extends BaseTo implements MenuItemTo {

    @NotNull
    private Integer restaurantId;

    private java.sql.Date date;

    @NotNull
    private Integer dishId;

    @DecimalMin("0.009")
    @DecimalMax("100000")
    @NotNull
    private Float price;

    public MenuItemToImpl() {
    }

    public MenuItemToImpl(Integer id, Integer restaurantId, Date date, Integer dishId, Float price) {
        super(id);
        this.restaurantId = restaurantId;
        this.date = date;
        this.dishId = dishId;
        this.price = price;
    }

    public MenuItemToImpl(Integer restaurantId, Date date, Integer dishId, Float price) {
        this.restaurantId = restaurantId;
        this.date = date;
        this.dishId = dishId;
        this.price = price;
    }

    @Override
    public Integer getRestaurantId() {
        return restaurantId;
    }

    @Override
    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public Integer getDishId() {
        return dishId;
    }

    @Override
    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    @Override
    public Float getPrice() {
        return price;
    }

    @Override
    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MenuItemToImpl{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", date=" + date +
                ", dishId=" + dishId +
                ", price=" + price +
                '}';
    }
}
