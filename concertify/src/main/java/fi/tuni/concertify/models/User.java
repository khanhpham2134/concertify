package fi.tuni.concertify.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a user with their credentials, favorite events and artists, and
 * search history.
 */
public class User {
  private UUID id;
  private String username, passwordHash;
  private ArrayList<Event> favoriteEvents;
  private ArrayList<Artist> favoriteArtists;
  private ArrayList<String> recentlySearchedLocations;
  private Boolean isCurrentLogin;
  private Date lastLogin;

  /**
   * Constructs a new empty User object.
   */
  public User() {
  }

  /**
   * Constructs a new User object with the specified username, password hash, and
   * login status.
   * 
   * @param username       the username of the user
   * @param passwordHash   the hashed password of the user
   * @param isCurrentLogin the login status of the user
   */
  public User(String username, String passwordHash, Boolean isCurrentLogin) {
    this.id = UUID.randomUUID();
    this.username = username;
    this.passwordHash = passwordHash;
    this.favoriteEvents = new ArrayList<>();
    this.favoriteArtists = new ArrayList<>();
    this.recentlySearchedLocations = new ArrayList<>();
    this.isCurrentLogin = isCurrentLogin;
    this.lastLogin = new Date();
  }

  /**
   * Gets the unique ID of the user.
   * 
   * @return the ID of the user
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the unique ID of the user.
   * 
   * @param id the new ID of the user
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Gets the username of the user.
   * 
   * @return the username of the user
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username of the user.
   * 
   * @param username the new username of the user
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets the hashed password of the user.
   * 
   * @return the password hash of the user
   */
  public String getPasswordHash() {
    return passwordHash;
  }

  /**
   * Sets the hashed password of the user.
   * 
   * @param passwordHash the new password hash of the user
   */
  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  /**
   * Gets the list of favorite events of the user.
   * 
   * @return the list of favorite events
   */
  public ArrayList<Event> getFavoriteEvents() {
    return favoriteEvents;
  }

  /**
   * Sets the list of favorite events of the user.
   * 
   * @param favoriteEvents the new list of favorite events
   */
  public void setFavoriteEvents(ArrayList<Event> favoriteEvents) {
    this.favoriteEvents = favoriteEvents;
  }

  /**
   * Gets the list of favorite artists of the user.
   * 
   * @return the list of favorite artists
   */
  public ArrayList<Artist> getFavoriteArtists() {
    return favoriteArtists;
  }

  /**
   * Sets the list of favorite artists of the user.
   * 
   * @param favoriteArtists the new list of favorite artists
   */
  public void setFavoriteArtists(ArrayList<Artist> favoriteArtists) {
    this.favoriteArtists = favoriteArtists;
  }

  /**
   * Gets the login status of the user.
   * 
   * @return true if the user is currently logged in, false otherwise
   */
  public Boolean getIsCurrentLogin() {
    return isCurrentLogin;
  }

  /**
   * Sets the login status of the user.
   * 
   * @param isCurrentLogin the new login status
   */
  public void setIsCurrentLogin(Boolean isCurrentLogin) {
    this.isCurrentLogin = isCurrentLogin;
  }

  /**
   * Gets the last login date of the user.
   * 
   * @return the last login date
   */
  public Date getLastLogin() {
    return lastLogin;
  }

  /**
   * Sets the last login date of the user.
   * 
   * @param lastLogin the new last login date
   */
  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }

  /**
   * Gets the list of recently searched locations by the user.
   * 
   * @return the list of recently searched locations
   */
  public ArrayList<String> getRecentlySearchedLocations() {
    return recentlySearchedLocations;
  }

  /**
   * Sets the list of recently searched locations by the user.
   * 
   * @param recentlySearchedLocations the new list of recently searched locations
   */
  public void setRecentlySearchedLocations(ArrayList<String> recentlySearchedLocations) {
    this.recentlySearchedLocations = recentlySearchedLocations;
  }
}