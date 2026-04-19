// Service layer is used to apply business rules, validate input, coordinate domain objects and call DAO methods
package service;

import model.user.Admin;
import model.user.Student;

// AdminService coordinates admin use cases
// Design principle used - Single Responsibility Principle (SRP) as the AdminService only handles admin use cases
public class AdminService {

    // Delegates suspension through the admin role, keeping UI code independent from domain operations.
    public void suspendStudent(Admin admin, Student student) {
        admin.suspendStudent(student);
    }

    // Delegates reactivation through the admin role, supporting separation of concerns.
    public void activateStudent(Admin admin, Student student) {
        admin.activateStudent(student);
    }
}
