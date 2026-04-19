package model.user;

// Defines common properties and behavior for all user types. It follows inheritance, abstraction, polymorphism, and OCP (Open-Closed Principle).
// Reason for this design: 
// 1. It eliminates code duplication
// 2. It allows flexible authentication for different user types
// 3. Makes it easy to add new user types in the future
// 4. Supports login validation at the user level while allowing subclass-specific rules

public abstract class User {

    protected String id;
    protected String name;
    protected String email;
    protected String phone;
    protected String password;

    public User(String id, String name, String email, String phone, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
    public String getPassword() {
    return password;
}
    // Lets each user subtype define its own login rule, which uses polymorphism.
    // For student, it checks suspension status and password
    // For admin, it just checks password
    public abstract boolean login(String password);

    // Lets each user subtype define its own logout behavior, which also uses polymorphism.
    public abstract void logout();

    // Updates common contact fields in the base class, keeping shared logic centralized.
    public void updateContact(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }
}
