package services;

import models.FoursquareVenue;
import models.UserVenue;
import models.exceptions.UserException;
import org.bson.types.ObjectId;

import javax.inject.Inject;


public class UserVenuesService {

    private final MongoDbConnectionService dbConnectionService;

    @Inject
    public UserVenuesService(MongoDbConnectionService dbConnectionService) {
        this.dbConnectionService = dbConnectionService;
    }

    public void create(UserVenue userVenue) throws UserException {
        try {
            dbConnectionService.getDatastore().save(userVenue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public UserVenue findById(String id)
    {
        return new UserVenue(
                dbConnectionService.getDatastore().createQuery(FoursquareVenue.class).field("id").equal(new ObjectId(id)).first(),
                true);
    }



//    public Optional<UserVenue> getVenue(String venueId) {
//        return venueslists.stream()
//                .map(list -> list.getVenue(venueId))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .findAny();
//    }

}

