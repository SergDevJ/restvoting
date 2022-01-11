package ru.ssk.restvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ssk.restvoting.model.Restaurant;
import ru.ssk.restvoting.service.MenuService;
import ru.ssk.restvoting.service.RestaurantService;
import ru.ssk.restvoting.to.MenuItemDisplay;
import ru.ssk.restvoting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping(value = {RestaurantRestController.URL, RestaurantRestController.REST_URL},
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RestaurantRestController {
    static final String URL = "/admin/restaurants";
    static final String REST_URL = "/rest/admin/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    RestaurantService service;

    @Autowired
    MenuService menuService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<Restaurant> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    Restaurant get(@PathVariable("id") int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer id,
                @Valid @RequestBody Restaurant restaurant) {
        log.info("update {} with id={}", restaurant, id);
        ValidationUtil.assureIdConsistent(restaurant, id);
        service.update(restaurant);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        Restaurant created = service.create(restaurant);
        log.info("create {} with id={}", created, created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        log.info("delete with id={}", id);
        service.delete(id);
    }

    @GetMapping(value = "/menu/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<MenuItemDisplay> getTodayMenu(@PathVariable("id") int id,
                                              @RequestParam("date") Date date) {
        log.info("getTodayMenu id={}", id);
        return menuService.getMenu(id, date);
    }
}
