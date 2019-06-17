package controllers;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import annotations.Authenticate;

import javax.inject.Inject;

import bussiness.Venues;
import com.typesafe.config.Config;
import com.fasterxml.jackson.databind.JsonNode;

import models.exceptions.FoursquareException;
import play.libs.Json;
import play.mvc.*;
import play.libs.ws.*;
import services.FoursquareVenueService;
import services.UsersService;


public class VenuesController extends Controller {



    private final Venues businessVenues;
    private final UsersService usersService;
    private final FoursquareVenueService foursquareVenueService;

    @Inject
    public VenuesController(Venues bVenues,UsersService usersService, FoursquareVenueService foursquareVenueService) {
        this.businessVenues = bVenues;
        this.usersService = usersService;
        this.foursquareVenueService = foursquareVenueService;
    }

    @Authenticate(types = {"ROOT"})
    public Result usersInterested(String venueId) {
        long interestedCount = usersService.index()
                .stream()
                .filter(user -> user.hasVenue(venueId))
                .count();

        return ok(
            Json.newObject().put("usersInterested", interestedCount)
        );
    }



    @Authenticate(types = {"ROOT"})
    public Result venuesAddedSince(String since) {

        var opt = Venues.parseSince(since);

        if (opt.isEmpty()) {
            return badRequest("Invalid \"since\" value");
        }
        var sinceDays = opt.get();

        Date current = new Date();

        var count = foursquareVenueService.getCountByDate(current, sinceDays);

        return ok(
                Json.newObject().put("count", count)
        ).as("application/json");


    }

    private String[] parsePositionParam(Http.Request request) {
        if(request.queryString().containsKey(Venues.NEAR_PARAM)) {
            return new String[]{
                    Venues.NEAR_PARAM,
                    request.getQueryString(Venues.NEAR_PARAM)
            };
        } else if (request.queryString().containsKey("latitude") &&
                request.queryString().containsKey("longitude"))  {
            String latitude = request.getQueryString("latitude");
            String longitude = request.getQueryString("longitude");

            return new String[]{
                    Venues.LAT_LONG_PARAM,
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

        try {
            var venues = businessVenues.search(query, positionParamKey, positionParamVal);

            return CompletableFuture.completedFuture(ok(venues));

        } catch (FoursquareException e) {

            String errorMessage = e.errorType + " " + e.errorText;

            return CompletableFuture.completedFuture(
                    badRequest(Utils.createErrorMessage(errorMessage))
            );
        }

    }

}
