package ru.ssk.restvoting;

import ru.ssk.restvoting.to.MenuItemDisplay;

import java.beans.ConstructorProperties;

public class MenuItemDisplayImpl implements MenuItemDisplay {
    private Integer id;
    private String name;
    private Float price;
    private Integer weight;

    @ConstructorProperties({"id", "name", "weight", "price"})
    public MenuItemDisplayImpl(Integer id, String name, Integer weight, Float price) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getWeight() {
        return weight;
    }

    @Override
    public Float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "MenuItemDisplayImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", weight=" + weight +
                '}';
    }
}
