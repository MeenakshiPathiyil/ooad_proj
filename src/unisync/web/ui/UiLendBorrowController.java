package unisync.web.ui;

import model.resource.ListingType;
import model.resource.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.ResourceService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UiLendBorrowController {

    private final ResourceService resourceService;

    public UiLendBorrowController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/ui/lend-borrow")
    public String lendBorrow(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "success", required = false) String success,
                             Model model) {
        List<Resource> all = resourceService.getAvailableResources();
        List<Resource> lendOnly = new ArrayList<>();
        for (Resource r : all) {
            if (r.getListingType() == ListingType.LEND) {
                lendOnly.add(r);
            }
        }
        model.addAttribute("resources", lendOnly);
        model.addAttribute("count", lendOnly.size());
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "lendborrow";
    }
}

