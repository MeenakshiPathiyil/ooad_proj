package unisync.web.command;

import java.util.Map;

/**
 * COMMAND PATTERN - TransactionCommand Interface
 * 
 * PURPOSE: Encapsulate transaction requests as executable objects
 * 
 * DESIGN PRINCIPLES:
 * 1. COMMAND PATTERN - Encapsulate request as object
 * 2. SEPARATION OF CONCERNS - Isolate business logic
 * 3. SINGLE RESPONSIBILITY - Each command does one transaction type
 * 4. OPEN/CLOSED PRINCIPLE - Open for extension (new commands), closed for modification
 * 
 * WHY THIS DESIGN:
 * - ❌ Before: Each controller duplicated transaction logic
 * - ✅ After: Command pattern centralizes logic in reusable command objects
 * 
 * BENEFITS:
 * 1. ELIMINATES DUPLICATION: Shared code in one place
 * 2. CONSISTENCY: All transactions handled same way
 * 3. TESTABILITY: Easy to unit test each command independently
 * 4. EXTENSIBILITY: Add new transaction types by creating new command classes
 * 5. MAINTAINABILITY: Logic centralized, easy to modify
 * 6. FUTURE FEATURES: Can implement undo/redo, command queuing, logging
 * 
 * FLOW:
 * 1. Controller receives HTTP request
 * 2. Controller creates appropriate Command object
 * 3. Command encapsulates: validation + business logic + persistence
 * 4. Controller calls command.execute()
 * 5. Command handles everything, returns response Map
 * 
 * EXAMPLE USAGE:
 * TransactionCommand cmd = new BorrowTransactionCommand(request, borrowerId, services);
 * Map<String, Object> response = cmd.execute();
 * return ResponseEntity.ok(response);
 * 
 * IMPLEMENTED COMMANDS:
 * - BuyTransactionCommand (for LENDBORROW transactions)
 * - (Can add more: BuySellTransactionCommand, BarterTransactionCommand, etc.)
 * 
 * RESPONSE FORMAT:
 * Success: { "status": "success", "message": "...", "data": {...} }
 * Failure: { "status": "fail", "message": "Error description" }
 */
public interface TransactionCommand {
    
    /**
     * PURPOSE: Execute the transaction command
     * 
     * RESPONSIBILITIES:
     * 1. Validate all prerequisites
     * 2. Create transaction object
     * 3. Save to database
     * 4. Return response
     * 
     * RETURNS: Map<String, Object> with status, message, and optionally data
     *          Format: {"status": "success"/"fail", "message": "...", ...}
     * 
     * THROWS: None (catches internally and returns error in response)
     */
    Map<String, Object> execute();
    
    /**
     * PURPOSE: Validate command before execution
     * 
     * CHECKS:
     * - All required parameters present
     * - Resources/students exist in database
     * - Business rules satisfied (e.g., can't buy from yourself)
     * - Data format valid (dates, prices, etc.)
     * 
     * THROWS: IllegalArgumentException if validation fails
     * 
     * CALLED BY: execute() method (before attempting transaction)
     */
    void validate() throws IllegalArgumentException;
    
    /**
     * PURPOSE: Get human-readable command description
     * 
     * USAGE: Logging, debugging, user messages
     * 
     * EXAMPLE: "BUY transaction for resource #5 by Rajesh Kumar"
     * 
     * RETURNS: Descriptive string
     */
    String getDescription();
}
