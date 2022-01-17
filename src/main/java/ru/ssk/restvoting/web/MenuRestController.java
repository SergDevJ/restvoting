package ru.ssk.restvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.ssk.restvoting.service.MenuService;
import ru.ssk.restvoting.to.MenuItemDisplay;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping(value = {MenuRestController.URL, MenuRestController.REST_URL},
        produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestController {
    static final String URL = "/menu";
    static final String REST_URL = "/rest/menu";
    private static final Logger log = LoggerFactory.getLogger(MenuRestController.class);

    @Autowired
    private MenuService service;

    @GetMapping(value = "/voting/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public List<MenuItemDisplay> getMenuForVoting(@PathVariable("restaurantId") int id,
                                                  @RequestParam("date") Date date) {
        log.info("get menu for restaurant with id={}", id);
        return service.getMenuForVoting(id, date);
    }

    @GetMapping(value = "/history/{restaurantId}")
    @ResponseStatus(HttpStatus.OK)
    public List<MenuItemDisplay> getMenuHistory(@PathVariable("restaurantId") int id,
                                                @RequestParam(value = "date") Date date) {
        log.info("get menu history for restaurant with id={} and date={}", id, date);
        return service.getMenu(id, date);
    }

}
