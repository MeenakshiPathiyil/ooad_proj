package dao.dto;

import java.time.LocalDate;

public class BorrowedItem {
    private final int transactionId;
    private final int resourceId;
    private final String title;
    private final String status;
    private final String lenderId;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public BorrowedItem(int transactionId, int resourceId, String title, String status,
                        String lenderId, LocalDate startDate, LocalDate endDate) {
        this.transactionId = transactionId;
        this.resourceId = resourceId;
        this.title = title;
        this.status = status;
        this.lenderId = lenderId;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getLenderId() {
        return lenderId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}

