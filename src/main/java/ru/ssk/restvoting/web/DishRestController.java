package ru.ssk.restvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ssk.restvoting.model.Dish;
import ru.ssk.restvoting.service.DishService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.ssk.restvoting.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = {DishRestController.URL, DishRestController.REST_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController {
    static final String URL = "/admin/dishes";
    static final String REST_URL = "/rest/admin/dishes";
    private Logger log = LoggerFactory.getLogger(DishRestController.class);

    @Autowired
    private DishService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Dish> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Dish get(@PathVariable("id") int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @Valid @RequestBody Dish dish) {
        log.info("update {} with id={}", dish, id);
        assureIdConsistent(dish, id);
        service.update(dish);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish) {
        log.info("creating {}", dish);
        Dish created = service.create(dish);
        log.info("created {} with id={}", created, created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete with id={}", id);
        service.delete(id);
    }
}
