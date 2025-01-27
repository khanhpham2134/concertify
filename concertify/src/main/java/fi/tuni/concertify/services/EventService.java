package fi.tuni.concertify.services;

import java.util.ArrayList;
import java.util.stream.StreamSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.tuni.concertify.models.Artist;
import fi.tuni.concertify.models.Event;
import fi.tuni.concertify.models.User;
import fi.tuni.concertify.utilities.ArtistUtils;

/**
 * Service class responsible for handling events related to artists and users,
 * including searching events by artist, location, and managing user's favorite
 * events.
 */
public class EventService extends IOAbstractService {
  private final String ARTIST_DB = "./database/artist.json";
  private UserService userService = new UserService();
  private TicketMasterAPIService ticketmasterAPIService = new TicketMasterAPIService();

  // Added for testing purposes to allow dependency injection
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  // Added for testing purposes to allow dependency injection
  public void setTicketMasterAPIService(TicketMasterAPIService ticketmasterAPIService) {
    this.ticketmasterAPIService = ticketmasterAPIService;
  }

  /**
   * Retrieves a list of events for a given artist name. It first checks if the
   * artist
   * has an associated Ticketmaster ID or MusicBrainz ID, and fetches events
   * accordingly.
   * If no ID is found, a general keyword search is performed.
   * 
   * @param artistName the name of the artist
   * @return a list of events related to the artist
   */
  public ArrayList<Event> getEventsByArtist(String artistName) {
    ArrayList<Artist> cachedArtists = readFromFile(ARTIST_DB, Artist.class);
    Artist artist = cachedArtists.stream().filter(cachedArtist -> artistName.equals(cachedArtist.getName())).findAny()
        .orElse(null);

    if (artist != null && artist.getTicketmasterId() != null) {
      return getEventsByArtistID(artist.getTicketmasterId());
    }

    if (artist != null && artist.getMusicBrainzId() != null) {
      String artistID = getArtistIDByArtistName(cachedArtists, artist);
      if (artistID != null) {
        return getEventsByArtistID(artistID);
      }
    }

    return getEventsByKeyword(artistName);
  }

  /**
   * Retrieves a list of events for a given location (country and city).
   * 
   * @param country the country to search for events
   * @param city    the city to search for events
   * @return a list of events in the specified location
   */
  public ArrayList<Event> getEventsByLocation(String country, String city) {
    JsonArray events = ticketmasterAPIService.searchEvents("", "", country, city);

    if (events == null)
      return new ArrayList<>();

    ArrayList<Event> searchedEvents = ticketmasterAPIService.getEvents(events);

    return searchedEvents;
  }

  /**
   * Retrieves a list of events for a given keyword (e.g., artist name).
   * 
   * @param keyword the keyword to search for events
   * @return a list of events matching the keyword
   */
  private ArrayList<Event> getEventsByKeyword(String keyword) {
    JsonArray events = ticketmasterAPIService.searchEvents(keyword, "", "", "");

    if (events == null)
      return new ArrayList<>();

    ArrayList<Event> searchedEvents = ticketmasterAPIService.getEvents(events);

    return searchedEvents;
  }

  /**
   * Retrieves a list of events for a given artist's Ticketmaster ID.
   * 
   * @param artistID the Ticketmaster ID of the artist
   * @return a list of events related to the artist's ID
   */
  private ArrayList<Event> getEventsByArtistID(String artistID) {
    JsonArray events = ticketmasterAPIService.searchEvents("", artistID, "", "");

    if (events == null)
      return new ArrayList<>();

    ArrayList<Event> searchedEvents = ticketmasterAPIService.getEvents(events);

    return searchedEvents;
  }

  /**
   * Retrieves the user's favorite events.
   * 
   * @return a list of the user's favorite events
   */
  public ArrayList<Event> getFavoriteEvents() {
    User currentUser = userService.getCurrentUser();

    if (currentUser == null) {
      throw new IllegalStateException("User is not logged in");
    }

    ArrayList<Event> searchedEvents = currentUser.getFavoriteEvents();

    return searchedEvents;
  }

  /**
   * Retrieves the artist ID from Ticketmaster API based on the artist's
   * MusicBrainz ID.
   * If the artist's Ticketmaster ID is found, it is saved in the artist's record.
   * 
   * @param cachedArtists the list of cached artists
   * @param currentArtist the artist whose ID needs to be retrieved
   * @return the artist's Ticketmaster ID, or null if not found
   */
  private String getArtistIDByArtistName(ArrayList<Artist> cachedArtists, Artist currentArtist) {
    JsonArray artists = ticketmasterAPIService.searchArtists(currentArtist.getName());

    if (artists == null)
      return null;

    JsonObject artistJson = StreamSupport.stream(artists.spliterator(), false)
        .map(JsonElement::getAsJsonObject)
        .filter(jsonObj -> currentArtist.getMusicBrainzId().equals(ArtistUtils.getMusicBrainzId(jsonObj)))
        .findAny()
        .orElse(null);

    if (artistJson != null) {
      String artistId = artistJson.get("id").getAsString();
      currentArtist.setTicketmasterId(artistId);
      writeToFile(ARTIST_DB, cachedArtists);
      return artistId;
    }

    return null;
  }

  /**
   * Retrieves the user's recently searched locations.
   * 
   * @return a list of the user's recently searched locations
   */
  public ArrayList<String> getRecentLocations() {
    User currentUser = userService.getCurrentUser();

    if (currentUser == null) {
      throw new IllegalStateException("User is not logged in");
    }

    ArrayList<String> searchedLocations = currentUser.getRecentlySearchedLocations();

    return searchedLocations;
  }

  /**
   * Adds a location (city and country) to the user's recent locations.
   * If the list exceeds 20 locations, the oldest location is removed.
   * 
   * @param city    the city to add
   * @param country the country to add
   */
  public void addRecentLocation(String city, String country) {
    User currentUser = userService.getCurrentUser();
    String location;

    if (currentUser == null) {
      throw new IllegalStateException("User is not logged in");
    }

    if (isNotEmpty(city) && isNotEmpty(country)) {
      location = String.format("City: %s, Country: %s", city, country);
    } else {
      location = isNotEmpty(city) ? String.format("City: %s", city)
          : isNotEmpty(country) ? String.format("Country: %s", country) : null;
    }

    if (location == null)
      return;

    ArrayList<String> recentLocations = currentUser.getRecentlySearchedLocations();
    recentLocations.remove(location);
    recentLocations.add(0, location);

    if (recentLocations.size() > 20) {
      recentLocations.remove(20);
    }

    userService.updateCurrentUser(currentUser);
  }

  /**
   * Adds an event to the user's list of favorite events.
   * 
   * @param event the event to add to favorites
   */
  public void addEventToFavorite(Event event) {
    User currentUser = userService.getCurrentUser();

    if (currentUser == null) {
      throw new IllegalStateException("User is not logged in");
    }

    currentUser.getFavoriteEvents().add(event);

    userService.updateCurrentUser(currentUser);
  }

  /**
   * Removes an event from the user's list of favorite events.
   * 
   * @param event the event to remove from favorites
   */
  public void removeEventFromFavorite(Event event) {
    User currentUser = userService.getCurrentUser();

    if (currentUser == null) {
      throw new IllegalStateException("User is not logged in");
    }

    currentUser.getFavoriteEvents().remove(event);

    userService.updateCurrentUser(currentUser);
  }

  /**
   * Checks if the given string is not empty or null.
   * 
   * @param str the string to check
   * @return true if the string is not empty or null, false otherwise
   */
  private boolean isNotEmpty(String str) {
    return str != null && !str.trim().isEmpty();
  }
}