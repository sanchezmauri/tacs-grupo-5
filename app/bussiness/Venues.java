package bussiness;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import models.exceptions.FoursquareException;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;

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

    public static Integer parseSince(String since) {
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

    public Long countVenuesAddedSince(Integer days) {
        return 0L;
    }


        public JsonNode search(String query, String positionTypeKey, String positionTypeParameter) throws FoursquareException {

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

            return processFoursquareResponse(response);


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
