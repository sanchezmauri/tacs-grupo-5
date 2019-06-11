package services;

import com.mongodb.BasicDBObject;
import dev.morphia.Datastore;
import dev.morphia.Key;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import models.*;
import models.communication.LoginResult;
import models.exceptions.UserException;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class UsersService {
    public static void create(User user) throws UserException {
        if (MongoDbConectionService.getDatastore().createQuery(User.class).filter("email =", user.getEmail()).first() == null) {
            MongoDbConectionService.getDatastore().save(user);
        } else {
            throw new UserException("email ya existente.");
        }
    }

    public static List<User> index() {
        return MongoDbConectionService.getDatastore().createQuery(User.class).asList();

    }

    public static User findById(String id) {
        return MongoDbConectionService.getDatastore().createQuery(User.class).field("id").equal(new ObjectId(id)).first();
    }

    public static User findByEmail(String email) {
        return MongoDbConectionService.getDatastore().createQuery(User.class).filter("email =", email).first();
    }

    public static void addList(User user, VenueList list) {
        try {
            ListsService.create(list);
            Query<User> userToUpdate = MongoDbConectionService.getDatastore().createQuery(User.class).field("id").equal(new ObjectId(user.getId()));
            UpdateOperations<User> userUpdate = MongoDbConectionService.getDatastore().createUpdateOperations(User.class)
                    .push("venueslists", list);
            MongoDbConectionService.getDatastore().update(userToUpdate, userUpdate);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addVenueToList(User user, VenueList list, UserVenue addedVenue) {
        try {
            Datastore datastore = MongoDbConectionService.getDatastore();

            Query<User> userQuery = datastore.createQuery(User.class).field("id").equal(new ObjectId(user.getId()));

            // todo: averiguar cómo hacer en mongo esto mejor
            int index = user.listIndex(list);

            UpdateOperations<User> addVenueUpdate = datastore.createUpdateOperations(User.class).push("venueslists." + Integer.toString(index) + ".venues", addedVenue);
            datastore.update(userQuery, addVenueUpdate);

            // ListsService.addVenue(list, addedVenue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static LoginResult login(String email, String password) {
        Optional<User> user = Optional.ofNullable(UsersService.findByEmail(email));

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

    public static void deleteUserVenue(User user, String venueId) {
        try {


            int venueCount = 0;

            for(VenueList venueList : user.getAllLists())
            {
                for (UserVenue userVenue : venueList.getVenues())
                {

                    UpdateOperations<User> ops = MongoDbConectionService.getDatastore().createUpdateOperations(User.class).disableValidation().removeAll("venueslists."+venueCount+".venues", new BasicDBObject("_id", venueId));
                    final Query<User> userVenueQuery = MongoDbConectionService.getDatastore().createQuery(User.class).field("id").equal(new ObjectId(user.getId()));
                    MongoDbConectionService.getDatastore().update(userVenueQuery, ops);
                }
                venueCount ++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteUserVenueList(User user, String listId) {
        try {

                    UpdateOperations<User> ops = MongoDbConectionService.getDatastore().createUpdateOperations(User.class).disableValidation().removeAll("venueslists", new BasicDBObject("_id", listId));
                    final Query<User> userVenueListQuery = MongoDbConectionService.getDatastore().createQuery(User.class).field("id").equal(new ObjectId(user.getId()));
                    MongoDbConectionService.getDatastore().update(userVenueListQuery, ops);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
