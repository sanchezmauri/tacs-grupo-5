package services;

import bussiness.Venues;
import models.Rol;
import models.User;
import models.communication.LoginResult;
import models.telegram.Update;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import utils.TelegramCommunicator;
import utils.TelegramState;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TelegramBotTests {

    private TelegramCommunicator comunicatorMock;
    private TelegramState stateMock;

    private Venues venuesMock;
    private UsersService usersServiceMock;
    private ListsService listsServiceMock;

    private TelegramBot bot;
    @Before
    public void setUp() {

        usersServiceMock = mock(UsersService.class);
        comunicatorMock = mock(TelegramCommunicator.class);
        venuesMock = mock(Venues.class);
        listsServiceMock = mock(ListsService.class);

        stateMock = new TelegramState();

        bot = new TelegramBot(comunicatorMock, stateMock, usersServiceMock, venuesMock, listsServiceMock);

        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            System.out.println("called with arguments: " + Arrays.toString(args));
            return null;
        }).when(comunicatorMock).sendMessage(anyLong(), anyString());

        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            System.out.println("called with arguments: " + Arrays.toString(args));
            return null;
        }).when(comunicatorMock).maskMessage(any(Update.class), anyString());

        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            System.out.println("called with arguments: " + Arrays.toString(args));
            return null;
        }).when(comunicatorMock).sendMessageWithOptions(anyLong(), anyString(), any());


        var mockUser = new User("I'm Root", "root@root.com", "root", Rol.ROOT);
        mockUser.setId("SomeTestId");
        when(usersServiceMock.findByEmail("root@root.com")).thenReturn(
                Optional.of(mockUser)
        );


        when(usersServiceMock.login(anyString(),anyString()))
                .thenReturn(LoginResult.InvalidUsernameOrPassword);

        when(usersServiceMock.login("root@root.com","root"))
                .thenReturn(LoginResult.Success("SomeBogusToken", mockUser));


    }

    @After
    public void tearDown(){

    }




    @Test
    public void loginRequestTest(){

        try {
            var update = Update.fromJsonString("{\"update_id\":44388793,\"message\":{\"message_id\":173,\"from\":{\"id\":277016262,\"is_bot\":false,\"first_name\":\"Martin\",\"last_name\":\"Loguancio\",\"username\":\"maadlog\",\"language_code\":\"es\"},\"chat\":{\"id\":277016262,\"first_name\":\"Martin\",\"last_name\":\"Loguancio\",\"username\":\"maadlog\",\"type\":\"private\"},\"date\":1560784500,\"text\":\"/login root@root.com root\",\"entities\":[{\"offset\":0,\"length\":6,\"type\":\"bot_command\"},{\"offset\":7,\"length\":13,\"type\":\"email\"}]}}");

            var testChatId = new Random().nextLong();

            var callback = bot.routeUpdate(testChatId,"/login","/login root@root.com root",update);

            callback.accept(bot);

            assertTrue(stateMock.loggedUserTokens.containsKey(testChatId));
            verify(comunicatorMock).sendMessage(testChatId,TelegramBot.MESSAGES_PERFORMING_LOGIN);
            verify(comunicatorMock).sendMessage(testChatId,TelegramBot.MESSAGES_LOGIN_SUCCESS);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
