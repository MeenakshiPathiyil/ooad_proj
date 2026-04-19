package unisync.web.ui;

import jakarta.servlet.http.HttpSession;
import model.user.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
// UiProfileController prepares profile data for the web view, following MVC and SRP.
public class UiProfileController {

    @GetMapping("/ui/profile")
    // Loads the current student from session and exposes it to the profile template.
    public String profile(HttpSession session, Model model) {
        Object u = session.getAttribute(UiSession.CURRENT_USER);
        if (u instanceof Student s) {
            model.addAttribute("student", s);
        }
        return "profile";
    }
}
