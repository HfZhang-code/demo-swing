import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    List<User> users = new ArrayList<>();
    // write method to get User objects
    public User getUser(String name, int age) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        return new User(name, age);
    }

    //write CRUD methods for User objects
    /**
     * Creates a new User object with the specified name and age.
     *
     * @param name the name of the user; must not be null or empty
     * @param age the age of the user; must be non-negative
     */
    public void createUser(String name, int age) {
        User user = new User(name, age);
        System.out.println("User created: " + user + ", Age: " + age);
    }

    public void addUser (User user) {
        users.add(user);
        System.out.println("User added: " + user);
    }

    public void deleteUser (User user) {
        users.remove(user);
        System.out.println("User deleted: " + user);
    }

}
