package fi.tuni.concertify.controllers;

import java.util.ArrayList;

import fi.tuni.concertify.models.Event;
import fi.tuni.concertify.services.EventService;

/**
 * The EventController class manages the interactions between the application
 * and event-related services.
 * It provides methods to retrieve, add, and manage events and their associated
 * data.
 */
public class EventController {
  private EventService eventService = new EventService();

  /**
   * Retrieves a list of events based on a specific location.
   * 
   * @param countryCode The country code of the location.
   * @param city        The city name of the location.
   * @return A list of events matching the specified location.
   */
  public ArrayList<Event> getEventsByLocation(String countryCode, String city) {
    ArrayList<Event> searchedEvents = eventService.getEventsByLocation(countryCode, city);
    return searchedEvents;
  }

  /**
   * Retrieves a list of events related to a specific artist.
   * 
   * @param artistName The name of the artist.
   * @return A list of events associated with the artist.
   */
  public ArrayList<Event> getEventsRelatedToArtist(String artistName) {
    ArrayList<Event> searchedEvents = eventService.getEventsByArtist(artistName);
    return searchedEvents;
  }

  /**
   * Retrieves a list of the user's favorite events.
   * 
   * @return A list of favorite events.
   */
  public ArrayList<Event> getFavoriteEvents() {
    ArrayList<Event> searchedEvents = eventService.getFavoriteEvents();
    return searchedEvents;
  }

  /**
   * Adds an event to the user's list of favorite events.
   * 
   * @param event The event to add to favorites.
   */
  public void addEventToFavorites(Event event) {
    eventService.addEventToFavorite(event);
  }

  /**
   * Removes an event from the user's list of favorite events.
   * 
   * @param event The event to remove from favorites.
   */
  public void removeEventFromFavorites(Event event) {
    eventService.removeEventFromFavorite(event);
  }

  /**
   * Retrieves a list of recently searched locations.
   * 
   * @return A list of recent locations in the format of city names.
   */
  public ArrayList<String> getSearchedLocations() {
    return eventService.getRecentLocations();
  }

  /**
   * Adds a location to the list of recently searched locations.
   * 
   * @param city    The name of the city.
   * @param country The name of the country.
   */
  public void addToSearchedLocation(String city, String country) {
    eventService.addRecentLocation(city, country);
  }
}