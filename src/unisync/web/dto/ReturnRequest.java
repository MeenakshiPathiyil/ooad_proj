package unisync.web.dto;

import jakarta.validation.constraints.Min;

public class ReturnRequest {
    @Min(1)
    private int transactionId;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}

