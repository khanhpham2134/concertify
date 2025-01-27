package fi.tuni.concertify.controllers;

import fi.tuni.concertify.services.ArtistService;

import java.util.ArrayList;

import fi.tuni.concertify.models.Artist;
import fi.tuni.concertify.models.Track;
import javafx.scene.chart.XYChart;

/**
 * The ArtistController class handles the logic for managing artist data and
 * interaction
 * between the view and the underlying artist-related services.
 */
public class ArtistController {
  private ArtistService artistService;
  private Artist currentArtist;

  /**
   * Constructs an ArtistController and initializes the ArtistService.
   */
  public ArtistController() {
    this.artistService = new ArtistService();
  }

  /**
   * Loads artist details based on the provided artist name.
   * 
   * @param artistName The name of the artist to load.
   */
  public void loadArtist(String artistName) {
    this.currentArtist = artistService.getArtistByName(artistName);
  }

  /**
   * Retrieves the biography of the currently loaded artist.
   * 
   * @return The biography of the artist, or a default message if unavailable.
   */
  public String getArtistBio() {
    return currentArtist != null && currentArtist.getBio() != null ? currentArtist.getBio() : "No biography available";
  }

  /**
   * Retrieves the name of the currently loaded artist.
   * 
   * @return The name of the artist, or null if no artist is loaded.
   */
  public String getArtistName() {
    return currentArtist.getName();
  }

  /**
   * Retrieves the profile picture URL of the currently loaded artist.
   * 
   * @return The URL of the artist's profile picture, or null if unavailable.
   */
  public String getArtistProfilePicture() {
    return currentArtist.getProfilePicture();
  }

  /**
   * Generates a series for displaying the top tracks of the current artist using
   * the specified metric type.
   * 
   * @param metricType The metric type ("Listeners" or "Playcount") to be used for
   *                   the series.
   * @return A series containing track names and their corresponding metric
   *         values.
   */
  public XYChart.Series<String, Number> getTopTracksSeries(String metricType) {
    if (currentArtist == null || currentArtist.getTopTracks() == null) {
      return new XYChart.Series<>();
    }

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Top Tracks");

    for (Track track : currentArtist.getTopTracks()) {
      Number metricValue = getMetricValue(track, metricType);
      series.getData().add(new XYChart.Data<>(track.getName(), metricValue));
    }
    return series;
  }

  /**
   * Retrieves the metric value of a track based on the specified metric type.
   * 
   * @param track      The track for which the metric value is needed.
   * @param metricType The type of metric ("Listeners" or "Playcount").
   * @return The metric value, or 0 if the metric type is invalid.
   */
  private Number getMetricValue(Track track, String metricType) {
    switch (metricType) {
      case "Listeners":
        return track.getListeners();
      case "Playcount":
        return track.getPlayCount();
      default:
        return 0;
    }
  }

  /**
   * Adds an artist to the user's list of favorite artists.
   * 
   * @param name The name of the artist to add to favorites.
   */
  public void addToFavorites(String name) {
    artistService.addArtistToFavorite(name);
  }

  /**
   * Removes an artist from the user's list of favorite artists.
   * 
   * @param name The name of the artist to remove from favorites.
   */
  public void removeFromFavorites(String name) {
    artistService.removeArtistFromFavorite(name);
  }

  /**
   * Retrieves the currently loaded artist.
   * 
   * @return The currently loaded artist, or null if no artist is loaded.
   */
  public Artist getCurrentArtist() {
    return currentArtist;
  }

  /**
   * Retrieves the list of favorite artists.
   * 
   * @return An ArrayList of favorite artists.
   */
  public ArrayList<Artist> getFavoriteArtists() {
    return artistService.getFavoriteArtists();
  }

  /**
   * Searches for artists based on the provided search text.
   * 
   * @param text The search text to use for finding artists.
   * @return An ArrayList of artists matching the search text.
   */
  public ArrayList<Artist> searchArtist(String text) {
    return artistService.searchArtistByKey(text);
  }
}