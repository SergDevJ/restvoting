package ru.ssk.restvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.ssk.restvoting.service.MenuService;
import ru.ssk.restvoting.to.MenuItemDisplay;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping(value = {MenuRestController.URL, MenuRestController.REST_URL},
        produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestController {
    static final String URL = "/restaurant-menu";
    static final String REST_URL = "/rest/restaurant-menu";
    private static final Logger log = LoggerFactory.getLogger(MenuRestController.class);

    @Autowired
    private MenuService service;

    @GetMapping(value = "/{restaurantId}/voting")
    @ResponseStatus(HttpStatus.OK)
    public List<MenuItemDisplay> getRestaurantMenuForVoting(@PathVariable("restaurantId") int id,
                                                  @RequestParam("date") Date date) {
        log.info("get menu for voting for restaurant with id={}", id);
        return service.getAllForVoting(id, date);
    }

    @GetMapping(value = "/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public List<MenuItemDisplay> getRestaurantMenu(@PathVariable("restaurantId") int id,
                                                   @RequestParam(value = "date", required = false) @Nullable Date date) {
        log.info("get menu for restaurant with id={} and date={}", id, date);
        return service.getAll(id, date);
    }
}
