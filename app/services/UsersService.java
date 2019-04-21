package services;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import models.User;
import models.exceptions.UserException;
import repos.UserRepository;

import java.util.Map;

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



}
