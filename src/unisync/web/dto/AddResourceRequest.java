package unisync.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

// AddResourceRequest captures validated web input for new resources, supporting MVC boundary separation.
public class AddResourceRequest {
    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String condition;

    @NotBlank
    private String type; // ListingType enum name

    @NotBlank
    private String ownerId;

    @Min(1)
    private int categoryId;

    // Fixed by seller at listing time (optional for non-SELL listings)
    private Double price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
