package daniil.dobris.authorizationbot.controller;

import daniil.dobris.authorizationbot.dto.TelegramUser;
import daniil.dobris.authorizationbot.entity.TelegramUserEntity;
import daniil.dobris.authorizationbot.service.TelegramAuthService;
import daniil.dobris.authorizationbot.service.TelegramUserService;
import daniil.dobris.authorizationbot.util.JwtUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Value("${telegram.bot.username}")
    private String botUsername;

    private final TelegramAuthService authService;
    private final TelegramUserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(TelegramAuthService authService, TelegramUserService userService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/token")
    public Map<String, String> login(@RequestParam String initData) {
        Optional<TelegramUser> userOpt = authService.validateAndExtractUser(initData);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }
        TelegramUser user = userOpt.get();
        userService.saveOrUpdate(user);
        String token = jwtUtil.generateToken(user.id());
        return Map.of("token", token);
    }

    /*@GetMapping("/login")
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
    }*/
}
