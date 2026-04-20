package unisync.web.command;

import model.transaction.Transaction;
import service.TransactionService;
import unisync.web.ApiResponse;
import unisync.web.dto.BorrowRequest;
import java.util.Map;

/**
 * CONCRETE COMMAND: BorrowTransactionCommand
 * 
 * PURPOSE: Encapsulate borrow/lend transaction logic
 * 
 * PATTERN: Command Pattern implementation
 * 
 * FLOW:
 * 1. Validate request (borrower exists, resource exists, dates valid, etc.)
 * 2. Call TransactionService.createLendBorrowTransaction()
 * 3. Service handles: resource fetching, transaction creation, status updates, DB persistence
 * 4. Return success response with transaction details
 * 
 * SINGLE RESPONSIBILITY:
 * - Only responsible for LENDBORROW transactions
 * - Delegates to service (doesn't mix concerns)
 * - Does NOT handle HTTP requests (controller does)
 * - Can be tested independently without Spring
 * 
 * ADVANTAGES:
 * - Clean separation: Validation + Service Call + Response Mapping
 * - Reusable: Can be called from any controller or service
 * - Testable: Unit test without HTTP/Spring
 * - Maintainable: All borrow logic orchestration in one place
 * - Leverages existing TransactionService methods
 * 
 * EXAMPLE USAGE:
 * BorrowRequest request = new BorrowRequest(resourceId, borrowerId, lenderId, start, end);
 * Student borrower = getLoggedInStudent(session);
 * TransactionCommand cmd = new BorrowTransactionCommand(
 *     request,
 *     borrower,
 *     resourceService,
 *     transactionService
 * );
 * Map<String, Object> response = cmd.execute();
 */
public class BuyTransactionCommand implements TransactionCommand {
    
    private final BorrowRequest request;
    private final String borrowerId;
    private final TransactionService transactionService;
    
    private static final String MESSAGE = "message";
    
    /**
     * Constructor: Initialize command with all dependencies
     * 
     * DEPENDENCY INJECTION: Services passed in (easier to test with mocks)
     */
    public BuyTransactionCommand(
            BorrowRequest request,
            String borrowerId,
            TransactionService transactionService) {
        this.request = request;
        this.borrowerId = borrowerId;
        this.transactionService = transactionService;
    }
    
    /**
     * EXECUTE: Perform the borrow transaction
     * 
     * STEPS:
     * 1. Validate input (throws exception if invalid)
     * 2. Call TransactionService to create the transaction
     *    (Service handles: resource fetching, factory creation, DB persistence, status updates)
     * 3. Return success response
     * 
     * ERROR HANDLING:
     * - Catches all exceptions
     * - Returns meaningful error messages via ApiResponse.fail()
     * - Does not throw exceptions to caller
     * 
     * RETURNS:
     * - Success: Map with status="success" and transaction ID
     * - Failure: Map with status="fail" and error message
     */
    @Override
    public Map<String, Object> execute() {
        try {
            // ✅ STEP 1: Validate - will throw if invalid
            validate();
            
            // ✅ STEP 2: Use TransactionService to create transaction
            // Service handles:
            // - Resource fetching and availability check
            // - Student creation
            // - Factory pattern to create LendBorrowTransaction
            // - Resource status change (AVAILABLE -> BORROWED)
            // - DB persistence
            // - Reminder creation
            Transaction transaction = transactionService.createLendBorrowTransaction(
                request.getResourceId(),
                request.getLenderId(),
                borrowerId,
                request.getStartDate(),
                request.getEndDate()
            );
            
            // ✅ STEP 3: Return success response
            Map<String, Object> response = ApiResponse.success();
            response.put(MESSAGE, "Borrow request sent successfully!");
            response.put("transactionId", transaction.getTransactionId());
            response.put("status", transaction.getStatus().name());
            return response;
            
        } catch (IllegalArgumentException e) {
            // Validation error - return user-friendly message
            return ApiResponse.fail(e.getMessage());
        } catch (Exception e) {
            // Unexpected error - return generic message
            return ApiResponse.fail("Error processing borrow request: " + e.getMessage());
        }
    }
    
    /**
     * VALIDATE: Check all prerequisites before executing
     * 
     * CHECKS:
     * 1. Borrower ID is not null/empty
     * 2. Resource ID is valid (> 0)
     * 3. Lender ID is provided
     * 4. Start date is provided
     * 5. End date is provided
     * 6. Borrower is not the lender (can't borrow from yourself)
     * 
     * NOTE: Resource existence and availability are checked by TransactionService
     * 
     * THROWS: IllegalArgumentException with descriptive message
     * 
     * CALLED BY: execute() before business logic
     */
    @Override
    public void validate() throws IllegalArgumentException {
        // ✅ CHECK 1: Borrower ID exists
        if (borrowerId == null || borrowerId.isEmpty()) {
            throw new IllegalArgumentException("Borrower not found. Please login first.");
        }
        
        // ✅ CHECK 2: Resource ID valid
        if (request.getResourceId() <= 0) {
            throw new IllegalArgumentException("Invalid resource ID");
        }
        
        // ✅ CHECK 3: Lender ID provided
        if (request.getLenderId() == null || request.getLenderId().isEmpty()) {
            throw new IllegalArgumentException("Lender ID not provided");
        }
        
        // ✅ CHECK 4: Start date provided
        if (request.getStartDate() == null || request.getStartDate().isEmpty()) {
            throw new IllegalArgumentException("Start date is required");
        }
        
        // ✅ CHECK 5: End date provided
        if (request.getEndDate() == null || request.getEndDate().isEmpty()) {
            throw new IllegalArgumentException("End date is required");
        }
        
        // ✅ CHECK 6: Not borrowing from yourself
        if (borrowerId.equals(request.getLenderId())) {
            throw new IllegalArgumentException("You cannot borrow from yourself");
        }
    }
    
    /**
     * GET DESCRIPTION: Human-readable command description
     * 
     * FORMAT: "BORROW transaction for resource #X by Student ID from Lender ID"
     * 
     * USAGE: Logging, debugging, audit trails
     * 
     * EXAMPLE: "BORROW transaction for resource #42 by STU001 from STU002 (Start: 2024-01-15, End: 2024-01-22)"
     */
    @Override
    public String getDescription() {
        return "BORROW transaction for resource #" + request.getResourceId()
               + " by " + borrowerId
               + " from " + request.getLenderId()
               + " (Start: " + request.getStartDate() + ", End: " + request.getEndDate() + ")";
    }
}
