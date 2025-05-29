package daniil.dobris.authorizationbot.controller;

import daniil.dobris.authorizationbot.entity.TelegramUserEntity;
import daniil.dobris.authorizationbot.repository.TelegramUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private final TelegramUserRepository repository;

    public ProfileController(TelegramUserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request, Model model) {
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            return "unauthorized";
        }

        TelegramUserEntity user = repository.findById(userId).orElse(null);
        if (user == null) {
            return "unauthorized";
        }
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }
}
