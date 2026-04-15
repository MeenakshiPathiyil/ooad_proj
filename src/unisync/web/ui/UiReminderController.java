package unisync.web.ui;

import jakarta.servlet.http.HttpSession;
import model.reminder.Reminder;
import model.user.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.ReminderDbService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class UiReminderController {

    private final ReminderDbService reminderDbService;

    public UiReminderController(ReminderDbService reminderDbService) {
        this.reminderDbService = reminderDbService;
    }

    @GetMapping("/ui/reminders")
    public String reminders(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "success", required = false) String success,
                            HttpSession session,
                            Model model) {
        Student me = current(session);
        List<Reminder> reminders = reminderDbService.getRemindersForStudent(me.getId());
        model.addAttribute("reminders", reminders);
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "reminders";
    }

    @PostMapping("/ui/reminders/read")
    public String markRead(@RequestParam int reminderId, HttpSession session) {
        current(session);
        try {
            reminderDbService.markRead(reminderId);
            return "redirect:/ui/reminders?success=" + enc("Marked as read");
        } catch (Exception e) {
            return "redirect:/ui/reminders?error=" + enc("Failed to mark read");
        }
    }

    private static Student current(HttpSession session) {
        Object u = session.getAttribute(UiSession.CURRENT_USER);
        if (u instanceof Student s) return s;
        throw new IllegalStateException("Not logged in");
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}

