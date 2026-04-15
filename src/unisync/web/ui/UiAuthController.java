package unisync.web.ui;

import jakarta.servlet.http.HttpSession;
import model.user.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.StudentService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class UiAuthController {

    private final StudentService studentService;

    public UiAuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String home(HttpSession session,
                       @RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "success", required = false) String success,
                       Model model) {
        if (session.getAttribute(UiSession.CURRENT_USER) != null) {
            return "redirect:/ui/dashboard";
        }
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "home";
    }

    @PostMapping("/ui/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {
        try {
            Student student = studentService.login(email, password);
            session.setAttribute(UiSession.CURRENT_USER, student);
            return "redirect:/ui/dashboard";
        } catch (Exception e) {
            return "redirect:/?error=" + enc("Invalid credentials");
        }
    }

    @PostMapping("/ui/signup")
    public String signup(@RequestParam String id,
                         @RequestParam String name,
                         @RequestParam String email,
                         @RequestParam String phone,
                         @RequestParam String password,
                         @RequestParam String dept,
                         HttpSession session) {
        try {
            Student student = new Student(id, name, email, phone, password, dept);
            studentService.registerStudent(student);
            // Auto-login after signup
            session.setAttribute(UiSession.CURRENT_USER, student);
            return "redirect:/ui/dashboard";
        } catch (Exception e) {
            return "redirect:/?error=" + enc("Signup failed: " + safeMsg(e));
        }
    }

    @GetMapping("/ui/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/?success=" + enc("Logged out");
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private static String safeMsg(Exception e) {
        if (e.getMessage() == null || e.getMessage().isBlank()) return "unknown error";
        return e.getMessage();
    }
}

