package unisync.web.ui;

import jakarta.servlet.http.HttpSession;
import model.resource.Category;
import model.resource.ListingType;
import model.resource.Resource;
import model.user.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.ResourceService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class UiSellController {

    private final ResourceService resourceService;

    public UiSellController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/ui/sell")
    public String sellForm(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "success", required = false) String success,
                           Model model) {
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "sell";
    }

    @PostMapping("/ui/sell")
    public String sellSubmit(@RequestParam String title,
                             @RequestParam(required = false) String description,
                             @RequestParam String condition,
                             @RequestParam String type,
                             @RequestParam(required = false) Double price,
                             @RequestParam int categoryId,
                             HttpSession session) {

        Object u = session.getAttribute(UiSession.CURRENT_USER);
        Student owner = (u instanceof Student s) ? s : null;
        if (owner == null) {
            return "redirect:/?error=" + enc("Please login first");
        }

        try {
            double fixedPrice = 0.0;
            if ("SELL".equalsIgnoreCase(type)) {
                fixedPrice = price != null ? price : 0.0;
            }
            Resource resource = new Resource(
                    0,
                    title,
                    description,
                    condition,
                    ListingType.valueOf(type),
                    fixedPrice,
                    owner,
                    new Category(categoryId, "", "")
            );
            resourceService.addResource(resource);
            return "redirect:/ui/resources?success=" + enc("Resource listed");
        } catch (Exception e) {
            return "redirect:/ui/sell?error=" + enc(e.getMessage() == null ? "Failed to list resource" : e.getMessage());
        }
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
