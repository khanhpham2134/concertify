package fi.tuni.concertify.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.JsonArray;

import fi.tuni.concertify.models.Event;
import fi.tuni.concertify.models.User;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    private EventService eventService;

    @Mock
    private UserService userService;
    
    @Mock
    private TicketMasterAPIService ticketMasterAPIService;
    
    private User testUser;
    private Event mockEvent;
    private JsonArray mockEvents;

    @BeforeEach
    public void setUp() {
        eventService = spy(new EventService());
        
        // Initialize test data
        testUser = new User("testUser", "hashedPassword", true);
        testUser.setFavoriteEvents(new ArrayList<>());

        mockEvent = new Event(
            "tm1234", 
            "Mock Event", 
            "http://example.com", 
            "http://example.com/banner.jpg", 
            new Date(), 
            "UTC", 
            "Venue Name", 
            "City", 
            "Country", 
            40.7128, 
            -74.0060, 
            new ArrayList<>(List.of("Artist 1", "Artist 2"))
        );

        mockEvents = new JsonArray();
        
        // Set mocked services
        eventService.setUserService(userService);
        eventService.setTicketMasterAPIService(ticketMasterAPIService);
    }

    @Test
    @DisplayName("Should retrieve events when searching by artist")
    void testGetEventsByArtist() {
        String artistName = "Artist 1";
        ArrayList<Event> expectedEvents = new ArrayList<>();
        expectedEvents.add(mockEvent);
        
        doReturn(mockEvents).when(ticketMasterAPIService).searchEvents(anyString(), anyString(), anyString(), anyString());
        doReturn(expectedEvents).when(ticketMasterAPIService).getEvents(any(JsonArray.class));

        ArrayList<Event> events = eventService.getEventsByArtist(artistName);

        assertNotNull(events);
        assertFalse(events.isEmpty());
        assertTrue(events.get(0).getArtistNames().contains(artistName));
    }

    @Test
    @DisplayName("Should retrieve events when searching by location")
    void testGetEventsByLocation() {
        String city = "City";
        String country = "Country";
        ArrayList<Event> expectedEvents = new ArrayList<>();
        expectedEvents.add(mockEvent);

        doReturn(mockEvents).when(ticketMasterAPIService).searchEvents(anyString(), anyString(), anyString(), anyString());
        doReturn(expectedEvents).when(ticketMasterAPIService).getEvents(any(JsonArray.class));

        ArrayList<Event> events = eventService.getEventsByLocation(city, country);

        assertNotNull(events);
        assertFalse(events.isEmpty());
        assertEquals(city, events.get(0).getCity());
        assertEquals(country, events.get(0).getCountry());
    }

    @Test
    @DisplayName("Should successfully add event to favorites")
    void testSaveEvent() {
        doReturn(testUser).when(userService).getCurrentUser();
        
        eventService.addEventToFavorite(mockEvent);

        assertTrue(testUser.getFavoriteEvents().contains(mockEvent));
        verify(userService).updateCurrentUser(testUser);
    }

    @Test
    @DisplayName("Should retrieve favorite events for logged-in user")
    void testGetFavoriteEvents() {
        testUser.getFavoriteEvents().add(mockEvent);
        doReturn(testUser).when(userService).getCurrentUser();

        ArrayList<Event> favoriteEvents = eventService.getFavoriteEvents();

        assertNotNull(favoriteEvents);
        assertEquals(1, favoriteEvents.size());
        assertTrue(favoriteEvents.contains(mockEvent));
    }

    @Test
    @DisplayName("Should throw exception when getting favorites for logged-out user")
    void testGetFavoriteEvents_UserNotLoggedIn() {
        doReturn(null).when(userService).getCurrentUser();

        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> eventService.getFavoriteEvents());
        assertEquals("User is not logged in", exception.getMessage());
    }
}