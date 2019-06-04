package services;


import com.google.gson.Gson;
import models.User;
import models.communication.LoginResult;
import models.exceptions.UserException;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;
import repos.UserRepository;

import java.util.*;
import java.util.function.Consumer;

public class UsersService {


    public static void create(User user) throws UserException {
        if (!UserRepository.findByEmail(user.getEmail()).isPresent()) {
            //UserRepository.add(user);
            MongoDbService.insertDocument(MongoDbService.getActualWorkingDataBaseName(),"user", user);
        } else {
            throw new UserException("email ya existente.");
        }
    }

    public static List<User> index() {
        UserRepository.all();
        List<User> users = new ArrayList<>();
        MongoDbService.getAllDocuments(MongoDbService.getProductionDataBaseName(),"user").forEach((Consumer<? super Document>) a ->{
            Gson gson = new Gson();
            User user = gson.fromJson(a.toJson(), User.class);
            users.add(user);
        });
        return users;
    }

    public static void update(User user) {
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
