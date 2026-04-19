package model.user;

// Admin models admin-only actions, following SRP and role-based polymorphism.
public class Admin extends User {

    public Admin(String id, String name, String email, String phone, String password) {
        super(id, name, email, phone, password);
    }

    @Override
    // Provides admin-specific authentication behavior through polymorphism.
    public boolean login(String password) {
        return this.password.equals(password);
    }

    @Override
    public void logout() {
        System.out.println("Admin logged out successfully.");
    }

    // Triggers suspension through the domain model while keeping admin logic focused.
    public void suspendStudent(Student student) {
        student.suspend();
    }

    // Reactivates a student without exposing state changes directly to the UI layer.
    public void activateStudent(Student student) {
        student.activate();
    }
}
