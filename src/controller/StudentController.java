package controller;

import model.user.Student;
import service.StudentService;

import java.util.List;

// StudentController handles legacy student requests, following GRASP Controller and low coupling.
public class StudentController {

    private final StudentService studentService;

    public StudentController() {
        this.studentService = new StudentService();
    }

    // Delegates student registration to the service layer, keeping input handling separate from logic.
    public void registerStudent(Student student) {
        studentService.registerStudent(student);
        System.out.println("[INFO] Student registered successfully.");
    }

    // Delegates login to the service so authentication rules stay centralized.
    public Student loginStudent(String email, String password) {
        Student student = studentService.login(email, password);
        System.out.println("[INFO] Login successful.");
        return student;
    }

    // Returns all students for legacy admin features without exposing DAO access directly.
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Sends profile updates through the service layer to preserve separation of concerns.
    public void updateStudent(Student student) {
        studentService.updateStudent(student);
        System.out.println("[INFO] Student updated.");
    }

    // Delegates deletion to the service layer, which keeps menu classes thin.
    public void deleteStudent(String studentId) {
        studentService.removeStudent(studentId);
        System.out.println("[INFO] Student deleted.");
    }
}
