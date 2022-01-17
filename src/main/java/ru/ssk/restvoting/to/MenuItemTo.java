package ru.ssk.restvoting.to;

import com.fasterxml.jackson.annotation.JsonGetter;
import ru.ssk.restvoting.model.HasId;

import java.sql.Date;

public interface MenuItemTo extends HasId {
    @JsonGetter("restaurantId")
    Integer getRestaurantId();
    void setRestaurantId(Integer restaurantId);

    @JsonGetter("dishId")
    Integer getDishId();
    void setDishId(Integer dishId);

    @JsonGetter("price")
    Float getPrice();
    void setPrice(Float price);

    @JsonGetter("date")
    Date getDate();
    void setDate(Date date);
}
