package controller;

import model.resource.Resource;
import service.ResourceService;

import java.util.List;

// ResourceController receives resource requests from the legacy UI, following the GRASP Controller principle.
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController() {
        this.resourceService = new ResourceService();
    }

    // Delegates resource creation to the service layer, keeping UI code thin and focused.
    public void addResource(Resource resource) {
        resourceService.addResource(resource);
        System.out.println("[INFO] Resource added successfully.");
    }

    // Retrieves a resource for the legacy UI without exposing service details to menu classes.
    public Resource getResource(int id) {
        return resourceService.getResourceById(id);
    }

    // Returns visible catalog items while preserving separation between UI and business logic.
    public List<Resource> getAvailableResources() {
        return resourceService.getAvailableResources();
    }

    // Removes a resource via the service layer, supporting low coupling.
    public void removeResource(int id) {
        resourceService.removeResource(id);
        System.out.println("[INFO] Resource removed.");
    }

    // Forwards a sell action to the service layer, following GRASP Controller.
    public void markSold(Resource resource) {
        resourceService.markAsSold(resource);
    }

    // Forwards a borrow action to the service layer instead of handling status logic in the UI.
    public void markBorrowed(Resource resource) {
        resourceService.markAsBorrowed(resource);
    }
}
