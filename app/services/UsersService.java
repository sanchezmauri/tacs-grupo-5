package services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.mongodb.util.JSON;
import models.User;
import models.communication.LoginResult;
import models.exceptions.UserException;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;
import play.libs.Json;
import repos.UserRepository;

import javax.print.Doc;
import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;

public class UsersService {

    static String mongoCollectionName = "user";

    public static void create(User user) throws UserException {
        if ( MongoDbService.getDocument(MongoDbService.getActualWorkingDataBase(), "user", "email", user.getEmail()) == null ){
            //UserRepository.add(user);
            MongoDbService.insertDocument(MongoDbService.getActualWorkingDataBase(), mongoCollectionName, user);
        } else{
            throw new UserException("email ya existente.");
        }
    }

    public static User findByProperty(String key, String value) throws IOException {
        Document doc = MongoDbService.getDocument(MongoDbService.getActualWorkingDataBase(), mongoCollectionName, key, value);
        //String date = doc.get("lastAccess");
        doc.remove("lastAccess");
        return new ObjectMapper().readValue(doc.toJson(), User.class);
    }

    public static List<User> index() {
        UserRepository.all();
        List<User> users = new ArrayList<>();
        MongoDbService.getAllDocuments(MongoDbService.getProductionDataBaseName(), mongoCollectionName).forEach((Consumer<? super Document>) a -> {
            Gson gson = new Gson();
            User user = gson.fromJson(a.toJson(), User.class);
            users.add(user);
        });
        return users;
    }

    public static void update(User user) {
    }

    public static LoginResult login(String email, String password) throws IOException {
        Optional<User> user = Optional.ofNullable(UsersService.findByProperty("email", email));
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
