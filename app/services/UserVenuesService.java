package services;

import models.User;
import models.UserVenue;
import models.Venue;
import models.VenueList;
import models.exceptions.UserException;
import org.bson.types.ObjectId;


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


//    public Optional<UserVenue> getVenue(String venueId) {
//        return venueslists.stream()
//                .map(list -> list.getVenue(venueId))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .findAny();
//    }

}

