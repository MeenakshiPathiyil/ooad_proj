package ui;

import controller.ResourceController;
import model.resource.*;
import model.user.Student;

import java.util.Scanner;

// ResourceMenu provides a CLI view for resource use cases, following SRP and separation of concerns.
public class ResourceMenu {

    private final Scanner scanner;
    private final ResourceController resourceController;
    private final Student student;

    public ResourceMenu(Student student) {
        scanner = new Scanner(System.in);
        resourceController = new ResourceController();
        this.student = student;
    }

    // Displays the legacy resource options and routes user input to the proper controller action.
    public void show() {

        while (true) {
            System.out.println("\n===== Resource Menu =====");
            System.out.println("1. Add Resource");
            System.out.println("2. View Available Resources");
            System.out.println("3. Transactions");
            System.out.println("4. Reviews");
            System.out.println("5. Back");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    addResource();
                    break;

                case 2:
                    viewResources();
                    break;

                case 3:
                    new TransactionMenu(student).show();
                    break;

                case 4:
                    new ReviewMenu().show();
                    break;

                case 5:
                    return;
            }
        }
    }

    // Collects console input and creates a Resource object for the controller, keeping UI concerns local.
    private void addResource() {

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Description: ");
        String desc = scanner.nextLine();

        Category category = new Category(1, "Books", "Textbook");

        Resource resource = new Resource(
                0,
                title,
                desc,
                "Good",
                ListingType.SELL,
                student,
                category
        );

        resourceController.addResource(resource);
    }

    // Prints available resources for the CLI without embedding business logic in the menu.
    private void viewResources() {
        resourceController.getAvailableResources()
                .forEach(r -> System.out.println(r.getTitle()));
    }
}
