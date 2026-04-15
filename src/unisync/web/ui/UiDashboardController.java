package unisync.web.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiDashboardController {

    @GetMapping("/ui/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}

