package fi.tuni.concertify.services;

import java.util.ArrayList;
import java.util.HashMap;

import fi.tuni.concertify.models.Artist;
import fi.tuni.concertify.models.Track;
import fi.tuni.concertify.models.User;

/**
 * Service class for managing artist-related operations including searching,
 * retrieving, adding, and removing favorite artists.
 */
public class ArtistService extends IOAbstractService {
  private final String ARTIST_DB = "./database/artist.json";

  private UserService userService;
  private LastFmAPIService lastFmAPIService;

  /**
   * Constructs an ArtistService with default dependencies.
   */
  public ArtistService() {
    this.userService = new UserService();
    this.lastFmAPIService = new LastFmAPIService();
  }

  /**
   * Sets the UserService for dependency injection, used for testing purposes.
   * 
   * @param userService the UserService to inject
   */
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  /**
   * Sets the LastFmAPIService for dependency injection, used for testing
   * purposes.
   * 
   * @param lastFmAPIService the LastFmAPIService to inject
   */
  public void setLastFmAPIService(LastFmAPIService lastFmAPIService) {
    this.lastFmAPIService = lastFmAPIService;
  }

  /**
   * Searches for artists based on a keyword.
   * 
   * @param key the search keyword
   * @return a list of artists matching the search keyword
   */
  public ArrayList<Artist> searchArtistByKey(String key) {
    ArrayList<Artist> artistsFile = readFromFile(ARTIST_DB, Artist.class);
    ArrayList<Artist> searchArtists = lastFmAPIService.getArtists(artistsFile, "artist.search", key, null);
    writeToFile(ARTIST_DB, artistsFile);
    return searchArtists;
  }

  /**
   * Gets the current user's favorite artists.
   * 
   * @return a list of the user's favorite artists
   * @throws IllegalStateException if no user is logged in
   */
  public ArrayList<Artist> getFavoriteArtists() {
    User currentUser = userService.getCurrentUser();

    if (currentUser == null) {
      throw new IllegalStateException("User is not logged in");
    }

    return currentUser.getFavoriteArtists();
  }

  /**
   * Gets the top chart artists based on a specified sorting criterion.
   * 
   * @param sortBy the criterion to sort the artists by (e.g., listeners,
   *               playcount)
   * @return a list of the top chart artists
   */
  public ArrayList<Artist> getTopChartArtist(String sortBy) {
    ArrayList<Artist> artistsFile = readFromFile(ARTIST_DB, Artist.class);
    ArrayList<Artist> topChartArtists = lastFmAPIService.getArtists(artistsFile, "chart.gettopartists", sortBy, null);
    writeToFile(ARTIST_DB, artistsFile);
    return topChartArtists;
  }

  /**
   * Gets the top artists for a specified country.
   * 
   * @param country the country for which to retrieve top artists
   * @return a list of the top artists for the specified country
   */
  public ArrayList<Artist> getTopCountryArtist(String country) {
    ArrayList<Artist> artistsFile = readFromFile(ARTIST_DB, Artist.class);
    ArrayList<Artist> topChartArtists = lastFmAPIService.getArtists(artistsFile, "geo.gettopartists", "listeners",
        country);
    writeToFile(ARTIST_DB, artistsFile);
    return topChartArtists;
  }

  /**
   * Retrieves an artist by name, including additional information such as
   * listeners,
   * play count, biography, and top tracks from the LastFM API.
   * 
   * @param name the name of the artist
   * @return the artist with detailed information
   */
  public Artist getArtistByName(String name) {
    ArrayList<Artist> artists = readFromFile(ARTIST_DB, Artist.class);
    Artist currentArtist = artists.stream().filter(artist -> name.equals(artist.getName())).findAny().orElse(null);

    if (currentArtist == null) {
      throw new IllegalArgumentException("Artist not found");
    }

    HashMap<String, String> artistInfoMap = lastFmAPIService.getArtistInfo(name);
    ArrayList<Track> artistTopTracks = lastFmAPIService.getArtistTopTracks(name);

    currentArtist.setListeners(Integer.parseInt(artistInfoMap.get("listeners")));
    currentArtist.setPlayCount(Long.parseLong(artistInfoMap.get("playcount")));
    currentArtist.setBio(artistInfoMap.get("bio"));
    currentArtist.setTopTracks(artistTopTracks);

    writeToFile(ARTIST_DB, artists);

    return currentArtist;
  }

  /**
   * Adds an artist to the current user's list of favorite artists.
   * 
   * @param artistName the name of the artist to add
   * @throws IllegalStateException    if no user is logged in
   * @throws IllegalArgumentException if the artist is not found
   */
  public void addArtistToFavorite(String artistName) {
    User currentUser = userService.getCurrentUser();

    if (currentUser == null) {
      throw new IllegalStateException("User is not logged in");
    }

    ArrayList<Artist> artists = readFromFile(ARTIST_DB, Artist.class);
    Artist currentArtist = artists.stream().filter(artist -> artistName.equals(artist.getName())).findFirst()
        .orElse(null);

    if (currentArtist == null) {
      throw new IllegalArgumentException("Artist not found");
    }

    currentUser.getFavoriteArtists().add(currentArtist);
    userService.updateCurrentUser(currentUser);
  }

  /**
   * Removes an artist from the current user's list of favorite artists.
   * 
   * @param artistName the name of the artist to remove
   * @throws IllegalStateException    if no user is logged in
   * @throws IllegalArgumentException if the artist is not found
   */
  public void removeArtistFromFavorite(String artistName) {
    User currentUser = userService.getCurrentUser();

    if (currentUser == null) {
      throw new IllegalStateException("User is not logged in");
    }

    ArrayList<Artist> artists = readFromFile(ARTIST_DB, Artist.class);
    Artist currentArtist = artists.stream().filter(artist -> artistName.equals(artist.getName())).findFirst()
        .orElse(null);

    if (currentArtist == null) {
      throw new IllegalArgumentException("Artist not found");
    }

    currentUser.getFavoriteArtists().remove(currentArtist);
    userService.updateCurrentUser(currentUser);
  }
}