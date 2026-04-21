package service;

import dao.interfaces.TransactionDAO;
import dao.impl.TransactionDAOImpl;

import factory.TransactionFactory;

import model.resource.Resource;
import model.transaction.Transaction;
import model.user.Student;
import org.springframework.stereotype.Service;
import dao.dto.BorrowedItem;
import dao.dto.BoughtItem;

import java.util.List;
import java.time.LocalDate;

@Service
public class TransactionService {

    private final TransactionDAO transactionDAO;
    private final ResourceService resourceService;
    private final ReminderDbService reminderDbService;
    private final StudentService studentService;

    public TransactionService() {
        this.transactionDAO = new TransactionDAOImpl();
        this.resourceService = new ResourceService();
        this.reminderDbService = new ReminderDbService();
        this.studentService = new StudentService();
    }

    // 🔥 LEND / BORROW TRANSACTION
    public Transaction createLendBorrowTransaction(int resourceId,
                                                   String lenderId,
                                                   String borrowerId,
                                                   String startDate,
                                                   String endDate) {

        Resource resource = resourceService.getResourceById(resourceId);

        // Load full student objects to check suspension status
        Student lender = studentService.getStudentById(lenderId);
        Student borrower = studentService.getStudentById(borrowerId);

        // Validate that neither lender nor borrower is suspended
        if (lender.isSuspended()) {
            throw new IllegalStateException("Lender account is suspended and cannot lend items.");
        }
        if (borrower.isSuspended()) {
            throw new IllegalStateException("Borrower account is suspended and cannot borrow items.");
        }

        Transaction transaction = TransactionFactory.createTransaction(
                "LENDBORROW",
                resource,
                lender,
                borrower,
                0.0
        );

        transaction.initiate();

        // Persist resource status change (AVAILABLE -> BORROWED)
        resourceService.markAsBorrowed(resource);

        int txId = transactionDAO.createTransaction("LENDBORROW", transaction.getStatus().name());
        if (txId <= 0) {
            throw new IllegalStateException("Failed to create transaction");
        }
        LocalDate s = safeParseDate(startDate, LocalDate.now());
        LocalDate e = safeParseDate(endDate, LocalDate.now().plusDays(7));
        transactionDAO.insertLendBorrow(txId, resourceId, lenderId, borrowerId, s, e, 0.0);

        // DB-backed reminder (due date - 1 day)
        try {
            String msg = "Return \"" + resource.getTitle() + "\" by " + e + " (Transaction #" + txId + ")";
            reminderDbService.createReminder(borrowerId, txId, msg, e.minusDays(1));
        } catch (Exception ignored) {
        }

        return transaction;
    }

    // Minimal-change helper for UI: BUY action by ids.
    // Note: This currently writes only the base Transaction row + updates Resource status.
    public Transaction createBuySellTransaction(int resourceId,
                                                String sellerId,
                                                String buyerId,
                                                double price) {

        Resource resource = resourceService.getResourceById(resourceId);

        // Load full student objects to check suspension status
        Student seller = studentService.getStudentById(sellerId);
        Student buyer = studentService.getStudentById(buyerId);

        // Validate that neither seller nor buyer is suspended
        if (seller.isSuspended()) {
            throw new IllegalStateException("Seller account is suspended and cannot sell items.");
        }
        if (buyer.isSuspended()) {
            throw new IllegalStateException("Buyer account is suspended and cannot buy items.");
        }

        Transaction transaction = TransactionFactory.createTransaction(
                "BUYSELL",
                resource,
                seller,
                buyer,
                price
        );

        transaction.initiate();
        transaction.complete();

        resourceService.markAsSold(resource);

        int txId = transactionDAO.createTransaction("BUYSELL", transaction.getStatus().name());
        if (txId <= 0) {
            throw new IllegalStateException("Failed to create transaction");
        }
        transactionDAO.insertBuySell(txId, resourceId, sellerId, buyerId, price);
        return transaction;
    }

    public List<BorrowedItem> getBorrowedItems(String borrowerId) {
        return transactionDAO.findBorrowedByBorrower(borrowerId);
    }

    public List<BoughtItem> getBoughtItems(String buyerId) {
        return transactionDAO.findBoughtByBuyer(buyerId);
    }

    public void returnBorrowedItem(int transactionId) {
        transactionDAO.completeLendBorrow(transactionId);
    }

    private static LocalDate safeParseDate(String v, LocalDate fallback) {
        try {
            return LocalDate.parse(v);
        } catch (Exception e) {
            return fallback;
        }
    }

    // 🔥 BUY / SELL TRANSACTION
    public Transaction createBuySellTransaction(Resource resource,
                                                Student seller,
                                                Student buyer,
                                                double price) {

        Transaction transaction = TransactionFactory.createTransaction(
                "BUYSELL",
                resource,
                seller,
                buyer,
                price
        );

        transaction.initiate();
        transaction.complete();

        resourceService.markAsSold(resource);
        int txId = transactionDAO.createTransaction("BUYSELL", transaction.getStatus().name());
        transactionDAO.insertBuySell(txId, resource.getResourceId(), seller.getId(), buyer.getId(), price);

        return transaction;
    }

    // 🔥 COMPLETE TRANSACTION (GENERIC)
    public void completeTransaction(Transaction transaction) {

        transaction.complete();

        // Only update transaction status if DAO supports it
        try {
            transactionDAO.updateStatus(
                    transaction.getTransactionId(),
                    transaction.getStatus().name()
            );
        } catch (Exception ignored) {
            // Safe fallback if method not implemented
        }
    }

    // 🔥 RETURN BORROWED ITEM
    public void completeLendBorrowTransaction(int transactionId) {

        try {
            transactionDAO.completeLendBorrow(transactionId);
        } catch (Exception ignored) {
            // fallback if DAO method not present
        }
    }

    // 🔥 GET ALL TRANSACTIONS
    public List<Transaction> getAllTransactions() {
        return transactionDAO.findAll();
    }
}
