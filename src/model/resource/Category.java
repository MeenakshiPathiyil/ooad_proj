package model.resource;

public class Category {

    private int categoryId;
    private String mainType;
    private String subType;

    public Category(int categoryId, String mainType, String subType) {
        this.categoryId = categoryId;
        this.mainType = mainType;
        this.subType = subType;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getMainType() {
        return mainType;
    }

    public String getSubType() {
        return subType;
    }
}