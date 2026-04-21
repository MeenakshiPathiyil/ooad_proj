// Service layer is used to apply business rules, validate input, coordinate domain objects and call DAO methods
package service;

import model.user.Admin;
import model.user.Student;
import org.springframework.stereotype.Service;

// AdminService coordinates admin use cases
// Design principle used - Single Responsibility Principle (SRP) as the AdminService only handles admin use cases
@Service
public class AdminService {

    // Hard-coded admin credentials (can be replaced with database later)
    private static final String ADMIN_ID = "ADMIN001";
    private static final String ADMIN_NAME = "Administrator";
    private static final String ADMIN_EMAIL = "admin@unisync.edu";
    private static final String ADMIN_PASS = "admin123"; // In production, use hashed password

    // Authenticates an admin by verifying credentials
    public Admin login(String email, String password) {
        // Check if email and password match admin credentials
        if (!ADMIN_EMAIL.equals(email)) {
            throw new IllegalArgumentException("Admin email not found.");
        }

        if (!ADMIN_PASS.equals(password)) {
            throw new IllegalArgumentException("Invalid password.");
        }

        // Return authenticated admin
        return new Admin(ADMIN_ID, ADMIN_NAME, ADMIN_EMAIL, "0000000000", ADMIN_PASS);
    }

    // Delegates suspension through the admin role, keeping UI code independent from domain operations.
    public void suspendStudent(Admin admin, Student student) {
        admin.suspendStudent(student);
    }

    // Delegates reactivation through the admin role, supporting separation of concerns.
    public void activateStudent(Admin admin, Student student) {
        admin.activateStudent(student);
    }
}
