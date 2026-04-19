package observer;

// StudentNotification reacts to reminder events, making it the concrete Observer in the reminder flow.
public class StudentNotification implements ReminderObserver {

    private final String studentName;

    public StudentNotification(String studentName) {
        this.studentName = studentName;
    }

    @Override
    // Receives notification messages from the subject, which demonstrates the Observer principle.
    public void update(String message) {
        System.out.println("[NOTIFICATION] " + studentName + ": " + message);
    }
}
