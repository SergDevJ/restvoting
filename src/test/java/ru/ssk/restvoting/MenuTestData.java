package ru.ssk.restvoting;

import ru.ssk.restvoting.model.Dish;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.to.MenuItemTo;
import ru.ssk.restvoting.to.MenuItemToImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static ru.ssk.restvoting.model.AbstractBaseEntity.START_SEQ;

public class MenuTestData {
    public static final int MENU_ITEM1_ID = START_SEQ;
    public static final int TODAY_MENU_ITEM1_ID = START_SEQ + 3;
    public static final int NOT_FOUND_ID = START_SEQ + 100;

    public static final int RESTAURANT_ID = START_SEQ;
    public static final int DISH_ID = START_SEQ;
    private static final Date MENU_DATE = Date.valueOf(LocalDate.now());


    public static final MenuItemTo menuItemTo = new MenuItemToImpl(START_SEQ, RESTAURANT_ID, MENU_DATE, DISH_ID, 500F);
    public static final MenuItemTo doubleNewMenuItemTo = new MenuItemToImpl(RESTAURANT_ID, MENU_DATE, DISH_ID, 500F);

    public static final MenuItemDisplayImpl todayMenuItem1 = new MenuItemDisplayImpl(TODAY_MENU_ITEM1_ID, "Дичь", 300, 500F);
    public static final MenuItemDisplayImpl todayMenuItem2 = new MenuItemDisplayImpl(TODAY_MENU_ITEM1_ID + 1, "Борщ", 250, 150.33F);
    public static final MenuItemDisplayImpl todayMenuItem3 = new MenuItemDisplayImpl(TODAY_MENU_ITEM1_ID + 2, "Бифстроганов", 220, 3000.03F);
    public static final MenuItemDisplayImpl todayMenuItem4 = new MenuItemDisplayImpl(TODAY_MENU_ITEM1_ID + 3, "Икра", 50, 110.05F);
    public static final List<MenuItemDisplayImpl> todayMenu = List.of(todayMenuItem3, todayMenuItem2, todayMenuItem1, todayMenuItem4);

    public static final MenuItemDisplayImpl menuItem1 = new MenuItemDisplayImpl(MENU_ITEM1_ID, "Борщ", 250, 100F);
    public static final MenuItemDisplayImpl menuItem2 = new MenuItemDisplayImpl(MENU_ITEM1_ID + 1, "Бифстроганов", 220, 200.44F);
    public static final MenuItemDisplayImpl menuItem3 = new MenuItemDisplayImpl(MENU_ITEM1_ID + 2, "Икра", 50, 2000.5F);
    public static final List<MenuItemDisplayImpl> anyDateMenu = List.of(menuItem2, menuItem1, menuItem3);



    public static MenuItemTo getNew() {
        return new MenuItemToImpl(null, RESTAURANT_ID, MENU_DATE, START_SEQ + 3, 150.5F);
    }

    public static MenuItemTo createToFromMenuItem(Integer id, Restaurant restaurant, Date date, Dish dish, int price) {
        return new MenuItemToImpl(id, restaurant.getId(), date, dish.getId(), price/100F);

    }

    public static MenuItemTo getUpdated() {
        return new MenuItemToImpl(MENU_ITEM1_ID, menuItemTo.getRestaurantId(), menuItemTo.getDate(), menuItemTo.getDishId() + 3, menuItemTo.getPrice() + 5.55F);
    }

    public static MenuItemTo getDuplicatedDish() {
        return new MenuItemToImpl(MENU_ITEM1_ID, RESTAURANT_ID, MENU_DATE, START_SEQ + 1, 100F);
    }

}
