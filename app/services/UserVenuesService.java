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
        return new UserVenue(MongoDbConectionService.getDatastore().createQuery(FoursquareVenue.class).field("id").equal(new ObjectId(id)).first(),true);
    }



//    public Optional<UserVenue> getVenue(String venueId) {
//        return venueslists.stream()
//                .map(list -> list.getVenue(venueId))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .findAny();
//    }

}

