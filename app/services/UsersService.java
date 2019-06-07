package services;

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
        try {
            if (MongoDbConectionService.getDatastore().createQuery(User.class).filter("email =", user.getEmail()).first() == null) {
                MongoDbConectionService.getDatastore().save(user);
            } else {
                throw new UserException("email ya existente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            UpdateOperations<User> userUpdate = MongoDbConectionService.getDatastore().createUpdateOperations(User.class)
                    .push("venueslists", list);
            MongoDbConectionService.getDatastore().update(MongoDbConectionService.getDatastore().createQuery(User.class).field("id").equal(new ObjectId(user.getId())), userUpdate);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addVenue(User user, VenueList list, Venue venue) {
        try {
            if (UserVenuesService.findById(venue.getId())==null) {

                UserVenue userVenue = new UserVenue(new FoursquareVenue(venue.getId(), venue.getName(), venue.getAddress(), LocalDate.now()), false);
                UpdateOperations<User> userUpdate = MongoDbConectionService.getDatastore().createUpdateOperations(User.class).push("venueslists.venues", userVenue);
                MongoDbConectionService.getDatastore().update(MongoDbConectionService.getDatastore().createQuery(User.class).field("id").equal(new ObjectId(user.getId())), userUpdate);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        public void addVenueToList(VenueList list, String venueId, String venueName, String venueAddress) {
//        if (!list.hasVenue(venueId)) {
//            Optional<UserVenue> existingVenue = getVenue(venueId);
//
//            list.addVenue(
//                    existingVenue.orElse(
//                            new UserVenue(
//                                    new FoursquareVenue(venueId, venueName, venueAddress, LocalDate.now()),
//                                    false)
//                    )
//            );
//        }
//    }
//        public Optional<UserVenue> getVenue(String venueId) {
//        return venueslists.stream()
//                .map(list -> list.getVenue(venueId))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .findAny();
//    }
//        public boolean hasVenue(String venueId) {
//        return getVenue(venueId).isPresent();
//    }

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
}
