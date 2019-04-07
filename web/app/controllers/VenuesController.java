package controllers;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;

import models.User;
import models.Venue;
import play.libs.Json;
import play.mvc.*;

public class VenuesController extends Controller {

    private List<Venue> venues = new ArrayList<>(
            Arrays.asList(
                    new Venue(1L, "Jason's Bar"),
                    new Venue(2L, "Chimichangas Car"),
                    new Venue(3L, "Ye Ole Generic Palermitan Craft Beer")
            )
    );

    protected class VenueListed extends Venue {

        private Boolean visited;
        private Date dateAdded;

        public VenueListed(Venue base,Boolean visited, Date dateAdded) {
            super(base.getId(), base.getName());
            this.visited = visited;
            this.dateAdded = dateAdded;
        }

        public Boolean getVisited() { return visited; }

        public Date getDateAdded() { return dateAdded; }
    }

    protected class VenueInterested extends Venue {

        private Long usersInterested;

        public VenueInterested(Venue base,Long usersInterested) {
            super(base.getId(), base.getName());
            this.usersInterested = usersInterested;
        }

        public Long getUsersInterested() { return usersInterested; }
    }

    public Result usersInterested(Long venueId) {
        return venues.stream()
                .filter(x -> x.getId().equals(venueId))
                .findFirst()
                .map(x -> new VenueInterested(x, new Random().nextLong()))
                .map(x -> ok(Json.toJson(x)).as("application/json"))
                .orElse(noContent());
    }


    public Result venuesAddedSince(int days) {

        Date current = new Date();
        return ok(Json.toJson(venues.stream()
                .map(x -> new VenueListed(x, new Random().nextBoolean(), new Date(2019,Calendar.MARCH,new Random().nextInt(25))))
                .filter(x -> days < 0 || (TimeUnit.DAYS.convert(current.getTime() - x.getDateAdded().getTime(), TimeUnit.MILLISECONDS)) <= days )
                .count())).as("application/json");
    }


    public Result search(Http.Request request) {

        if (!request.queryString().containsKey("query")){
            return badRequest("Query is required");
        }
        String query = request.getQueryString("query");



        if(request.queryString().containsKey("near")) {
            String near = request.getQueryString("near");
        } else {
            if (request.queryString().containsKey("latitude") &&
                    request.queryString().containsKey("longitude"))  {
                String longitude = request.getQueryString("longitude");
                String latitude = request.getQueryString("latitude");

            } else {
                return badRequest("Either a geo-codable \"near\" parameter or longitude and latitude must be provided");
            }

        }

        return ok(Json.toJson(venues)).as("application/json");
    }

}
