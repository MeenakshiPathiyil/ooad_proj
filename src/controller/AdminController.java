package controller;

import model.user.Admin;
import model.user.Student;
import service.AdminService;

// AdminController coordinates legacy admin actions, following the GRASP Controller principle.
public class AdminController {

    private final AdminService adminService;

    public AdminController() {
        this.adminService = new AdminService();
    }

    // Delegates suspension to the admin service so UI code stays focused on interaction only.
    public void suspendStudent(Admin admin, Student student) {
        adminService.suspendStudent(admin, student);
        System.out.println("[INFO] Student suspended.");
    }

    // Delegates activation to the service layer, which supports low coupling.
    public void activateStudent(Admin admin, Student student) {
        adminService.activateStudent(admin, student);
        System.out.println("[INFO] Student activated.");
    }
}
