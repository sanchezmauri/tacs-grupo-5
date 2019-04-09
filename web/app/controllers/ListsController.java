package controllers;

import models.Venue;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

public class ListsController extends Controller {
    public Result compareLists(Long listId1, Long listId2) {
        List<Venue> commonVenues = new ArrayList<>();
        commonVenues.add(new Venue(1L, "Maxi Kiosco"));
        commonVenues.add(new Venue(2L, "Verdulería"));

        return ok(Json.toJson(commonVenues));
    }
}
