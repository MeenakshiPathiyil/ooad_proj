package dao.dto;

public class BoughtItem {
    private final int transactionId;
    private final int resourceId;
    private final String title;
    private final String status;
    private final String sellerId;
    private final double price;

    public BoughtItem(int transactionId, int resourceId, String title, String status, String sellerId, double price) {
        this.transactionId = transactionId;
        this.resourceId = resourceId;
        this.title = title;
        this.status = status;
        this.sellerId = sellerId;
        this.price = price;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public String getSellerId() {
        return sellerId;
    }

    public double getPrice() {
        return price;
    }
}

