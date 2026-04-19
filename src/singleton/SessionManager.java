package singleton;

import model.user.Student;

// SessionManager keeps one shared session object for the legacy flow, implementing the Singleton pattern.
public class SessionManager {

    private static SessionManager instance;
    private Student currentStudent;

    private SessionManager() {
    }

    // Returns the only SessionManager instance, which is the core Singleton access point.
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Stores the current student in one global place for the old CLI/API flow.
    public void login(Student student) {
        this.currentStudent = student;
    }

    // Clears the shared session state when the user logs out.
    public void logout() {
        this.currentStudent = null;
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public boolean isLoggedIn() {
        return currentStudent != null;
    }
}
