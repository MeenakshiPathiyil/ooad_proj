package unisync.web.helper;

import model.resource.Resource;
import model.resource.ResourceBuilder;
import model.resource.ListingType;
import model.resource.Category;
import model.user.Student;
import unisync.web.dto.AddResourceRequest;

/**
 * RESOURCE CREATION HELPER - Shows how to use ResourceBuilder
 * 
 * PURPOSE: Demonstrate Builder Pattern usage in practical scenarios
 * 
 * BENEFITS:
 * 1. CENTRALIZED: All resource creation logic in one place
 * 2. SAFE: Uses builder validation
 * 3. READABLE: Clear what's being set
 * 4. EXTENSIBLE: Easy to add new creation patterns
 * 
 * PATTERN: BUILDER PATTERN
 * ✅ Fluent interface for building resources
 * ✅ Validation before creation
 * ✅ Optional parameters
 * ✅ Clear, self-documenting code
 */
public class ResourceCreationHelper {
    
    /**
     * Private constructor - utility class, not instantiable
     */
    private ResourceCreationHelper() {
    }
    
    /**
     * Create resource from API request
     * 
     * USAGE: Called from REST API endpoints
     * 
     * FLOW:
     * 1. Receive AddResourceRequest from client
     * 2. Use builder to create Resource
     * 3. Builder validates fields
     * 4. Return created resource
     * 
     * @param req The request with resource details
     * @param owner The student creating the resource
     * @return Validated Resource object
     * @throws IllegalArgumentException if validation fails
     */
    public static Resource createFromRequest(AddResourceRequest req, Student owner) {
        return new ResourceBuilder()
            .title(req.getTitle())
            .description(req.getDescription())
            .condition(req.getCondition())
            .listingType(ListingType.valueOf(req.getType()))
            .price(req.getPrice() != null ? req.getPrice() : 0.0)
            .owner(owner)
            .category(new Category(req.getCategoryId(), "", ""))
            .build();
    }
    
    /**
     * Create a textbook resource
     * 
     * USAGE: Quick helper for listing textbooks
     * 
     * @param title Book title
     * @param author Author name
     * @param owner Student listing
     * @param condition Item condition
     * @param price Price in currency
     * @return Resource object for textbook
     */
    public static Resource createTextbook(
            String title,
            String author,
            Student owner,
            String condition,
            double price) {
        
        return new ResourceBuilder()
            .title(title)
            .description("Author: " + author)
            .condition(condition)
            .listingType(ListingType.SELL)
            .price(price)
            .owner(owner)
            .category(new Category(1, "Textbooks", "Engineering"))  // Assume category 1
            .build();
    }
    
    /**
     * Create a borrowed item (for lending)
     * 
     * USAGE: When student lends something temporarily
     * 
     * @param title Item name
     * @param description What it is
     * @param owner Student lending
     * @param condition Item condition
     * @return Resource object for lending (price = 0)
     */
    public static Resource createBorrowable(
            String title,
            String description,
            Student owner,
            String condition) {
        
        return new ResourceBuilder()
            .title(title)
            .description(description)
            .condition(condition)
            .listingType(ListingType.LEND)
            .price(0.0)  // Free to borrow
            .owner(owner)
            .category(new Category(2, "Equipment", "Misc"))  // Assume category 2
            .build();
    }
    
    /**
     * Create a barter item
     * 
     * USAGE: When student wants to trade
     * 
     * @param title Item name
     * @param description What it is
     * @param owner Student bartering
     * @param condition Item condition
     * @return Resource object for bartering
     */
    public static Resource createBarterable(
            String title,
            String description,
            Student owner,
            String condition) {
        
        return new ResourceBuilder()
            .title(title)
            .description(description)
            .condition(condition)
            .listingType(ListingType.BARTER)
            .price(0.0)  // No price for barter
            .owner(owner)
            .category(new Category(3, "Misc", "Barter"))
            .build();
    }
    
    /**
     * EXAMPLE USAGE IN CONTROLLER:
     * 
     * @PostMapping("/addResource")
     * public ResponseEntity<?> addResource(
     *     @RequestBody AddResourceRequest req,
     *     HttpSession session) {
     *     
     *     Student currentUser = (Student) session.getAttribute(UiSession.CURRENT_USER);
     *     
     *     try {
     *         // ✅ Use helper to create resource with builder
     *         Resource resource = ResourceCreationHelper.createFromRequest(req, currentUser);
     *         
     *         // ✅ Builder already validated, safe to save
     *         resourceService.addResource(resource);
     *         
     *         return ResponseEntity.ok(ApiResponse.success("Resource added!"));
     *     } catch (IllegalArgumentException e) {
     *         return ResponseEntity.badRequest()
     *             .body(ApiResponse.error(e.getMessage()));
     *     }
     * }
     */
}
