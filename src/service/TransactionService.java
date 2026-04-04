package service;

import dao.interfaces.TransactionDAO;
import dao.impl.TransactionDAOImpl;
import factory.TransactionFactory;
import model.resource.Resource;
import model.transaction.Transaction;
import model.user.Student;

import java.util.List;

public class TransactionService {

    private final TransactionDAO transactionDAO;
    private final ResourceService resourceService;

    public TransactionService() {
        this.transactionDAO = new TransactionDAOImpl();
        this.resourceService = new ResourceService();
    }

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
        transactionDAO.save(transaction);

        return transaction;
    }

    public void completeTransaction(Transaction transaction, Resource resource) {

        transaction.complete();

        if (!resource.isAvailable()) {
            resourceService.markAsSold(resource);
        }

        transactionDAO.updateStatus(
                transaction.getTransactionId(),
                transaction.getStatus().name()
        );
    }

    public List<Transaction> getAllTransactions() {
        return transactionDAO.findAll();
    }
}