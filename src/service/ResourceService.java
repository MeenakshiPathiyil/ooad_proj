package service;

import dao.interfaces.ResourceDAO;
import dao.impl.ResourceDAOImpl;
import model.resource.Resource;
import model.resource.ResourceStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    private final ResourceDAO resourceDAO;

    public ResourceService() {
        this.resourceDAO = new ResourceDAOImpl();
    }

    public void addResource(Resource resource) {

        if (!resource.isAvailable()) {
            throw new IllegalArgumentException("Resource must be available before listing.");
        }

        if (resource.getListingType() == model.resource.ListingType.SELL && resource.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        resourceDAO.save(resource);
    }

    public Resource getResourceById(int id) {

        Optional<Resource> resource = resourceDAO.findById(id);

        if (resource.isEmpty()) {
            throw new IllegalArgumentException("Resource not found.");
        }

        return resource.get();
    }

    public List<Resource> getAvailableResources() {
        return resourceDAO.findAvailableResources();
    }

    public void markAsSold(Resource resource) {
        resource.markSold();
        resourceDAO.update(resource);
    }

    public void markAsBorrowed(Resource resource) {
        resource.markBorrowed();
        resourceDAO.update(resource);
    }

    public void makeAvailable(Resource resource) {
        resource.makeAvailable();
        resourceDAO.update(resource);
    }

    public void removeResource(int id) {
        resourceDAO.delete(id);
    }
}
