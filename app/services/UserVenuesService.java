package services;

import models.FoursquareVenue;
import models.UserVenue;
import models.exceptions.UserException;
import org.bson.types.ObjectId;


public class UserVenuesService {
    public static void create(UserVenue userVenue) throws UserException {
        try {
            MongoDbConnectionService.getDatastore().save(userVenue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static UserVenue findById(String id)
    {
        return new UserVenue(MongoDbConnectionService.getDatastore().createQuery(FoursquareVenue.class).field("id").equal(new ObjectId(id)).first(),true);
    }



//    public Optional<UserVenue> getVenue(String venueId) {
//        return venueslists.stream()
//                .map(list -> list.getVenue(venueId))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .findAny();
//    }

}

