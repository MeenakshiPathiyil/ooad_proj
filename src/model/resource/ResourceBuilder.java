package model.resource;

import model.user.Student;

// The resource builder is responsible for creating a resource object step-by-step instead of calling a long constructor directly
// This improves readability and avoids any parameter order mistakes
// It follows SRP as ResourceBuilder only focuses on the construction logic not the business behavior. 

public class ResourceBuilder {
    
    private int resourceId = 0;
    private String title;
    private String description;
    private String condition;
    private ListingType listingType;
    private double price = 0.0;
    private Student owner;
    private Category category;
    
    public ResourceBuilder title(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
        return this; 
    }
    
    public ResourceBuilder description(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        this.description = description;
        return this;
    }

    public ResourceBuilder condition(String condition) {
        if (condition == null || condition.isBlank()) {
            throw new IllegalArgumentException("Condition cannot be empty");
        }
        this.condition = condition;
        return this;
    }
 
    public ResourceBuilder listingType(ListingType listingType) {
        if (listingType == null) {
            throw new IllegalArgumentException("Listing type cannot be null");
        }
        this.listingType = listingType;
        return this;
    }
    
    public ResourceBuilder price(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
        return this;
    }

    public ResourceBuilder owner(Student owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        this.owner = owner;
        return this;
    }

    public ResourceBuilder category(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category;
        return this;
    }

    public ResourceBuilder resourceId(int resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    // the build method creates the final object. It is used to validate all required fields and create the resource
    
    public Resource build() {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (condition == null || condition.isBlank()) {
            throw new IllegalArgumentException("Condition is required");
        }
        if (listingType == null) {
            throw new IllegalArgumentException("Listing type is required");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Owner is required");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category is required");
        }

        if (listingType == ListingType.SELL && price <= 0) {
            throw new IllegalArgumentException(
                "SELL items must have price > 0. Got: " + price
            );
        }

        return new Resource(
            resourceId,
            title,
            description,
            condition,
            listingType,
            price,
            owner,
            category
        );
    }
}
