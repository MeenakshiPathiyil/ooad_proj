package unisync.web.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        if (!path.startsWith("/ui")) {
            return true;
        }

        // Allow auth endpoints without a session
        if (path.equals("/ui/login") || path.equals("/ui/signup")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(UiSession.CURRENT_USER) == null) {
            String msg = URLEncoder.encode("Please login first", StandardCharsets.UTF_8);
            response.sendRedirect("/?error=" + msg);
            return false;
        }

        return true;
    }
}

