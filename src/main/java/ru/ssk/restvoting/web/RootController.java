package ru.ssk.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ssk.restvoting.service.RestaurantService;
import ru.ssk.restvoting.util.SecurityUtil;

@Controller
public class RootController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/")
    public String root() {
        if (SecurityUtil.isAdminUser()) {
            return "index_admin";
        } else {
            return "redirect:/voting";
        }
    }

    @GetMapping("/voting")
    public String voting(Model model) {
        model.addAttribute("restaurants",
                restaurantService.getRestaurantsWithUserVote(SecurityUtil.getAuthUserId()));
        return "voting";
    }

    @GetMapping("/login")
    public String login(ModelMap model,
                        @RequestParam(value = "error", required = false) boolean error,
                        @RequestParam(value = "message", required = false) String message) {
        model.addAttribute("error", error);
        model.addAttribute("message", message);
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "login";
    }

    @GetMapping("/profile/register")
    public String register(ModelMap model) {
        model.addAttribute("register", true);
        return "profile";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
}
