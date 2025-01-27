package fi.tuni.concertify.controllers;

import fi.tuni.concertify.models.Artist;
import fi.tuni.concertify.models.Track;
import fi.tuni.concertify.services.ArtistService;
import fi.tuni.concertify.services.TrackService;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;

/**
 * The EventSearchChartController class manages the display of charts showing
 * top songs or artists
 * by country based on user selection.
 */
public class EventSearchChartController {
  private TrackService trackService;
  private ArtistService artistService;
  private BarChart<String, Number> barChart;
  private ComboBox<String> entityTypeComboBox;
  private ComboBox<String> countryComboBox;

  /**
   * Constructs an EventSearchChartController with the specified UI components.
   *
   * @param barChart           The BarChart component used to display data.
   * @param entityTypeComboBox The ComboBox for selecting the entity type (e.g.,
   *                           Songs or Artists).
   * @param countryComboBox    The ComboBox for selecting the country.
   */
  public EventSearchChartController(BarChart<String, Number> barChart,
      ComboBox<String> entityTypeComboBox,
      ComboBox<String> countryComboBox) {
    this.trackService = new TrackService();
    this.artistService = new ArtistService();
    this.barChart = barChart;
    this.entityTypeComboBox = entityTypeComboBox;
    this.countryComboBox = countryComboBox;

    initialize();
  }

  /**
   * Initializes the controller by setting up event listeners and updating the
   * chart on startup.
   */
  private void initialize() {
    entityTypeComboBox.setOnAction(event -> updateChart());
    countryComboBox.setOnAction(event -> updateChart());
    updateChart();
  }

  /**
   * Updates the chart based on the selected entity type and country.
   */
  public void updateChart() {
    String entityType = entityTypeComboBox.getValue();
    String country = countryComboBox.getValue();

    if (entityType == null || country == null || country.isEmpty()) {
      System.out.println("Entity type or country is not selected.");
      return;
    }

    barChart.getData().clear();

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName(entityType + " in " + country);

    try {
      if (entityType.equals("Songs")) {
        ArrayList<Track> tracks = trackService.getTopTracksByCountry(country);
        for (Track track : tracks) {
          series.getData().add(new XYChart.Data<>(track.getName(), track.getListeners()));
        }
      } else {
        ArrayList<Artist> artists = artistService.getTopCountryArtist(country);
        for (Artist artist : artists) {
          series.getData().add(new XYChart.Data<>(artist.getName(), artist.getListeners()));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error updating chart: " + e.getMessage());
    }

    barChart.getData().add(series);
    barChart.setTitle("Top " + entityType + " in " + country);
  }
}