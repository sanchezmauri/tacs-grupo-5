package bussiness;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.ActorMaterializerSettings;
import com.typesafe.config.Config;
import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.ws.WSClient;
import play.libs.ws.ahc.AhcWSClient;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClientConfig;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClientConfig;


import javax.inject.Inject;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VenuesTest {

    private Venues venues;

    private Config configMock;
    @Inject WSClient ws;

    @Before
    public void setUp() {

        configMock = mock(Config.class);


        // Set up Akka
        String name = "wsclient";
        ActorSystem system = ActorSystem.create(name);
        ActorMaterializerSettings settings = ActorMaterializerSettings.create(system);
        ActorMaterializer materializer = ActorMaterializer.create(settings, system, name);

// Set up AsyncHttpClient directly from config
        AsyncHttpClientConfig asyncHttpClientConfig =
                new DefaultAsyncHttpClientConfig.Builder()
                        .setMaxRequestRetry(0)
                        .setShutdownQuietPeriod(0)
                        .setShutdownTimeout(0)
                        .build();
        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient(asyncHttpClientConfig);

// Set up WSClient instance directly from asynchttpclient.
        ws = new AhcWSClient(asyncHttpClient, materializer);


        when(configMock.getString("foursquare.url")).thenReturn("https://api.foursquare.com/v2/venues/search");
        when(configMock.getString("foursquare.version")).thenReturn("20190101");
        when(configMock.getString("foursquare.clientSecret")).thenReturn("VD3SA5CMJCKYRVO4DKSZS2QJPSWU5JL2VIPGBHBPWKUJZRFD");
        when(configMock.getString("foursquare.clientId")).thenReturn("10G3OANLX2GVYZSVBUAUTC55YCW5ON3ID2GT45AHTTACBJ3T");


        venues = new Venues(configMock, ws);


    }

    @After
        public void tearDown(){
            try {
                ws.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




    @Test
    public void venues_searchByLatLong_RetrievesSuccessfully(){

        try {

            var res = venues.search("Taco Box",Venues.LAT_LONG_PARAM,"-34.5821828,-58.4085855");

            assertTrue(res.size() != 0);

        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionFailedError(e.getMessage());
        }


    }

}
