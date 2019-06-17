package services;

import dev.morphia.query.Sort;
import models.FoursquareVenue;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FoursquareVenueService {
    public static void create(FoursquareVenue fqVenue) {
            MongoDbConnectionService.getDatastore().save(fqVenue);
    }

    public static FoursquareVenue findById(String id) {
        return MongoDbConnectionService.getDatastore()
                .createQuery(FoursquareVenue.class)
                .field("id")
                .equal(new ObjectId(id))
                .first();
    }

    // acá le paso el id que viene desde foursquare
    // el formato parece el mismo que usa mongo
    // si empieza a quejarse chequear esto
    public static FoursquareVenue getOrCreate(String name, String address) {
//        FoursquareVenue fqVenueFromDB = findById(id);
//
//        if (fqVenueFromDB != null) return fqVenueFromDB;

        FoursquareVenue newFqVenue = new FoursquareVenue(name, address, LocalDate.now());

        try {
            create(newFqVenue);
        } catch (Exception discarded) { } // este error no puede pasar, porque solo pasa si trato de crear uno q ya existe

        return newFqVenue;
    }

    public static Long getCountByDate(Date fromDate, Integer days) {

        if (days == Integer.MAX_VALUE) {
            return MongoDbConnectionService.getDatastore()
                    .createQuery(FoursquareVenue.class)
                    .count();
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            cal.add(Calendar.DATE, -days);
            
            return MongoDbConnectionService.getDatastore()
                    .createQuery(FoursquareVenue.class)
                    .order(Sort.ascending("added"))
                    .field("added")
                    .greaterThanOrEq(cal.getTime())
                    .count();
        }


    }
}
