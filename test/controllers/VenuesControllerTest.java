package controllers;

import bussiness.Venues;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.AssertionFailedError;
import models.venues.FSVenueSearch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import services.FoursquareVenueService;
import services.UsersService;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.test.Helpers.GET;
import static play.test.Helpers.OK;

public class VenuesControllerTest {

        private VenuesController vc;

        private Venues businessVenuesMock;
        private UsersService usersServiceMock;
        private FoursquareVenueService foursquareVenueServiceMock;

        @Before
        public void setUp(){

            businessVenuesMock = mock(Venues.class);
            usersServiceMock = mock(UsersService.class);
            foursquareVenueServiceMock = mock(FoursquareVenueService.class);

            try {
                var venueSearches = new ArrayList<FSVenueSearch>();
                String venue1 = "{ \"id\": \"4ed9866346907c1b41a0e5f5\", \"name\": \"Taco's House\", \"location\": { \"address\": \"Pueyrredon 1462\", \"crossStreet\": \"esq. Santa Fe\", \"formattedAddress\": [ \"Pueyrredon 1462 (esq. Santa Fe)\", \"Argentina\"]} }";
                String venue2 = "{ \"id\": \"4ed9866346907c1b41a0e5f6\", \"name\": \"Taco's House\", \"location\": { \"crossStreet\": \"esq. Santa Fe\", \"formattedAddress\": [ \"Pueyrredon 1462 (esq. Santa Fe)\", \"Argentina\" ]} }";
                String venue3 = "{ \"id\": \"4ed9866346907c1b41a0e5f7\", \"name\": \"Taco's House\", \"location\": { \"formattedAddress\": [ \"Pueyrredon 1462 (esq. Santa Fe)\", \"Argentina\"]} }";

                ObjectMapper mapper = new ObjectMapper();
                JsonNode venue1Obj = mapper.readTree(venue1);
                JsonNode venue2Obj = mapper.readTree(venue2);
                JsonNode venue3Obj = mapper.readTree(venue3);

                venueSearches.add(new FSVenueSearch(venue1Obj));
                venueSearches.add(new FSVenueSearch(venue2Obj));
                venueSearches.add(new FSVenueSearch(venue3Obj));

                when(businessVenuesMock.search(anyString(), anyString(), anyString()))
                        .thenReturn(venueSearches);
            } catch (Exception e) {
                throw new AssertionFailedError("Failed initialization");
            }

            vc = new VenuesController(businessVenuesMock, usersServiceMock, foursquareVenueServiceMock);

        }

        @After
        public void tearDown(){

        }

        @Test
        public void searchVenues_ByLatLongLocation_RetrievesSuccessfully(){

        try {

            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method(GET)
                    .uri("/api/venues?latitude=-34.6338976&longitude=-58.4026626&query=Taco+Box");

            var result = vc.search(request.build()).toCompletableFuture().get();

            assertEquals(OK, result.status());

        } catch (Exception e){
            e.printStackTrace();
            throw new AssertionFailedError(e.getMessage());
        }



        }

        @Test
        public void createDuplicated(){
            // Cuidado, este test dependia de usar el Mongo como estatico, ya no se puede
        /*
        try {
            ObjectNode user = JsonNodeFactory.instance.objectNode();
            user.put("name","test");
            user.put("email","test@gmail.com");
            user.put("password","test");
            route(fakeApplication(), fakeRequest(POST, "/api/users").bodyJson(user));
            Result result = route(fakeApplication(), fakeRequest(POST, "/api/users").bodyJson(user));
            assertEquals(BAD_REQUEST, result.status());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
*/


    }

}


