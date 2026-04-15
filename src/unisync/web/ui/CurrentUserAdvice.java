package unisync.web.ui;

import jakarta.servlet.http.HttpSession;
import model.user.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class CurrentUserAdvice {

    @ModelAttribute("currentUser")
    public Student currentUser(HttpSession session) {
        Object u = session.getAttribute(UiSession.CURRENT_USER);
        if (u instanceof Student s) {
            return s;
        }
        return null;
    }
}

