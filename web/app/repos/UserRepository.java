package repos;

import models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class UserRepository {
    private static List<User> users = new ArrayList<>(
        Arrays.asList(
            new User(1L, "José", "jose@gmail.com", "jose"),
            new User(2L, "Pepe", "pepe@yahoo.com", "pepe"),
            new User(3L, "Otro", "otro@mail.com", "orto")
        )
    );

    public static List<User> all() {
        return users;
    }

    private static Optional<User> find(Predicate<User> predicate) {
        return users.stream()
                .filter(predicate)
                .findFirst();
    }

    public static Optional<User> find(Long id) {
        return find(user -> user.getId().equals(id));
    }

    public static Optional<User> findByEmail(String email) {
        return find(user -> user.getEmail().equals(email));
    }

    public static void add(User newUser) {
        users.add(newUser);
    }

    public static Long nextId() {
        return users.get(users.size() - 1).getId() + 1;
    }
}
