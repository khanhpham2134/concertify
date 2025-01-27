package fi.tuni.concertify.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import fi.tuni.concertify.models.Event;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Service class for interacting with the TicketMaster API.
 * This class provides methods to search for events and artists, and parse event
 * data.
 */
public class TicketMasterAPIService extends IOAbstractService {
  private static final Dotenv dotenv = Dotenv.load();
  private static final String TICKETMASTER_API_KEY = dotenv.get("TICKETMASTER_API_KEY");
  private static final String TICKETMASTER_API_PREFIX = "https://app.ticketmaster.com/discovery/v2/";
  private static final String TICKETMASTER_API_SUFFIX = String.format("&classificationName=Music&apikey=%s&locale=*",
      TICKETMASTER_API_KEY);

  /**
   * Retrieves data from the TicketMaster API.
   * 
   * @param endpoint   The API endpoint to call.
   * @param parameters The query parameters for the API call.
   * @param size       The number of results to return.
   * @return A JSON string with the retrieved data.
   */
  private String getDataFromTicketMaster(String endpoint, String parameters, int size) {
    String data = null;

    try {
      String apiURL = String.format("%s%s?%s&size=%d%s", TICKETMASTER_API_PREFIX, endpoint, parameters, size,
          TICKETMASTER_API_SUFFIX);

      data = retrieveDataFromAPI(apiURL);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return data;
  }

  /**
   * Searches for events based on provided parameters.
   * 
   * @param keyword  The search keyword (e.g., event name).
   * @param artistID The ID of the artist (optional).
   * @param country  The country code (optional).
   * @param city     The city name (optional).
   * @return A JsonArray of events matching the search criteria.
   */
  public JsonArray searchEvents(String keyword, String artistID, String country, String city) {
    String parameters = Stream.of(
        keyword == null || keyword.isEmpty() ? null : "keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8),
        artistID == null || artistID.isEmpty() ? null
            : "attractionId=" + URLEncoder.encode(artistID, StandardCharsets.UTF_8),
        country == null || country.isEmpty() ? null
            : "countryCode=" + URLEncoder.encode(country, StandardCharsets.UTF_8),
        city == null || city.isEmpty() ? null : "city=" + URLEncoder.encode(city, StandardCharsets.UTF_8))
        .filter(Objects::nonNull)
        .collect(Collectors.joining("&"));

    String data = getDataFromTicketMaster("events", parameters, 20);

    try {
      JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
      JsonObject embedded = jsonObject.getAsJsonObject("_embedded");
      if (embedded == null)
        return new JsonArray();

      JsonArray events = embedded.getAsJsonArray("events");

      return events;
    } catch (JsonParseException e) {
      return null;
    }
  }

  /**
   * Searches for artists based on a keyword.
   * 
   * @param keyword The search keyword (e.g., artist name).
   * @return A JsonArray of artists matching the search criteria.
   */
  public JsonArray searchArtists(String keyword) {
    String data = getDataFromTicketMaster("attractions", String.format("keyword=%s", keyword), 20);

    try {
      JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
      JsonObject embedded = jsonObject.getAsJsonObject("_embedded");
      if (embedded == null)
        return new JsonArray();

      JsonArray artists = embedded.getAsJsonArray("attractions");

      return artists;
    } catch (JsonParseException e) {
      return null;
    }
  }

  /**
   * Safely parses a JSON object and handles potential exceptions.
   * 
   * @param supplier A supplier that retrieves the value to parse.
   * @param <T>      The type of the value.
   * @return The parsed value, or null if an exception occurs.
   */
  private <T> T safeParse(Supplier<T> supplier) {
    try {
      return supplier.get();
    } catch (NullPointerException | IllegalStateException e) {
      return null;
    }
  }

  /**
   * Converts a JsonArray of events into a list of Event objects.
   * 
   * @param events A JsonArray of events retrieved from the TicketMaster API.
   * @return A list of Event objects created from the JsonArray.
   */
  public ArrayList<Event> getEvents(JsonArray events) {
    ArrayList<Event> searchedEvents = new ArrayList<>();

    events.forEach(element -> {
      JsonObject eventJson = element.getAsJsonObject();
      String ticketmasterID = safeParse(() -> eventJson.get("id").getAsString());
      String name = safeParse(() -> eventJson.get("name").getAsString());
      String url = safeParse(() -> eventJson.get("url").getAsString());
      String bannerImage = parseBannerImage(eventJson);
      JsonObject date = safeParse(() -> eventJson.get("dates").getAsJsonObject());
      Date dateTimeStart = date != null ? safeParse(() -> Date.from(Instant.parse(date
          .get("start").getAsJsonObject()
          .get("dateTime").getAsString()))) : null;
      String timezone = safeParse(() -> date.get("timezone").getAsString());
      JsonObject location = safeParse(() -> eventJson.get("_embedded").getAsJsonObject()
          .get("venues").getAsJsonArray()
          .get(0).getAsJsonObject());
      String locationName = location != null ? safeParse(() -> location.get("name").getAsString()) : null;
      String city = location != null ? safeParse(() -> location.get("city").getAsJsonObject().get("name").getAsString())
          : null;
      String country = location != null
          ? safeParse(() -> location.get("country").getAsJsonObject().get("name").getAsString())
          : null;
      JsonObject coordinates = location != null ? safeParse(() -> location.get("location").getAsJsonObject()) : null;
      double lat = coordinates != null ? safeParse(() -> coordinates.get("latitude").getAsFloat()) : 0;
      double lon = coordinates != null ? safeParse(() -> coordinates.get("longitude").getAsFloat()) : 0;
      JsonObject eventInnerEmbeds = safeParse(() -> eventJson.get("_embedded").getAsJsonObject());
      JsonArray attractions = safeParse(() -> eventInnerEmbeds.get("attractions").getAsJsonArray());
      ArrayList<String> artistNames = attractions != null ? StreamSupport.stream(attractions.spliterator(), false)
          .map(el -> safeParse(() -> el.getAsJsonObject().get("name").getAsString()))
          .collect(Collectors.toCollection(ArrayList::new)) : null;

      Event event = new Event(ticketmasterID, name, url, bannerImage, dateTimeStart, timezone, locationName, city,
          country, lat, lon, artistNames);

      if (name != null && locationName != null && dateTimeStart != null) {
        searchedEvents.add(event);
      }
    });

    return searchedEvents;
  }

  /**
   * Parses the banner image URL from an event's JSON data.
   * 
   * @param eventJson The event's JSON data.
   * @return The URL of the banner image, or null if no suitable image is found.
   */
  private String parseBannerImage(JsonObject eventJson) {
    String bannerImage = safeParse(() -> {
      JsonArray imagesArray = eventJson.getAsJsonArray("images");
      for (int i = 0; i < imagesArray.size(); i++) {
        JsonObject imageObject = imagesArray.get(i).getAsJsonObject();
        int width = imageObject.get("width").getAsInt();
        int height = imageObject.get("height").getAsInt();
        if (width == 640 && height == 360) {
          return imageObject.get("url").getAsString();
        }
      }
      return null;
    });
    return bannerImage;
  }
}