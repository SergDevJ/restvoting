package ru.ssk.restvoting.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ssk.restvoting.repository.DishRepository;
import ru.ssk.restvoting.model.Dish;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service(value = "dishUtil")
public class DishUtil {
    private static DishRepository repository;

    @Autowired
    private DishUtil(DishRepository repository) {
        DishUtil.repository = repository;
    }

    public static Map<Integer, String> getDishList() {
        List<Dish> list = repository.getAll();
        Map<Integer, String> result = new LinkedHashMap<>();
        list.forEach(d -> result.put(d.getId(), d.getName()));
        return result;
    }


}
