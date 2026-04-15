package unisync.web;

import model.resource.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.ResourceService;

import java.util.List;

@Controller
public class UiController {

    private final ResourceService resourceService;

    public UiController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/ui/resources")
    public String resources(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "success", required = false) String success,
                            Model model) {
        List<Resource> resources = resourceService.getAvailableResources();
        model.addAttribute("resources", resources);
        model.addAttribute("count", resources.size());
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "resources";
    }
}
