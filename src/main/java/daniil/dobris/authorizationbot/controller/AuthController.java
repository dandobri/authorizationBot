package daniil.dobris.authorizationbot.controller;

import daniil.dobris.authorizationbot.dto.TelegramUser;
import daniil.dobris.authorizationbot.service.TelegramAuthService;
import daniil.dobris.authorizationbot.service.TelegramUserService;
import daniil.dobris.authorizationbot.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final TelegramAuthService authService;
    private final TelegramUserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(TelegramAuthService authService, TelegramUserService userService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/telegram")
    public void handleTelegramLogin(@RequestParam Map<String, String> initData, HttpServletResponse response) throws IOException {
        Optional<TelegramUser> userOpt = authService.validateAndExtractUser(initData);
        if (userOpt.isEmpty()) {
            response.sendRedirect("/unauthorized");
            return;
        }

        TelegramUser user = userOpt.get();
        userService.saveOrUpdate(user);

        String token = jwtUtil.generateToken(user.id());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600 * 10);
        response.addCookie(cookie);
    }
}
