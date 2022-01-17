package ru.ssk.restvoting.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

@Controller
@RequestMapping(value = "/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    @GetMapping(value = "/restaurants_list")
    public String restaurants() {
        return "restaurants";
    }

    @GetMapping(value = "/users_list")
    public String users() {
        return "users";
    }

    @GetMapping(value = "/dishes_list")
    public String dishes() {
        return "dishes";
    }

    @GetMapping(value = "/menu_list/{id}")
    public String menu(Model model, @PathVariable("id") int restaurantId,
                       @RequestParam("menuDate") Date menuDate) {
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("menuDate", menuDate);
        return "menu";
    }

}
