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
import services.ListsService;
import services.UsersService;

import java.util.*;
import java.util.stream.Collectors;

public class ListsController extends Controller {

    private long listId = 0L;
    private Long nextListId() {
        listId++;
        return listId;
    }

    @Authenticate(types = {"ROOT", "SYSUSER"})
    public Result list(Http.Request request) {
        User user = request.attrs().get(RequestAttrs.USER);
        List<VenueList> allLists;

        // si es admin, mandarle todas las listas
        // si es user, solo las de el
        if (user.getRol().equals(Rol.ROOT)) {
            allLists = UsersService.index()
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
        ListsService.create(newList);
        UsersService.addList(user,newList);
        return created(Json.toJson(newList));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result delete(Long listId, Http.Request request) {
        User user = request.attrs().get(RequestAttrs.USER);

        user.removeList(listId);

        return ok();
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result changeListName(Long listId, Http.Request request) {
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
    public Result addVenuesToList(Long listId, Http.Request request) {
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
            String id = venueJson.get("id").asText();
            String name = venueJson.get("name").asText();
            String address = venueJson.get("location").get("address").asText();
            user.addVenueToList(list, id, name, address);
        });

        return ok(Json.toJson(list));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result removeVenueFromList(Long listId, Http.Request request) {
        JsonNode venueIdJson = request.body().asJson();

        if (!venueIdJson.has("id"))
            return badRequest("Missing field: venueId");

        String venueId = venueIdJson.get("id").asText();


        VenueList list = request.attrs().get(RequestAttrs.LIST);


        if (list.removeVenue(venueId))
            return ok(Json.toJson(list));
        else
            return badRequest(Utils.createErrorMessage("No venue with id " + venueId));
    }

    @Authenticate(types = {"SYSUSER"})
    @With(VenueListAction.class)
    public Result visitVenue(Long listId, String venueId, Http.Request request) {
        VenueList venueList = request.attrs().get(RequestAttrs.LIST);

        return venueList.getVenue(venueId)
                .map(venue -> {
                    venue.visit();
                    return ok(Json.toJson(venue));
                })
                .orElse(
                    badRequest(Utils.createErrorMessage("No venue with id = " + venueId))
                );
    }

    @Authenticate(types = {"ROOT"})
    public Result compareUsersLists(Http.Request request) {
        // requerir query params user1, list1, user2, list2
        Map<String, Long> requiredQueryParams = new HashMap<>();
        requiredQueryParams.put("user1", null);
        requiredQueryParams.put("list1", null);
        requiredQueryParams.put("user2", null);
        requiredQueryParams.put("list2", null);

        for (Map.Entry<String, Long> requiredParamName : requiredQueryParams.entrySet()) {
            F.Either<String, Long> result = Utils.parseLongQueryParam(request, requiredParamName.getKey());

            if (result.left.isPresent()) {
                return badRequest(
                    Utils.createErrorMessage(result.left.get())
                );
            }

            requiredQueryParams.put(
                requiredParamName.getKey(),
                result.right.get()
            );
        }

        Long userId1 = requiredQueryParams.get("user1");
        Long userId2 = requiredQueryParams.get("user2");
        Long listId1 = requiredQueryParams.get("list1");
        Long listId2 = requiredQueryParams.get("list2");

        F.Either<Result, VenueList> errOrlist1 = getListFromUser(userId1.toString(), listId1);

        if (errOrlist1.left.isPresent())
            return errOrlist1.left.get();

        F.Either<Result, VenueList> errOrlist2 = getListFromUser(userId2.toString(), listId2);

        if (errOrlist2.left.isPresent())
            return errOrlist2.left.get();


        Set<String> list2VenuesIds = errOrlist2.right.get().getVenues().stream()
                .map(UserVenue::getId)
                .collect(Collectors.toSet());

        Set<String> commonVenues = errOrlist1.right.get().getVenues().stream()
                .map(UserVenue::getId)
                .filter(list2VenuesIds::contains)
                .collect(Collectors.toSet());


        return ok(Json.toJson(commonVenues));
    }

    private F.Either<Result, VenueList> getListFromUser(String userId, Long listId) {
        // obtener usuarios y listas
        Optional<User> user = Optional.ofNullable(UsersService.findById(userId));

        if (user.isEmpty())
            return F.Either.Left(
                badRequest("No user with id = " + userId.toString())
            );

        Optional<VenueList> list = user.get().getList(listId);

        if (list.isEmpty())
            return F.Either.Left(
                badRequest("No list with id = " + listId.toString())
            );

        return F.Either.Right(list.get());
    }
}