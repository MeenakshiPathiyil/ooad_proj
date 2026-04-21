// A controller receives HTTP request, form input, and session data, and then it calls the service layer, prepares model data, and chooses which view to return
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
// UiAuthController handles login, signup, and logout for the web app, following MVC and GRASP Controller.
// It handles the authentication flow for web users
public class UiAuthController {

    private final StudentService studentService;

    public UiAuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    // Opens the home page or redirects authenticated users to the dashboard.
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
    // Authenticates a user and stores session state for the web flow.
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {
        try {
            Student student = studentService.login(email, password);    // Call service to authenticate user
            session.setAttribute(UiSession.CURRENT_USER, student);  // Store in session for later requests
            return "redirect:/ui/dashboard";    // Redirect to dashboard
        } catch (Exception e) {
            return "redirect:/?error=" + enc("Invalid credentials");    // If authentication fails, redirect with error
        }
    }

    @PostMapping("/ui/signup")
    // Registers a new student and starts a session
    public String signup(@RequestParam String id,
                         @RequestParam String name,
                         @RequestParam String email,
                         @RequestParam String phone,
                         @RequestParam String password,
                         @RequestParam String dept,
                         HttpSession session) {
        try {
            Student student = new Student(id, name, email, phone, password, dept);  // Create student object from form data
            studentService.registerStudent(student);    // Call service to register
            // Auto-login after signup
            session.setAttribute(UiSession.CURRENT_USER, student);  // Auto-login after signup
            return "redirect:/ui/dashboard";
        } catch (Exception e) {
            return "redirect:/?error=" + enc("Signup failed: " + safeMsg(e));
        }
    }

    @GetMapping("/ui/logout")
    // Ends the current session and redirects the user back to the home page.
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/?success=" + enc("Logged out");
    }

    // Encodes redirect parameters safely for web responses.
    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    // Extracts a safe display message so exception handling stays readable and centralized.
    private static String safeMsg(Exception e) {
        if (e.getMessage() == null || e.getMessage().isBlank()) return "unknown error";
        return e.getMessage();
    }
}
