package controllers;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import annotations.Authenticate;

import javax.inject.Inject;
import com.typesafe.config.Config;
import com.fasterxml.jackson.databind.JsonNode;

import models.Venue;
import play.libs.Json;
import play.mvc.*;
import play.libs.ws.*;
import repos.UserRepository;


public class VenuesController extends Controller {
    private static final String NEAR_PARAM = "near";
    private static final String LAT_LONG_PARAM = "ll";

    private final Config config;
    private final WSClient ws;

    private final String foursquareURL;
    private final String foursquareVersion;
    private final String clientSecret;
    private final String clientId;

    private List<Venue> venues = new ArrayList<>(
            Arrays.asList(
                    new Venue(1L, "Jason's Bar"),
                    new Venue(2L, "Chimichangas Car"),
                    new Venue(3L, "Ye Ole Generic Palermitan Craft Beer")
            )
    );

    @Inject
    public VenuesController(Config config, WSClient ws) {
        this.config = config;
        this.ws = ws;

        foursquareURL = this.config.getString("foursquare.url");
        foursquareVersion = this.config.getString("foursquare.version");
        clientSecret = this.config.getString("foursquare.clientSecret");
        clientId = this.config.getString("foursquare.clientId");
    }

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

    @Authenticate(types = {"ROOT","SYSUSER"})
    public Result usersInterested(Long venueId) {
        long interestedCount = UserRepository.all()
                .stream()
                .filter(user -> user.hasVenue(venueId))
                .count();

        return ok(
            Json.newObject().put("usersInterested", interestedCount)
        );
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
        return ok(Json.toJson(countVenuesAddedSince(since))).as("application/json");
    }


    public Long countVenuesAddedSince(String since) {
        Integer sinceDays = parseSince(since);

        Date current = new Date();
        return venues.stream()
                .map(x -> new VenueListed(x, new Random().nextBoolean(), new Date(2019,Calendar.MARCH,new Random().nextInt(25))))
                .filter(x -> (TimeUnit.DAYS.convert(current.getTime() - x.getDateAdded().getTime(), TimeUnit.MILLISECONDS)) <= sinceDays.longValue())
                .count();
    }

    private String[] parsePositionParam(Http.Request request) {
        if(request.queryString().containsKey(NEAR_PARAM)) {
            return new String[]{
                    NEAR_PARAM,
                    request.getQueryString(NEAR_PARAM)
            };
        } else if (request.queryString().containsKey("latitude") &&
                request.queryString().containsKey("longitude"))  {
            String latitude = request.getQueryString("latitude");
            String longitude = request.getQueryString("longitude");

            return new String[]{
                    LAT_LONG_PARAM,
                    latitude + "," + longitude
            };
        }

        return null;
    }

    @Authenticate(types = {"ROOT","SYSUSER"})
    public CompletionStage<Result> search(Http.Request request) {
        // check query param
        if (!request.queryString().containsKey("query")){
            return CompletableFuture.completedFuture(
                badRequest("Query is required")
            );
        }
        String query = request.getQueryString("query");


        // get position param
        String[] positionParams = parsePositionParam(request);

        if (positionParams == null) {
            return CompletableFuture.completedFuture(
                    badRequest("Either a geo-codable \"near\" parameter or longitude and latitude must be provided")
            );
        }

        String positionParamKey = positionParams[0];
        String positionParamVal = positionParams[1];

        // make request to foursquare
        return ws.url(foursquareURL)
                .addQueryParameter("client_id", clientId)
                .addQueryParameter("client_secret", clientSecret)
                .addQueryParameter("v", foursquareVersion)
                .addQueryParameter("query", query)
                .addQueryParameter(positionParamKey, positionParamVal)
                .addQueryParameter("intent", "checkin")
                .get()
                .thenApply(this::proccessFoursquareResponse);
    }

    private Result proccessFoursquareResponse(WSResponse foursquareResp) {
        JsonNode responseJson = foursquareResp.asJson();
        JsonNode meta = responseJson.get("meta");
        int statusCode = meta.get("code").asInt();

        if (statusCode == 200) {
            JsonNode venuesJson = responseJson.at("/response/venues");
            return ok(venuesJson);
        } else {
            JsonNode errorType = meta.get("errorType");
            JsonNode errorDetail = meta.get("errorDetail");
            String errorMessage = errorType.asText() + " " + errorDetail.asText();

            return badRequest(Utils.createErrorMessage(errorMessage));
        }
    }
}
