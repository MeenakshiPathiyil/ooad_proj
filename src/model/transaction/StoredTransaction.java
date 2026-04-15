package model.transaction;

import java.time.LocalDateTime;

/**
 * Lightweight Transaction representation loaded from the database.
 * This avoids a big refactor while allowing /transactions to return real rows.
 */
public class StoredTransaction extends Transaction {

    private final String transactionType;

    public StoredTransaction(int transactionId,
                             String transactionType,
                             TransactionStatus status,
                             LocalDateTime createdAt) {
        super(transactionId);
        this.transactionType = transactionType;
        this.status = status;
        if (createdAt != null) {
            this.createdAt = createdAt;
        }
    }

    public String getTransactionType() {
        return transactionType;
    }

    @Override
    public void initiate() {
        // No-op: this is a persisted snapshot
    }

    @Override
    public void complete() {
        // No-op: use TransactionService + DAO updateStatus()
    }

    @Override
    public void cancel() {
        // No-op: use TransactionService + DAO updateStatus()
    }
}

