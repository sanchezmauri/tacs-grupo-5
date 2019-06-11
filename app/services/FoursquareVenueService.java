package services;

import models.FoursquareVenue;
import models.exceptions.UserException;
import org.bson.types.ObjectId;

import java.time.LocalDate;


public class FoursquareVenueService {
    public static void create(FoursquareVenue fqVenue) throws Exception {
        FoursquareVenue fqVenueFromDB = findById(fqVenue.getId());

        if (fqVenueFromDB == null) {
            MongoDbConectionService.getDatastore().save(fqVenue);
        } else {
            throw new UserException("ya había un fqVenue con id " + fqVenue.getId());
        }
    }

    public static FoursquareVenue findById(String id) {
        return MongoDbConectionService.getDatastore()
                .createQuery(FoursquareVenue.class)
                .field("id")
                .equal(new ObjectId(id))
                .first();
    }

    // acá le paso el id que viene desde foursquare
    // el formato parece el mismo que usa mongo
    // si empieza a quejarse chequear esto
    public static FoursquareVenue getOrCreate(String id, String name, String address) {
        FoursquareVenue fqVenueFromDB = findById(id);

        if (fqVenueFromDB != null) return fqVenueFromDB;

        FoursquareVenue newFqVenue = new FoursquareVenue(id, name, address, LocalDate.now());

        try {
            create(newFqVenue);
        } catch (Exception discarded) { } // este error no puede pasar, porque solo pasa si trato de crear uno q ya existe

        return newFqVenue;
    }
}
