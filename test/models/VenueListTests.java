package models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VenueListTests {

    @Test
    public void VenueList_IdFromChatId_SolvesCorrectly(){

        var chatId = "This is a random List (dfblk2341n2f3o34n4un3423n)";

        var id = VenueList.getIdFromChatId(chatId);

        assertEquals("dfblk2341n2f3o34n4un3423n", id);

    }

}
