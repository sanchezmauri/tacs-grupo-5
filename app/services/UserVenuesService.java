package services;

import com.mongodb.BasicDBObject;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import models.FoursquareVenue;
import models.User;
import models.UserVenue;
import models.VenueList;
import models.exceptions.UserException;
import org.bson.types.ObjectId;

import java.util.Map;
import java.util.Optional;


public class UserVenuesService {
    public static void create(UserVenue userVenue) throws UserException {
        try {
            MongoDbConectionService.getDatastore().save(userVenue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static UserVenue findById(String id)
    {
        return MongoDbConectionService.getDatastore().createQuery(UserVenue.class).field("id").equal(new ObjectId(id)).first();
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

//    public Optional<UserVenue> getVenue(String venueId) {
//        return venueslists.stream()
//                .map(list -> list.getVenue(venueId))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .findAny();
//    }

}

