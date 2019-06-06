package services;

import models.User;
import models.VenueList;
import models.communication.LoginResult;
import models.exceptions.UserException;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ListsService {
    public static void create(VenueList list) throws UserException {
        try {
            MongoDbConectionService.getDatastore().save(list);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

