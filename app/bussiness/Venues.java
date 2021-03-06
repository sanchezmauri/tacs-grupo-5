package bussiness;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import models.exceptions.FoursquareException;
import models.venues.FSLocation;
import models.venues.FSVenueSearch;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;

public class Venues {


        public static final String NEAR_PARAM = "near";
        public static final String LAT_LONG_PARAM = "ll";


        private final WSClient ws;

        private final String foursquareURL;
        private final String foursquareVersion;
        private final String clientSecret;
        private final String clientId;

        @Inject
        public Venues(Config config, WSClient ws) {

            this.ws = ws;

            foursquareURL = config.getString("foursquare.url");
            foursquareVersion = config.getString("foursquare.version");
            clientSecret = config.getString("foursquare.clientSecret");
            clientId = config.getString("foursquare.clientId");
        }

    private static Map<String, Integer> periodToDays = new HashMap<>() {{
        put("week", 7);
        put("month", 30);
        put("year", 365);
    }};

    public static Optional<Integer> parseSince(String since) {
        if (since == null || since.equals("forever"))
            return Optional.of(Integer.MAX_VALUE);

        if (periodToDays.containsKey(since)) {
            return Optional.of(periodToDays.get(since));
        }

        try
        {
            return Optional.of(Integer.parseInt(since));
        }
        catch(Exception e)
        {
            return Optional.empty();
        }

    }


        public List<FSVenueSearch> search(String query, String positionTypeKey, String positionTypeParameter) throws FoursquareException {

            WSResponse response =  ws.url(foursquareURL)
                    .addQueryParameter("client_id", clientId)
                    .addQueryParameter("client_secret", clientSecret)
                    .addQueryParameter("v", foursquareVersion)
                    .addQueryParameter("query", query)
                    .addQueryParameter(positionTypeKey, positionTypeParameter)
                    .addQueryParameter("intent", "checkin")
                    .get()
                    .toCompletableFuture()
                    .join();

            var fsResponse = processFoursquareResponse(response);

            var results = new ArrayList<FSVenueSearch>();
            fsResponse.elements().forEachRemaining( jn -> {
                var res = new FSVenueSearch(jn);
                results.add(res);
            });

            return results;
        }

        private JsonNode processFoursquareResponse(WSResponse foursquareResp) throws FoursquareException {
            JsonNode responseJson = foursquareResp.asJson();
            JsonNode meta = responseJson.get("meta");
            int statusCode = meta.get("code").asInt();

            if (statusCode == 200) {
                return responseJson.at("/response/venues");
            } else {
                JsonNode errorType = meta.get("errorType");
                JsonNode errorDetail = meta.get("errorDetail");

                throw new FoursquareException(
                        statusCode,
                        errorType.asText("Unknown Error"),
                        errorDetail.asText("Unknown Error")
                );
            }
        }

}
