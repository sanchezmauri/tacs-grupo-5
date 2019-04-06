package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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





    public Result search(Http.Request request) {

        if (!request.queryString().containsKey("query")){
            return badRequest("query is required");
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
                return badRequest("either a geo-codable \"near\" parameter or longitude and latitude must be provided");
            }

        }

        return ok(Json.toJson(venues)).as("application/json");
    }

}
