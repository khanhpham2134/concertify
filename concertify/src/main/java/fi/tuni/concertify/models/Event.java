package fi.tuni.concertify.models;

import java.util.UUID;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a musical event with associated metadata.
 */
public class Event {
  private UUID id;
  private String ticketmasterID;
  private String name;
  private String url;
  private String bannerImage;
  private Date dateTimeStart;
  private String timezone;
  private String locationName;
  private String city;
  private String country;
  private double lat;
  private double lon;
  private ArrayList<String> artistNames;

  /**
   * Constructs an empty Event object.
   */
  public Event() {
  }

  /**
   * Constructs an Event with the provided details.
   *
   * @param ticketmasterID The Ticketmaster ID of the event.
   * @param name           The name of the event.
   * @param url            The URL for the event details.
   * @param bannerImage    The banner image URL for the event.
   * @param dateTimeStart  The start date and time of the event.
   * @param timezone       The timezone of the event.
   * @param locationName   The name of the event's location.
   * @param city           The city where the event is located.
   * @param country        The country where the event is located.
   * @param lat            The latitude of the event location.
   * @param lon            The longitude of the event location.
   * @param artistNames    A list of artist names performing at the event.
   */
  public Event(String ticketmasterID, String name, String url, String bannerImage,
      Date dateTimeStart, String timezone, String locationName, String city, String country, double lat, double lon,
      ArrayList<String> artistNames) {
    this.id = UUID.randomUUID();
    this.ticketmasterID = ticketmasterID;
    this.name = name;
    this.url = url;
    this.bannerImage = bannerImage;
    this.dateTimeStart = dateTimeStart;
    this.timezone = timezone;
    this.locationName = locationName;
    this.city = city;
    this.country = country;
    this.lat = lat;
    this.lon = lon;
    this.artistNames = artistNames;
  }

  /**
   * Gets the unique ID of the event.
   * 
   * @return the ID of the event
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the unique ID of the event.
   * 
   * @param id the new ID of the event
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Gets the Ticketmaster ID of the event.
   * 
   * @return the Ticketmaster ID of the event
   */
  public String getTicketmasterID() {
    return ticketmasterID;
  }

  /**
   * Sets the Ticketmaster ID of the event.
   * 
   * @param ticketmasterID the new Ticketmaster ID of the event
   */
  public void setTicketmasterID(String ticketmasterID) {
    this.ticketmasterID = ticketmasterID;
  }

  /**
   * Gets the name of the event.
   * 
   * @return the name of the event
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the event.
   * 
   * @param name the new name of the event
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the banner image URL of the event.
   * 
   * @return the banner image URL of the event
   */
  public String getBannerImage() {
    return bannerImage;
  }

  /**
   * Sets the banner image URL of the event.
   * 
   * @param bannerImage the new banner image URL of the event
   */
  public void setBannerImage(String bannerImage) {
    this.bannerImage = bannerImage;
  }

  /**
   * Gets the start date and time of the event.
   * 
   * @return the start date and time of the event
   */
  public Date getDateTimeStart() {
    return dateTimeStart;
  }

  /**
   * Sets the start date and time of the event.
   * 
   * @param dateTimeStart the new start date and time of the event
   */
  public void setDateTimeStart(Date dateTimeStart) {
    this.dateTimeStart = dateTimeStart;
  }

  /**
   * Gets the URL for the event details.
   * 
   * @return the URL of the event
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the URL for the event details.
   * 
   * @param url the new URL of the event
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Gets the timezone of the event.
   * 
   * @return the timezone of the event
   */
  public String getTimezone() {
    return timezone;
  }

  /**
   * Sets the timezone of the event.
   * 
   * @param timezone the new timezone of the event
   */
  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  /**
   * Gets the list of artist names performing at the event.
   * 
   * @return the list of artist names
   */
  public ArrayList<String> getArtistNames() {
    return artistNames;
  }

  /**
   * Sets the list of artist names performing at the event.
   * 
   * @param artistNames the new list of artist names
   */
  public void setArtistNames(ArrayList<String> artistNames) {
    this.artistNames = artistNames;
  }

  /**
   * Gets the city where the event is located.
   * 
   * @return the city of the event
   */
  public String getCity() {
    return city;
  }

  /**
   * Sets the city where the event is located.
   * 
   * @param city the new city of the event
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Gets the country where the event is located.
   * 
   * @return the country of the event
   */
  public String getCountry() {
    return country;
  }

  /**
   * Sets the country where the event is located.
   * 
   * @param country the new country of the event
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Gets the latitude of the event location.
   * 
   * @return the latitude of the event
   */
  public double getLat() {
    return lat;
  }

  /**
   * Sets the latitude of the event location.
   * 
   * @param lat the new latitude of the event
   */
  public void setLat(Float lat) {
    this.lat = lat;
  }

  /**
   * Gets the longitude of the event location.
   * 
   * @return the longitude of the event
   */
  public double getLon() {
    return lon;
  }

  /**
   * Sets the longitude of the event location.
   * 
   * @param lon the new longitude of the event
   */
  public void setLon(Float lon) {
    this.lon = lon;
  }

  /**
   * Gets the name of the event's location.
   * 
   * @return the location name of the event
   */
  public String getLocationName() {
    return locationName;
  }

  /**
   * Sets the name of the event's location.
   * 
   * @param locationName the new location name of the event
   */
  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  /**
   * Returns a string representation of the event.
   *
   * @return a string representation of the event
   */
  @Override
  public String toString() {
    return "Event {" +
        "id=" + id +
        ", ticketmasterID='" + ticketmasterID + '\'' +
        ", name='" + name + '\'' +
        ", url='" + url + '\'' +
        ", bannerImage='" + bannerImage + '\'' +
        ", dateTimeStart=" + dateTimeStart +
        ", timezone='" + timezone + '\'' +
        ", locationName='" + locationName + '\'' +
        ", city='" + city + '\'' +
        ", country='" + country + '\'' +
        ", lat=" + lat +
        ", lon=" + lon +
        ", artistNames=" + artistNames +
        '}';
  }

  /**
   * Checks if two events are equal based on their Ticketmaster IDs.
   * 
   * @param obj the object to compare with
   * @return true if the events have the same Ticketmaster ID, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    Event event = (Event) obj;
    return ticketmasterID.equals(event.ticketmasterID);
  }

  /**
   * Generates a hash code for the event based on its ID.
   * 
   * @return a hash code for the event
   */
  @Override
  public int hashCode() {
    return id.hashCode();
  }
}