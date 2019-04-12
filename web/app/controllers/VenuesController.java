package controllers;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;

import com.typesafe.config.Config;
import play.libs.Json;
import play.mvc.*;
import play.libs.ws.*;

import models.Venue;


public class VenuesController extends Controller {

    private List<Venue> venues = new ArrayList<>(
            Arrays.asList(
                    new Venue(1L, "Jason's Bar"),
                    new Venue(2L, "Chimichangas Car"),
                    new Venue(3L, "Ye Ole Generic Palermitan Craft Beer")
            )
    );

    private final Config config;
    private final WSClient ws;

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

    @Inject
    public VenuesController(Config config, WSClient ws) {
        this.config = config;
        this.ws = ws;
    }

    public Result usersInterested(Long venueId) {
        return venues.stream()
                .filter(x -> x.getId().equals(venueId))
                .findFirst()
                .map(x -> new VenueInterested(x, new Random().nextLong()))
                .map(x -> ok(Json.toJson(x)).as("application/json"))
                .orElse(noContent());
    }



    private Map<String, Integer> periodToDays = new HashMap<String, Integer>() {{
        put("week", 7);
        put("month", 30);
        put("year", 365);
    }};

    private Integer parseSince(String since) {
        if (since == null || since.equals("forever"))
            return Integer.MAX_VALUE;

        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Number number = formatter.parse(since, pos);
        int days = Math.max(number.intValue(), 1);

        String period = since.substring(pos.getIndex());
        Integer periodDays = periodToDays.getOrDefault(period, 1);

        return days * periodDays;
    }

    public Result venuesAddedSince(String since) {
        Integer sinceDays = parseSince(since);

        Date current = new Date();
        return ok(Json.toJson(venues.stream()
                .map(x -> new VenueListed(x, new Random().nextBoolean(), new Date(2019,Calendar.MARCH,new Random().nextInt(25))))
                .filter(x -> (TimeUnit.DAYS.convert(current.getTime() - x.getDateAdded().getTime(), TimeUnit.MILLISECONDS)) <= sinceDays.longValue())
                .count())).as("application/json");
    }


    public CompletionStage<Result> search(Http.Request request) {

        if (!request.queryString().containsKey("query")){
            return CompletableFuture.completedFuture(badRequest("Query is required"));
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
                return CompletableFuture.completedFuture(
                    badRequest("Either a geo-codable \"near\" parameter or longitude and latitude must be provided")
                );
            }

        }

        String foursquareURL = config.getString("foursquare.url");
        String foursquareVersion = config.getString("foursquare.version");
        String clientSecret = config.getString("foursquare.clientSecret");
        String clientId = config.getString("foursquare.clientId");

        return ws.url(foursquareURL)
            .addQueryParameter("client_id", clientId)
            .addQueryParameter("client_secret", clientSecret)
            .addQueryParameter("v", foursquareVersion)
            .addQueryParameter("query", query)
            .addQueryParameter("near", "Buenos Aires, Argentina") // todo: esto varia
            .addQueryParameter("intent", "checkin")
            .get()
            .thenApply(response -> ok(response.asJson()));
    }

}
