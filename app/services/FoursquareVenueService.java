package services;

import bussiness.Venues;
import dev.morphia.query.Sort;
import models.FoursquareVenue;
import models.User;
import models.UserVenue;
import models.VenueList;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class FoursquareVenueService {

    private MongoDbConnectionService dbConnectionService;
    @Inject
    public FoursquareVenueService(MongoDbConnectionService dbConnectionService) {
        this.dbConnectionService = dbConnectionService;
    }

    public void create(FoursquareVenue fqVenue) {
        dbConnectionService.getDatastore().save(fqVenue);
    }

    public FoursquareVenue findById(String id) {
        return dbConnectionService.getDatastore()
                .createQuery(FoursquareVenue.class)
                .field("id")
                .equal(id)
                .first();
    }

    // acá le paso el id que viene desde foursquare
    // el formato parece el mismo que usa mongo
    // si empieza a quejarse chequear esto
    public  FoursquareVenue getOrCreate(String fsId, String name, String address) {
        FoursquareVenue fqVenueFromDB = findById(fsId);

        if (fqVenueFromDB != null) return fqVenueFromDB;

        FoursquareVenue newFqVenue = new FoursquareVenue(fsId, name, address, LocalDate.now());

        try {
            create(newFqVenue);
        } catch (Exception discarded) { } // este error no puede pasar, porque solo pasa si trato de crear uno q ya existe

        return newFqVenue;
    }

    public Long getCountByDate(Date fromDate, Integer days) {

        if (days == Integer.MAX_VALUE) {
            return dbConnectionService.getDatastore()
                    .createQuery(FoursquareVenue.class)
                    .count();
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            cal.add(Calendar.DATE, -days);
            
            return dbConnectionService.getDatastore()
                    .createQuery(FoursquareVenue.class)
                    .order(Sort.ascending("added"))
                    .field("added")
                    .greaterThanOrEq(cal.getTime())
                    .count();
        }
    }

    public List<FoursquareVenue> getUsersInterestedCount(String filter) {

        var asd = new ArrayList<FoursquareVenue>();
        dbConnectionService.getDatastore()
                .createQuery(FoursquareVenue.class)
                .search(filter)
                .find()
                .forEachRemaining(foursquareVenue -> {

                    var venueQuery = dbConnectionService.getDatastore()
                            .createQuery(UserVenue.class)
                            .field("_id")
                            .equal(foursquareVenue.getId());

                    var listQuery = dbConnectionService.getDatastore()
                            .createQuery(VenueList.class)
                            .field("venues")
                            .elemMatch(venueQuery);

                    foursquareVenue.count = dbConnectionService
                            .getDatastore()
                            .createQuery(User.class)
                            .field("venueslists")
                            .elemMatch(listQuery)
                            .count();

                    asd.add(foursquareVenue);
        });

        return asd.stream().filter(x -> x.count > 0).collect(Collectors.toList());

    }
}
