package unisync.web.command;

import service.TransactionService;
import unisync.web.dto.BorrowRequest;
import unisync.web.dto.ReturnRequest;

/**
 * FACTORY PATTERN: TransactionCommandFactory
 * 
 * PURPOSE: Create appropriate TransactionCommand instances based on transaction type
 * 
 * PATTERN: Factory Pattern implementation
 * 
 * WHY THIS DESIGN:
 * - Centralizes command instantiation logic
 * - Prevents "new" keyword scattered throughout controllers
 * - Makes it easy to add new transaction types
 * - Single point to add validation/preprocessing before command creation
 * 
 * BENEFITS:
 * 1. DECOUPLING: Controllers don't know about concrete command classes
 * 2. CENTRALIZATION: All command creation logic in one place
 * 3. EXTENSIBILITY: Add new transaction types by adding factory methods
 * 4. MAINTAINABILITY: Easy to modify how commands are created
 * 
 * FLOW:
 * 1. Controller needs to execute transaction
 * 2. Controller calls factory method: createBorrowCommand() or createBuySellCommand()
 * 3. Factory validates parameters and creates concrete command
 * 4. Factory returns command (type: TransactionCommand interface)
 * 5. Controller calls command.execute()
 * 
 * EXAMPLE USAGE:
 * // In controller method handling borrow request
 * String borrowerId = getLoggedInStudentId(session);
 * TransactionCommand cmd = TransactionCommandFactory.createBorrowCommand(
 *     borrowRequest,
 *     borrowerId,
 *     transactionService
 * );
 * Map<String, Object> response = cmd.execute();
 * return ResponseEntity.ok(response);
 * 
 * IMPLEMENTED FACTORIES:
 * - createBorrowCommand() - for LENDBORROW transactions
 * - createBuySellCommand() - for BUYSELL transactions (future)
 * - createReturnCommand() - for returning borrowed items (future)
 */
public class TransactionCommandFactory {
    
    // Private constructor: Factory shouldn't be instantiated
    private TransactionCommandFactory() {
    }
    
    /**
     * CREATE BORROW COMMAND
     * 
     * PURPOSE: Create BorrowTransactionCommand for lending/borrowing operations
     * 
     * PARAMETERS:
     * - request: BorrowRequest DTO with resourceId, lenderId, startDate, endDate
     * - borrowerId: ID of the student making the borrow request (current user)
     * - transactionService: Service to handle transaction persistence
     * 
     * VALIDATION:
     * - Ensures parameters are not null
     * - Throws IllegalArgumentException if parameters invalid
     * 
     * RETURNS: TransactionCommand ready to execute
     * 
     * EXAMPLE:
     * TransactionCommand cmd = TransactionCommandFactory.createBorrowCommand(
     *     borrowRequest,
     *     "STU123",
     *     transactionService
     * );
     * Map<String, Object> response = cmd.execute();
     * 
     * @param request BorrowRequest with resource, dates, and lender info
     * @param borrowerId Student ID of the borrower (current user)
     * @param transactionService Service for persistence
     * @return BorrowTransactionCommand ready to execute
     * @throws IllegalArgumentException if any parameter is null
     */
    public static TransactionCommand createBorrowCommand(
            BorrowRequest request,
            String borrowerId,
            TransactionService transactionService) {
        
        // ✅ STEP 1: Validate parameters
        if (request == null) {
            throw new IllegalArgumentException("BorrowRequest cannot be null");
        }
        
        if (borrowerId == null || borrowerId.isEmpty()) {
            throw new IllegalArgumentException("Borrower ID cannot be null or empty");
        }
        
        if (transactionService == null) {
            throw new IllegalArgumentException("TransactionService cannot be null");
        }
        
        // ✅ STEP 2: Create and return command
        return new BuyTransactionCommand(request, borrowerId, transactionService);
    }
    
    /**
     * CREATE BUYSELL COMMAND (Future Implementation)
     * 
     * PURPOSE: Create BuySellTransactionCommand for buying/selling operations
     * 
     * PARAMETERS:
     * - resourceId: ID of the resource being bought
     * - buyerId: ID of the student making the purchase (current user)
     * - sellerId: ID of the student selling the resource
     * - price: Asking price for the resource
     * - transactionService: Service to handle transaction persistence
     * 
     * NOTE: Requires BuyRequest DTO to be created first
     * 
     * RETURNS: TransactionCommand ready to execute
     * 
     * @param resourceId Resource being purchased
     * @param buyerId Student ID of the buyer (current user)
     * @param sellerId Student ID of the seller
     * @param price Purchase price
     * @param transactionService Service for persistence
     * @return TransactionCommand ready to execute
     * @throws IllegalArgumentException if parameters invalid
     */
    public static TransactionCommand createBuySellCommand(
            int resourceId,
            String buyerId,
            String sellerId,
            double price,
            TransactionService transactionService) {
        
        // ✅ STEP 1: Validate parameters
        if (buyerId == null || buyerId.isEmpty()) {
            throw new IllegalArgumentException("Buyer ID cannot be null or empty");
        }
        
        if (sellerId == null || sellerId.isEmpty()) {
            throw new IllegalArgumentException("Seller ID cannot be null or empty");
        }
        
        if (transactionService == null) {
            throw new IllegalArgumentException("TransactionService cannot be null");
        }
        
        if (resourceId <= 0) {
            throw new IllegalArgumentException("Resource ID must be greater than 0");
        }
        
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        
        throw new UnsupportedOperationException("Buy/Sell transactions not yet implemented");
    }
    
    /**
     * CREATE RETURN COMMAND (Future Implementation)
     * 
     * PURPOSE: Create ReturnTransactionCommand for returning borrowed items
     * 
     * PARAMETERS:
     * - request: ReturnRequest DTO with transaction ID and return details
     * - studentId: ID of the student returning the item
     * - transactionService: Service to handle transaction persistence
     * 
     * NOTE: ReturnRequest DTO already exists
     * 
     * RETURNS: TransactionCommand ready to execute
     * 
     * @param request ReturnRequest with transaction and return info
     * @param studentId Student ID of the borrower returning item
     * @param transactionService Service for persistence
     * @return TransactionCommand ready to execute
     * @throws IllegalArgumentException if parameters invalid
     */
    public static TransactionCommand createReturnCommand(
            ReturnRequest request,
            String studentId,
            TransactionService transactionService) {
        
        // ✅ STEP 1: Validate parameters
        if (request == null) {
            throw new IllegalArgumentException("ReturnRequest cannot be null");
        }
        
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        }
        
        if (transactionService == null) {
            throw new IllegalArgumentException("TransactionService cannot be null");
        }
        
        throw new UnsupportedOperationException("Return transactions not yet implemented");
    }
}
