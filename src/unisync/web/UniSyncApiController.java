package unisync.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import model.resource.Category;
import model.resource.ListingType;
import model.resource.Resource;
import model.resource.ResourceBuilder;
import model.transaction.Transaction;
import model.user.Student;
import model.user.Admin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ResourceService;
import service.StudentService;
import service.AdminService;
import service.TransactionService;
import unisync.web.dto.*;
import unisync.web.ui.UiSession;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UniSyncApiController {

    private final StudentService studentService;
    private final ResourceService resourceService;
    private final TransactionService transactionService;
    private final AdminService adminService;

    public UniSyncApiController(StudentService studentService,
                                ResourceService resourceService,
                                TransactionService transactionService,
                                AdminService adminService) {
        this.studentService = studentService;
        this.resourceService = resourceService;
        this.transactionService = transactionService;
        this.adminService = adminService;
    }

    @GetMapping("/api/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UniSync API Running");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try {
            // Try admin login first
            try {
                Admin admin = adminService.login(req.getEmail(), req.getPassword());
                Map<String, Object> res = ApiResponse.success();
                res.put("role", "admin");
                res.put("id", admin.getId());
                res.put("name", admin.getName());
                res.put("email", admin.getEmail());
                return ResponseEntity.ok(res);
            } catch (Exception adminLoginFailed) {
                // Admin login failed, try student login
            }

            // Try student login
            Student s = studentService.login(req.getEmail(), req.getPassword());

            Map<String, Object> res = ApiResponse.success();
            res.put("role", "student");
            res.put("srn", s.getId());
            res.put("name", s.getName());
            res.put("email", s.getEmail());
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            System.out.println("DEBUG - Login Error: " + e.getMessage());
            // Check if the error is due to suspension
            if (e.getMessage() != null && e.getMessage().contains("suspended")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Your account has been suspended"));
            }
            // Otherwise it's invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Invalid credentials"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest req) {
        // Constructor expects: (id, name, email, phone, password, dept)
        Student student = new Student(
                req.getId(),
                req.getName(),
                req.getEmail(),
                req.getPhone(),
                req.getPassword(),
                req.getDept()
        );

        studentService.registerStudent(student);
        return ResponseEntity.ok(ApiResponse.success("message", "User registered successfully"));
    }

    @GetMapping("/resources")
    public ResponseEntity<?> resources() {
        List<Resource> resources = resourceService.getAvailableResources();

        List<Object> data = new ArrayList<>();
        for (Resource r : resources) {
            // Keep existing JSON shape as much as possible
            data.add(r.toJson().toMap());
        }

        return ResponseEntity.ok(ApiResponse.success("data", data));
    }

    @PostMapping("/addResource")
    public ResponseEntity<?> addResource(@Valid @RequestBody AddResourceRequest req, HttpSession session) {
        Student currentUser = (Student) session.getAttribute(UiSession.CURRENT_USER);
        
        // ✅ BUILDER PATTERN: Use ResourceBuilder for clear, validated resource creation
        try {
            Resource resource = new ResourceBuilder()
                    .title(req.getTitle())
                    .description(req.getDescription())
                    .condition(req.getCondition())
                    .listingType(ListingType.valueOf(req.getType()))
                    .price(req.getPrice() != null ? req.getPrice() : 0.0)
                    .owner(currentUser)
                    .category(new Category(req.getCategoryId(), "", ""))
                    .build();  // ← Validates all fields before creating

            resourceService.addResource(resource);
            return ResponseEntity.ok(ApiResponse.success());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrow(@Valid @RequestBody BorrowRequest req) {
        transactionService.createLendBorrowTransaction(
                req.getResourceId(),
                req.getLenderId(),
                req.getBorrowerId(),
                req.getStartDate(),
                req.getEndDate()
        );

        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnBorrowed(@Valid @RequestBody ReturnRequest req) {
        transactionService.completeLendBorrowTransaction(req.getTransactionId());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<?> transactions(@PathVariable String userId) {
        // Keep behavior aligned with your current handler: no strict filtering yet
        List<Transaction> transactions = transactionService.getAllTransactions();

        List<Map<String, Object>> data = new ArrayList<>();
        for (Transaction t : transactions) {
            Map<String, Object> obj = new LinkedHashMap<>();
            obj.put("transactionId", t.getTransactionId());
            if (t instanceof model.transaction.StoredTransaction st) {
                obj.put("type", st.getTransactionType());
            } else {
                obj.put("type", t.getClass().getSimpleName());
            }
            obj.put("status", t.getStatus().name());
            data.add(obj);
        }

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("data", data);
        res.put("userId", userId);
        return ResponseEntity.ok(res);
    }

    @RequestMapping(path = {"/login", "/signup", "/addResource", "/borrow", "/return", "/resources", "/transactions/{userId}"}, method = {RequestMethod.OPTIONS})
    public ResponseEntity<?> options() {
        // Helpful for browser-based clients; harmless for Streamlit.
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
