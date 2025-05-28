package daniil.dobris.authorizationbot.config;

import daniil.dobris.authorizationbot.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Long userId = jwtUtil.validateTokenAndGetUserId(token);
                req.setAttribute("userId", userId);
            } catch (Exception e) {
                throw new ServletException("Invalid JWT token");
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
