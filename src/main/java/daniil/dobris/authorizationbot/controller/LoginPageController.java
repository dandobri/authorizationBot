package daniil.dobris.authorizationbot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("botUsername", botUsername);
        return "login";
    }
}
