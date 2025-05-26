package daniil.dobris.authorizationbot.controller;

import daniil.dobris.authorizationbot.model.TelegramUser;
import daniil.dobris.authorizationbot.service.TelegramAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@Slf4j
public class AuthController {

    @Value("${telegram.bot.username}")
    private String botUsername;

    private final TelegramAuthService authService;


    public AuthController(TelegramAuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("botUsername", botUsername);
        return "login";
    }

    @GetMapping("/auth/telegram")
    public String telegramAuth(@RequestParam Map<String, String> params, HttpSession session) {
        session.setAttribute("telegramData", params);
        if (!authService.isDataValid(params)) {
            session.setAttribute("authError", true);
            return "redirect:/login";
        }
        log.info("Telegram auth was successful");
        TelegramUser user = authService.mapToTelegramUser(params);
        session.setAttribute("telegramUser", user);
        return "redirect:/profile";
    }
}
