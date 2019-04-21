package repos;

import models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private static List<User> users = new ArrayList<>(
        Arrays.asList(
            new User(1L, "José", "jose", User.Rol.ROOT),
            new User(2L, "Pepe", "pepe", User.Rol.SYSUSER),
            new User(3L, "Otro", "orto", User.Rol.SYSUSER)
        )
    );

    public static List<User> all() {
        return users;
    }

    public static Optional<User> find(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public static void add(User newUser) {
        users.add(newUser);
    }

    public static Long nextId() {
        return users.get(users.size() - 1).getId() + 1;
    }
}
