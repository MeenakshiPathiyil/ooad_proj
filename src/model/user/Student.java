package model.user;

import java.util.ArrayList;
import java.util.List;
import model.resource.Resource;
import model.review.Review;

public class Student extends User {

    private String department;
    private boolean suspended;
    private List<Resource> listedResources;
    private List<Review> reviews;

    public Student(String id, String name, String email, String phone, String password, String department) {
        super(id, name, email, phone, password);
        this.department = department;
        this.suspended = false;
        this.listedResources = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    @Override
    public boolean login(String password) {
        return !suspended && this.password.equals(password);
    }

    @Override
    public void logout() {
        System.out.println("Student logged out successfully.");
    }

    public void suspend() {
        suspended = true;
    }

    public void activate() {
        suspended = false;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public String getDepartment() {
        return department;
    }

    public void addResource(Resource resource) {
        listedResources.add(resource);
    }

    public List<Resource> getListedResources() {
        return listedResources;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }
}