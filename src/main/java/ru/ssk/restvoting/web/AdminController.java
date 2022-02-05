package ru.ssk.restvoting.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    @GetMapping(value = "/restaurants-list")
    public String restaurants() {
        return "restaurants";
    }

    @GetMapping(value = "/users-list")
    public String users() {
        return "users";
    }

    @GetMapping(value = "/dishes-list")
    public String dishes() {
        return "dishes";
    }

    @GetMapping(value = "/restaurant-menu/{id}")
    public String menu(Model model, @PathVariable("id") int restaurantId) {
        model.addAttribute("restaurantId", restaurantId);
        return "menu";
    }
}
