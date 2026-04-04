package model.resource;

import model.user.Student;

public class Resource {

    private int resourceId;
    private String title;
    private String description;
    private String condition;
    private ResourceStatus status;
    private ListingType listingType;
    private Student owner;
    private Category category;

    public Resource(int resourceId,
                    String title,
                    String description,
                    String condition,
                    ListingType listingType,
                    Student owner,
                    Category category) {

        this.resourceId = resourceId;
        this.title = title;
        this.description = description;
        this.condition = condition;
        this.listingType = listingType;
        this.owner = owner;
        this.category = category;
        this.status = ResourceStatus.AVAILABLE;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getTitle() {
        return title;
    }

    public ResourceStatus getStatus() {
        return status;
    }

    public ListingType getListingType() {
        return listingType;
    }

    public Student getOwner() {
        return owner;
    }

    public Category getCategory() {
        return category;
    }

    public void markSold() {
        status = ResourceStatus.SOLD;
    }

    public void markBorrowed() {
        status = ResourceStatus.BORROWED;
    }

    public void reserve() {
        status = ResourceStatus.RESERVED;
    }

    public void makeAvailable() {
        status = ResourceStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return status == ResourceStatus.AVAILABLE;
    }
}