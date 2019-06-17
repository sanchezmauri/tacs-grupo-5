package controllers;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.actions.VenueListAction;
import models.exceptions.UserException;
import play.libs.F;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import services.*;

import javax.inject.Inject;
import java.awt.desktop.UserSessionEvent;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListsController extends Controller {


    private final UsersService usersService;
    private final ListsService listsService;
    private final FoursquareVenueService foursquareVenueService;
    private final UserVenuesService userVenuesService;

    @Inject
    public ListsController(
            UsersService usersService,
            ListsService listsService,
            FoursquareVenueService foursquareVenueService,
            UserVenuesService userVenuesService) {
        this.usersService = usersService;
        this.listsService = listsService;
        this.foursquareVenueService = foursquareVenueService;
        this.userVenuesService = userVenuesService;
    }

    @Authenticate(types = {"ROOT", "SYSUSER"})
    public Result list(Http.Request request) {
        User user = request.attrs().get(RequestAttrs.USER);
        List<VenueList> allLists;

        // si es admin, mandarle todas las listas
        // si es user, solo las de el
        if (user.getRol().equals(Rol.ROOT)) {
            allLists = usersService.index()
                    .stream()
                    .flatMap(u -> u.getAllLists().stream())
                    .collect(Collectors.toList());
        } else {
            allLists = user.getAllLists();
        }

        return ok(Json.toJson(allLists));
    }

    // acá dejo pasar solo users porque
    // agrego el venue al user que está logueado
    // por ahí si el admin quiere agregar a 1 user un lugar
    // debería hacer: users/:userId/lists/:listId en vez de lists/:listId
    @Authenticate(types = "SYSUSER")
    public Result create(Http.Request request) throws UserException {
        // extraer esto en algún lado tipo validar json o algo
        JsonNode newListJson = request.body().asJson();

        if (newListJson == null) {
            return badRequest(Utils.createErrorMessage("Ill formatted json."));
        }

        if (!newListJson.has("name")) {
            return badRequest(
                    Utils.createErrorMessage("Missing field: name")
            );
        }

        String listName = newListJson.get("name").asText();

        VenueList newList = new VenueList(listName);

        User user = request.attrs().get(RequestAttrs.USER);
        user.addList(newList);
        usersService.addList(user, newList);
        return created(Json.toJson(newList));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result delete(String listId, Http.Request request) {
        User user = request.attrs().get(RequestAttrs.USER);
        usersService.deleteUserVenueList(user, listId);
        user.removeList(listId);

        return ok();
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result changeListName(String listId, Http.Request request) {
        JsonNode changeJson = request.body().asJson();

        if (!changeJson.has("name"))
            return badRequest(Utils.createErrorMessage("Missing field: name."));

        VenueList list = request.attrs().get(RequestAttrs.LIST);
        String newName = changeJson.get("name").asText();

        list.setName(newName);

        return ok(Json.toJson(list));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result addVenuesToList(String listId, Http.Request request) {
        // todo: extraer parseo de json a algún lugar
        // esto de decir missing field y toda la gilada
        JsonNode venuesJson = request.body().asJson();

        if (venuesJson.isObject()) {
            venuesJson = Json.newArray().add(venuesJson);
        }

        User user = request.attrs().get(RequestAttrs.USER);
        VenueList list = request.attrs().get(RequestAttrs.LIST);


        // chequeo que esten todos bien o no hago nada.
        // todo: acumular todos los errores
        Iterator<JsonNode> venuesIter = venuesJson.elements();
        StringBuilder errorBuilder = new StringBuilder();

        while (venuesIter.hasNext()) {
            JsonNode venueJson = venuesIter.next();

            if (!venueJson.has("id")) {
                errorBuilder.append("Missing id in ");
                errorBuilder.append(venueJson.toString());
                errorBuilder.append('\n');
                continue;
            }

            if (!venueJson.has("name")) {
                errorBuilder.append("Missing name in ");
                errorBuilder.append(venueJson.toString());
                errorBuilder.append('\n');
                continue;
            }

            if (!venueJson.has("location") || !venueJson.get("location").has("address")) {
                errorBuilder.append("Missing name location.address");
                errorBuilder.append(venueJson.toString());
                errorBuilder.append('\n');
                continue;
            }
        }

        if (!errorBuilder.toString().isEmpty()) {
            return badRequest(Utils.createErrorMessage(errorBuilder.toString()));
        }

        venuesJson.forEach((venueJson) -> {
//            String id = venueJson.get("id").asText();
            String name = venueJson.get("name").asText();
            String address = venueJson.get("location").get("address").asText();

            FoursquareVenue fqVenue = foursquareVenueService.getOrCreate(name, address);

            UserVenue userVenue = new UserVenue(fqVenue, false);

            // user.addVenueToList(list, fqVenue).ifPresent(addedVenue -> {
            usersService.addVenueToList(user, list, userVenue);
            //});
        });

        return ok(Json.toJson(list));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result removeVenueFromList(String listId, Http.Request request) throws Exception {

        Map<String, Object> map = CodesService.decodeMapFromToken(request.session().data().get("token"));
        User user = usersService.findById(map.get("userId").toString()).orElseThrow(); //Aca deberia buscar el usuario segun id y traerlo con los PERMISOS QUE TIENE;


        JsonNode venueIdJson = request.body().asJson();

        if (!venueIdJson.has("id"))
            return badRequest("Missing field: venueId");

        String venueId = venueIdJson.get("id").asText();


        VenueList list = request.attrs().get(RequestAttrs.LIST);


        if (list.removeVenue(venueId)) {
            usersService.deleteUserVenue(user, venueId);
            return ok(Json.toJson(list));

        } else
            return badRequest(Utils.createErrorMessage("No venue with id " + venueId));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result visitVenue(String listId, String venueId, Http.Request request) {
        VenueList venueList = request.attrs().get(RequestAttrs.LIST);
        User user = request.attrs().get(RequestAttrs.USER);
        usersService.visitUserVenue(user, listId, venueId);
        UserVenue userVenue = userVenuesService.findById(venueId);
        userVenue.visit();
        return ok(Json.toJson(userVenue));
    }

    @Authenticate(types = {"ROOT"})
    public Result compareUsersLists(Http.Request request) {

        var list1Id = request.getQueryString("list1");
        var list2Id = request.getQueryString("list2");

        if (list1Id.isEmpty() || list2Id.isEmpty()) {
            return badRequest(
                    Utils.createErrorMessage("Both List Id's are required.")
            );
        }

        var list1 = listsService.getById(list1Id);
        var list2 = listsService.getById(list2Id);

        if (list1.isEmpty()) {
            return badRequest(
                    Utils.createErrorMessage("List 1 not found.")
            );
        } else if (list2.isEmpty()) {
            return badRequest(
                    Utils.createErrorMessage("List 2 not found.")
            );
        }

        Set<UserVenue> result = list1.get().getVenues().stream()
                .distinct()
                .filter(list2.get().getVenues()::contains)
                .collect(Collectors.toSet());

        return ok(Json.toJson(result));
    }

}