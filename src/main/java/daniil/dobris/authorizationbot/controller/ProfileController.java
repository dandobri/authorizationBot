package daniil.dobris.authorizationbot.controller;

import daniil.dobris.authorizationbot.dto.TelegramUser;
import daniil.dobris.authorizationbot.entity.TelegramUserEntity;
import daniil.dobris.authorizationbot.repository.TelegramUserRepository;
import daniil.dobris.authorizationbot.service.TelegramUserService;
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
    public String profile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "unauthorized";
        }

        TelegramUserEntity user = repository.findById(userId).orElse(null);
        model.addAttribute("user", user);
        return "profile";
    }
}
