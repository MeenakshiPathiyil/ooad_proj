package model.user;

import java.util.ArrayList;
import java.util.List;
import model.resource.Resource;
import model.review.Review;
import org.json.JSONObject;
import org.json.JSONArray;

// Encapsulates all student-specific properties and behaviors, supporting SRP and polymorphism
// Why this design: 
// 1. There is a clean separation - student models data, service enforces rules
// 2. Student controls its own state
// 3. Easy to understand as all student-related logic is in the student class
// 4. Easy to extend as new features can be added with minimal changes

public class Student extends User {

    private String department;
    private boolean suspended;
    private List<Resource> listedResources;
    private List<Review> reviews;



    public Student(String id, String name, String email, String phone, String password, String department) {

        // Call the parent constructor to initialize common user properties
        super(id, name, email, phone, password);

        // Initialize student-specific properties
        this.department = department;
        this.suspended = false;
        this.listedResources = new ArrayList<>(); // Start with no resources
        this.reviews = new ArrayList<>(); // Start with no reviews
    }

    @Override
    // Validates login using the student's own state, following GRASP Information Expert.
    public boolean login(String password) {
        return !suspended && this.password.equals(password);
    }

    @Override
    public void logout() {
        System.out.println("Student logged out successfully.");
    }

    // Suspends the student inside the entity, keeping status logic cohesive.
    public void suspend() {
        suspended = true;
    }

    // Reactivates the student inside the model, which again follows Information Expert.
    public void activate() {
        suspended = false;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public String getDepartment() {
        return department;
    }

    // Records a listed resource for this student, supporting high cohesion in the entity.
    public void addResource(Resource resource) {
        listedResources.add(resource);
    }

    public List<Resource> getListedResources() {
        return listedResources;
    }

    // Attaches a review to the student who owns it, keeping related data together.
    public void addReview(Review review) {
        reviews.add(review);
    }
    // Converts the student into JSON for API transport and UI integration.
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", getId());
        json.put("name", getName());
        json.put("email", getEmail());
        json.put("phone", getPhone());
        json.put("department", getDepartment());
        json.put("suspended", isSuspended());
        // Optionally add listed resources and reviews as arrays
        JSONArray resourcesArr = new JSONArray();
        for (Resource r : listedResources) {
            resourcesArr.put(r.toJson());
        }
        json.put("listedResources", resourcesArr);
        JSONArray reviewsArr = new JSONArray();
        for (Review rev : reviews) {
            reviewsArr.put(rev.toJson());
        }
        json.put("reviews", reviewsArr);
        return json;
    }
}
