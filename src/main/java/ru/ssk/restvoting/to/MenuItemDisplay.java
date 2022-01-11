package ru.ssk.restvoting.to;

import ru.ssk.restvoting.model.HasId;

public interface MenuItemDisplay extends HasId {
    String getName();
    Integer getWeight();
    Float getPrice();
}
