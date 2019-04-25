package services;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import models.User;
import models.communication.LoginResult;
import models.exceptions.UserException;
import org.mindrot.jbcrypt.BCrypt;
import repos.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UsersService {



    public static void create(User user) throws UserException {
        if (!UserRepository.findByEmail(user.getEmail()).isPresent())
        {
            UserRepository.add(user);
        }else
        {
            throw new UserException("email ya existente.");
        }
    }
    public static void index()
    {
        UserRepository.all();
    }

    public static void update(User user)
    {
    }

    public static LoginResult login(String email, String password) {
        Optional<User> user = UserRepository.findByEmail(email);
        LoginResult result;
        if (!user.isPresent()) {
            result = LoginResult.InvalidUsernameOrPassword;
        } else {

            if (BCrypt.checkpw(password, user.get().getPasswordHash())) {
                Map<String, Object> token = new HashMap<>();
                token.put("userId", user.get().getId());
                result = LoginResult.Success(CodesService.generateTokenFromMap(token));
            } else
                result = LoginResult.InvalidUsernameOrPassword;
        }
        return result;
    }



}
