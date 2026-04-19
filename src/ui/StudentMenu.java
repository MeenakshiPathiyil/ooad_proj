package ui;

import controller.StudentController;
import model.user.Student;

import java.util.Scanner;

// StudentMenu manages CLI registration and login screens, following SRP for user interaction.
public class StudentMenu {

    private final Scanner scanner;
    private final StudentController studentController;

    public StudentMenu() {
        scanner = new Scanner(System.in);
        studentController = new StudentController();
    }

    // Displays student portal actions and routes user choices without embedding business logic.
    public void show() {

        while (true) {
            System.out.println("\n===== Student Portal =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Back");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    registerStudent();
                    break;

                case 2:
                    loginStudent();
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Collects signup data from the console and sends it to the student controller.
    private void registerStudent() {

        System.out.print("SRN: ");
        String id = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Department: ");
        String dept = scanner.nextLine();

        Student student = new Student(id, name, email, phone, password, dept);

        studentController.registerStudent(student);
    }

    // Collects login data and hands authentication over to the controller and service layers.
    private void loginStudent() {

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Student student = studentController.loginStudent(email, password);

        System.out.println("Welcome " + student.getName());

        new ResourceMenu(student).show();
    }
}
