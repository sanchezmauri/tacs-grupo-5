package repos;

import models.Rol;
import models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private static List<User> users = new ArrayList<>(
        Arrays.asList(
            new User("José", "root@root.com", "root", Rol.ROOT),
            new User( "Pepe","sysuser@sysuser.com" ,"sysuser", Rol.SYSUSER),
            new User("Otro","otro@gmail.com", "orto", Rol.SYSUSER)
        )
    );
    private static long idGen = users.size() + 1;

    public static List<User> all() {
        return users;
    }

    public static Optional<User> find(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public static Optional<User> findByEmail(String email){
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public static void add(User newUser) {
        users.add(newUser);
    }

    public static Long nextId() {
        return idGen++;
    }
}
