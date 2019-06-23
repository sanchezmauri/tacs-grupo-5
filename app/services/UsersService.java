package services;

import com.mongodb.BasicDBObject;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import models.*;
import models.communication.LoginResult;
import models.exceptions.UserException;
import models.venues.FSVenueSearch;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class UsersService {

    private MongoDbConnectionService dbConnectionService;
    private ListsService listsService;
    private FoursquareVenueService foursquareVenueService;
    @Inject
    public UsersService(MongoDbConnectionService dbConnectionService,
                        ListsService listsService,
                        FoursquareVenueService foursquareVenueService) {
        this.dbConnectionService = dbConnectionService;
        this.listsService = listsService;
        this.foursquareVenueService = foursquareVenueService;
    }


    public void create(User user) throws UserException {
        if (dbConnectionService.getDatastore().createQuery(User.class).filter("email =", user.getEmail()).first() == null) {
            dbConnectionService.getDatastore().save(user);
        } else {
            throw new UserException("email ya existente.");
        }
    }

    public List<User> index() {
        return dbConnectionService.getDatastore().createQuery(User.class).find().toList();
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(
                    dbConnectionService.getDatastore()
                            .createQuery(User.class)
                            .field("id")
                            .equal(new ObjectId(id))
                            .first()
        );
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(dbConnectionService.getDatastore().createQuery(User.class).filter("email =", email).first());
    }

    public List<User> findByName(String name) {
        Datastore ds = dbConnectionService.getDatastore();

        return ds.createQuery(User.class)
                .field("name")
                .containsIgnoreCase(name)
                .find()
                .toList();
    }

    public void addList(User user, VenueList list) {
        try {
            listsService.create(list);
            Query<User> userToUpdate = dbConnectionService.getDatastore().createQuery(User.class).field("id").equal(new ObjectId(user.getId()));
            UpdateOperations<User> userUpdate = dbConnectionService.getDatastore().createUpdateOperations(User.class)
                    .push("venueslists", list);
            dbConnectionService.getDatastore().update(userToUpdate, userUpdate);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Boolean addVenueToList(User user, VenueList list, UserVenue addedVenue) {
        try {
            Datastore datastore = dbConnectionService.getDatastore();

            Query<User> userQuery = datastore.createQuery(User.class).field("id").equal(new ObjectId(user.getId()));

            // todo: averiguar cómo hacer en mongo esto mejor
            int index = user.listIndex(list);

            UpdateOperations<User> addVenueUpdate = datastore.createUpdateOperations(User.class).push("venueslists." + Integer.toString(index) + ".venues", addedVenue);
            datastore.update(userQuery, addVenueUpdate);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public LoginResult login(String email, String password) {
        Optional<User> user = this.findByEmail(email);

        if (user.isEmpty()) {
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

    public void deleteUserVenue(User user, String venueId) {
        try {
            int venueCount = 0;

            for(VenueList venueList : user.getAllLists())
            {
                for (UserVenue userVenue : venueList.getVenues())
                {

                    UpdateOperations<User> ops = dbConnectionService.getDatastore().createUpdateOperations(User.class).disableValidation().removeAll("venueslists."+venueCount+".venues", new BasicDBObject("_id", venueId));
                    final Query<User> userVenueQuery = dbConnectionService.getDatastore().createQuery(User.class).field("id").equal(new ObjectId(user.getId()));
                    dbConnectionService.getDatastore().update(userVenueQuery, ops);
                }
                venueCount ++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void visitUserVenue(User user,String listId, String venueId) {
        try {


            int venueListCount = 0;

            for(VenueList venueList : user.getAllLists())
            {
                int venueCount = 0;
                for (UserVenue userVenue : venueList.getVenues())
                {
                    if (dbConnectionService.getDatastore().createQuery(User.class).field("venueslists."+venueListCount+".venues."+venueCount+".id").equal(venueId).first() != null)

                    {
                        UpdateOperations<User> ops = dbConnectionService.getDatastore().createUpdateOperations(User.class).disableValidation().set("venueslists."+venueListCount+".venues."+venueCount+".visited", true);
                        final Query<User> userVenueQuery = dbConnectionService.getDatastore().createQuery(User.class).field("id").equal(new ObjectId(user.getId()));
                        dbConnectionService.getDatastore().update(userVenueQuery, ops);
                    }

                    venueCount ++;

                }
                venueListCount ++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUserVenueList(User user, String listId) {
        try {

            UpdateOperations<User> ops = dbConnectionService.getDatastore().createUpdateOperations(User.class).disableValidation().removeAll("venueslists", new BasicDBObject("_id", listId));
            final Query<User> userVenueListQuery = dbConnectionService.getDatastore().createQuery(User.class).field("id").equal(new ObjectId(user.getId()));
            dbConnectionService.getDatastore().update(userVenueListQuery, ops);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean addVenuesToList(User user, VenueList list, List<FSVenueSearch> venueSearches) {
        return venueSearches.stream().map((venue) -> {

            FoursquareVenue fqVenue = foursquareVenueService.getOrCreate(venue.id ,venue.name, venue.location.address);

            UserVenue userVenue = new UserVenue(fqVenue, false);

            return this.addVenueToList(user, list, userVenue);
        }).allMatch(x-> x);
    }
}
