package services;

import models.User;
import models.communication.LoginResult;
import models.exceptions.UserException;
import org.mindrot.jbcrypt.BCrypt;
import repos.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class UsersService {
    public static void create(User user) throws UserException {
        if (MongoDbConectionService.getDatastore().createQuery(User.class).filter("email <=", user.getEmail()).first() == null)
        {
            MongoDbConectionService.getDatastore().save(user);
        } else {
            throw new UserException("email ya existente.");
        }
    }

    public static List<User> index()
    {
        return MongoDbConectionService.getDatastore().createQuery(User.class).asList();
    }

    public static void update(User user)
    {

    }

    public static LoginResult login(String email, String password) {
        Optional<User> user = UserRepository.findByEmail(email);

        if (!user.isPresent()) {
            return LoginResult.InvalidUsernameOrPassword;
        } else {
            if (BCrypt.checkpw(password, user.get().getPasswordHash())) {
                Map<String, Object> token = new HashMap<>();
                token.put("userId", user.get().getId());
                return LoginResult.Success(CodesService.generateTokenFromMap(token), user.get());
            } else {
                return LoginResult.InvalidUsernameOrPassword;
            }
        }
    }
}
